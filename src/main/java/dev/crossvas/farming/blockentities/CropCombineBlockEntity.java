package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.CropCombineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CropCombineBlockEntity extends BaseBlockEntity {

    public CropCombineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.CROP_COMBINE_TILE.get(), pPos, pBlockState);
        this.ENERGY_USAGE = CrossFarmingConfig.CROP_COMBINE_ENERGY_USAGE.get();
        initCombineSidedCaps(this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.crop.combine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CropCombineMenu(pContainerId, pPlayerInventory, this, this.data);
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
                return stack.is(CrossFarmingData.CustomTags.FARM_CROPS) || stack.is(CrossFarmingData.CustomTags.FARM_SEEDS);
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
                    if (entity instanceof CropFarmBlockEntity farm) {
                        mainFarm = farm;
                    }
                }
            }

            if (!hasEnergyToWork()) {
                return;
            }
            if (mainFarm != null && seconds == CrossFarmingConfig.CROP_COMBINE_SECONDS_TICK.get()) {
                seconds = 0;
                for (BlockPos workPos : getWorkingSpace(mainFarm.getBlockPos(), farmRange)) {
                    if (!getWorkingSpace(mainFarm.getBlockPos(), farmArea).contains(workPos) && !getWaterBlockPos(mainFarm.getBlockPos(), farmRange).contains(workPos)) {
                        if (hasEnergyToWork()) {
                            if (shouldHarvest(workPos.above())) {
                                setChanged();
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean shouldHarvest(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) {
            return false;
        } else if (state.getBlock() instanceof IPlantable && state.getBlock() instanceof CropBlock) {
            if (state.getValue(CropBlock.AGE) == CropBlock.MAX_AGE) {
                collectDrops(pos, CrossFarmingData.CustomTags.FARM_CROPS);
                level.destroyBlock(pos, false);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                this.extractEnergy();
                return true;
            }
        }
        return false;
    }
}
