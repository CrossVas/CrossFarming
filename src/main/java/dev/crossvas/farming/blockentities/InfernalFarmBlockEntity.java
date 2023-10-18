package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.InfernalFarmMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

public class InfernalFarmBlockEntity extends BaseBlockEntity {

    public InfernalFarmBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.INFERNAL_FARM_TILE.get(), pPos, pBlockState);
        initFarmSidedCaps();
        this.ENERGY_USAGE = CrossFarmingConfig.INFERNAL_FARM_ENERGY_USAGE.get();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.infernal");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new InfernalFarmMenu(pContainerId, pPlayerInventory, this, this.data);
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
                    case 0 -> stack.is(CrossFarmingData.CustomTags.INFERNAL_FARM_SOIL);
                    case 1 -> stack.is(CrossFarmingData.CustomTags.INFERNAL_FARM_SEEDS);
                    default -> super.isItemValid(slot, stack);
                };
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

        if (hasEnergyToWork()) {
            tick++;
            if (tick == 20) {
                tick = 0;
                seconds++;

                ItemStack soilStack = null;
                if (this.ITEM_HANDLER.getStackInSlot(0) != null) {
                    soilStack = this.ITEM_HANDLER.getStackInSlot(0);
                }

                ItemStack seedStack = null;
                if (this.ITEM_HANDLER.getStackInSlot(1) != null) {
                    seedStack = this.ITEM_HANDLER.getStackInSlot(1);
                }

                if (platformBuilt && seconds == CrossFarmingConfig.INFERNAL_FARM_SECONDS_TICK.get()) {
                    if (seedStack.is(CrossFarmingData.CustomTags.INFERNAL_FARM_SEEDS)) {
                        for (BlockPos checkPos : getWorkingSpace(getBlockPos(), farmRange)) {
                            if (!getWorkingSpace(getBlockPos(), farmArea).contains(checkPos) && level.getBlockState(checkPos).is(CrossFarmingData.CustomTags.INFERNAL_FARM_SOIL_BLOCK)) {
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

                if (!platformBuilt || seconds == CrossFarmingConfig.INFERNAL_FARM_SECONDS_TICK.get()) {
                    seconds = 0;
                    for (BlockPos pos : getWorkingSpace(getBlockPos(), farmRange)) {
                        if (!getWorkingSpace(getBlockPos(), farmArea).contains(pos)) {
                            if (hasEnergyToWork() && soilStack.is(CrossFarmingData.CustomTags.INFERNAL_FARM_SOIL)) {
                                if (shouldReplace(pos)) {
                                    soilStack.shrink(1);
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
            }
        }
    }

    public boolean shouldReplace(BlockPos pos) {
        BlockState state = this.level.getBlockState(pos);
        if (!state.is(Blocks.SOUL_SAND)) {
            level.destroyBlock(pos, true);
            level.setBlock(pos, Blocks.SOUL_SAND.defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1F, 1F);
            this.extractEnergy();
            setChanged();
            return true;
        }
        return false;
    }

    public boolean shouldPlant(BlockPos pos, Block cropBlock) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(CrossFarmingData.CustomTags.INFERNAL_COMBINE_HARVESTABLE)) {
            level.setBlock(pos, cropBlock.defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1F, 1F);
            this.extractEnergy();
            setChanged();
            return true;
        }
        return false;
    }
}
