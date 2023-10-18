package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.TreeCombineMenu;
import dev.crossvas.farming.utils.helpers.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class TreeCombineBlockEntity extends BaseBlockEntity implements MenuProvider {

    protected final Predicate<ItemEntity> VALID_ITEM_ENTITY = item -> {
        if (!item.isAlive() || item.hasPickUpDelay()) {
            return false;
        }
        CompoundTag data = item.getPersistentData();
        return (!data.getBoolean("PreventRemoteMovement") || data.getBoolean("AllowMachineRemoteMovement")) && item.getItem().is(CrossFarmingData.CustomTags.TREE_FARM_CROPS);
    };

    public TreeCombineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.TREE_COMBINE_TILE.get(), pPos, pBlockState);
        this.ENERGY_USAGE = CrossFarmingConfig.TREE_COMBINE_ENERGY_USAGE.get();
        initCombineSidedCaps();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.tree.combine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new TreeCombineMenu(pContainerId, pPlayerInventory, this, this.data);
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
                return stack.is(CrossFarmingData.CustomTags.TREE_FARM_CROPS);
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

            for (BlockPos pos : aroundPoses) {
                BlockEntity entity = level.getBlockEntity(pos);
                if (entity != null) {
                    if (entity instanceof TreeFarmBlockEntity farm) {
                        mainFarm = farm;
                    }
                }
            }

            if (!hasEnergyToWork()) {
                return;
            }

            if (mainFarm != null && seconds == CrossFarmingConfig.TREE_COMBINE_SECONDS_TICK.get()) {
                seconds = 0;

                BlockPos logPos = mainFarm.getBlockPos();
                for (BlockPos areaPos : getWorkingSpace(mainFarm.getBlockPos().above(), farmRange)) {
                    if (level.getBlockState(areaPos).is(BlockTags.LOGS)) {
                        logPos = areaPos;
                    }
                }

                List<BlockPos> area = findPositions(level.getBlockState(logPos), logPos, level);
                for (BlockPos pos : area) {
                    if (hasEnergyToWork()) {
                        harvest(pos, false);
                        setChanged();
                    }
                }

                int radius = 7;
                int offset = 5;


                // collecting area
                AABB collectArea = new AABB(this.x - radius - offset, this.y - offset, this.z - radius - offset, this.x + radius + offset, this.y + offset, this.z + radius + offset);
                List<ItemEntity> items = this.level.getEntitiesOfClass(ItemEntity.class, collectArea, VALID_ITEM_ENTITY);
                for(ItemEntity item : items) {
                    if (hasEnergyToWork()) {
                        ItemStack insertedItem = ItemHelper.insertItemStacked(this.ITEM_HANDLER, item.getItem(), 0, 21, false);
                        if (insertedItem.isEmpty()) {
                            item.discard();
                        } else {
                            item.setItem(insertedItem);
                        }
                        this.extractEnergy(1);
                        setChanged();
                    }
                }
            }
        }
    }

    public void harvest(BlockPos pos, boolean drop) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) {
            return;
        } else if (state.is(BlockTags.LOGS)) {
            collectDrops(pos);
            this.extractEnergy();
            level.destroyBlock(pos, drop);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    protected void collectDrops(BlockPos pos) {
        for (ItemStack blockDrops : getBlockDrops(this.level, pos)) {
            ItemStack result = null;
            if (blockDrops.is(CrossFarmingData.CustomTags.TREE_FARM_CROPS)) {
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
