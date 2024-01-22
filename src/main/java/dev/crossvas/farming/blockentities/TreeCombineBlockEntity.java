package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.TreeCombineMenu;
import dev.crossvas.farming.utils.CustomTags;
import dev.crossvas.farming.utils.helpers.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class TreeCombineBlockEntity extends BaseBlockEntity {

    protected final Predicate<ItemEntity> VALID_ITEM_ENTITY = item -> {
        if (!item.isAlive() || item.hasPickUpDelay()) {
            return false;
        }
        CompoundTag data = item.getPersistentData();
        return (!data.getBoolean("PreventRemoteMovement") || data.getBoolean("AllowMachineRemoteMovement"))
                && (item.getItem().is(CustomTags.ITEM_TREE_HARVESTABLE) || item.getItem().is(CustomTags.ITEM_TREE_PLANTABLE));
    };

    public TreeCombineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.TREE_COMBINE_TILE.get(), pPos, pBlockState);
        this.ENERGY_USAGE = CrossFarmingConfig.TREE_COMBINE_ENERGY_USAGE.get();
        initCombineSidedCaps(this);
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
                return stack.is(CustomTags.ITEM_TREE_PLANTABLE) || stack.is(CustomTags.ITEM_TREE_HARVESTABLE);
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

            if (mainFarm != null && seconds == CrossFarmingConfig.TREE_COMBINE_SECONDS_TICK.get()) {
                seconds = 0;

                BlockPos logPos = mainFarm.getBlockPos();
                for (BlockPos areaPos : getWorkingSpace(mainFarm.getBlockPos().above(), farmRange)) {
                    if (level.getBlockState(areaPos).is(BlockTags.LOGS)) {
                        logPos = areaPos;
                        break;
                    }
                }

                Set<BlockPos> area = findPositions(logPos);
                for (BlockPos pos : area) {
                    if (hasEnergyToWork() && shouldHarvest(pos)) {
                        setChanged();
                    }
                }

                int offset = 5;

                // collecting area
                AABB collectArea = new AABB(this.x - farmRange - offset, this.y - offset, this.z - farmRange - offset, this.x + farmRange + offset, this.y + offset, this.z + farmRange + offset);
                List<ItemEntity> items = this.level.getEntitiesOfClass(ItemEntity.class, collectArea, VALID_ITEM_ENTITY);
                for(ItemEntity item : items) {
                    if (hasEnergyToWork()) {
                        ItemStack collected;
                        if (!getSurroundingCaps(ForgeCapabilities.ITEM_HANDLER).isEmpty()) {
                            IItemHandler itemHandler = getSurroundingCaps(ForgeCapabilities.ITEM_HANDLER).get(0); // get the first found itemHandler;
                            collected = ItemHelper.insertItemStacked(itemHandler, 0, item.getItem(), false);
                            if (collected.getCount() > 0) {
                                collected = ItemHelper.insertItemStacked(this.ITEM_HANDLER, 0, item.getItem(), false);
                            }
                        } else {
                            collected = ItemHelper.insertItemStacked(this.ITEM_HANDLER, 0, item.getItem(), false);
                        }
                        if (collected.isEmpty()) {
                            item.discard();
                        } else {
                            item.setItem(collected);
                        }
                        this.extractEnergy(1);
                        setChanged();
                    }
                }
            }
        }
    }

    public boolean shouldHarvest(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.LOGS)) {
            collectDrops(pos, CustomTags.ITEM_TREE_HARVESTABLE);
            level.destroyBlock(pos, false);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            this.extractEnergy();
            return true;
        }
        return false;
    }
}
