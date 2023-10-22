package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.TreeFarmMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
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

public class TreeFarmBlockEntity extends BaseBlockEntity {

    public int farmRange = 7;
    public int farmArea = 4;
    public int soilCounter;
    public int saplingCounter;

    public TreeFarmBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.TREE_FARM_TILE.get(), pPos, pBlockState);
        this.ENERGY_USAGE = CrossFarmingConfig.TREE_FARM_ENERGY_USAGE.get();
        initFarmSidedCaps(this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.tree");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new TreeFarmMenu(pContainerId, pPlayerInventory, this, this.data);
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
                    case 1 -> stack.is(CrossFarmingData.CustomTags.TREE_PLANTABLE);
                    default -> super.isItemValid(slot, stack);
                };
            }
        };
    }

    @Override
    public void init() {
        super.init();
        soilCounter = 0;
        saplingCounter = 0;
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

            ItemStack dirtStack = ItemStack.EMPTY;
            if (this.ITEM_HANDLER.getStackInSlot(0) != null) {
                dirtStack = this.ITEM_HANDLER.getStackInSlot(0);
            }

            // seeds slot
            ItemStack saplingStack = ItemStack.EMPTY;
            if (this.ITEM_HANDLER.getStackInSlot(1) != null) {
                saplingStack = this.ITEM_HANDLER.getStackInSlot(1);
            }

            if (platformBuilt && seconds == CrossFarmingConfig.TREE_FARM_SECONDS_TICK.get()) {
                for (BlockPos checkPos : getWorkingSpace(getBlockPos(), farmRange)) {
                    if (!getWorkingSpace(getBlockPos(), farmArea).contains(checkPos) && level.getBlockState(checkPos).is(CrossFarmingData.CustomTags.TREE_FARM_SOIL_BLOCK)) {
                        if (hasEnergyToWork() && saplingStack.is(ItemTags.SAPLINGS)) {
                            if (saplingStack.getItem() instanceof BlockItem blockItem) {
                                Block sapling = blockItem.getBlock();
                                if (shouldPlant(checkPos.above(), sapling)) {
                                    saplingStack.shrink(1);
                                    this.saplingCounter++;
                                    if (this.saplingCounter == 9) {
                                        saplingCounter = 0;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!platformBuilt || seconds == CrossFarmingConfig.TREE_FARM_SECONDS_TICK.get()) {
                seconds = 0;
                for (BlockPos pos : getWorkingSpace(getBlockPos(), farmRange)) {
                    if (!getWorkingSpace(getBlockPos(), farmArea).contains(pos)) {
                        if (hasEnergyToWork() && dirtStack.is(CrossFarmingData.CustomTags.TREE_FARM_SOIL)) {
                            if (shouldReplace(pos) ) {
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
        }
    }

    public boolean shouldReplace(BlockPos pos) {
        BlockState state = this.level.getBlockState(pos);
        if (!state.is(CrossFarmingData.CustomTags.TREE_FARM_SOIL_BLOCK)) {
            level.destroyBlock(pos, true);
            level.setBlock(pos, Blocks.DIRT.defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1F, 1F);
            this.extractEnergy();
            setChanged();
            return true;
        }
        return false;
    }

    public boolean shouldPlant(BlockPos pos, Block saplingBlock) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(BlockTags.SAPLINGS) && !state.is(BlockTags.LOGS)) {
            level.setBlock(pos, saplingBlock.defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1F, 1F);
            this.extractEnergy();
            setChanged();
            return true;
        }
        return false;
    }
}
