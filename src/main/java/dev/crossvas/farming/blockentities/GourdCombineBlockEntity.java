package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.GourdCombineMenu;
import dev.crossvas.farming.utils.helpers.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GourdCombineBlockEntity extends BaseBlockEntity implements MenuProvider {

    public GourdCombineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.GOURD_COMBINE_TILE.get(), pPos, pBlockState);
        this.ENERGY_USAGE = CrossFarmingConfig.GOURD_COMBINE_ENERGY_USAGE.get();
        initCombineSidedCaps();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.gourd.combine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GourdCombineMenu(pContainerId, pPlayerInventory, this, this.data);
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
                return stack.is(CrossFarmingData.CustomTags.GOURD_COMBINE_CROPS);
            }
        };
    }

    @Override
    protected boolean hasSidedCaps() {
        return true;
    }

    @Override
    public void onTick() {
        super.onTick();

        tick++;
        if (tick == 20) {
            tick = 0;
            seconds++;


            if (!hasEnergyToWork()) {
                return;
            }

            if (seconds == CrossFarmingConfig.GOURD_COMBINE_SECONDS_TICK.get()) {
                seconds = 0;
                for (BlockPos workPos : getWorkingSpace(getBlockPos(), farmRange)) {
                    if (hasEnergyToWork() && shouldHarvest(workPos.above())) {
                        this.extractEnergy();
                    }
                }
            }
        }
    }

    public boolean shouldHarvest(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(CrossFarmingData.CustomTags.GOURD_COMBINE_CROPS_BLOCK)) {
            collectDrops(pos);
            this.extractEnergy();
            level.destroyBlock(pos, false);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            return true;
        }
        return false;
    }

    protected void collectDrops(BlockPos pos) {
        for (ItemStack blockDrops : getBlockDrops(this.level, pos)) {
            ItemStack result = null;
            if (blockDrops.is(CrossFarmingData.CustomTags.GOURD_COMBINE_CROPS)) {
                result = ItemHelper.insertItemStacked(this.ITEM_HANDLER, blockDrops, 0, 21, false);
            }

            if (result != null) {
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
