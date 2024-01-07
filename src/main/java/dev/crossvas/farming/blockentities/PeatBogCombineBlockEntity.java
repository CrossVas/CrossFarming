package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.PeatBogCombineMenu;
import dev.crossvas.farming.utils.CustomTags;
import dev.crossvas.farming.utils.helpers.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PeatBogCombineBlockEntity extends BaseBlockEntity {

    public PeatBogCombineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.PEAT_BOG_COMBINE_TILE.get(), pPos, pBlockState);
        initCombineSidedCaps(this);
        this.ENERGY_USAGE = CrossFarmingConfig.PEAT_FARM_ENERGY_USAGE.get();
    }

    @Override
    protected boolean hasSidedCaps() {
        return true;
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
                return stack.is(CustomTags.ITEM_PEAT_HARVESTABLE);
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.peat_bog.combine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new PeatBogCombineMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public void onTick() {
        super.onTick();

        tick++;
        if (tick == 20) {
            tick = 0;
            seconds++;

            if(!hasEnergyToWork()) {
                return;
            }

            for (BlockPos pos : aroundPoses) {
                BlockEntity entity = level.getBlockEntity(pos);
                if (entity != null) {
                    if (entity instanceof PeatBogFarmBlockEntity farm) {
                        mainFarm = farm;
                    }
                }
            }

            if (mainFarm != null && seconds == CrossFarmingConfig.PEAT_COMBINE_SECONDS_TICK.get()) {
                seconds = 0;
                for (BlockPos workPos : getWorkingSpace(mainFarm.getBlockPos(), farmRange)) {
                    if (!getWorkingSpace(mainFarm.getBlockPos(), farmArea).contains(workPos) && !mainFarm.getWaterBlockPos(mainFarm.getBlockPos(), farmRange).contains(workPos)) {
                        if (hasEnergyToWork()) {
                            if (shouldHarvest(workPos)) {
                                setChanged();
                                soilCounter++;
                                if (soilCounter == 9) {
                                    soilCounter = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean shouldHarvest(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(CustomTags.BLOCK_PEAT_HARVESTABLE)) {
            collectDrops(pos, CustomTags.ITEM_PEAT_HARVESTABLE);
            this.extractEnergy();
            level.destroyBlock(pos, false);
            level.setBlock(pos, Blocks.SAND.defaultBlockState(), Block.UPDATE_ALL);
            return true;
        }
        return false;
    }

    @Override
    protected void collectDrops(BlockPos pos, TagKey<Item> whitelistItems) {
        for (ItemStack drop : getBlockDrops(level, pos)) {
            ItemStack result;
            if (drop.is(whitelistItems)) {
                if (!getSurroundingCaps(ForgeCapabilities.ITEM_HANDLER).isEmpty()) {
                    IItemHandler handler = getSurroundingCaps(ForgeCapabilities.ITEM_HANDLER).get(0);
                    result = ItemHelper.insertItemStacked(handler, 0, drop, false);
                    if (result.getCount() > 0) {
                        result = ItemHelper.insertItemStacked(this.ITEM_HANDLER, 0, drop, false);
                    }
                } else {
                    result = ItemHelper.insertItemStacked(this.ITEM_HANDLER, 0, drop, false);
                }
            } else {
                result = ItemHelper.insertItemStacked(mainFarm.ITEM_HANDLER, 1, drop, false);
            }

            if (result.getCount() > 0) {
                spawnItemStack(result, level, pos);
            }
        }
    }
}
