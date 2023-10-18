package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.PeatBogFarmMenu;
import dev.crossvas.farming.utils.helpers.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

import java.util.ArrayList;
import java.util.List;

public class PeatBogFarmBlockEntity extends BaseBlockEntity implements MenuProvider {

    public int soilCounter;
    public int waterCounter;

    public PeatBogFarmBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.PEAT_BOG_FARM_TILE.get(), pPos, pBlockState);
        initFarmSidedCaps();
        this.ENERGY_USAGE = CrossFarmingConfig.PEAT_FARM_ENERGY_USAGE.get();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.peat_bog");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new PeatBogFarmMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected boolean hasSidedCaps() {
        return true;
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return new ItemStackHandler(5) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return switch (slot) {
                    case 0 -> stack.is(CrossFarmingData.CustomTags.PEAT_FARM_SOIL);
                    case 1, 2, 3, 4 -> stack.is(CrossFarmingData.CustomTags.PEAT_FARM_WASTE);
                    default -> super.isItemValid(slot, stack);
                };
            }
        };
    }

    @Override
    public void init() {
        super.init();
        soilCounter = 0;
        waterCounter = 0;
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

            ItemStack soilStack = ItemStack.EMPTY;
            if (this.ITEM_HANDLER.getStackInSlot(0) != null) {
                soilStack = this.ITEM_HANDLER.getStackInSlot(0);
            }

            if (!platformBuilt || seconds == CrossFarmingConfig.PEAT_FARM_SECONDS_TICK.get()) {
                seconds = 0;
                for (BlockPos pos : getWorkingSpace(getBlockPos(), farmRange)) {
                    if (!getWorkingSpace(getBlockPos(), farmArea).contains(pos) && !getWaterBlockPos(getBlockPos(), farmRange).contains(pos)) {
                        if (hasEnergyToWork() && soilStack.is(CrossFarmingData.PEAT_BOG_BLOCK.get().asItem())) {
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

            if (!platformBuilt || seconds == 2) {
                for (BlockPos water : getWaterBlockPos(getBlockPos(), farmRange)) {
                    if (hasEnergyToWork()) {
                        if (shouldReplaceWater(water)) {
                            this.waterCounter++;
                            if (waterCounter == 12) {
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

    public boolean shouldReplace(BlockPos pos) {
        BlockState state = this.level.getBlockState(pos);
        if (state.is(Blocks.SAND) || state.is(Blocks.AIR)) {
            collectDrops(pos);
            level.destroyBlock(pos, false);
            level.setBlock(pos, CrossFarmingData.PEAT_BOG_BLOCK.get().defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1F, 1F);
            this.extractEnergy();
            setChanged();
            return true;
        }
        return false;
    }

    public boolean shouldReplaceWater(BlockPos pos) {
        BlockState state = this.level.getBlockState(pos);
        if (!state.is(Blocks.WATER) && hasWaterSides(pos, CrossFarmingData.PEAT_BOG_BLOCK.get())) {
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1F, 1F);
            this.extractEnergy();
            setChanged();
            return true;
        }
        return false;
    }

    protected void collectDrops(BlockPos pos) {
        for (ItemStack blockDrops : getBlockDrops(this.level, pos)) {
            ItemStack result = ItemStack.EMPTY;
            if (blockDrops.is(CrossFarmingData.CustomTags.PEAT_FARM_WASTE)) {
                result = ItemHelper.insertItemStacked(ITEM_HANDLER, blockDrops, 1, 5, false);
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

    @Override
    public List<BlockPos> getWaterBlockPos(BlockPos farmPos, int range) {
        int pX = farmPos.getX();
        int pY = farmPos.getY();
        int pZ = farmPos.getZ();
        List<BlockPos> waterPoses = new ArrayList<>();
        BlockPos topMostLeftPos = new BlockPos(pX - range + 1, pY, pZ - range + 1).immutable();
        BlockPos topMostRightPos = topMostLeftPos.east(12).immutable();
        BlockPos bottomMostRightPos = topMostRightPos.south(12).immutable();
        BlockPos bottomMostLeftPos = bottomMostRightPos.west(12).immutable();
        // main pos - top left
        waterPoses.add(topMostLeftPos);
        waterPoses.add(topMostLeftPos.east(3).immutable());
        waterPoses.add(topMostLeftPos.east(6).immutable());
        waterPoses.add(topMostLeftPos.east(9).immutable());
        // top line: most right pos - east(12)
        waterPoses.add(topMostRightPos);
        waterPoses.add(topMostRightPos.south(3).immutable());
        waterPoses.add(topMostRightPos.south(6).immutable());
        waterPoses.add(topMostRightPos.south(9).immutable());
        // bottom line: most right pos - south(12)
        waterPoses.add(bottomMostRightPos);
        waterPoses.add(bottomMostRightPos.west(3).immutable());
        waterPoses.add(bottomMostRightPos.west(6).immutable());
        waterPoses.add(bottomMostRightPos.west(9).immutable());
        // bottom line: most left pos - west(12)
        waterPoses.add(bottomMostLeftPos);
        waterPoses.add(bottomMostLeftPos.north(3).immutable());
        waterPoses.add(bottomMostLeftPos.north(6).immutable());
        waterPoses.add(bottomMostLeftPos.north(9).immutable());
        return waterPoses;
    }
}
