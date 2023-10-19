package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.utils.helpers.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ResinFarmBlockEntity extends BaseBlockEntity {

    int extracted;
    int extractionSeconds;

    public ResinFarmBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.RESIN_FARM_TILE.get(), pPos, pBlockState);
        this.ENERGY_USAGE = CrossFarmingConfig.RESIN_FARM_ENERGY_USAGE.get();
    }

    @Override
    protected boolean hasSidedCaps() {
        return false;
    }

    @Override
    public void init() {
        super.init();
        extracted = 0;
        extractionSeconds = 0;
    }

    @Override
    public void onTick() {
        super.onTick();

        tick++;
        if (tick == 20) {
            tick = 0;
            seconds++;
            extractionSeconds++;

            for (BlockPos pos : aroundPoses) {
                BlockEntity entity = level.getBlockEntity(pos);
                if (entity != null) {
                    if (entity instanceof ResinCombineBlockEntity farm) {
                        mainFarm = farm;
                    }
                }
            }

            List<BlockPos> collectablePos = new ArrayList<>();
            if (seconds == 10) {
                seconds = 0;
                for (BlockPos workPos : getWorkingSpace(getBlockPos().above(), farmRange)) {
                    if (hasEnergyToWork()) {
                        if (level.getBlockState(workPos).getBlock() instanceof ic2.core.block.resource.RubberwoodLogBlock) {
                            if (level.getBlockState(workPos).getValue(ic2.core.block.resource.RubberwoodLogBlock.COLLECTABLE)) {
                                if (!collectablePos.contains(workPos)) {
                                    collectablePos.add(workPos.immutable());
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (extractionSeconds == CrossFarmingConfig.RESIN_FARM_SECONDS_TICK.get()) {
                extractionSeconds = 0;
                for (BlockPos collectable : collectablePos) {
                    if (shouldExtract(collectable)) {
                        setChanged();
                        extracted++;
                        if (extracted == 9) {
                            extracted = 0;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return null;
    }

    @Override
    public List<BlockPos> getWorkingSpace(BlockPos mainPos, int range) {
        List<BlockPos> returnList = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(mainPos.getX() - range, mainPos.getY(), mainPos.getZ() - range, mainPos.getX() + range, mainPos.getY() + range, mainPos.getZ() + range)) {
            if (hasLevel()) {
                if (!(level.getBlockEntity(pos) instanceof BaseBlockEntity)) {
                    returnList.add(pos.immutable());
                }
            }
        }
        return returnList;
    }

    public boolean shouldExtract(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Direction side = level.getBlockState(pos).getValue(ic2.core.block.resource.RubberwoodLogBlock.RESIN_FACING);
        if (state.getValue(ic2.core.block.resource.RubberwoodLogBlock.COLLECTABLE)) {
            ItemStack drop;
            RandomSource rand = RandomSource.create();
            int amount = rand.nextInt(3);
            level.setBlockAndUpdate(pos, state.setValue(ic2.core.block.resource.RubberwoodLogBlock.COLLECTABLE, false));
            drop = new ItemStack(ic2.core.platform.registries.IC2Items.STICKY_RESIN, amount);
            if (mainFarm == null) {
                Block.popResourceFromFace(level, pos.relative(side), side.getOpposite(), drop);
            } else {
                ItemStack insertedStack = ItemStack.EMPTY;
                if (drop.is(CrossFarmingData.CustomTags.RESIN_CROPS)) {
                    insertedStack = ItemHelper.insertItemStacked(mainFarm.ITEM_HANDLER, drop, 0, 21, false);
                }
                if (insertedStack.getCount() > 0) {
                    spawnItemStack(insertedStack, level, pos);
                }
            }
            extractEnergy();
            return true;
        }
        return false;
    }

    @Override
    public Component getDisplayName() {return null;}

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {return null;}
}
