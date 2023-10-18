package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.InfernalCombineMenu;
import dev.crossvas.farming.utils.helpers.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfernalCombineBlockEntity extends BaseBlockEntity {

    private int netherWartCounter;

    public InfernalCombineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.INFERNAL_COMBINE_TILE.get(), pPos, pBlockState);
        this.ENERGY_USAGE = CrossFarmingConfig.INFERNAL_COMBINE_ENERGY_USAGE.get();
        initCombineSidedCaps();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.infernal.combine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new InfernalCombineMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return new ItemStackHandler(21) {

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.is(CrossFarmingData.CustomTags.INFERNAL_FARM_SEEDS);
            }
        };
    }

    @Override
    protected boolean hasSidedCaps() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        netherWartCounter = 0;
    }

    @Override
    public void onTick() {
        super.onTick();

        if (hasEnergyToWork()) {
            tick++;
            if (tick == 20) {
                tick = 0;
                seconds++;


                for (BlockPos pos : aroundPoses) {
                    BlockEntity entity = level.getBlockEntity(pos);
                    if (entity != null) {
                        if (entity instanceof InfernalFarmBlockEntity farm) {
                            mainFarm = farm;
                        }
                    }
                }

                if (mainFarm != null && seconds == CrossFarmingConfig.INFERNAL_COMBINE_SECONDS_TICK.get()) {
                    seconds = 0;
                    for (BlockPos workPos : getWorkingSpace(mainFarm.getBlockPos(), 7)) {
                        if (!getWorkingSpace(mainFarm.getBlockPos(), 4).contains(workPos) && level.getBlockState(workPos).is(CrossFarmingData.CustomTags.INFERNAL_FARM_SOIL_BLOCK)) {
                            if (hasEnergyToWork()) {
                                if (shouldReplace(workPos.above())) {
                                    setChanged();
                                    netherWartCounter++;
                                    if (netherWartCounter == 9) {
                                        netherWartCounter = 0;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean shouldReplace(BlockPos pos) {
        BlockState cropState = level.getBlockState(pos);
        if (!cropState.isAir() && cropState.getValue(NetherWartBlock.AGE) == NetherWartBlock.MAX_AGE) {
            collectDrops(pos);
            level.destroyBlock(pos, false);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            this.extractEnergy();
            return true;
        }
        return false;
    }

    protected void collectDrops(BlockPos pos) {
        for (ItemStack blockDrops : getBlockDrops(this.level, pos)) {
            ItemStack result = ItemStack.EMPTY;
            if (blockDrops.is(CrossFarmingData.CustomTags.INFERNAL_FARM_SEEDS)) {
                result = ItemHelper.insertItemStacked(this.ITEM_HANDLER, blockDrops, 0, 21, false);
            }

            if (result.getCount() > 0) {
                spawnItemStack(result, this.level, pos);
            }
        }
    }

    public static List<ItemStack> getBlockDrops(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        NonNullList<ItemStack> stacks = NonNullList.create();
        stacks.addAll(Block.getDrops(state, (ServerLevel)world, pos, world.getBlockEntity(pos)));
        return stacks;
    }
}
