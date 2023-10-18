package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.CropFarmMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CropFarmBlockEntity extends BaseBlockEntity implements MenuProvider {

    public CropFarmBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(CrossFarmingData.CROP_FARM_TILE.get(), pWorldPosition, pBlockState);
        this.ENERGY_USAGE = CrossFarmingConfig.CROP_FARM_ENERGY_USAGE.get();
        initFarmSidedCaps();
    }

    @Override
    public void init() {
        super.init();
        soilCounter = 0;
        seedCounter = 0;
        waterCounter = 0;
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.crop");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CropFarmMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected boolean hasSidedCaps() {
        return true;
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return switch (slot) {
                    case 0 -> stack.is(CrossFarmingData.CustomTags.FARM_SOIL);
                    case 1 -> stack.is(CrossFarmingData.CustomTags.FARM_SEEDS);
                    default -> super.isItemValid(slot, stack);
                };
            }
        };
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

            ItemStack dirtStack = null;
            if (this.ITEM_HANDLER.getStackInSlot(0) != null) {
                dirtStack = this.ITEM_HANDLER.getStackInSlot(0);
            }

            ItemStack seedStack = null;
            if (this.ITEM_HANDLER.getStackInSlot(1) != null) {
                seedStack = this.ITEM_HANDLER.getStackInSlot(1);
            }

             // check for seeds
            if (platformBuilt && seconds == CrossFarmingConfig.CROP_FARM_SECONDS_TICK.get()) {
                if (seedStack.is(CrossFarmingData.CustomTags.FARM_SEEDS)) {
                    for (BlockPos checkPos : getWorkingSpace(getBlockPos(), farmRange)) {
                        if (!getWorkingSpace(getBlockPos(), farmArea).contains(checkPos) && !getWaterBlockPos(getBlockPos(), farmRange).contains(checkPos) && level.getBlockState(checkPos).is(CrossFarmingData.CustomTags.FARM_SOIL_BLOCK)) {
                            if (hasEnergyToWork()) {
                                if (seedStack.getItem() instanceof BlockItem blockItem) {
                                    Block cropBlock = blockItem.getBlock();
                                    if (shouldPlant(checkPos.above(), cropBlock)) {
                                        seedStack.shrink(1);
                                        this.seedCounter++;
                                        if (this.seedCounter == 9) {
                                            this.seedCounter = 0;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!platformBuilt || seconds == CrossFarmingConfig.CROP_FARM_SECONDS_TICK.get()) {
                seconds = 0;
                for (BlockPos pos : getWorkingSpace(getBlockPos(), farmRange)) {
                    if (!getWorkingSpace(getBlockPos(), farmArea).contains(pos) && !getWaterBlockPos(getBlockPos(), farmRange).contains(pos)) {
                        if (hasEnergyToWork() && dirtStack.is(CrossFarmingData.CustomTags.FARM_SOIL)) {
                            if (shouldReplace(pos)) {
                                dirtStack.shrink(1);
                                this.soilCounter++;
                                if (this.soilCounter == 9) {
                                    this.soilCounter = 0;
                                    break;
                                }
                            } else {
                                platformBuilt = true;
                            }
                        }
                    }
                }
            }

            if (!platformBuilt || seconds == 2) {
                for (BlockPos water : getWaterBlockPos(getBlockPos(), farmRange)) {
                    if (hasEnergyToWork()) {
                        if (shouldReplaceWater(water)) {
                            this.waterCounter++;
                            if (waterCounter == 8) {
                                waterCounter = 0;
                                break;
                            }
                        } else {
                            platformBuilt = true;
                        }
                    }
                }
            }
        }
    }

    public boolean shouldReplaceWater(BlockPos pos) {
        BlockState state = this.level.getBlockState(pos);
        if (!state.is(Blocks.WATER) && hasWaterSides(pos, Blocks.FARMLAND) && hasEnergyToWork()) {
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1F, 1F);
            this.extractEnergy();
            setChanged();
            return true;
        }
        return false;
    }

    public boolean shouldReplace(BlockPos pos) {
        BlockState state = this.level.getBlockState(pos);
        if (!state.is(Blocks.FARMLAND)) {
            level.destroyBlock(pos, true);
            level.setBlock(pos, Blocks.FARMLAND.defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1F, 1F);
            this.extractEnergy();
            setChanged();
            return true;
        }
        return false;
    }

    public boolean shouldPlant(BlockPos pos, Block cropBlock) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(BlockTags.CROPS)) {
            level.setBlock(pos, cropBlock.defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1F, 1F);
            this.extractEnergy();
            setChanged();
            return true;
        }
        return false;
    }

}
