package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.CocoaCombineMenu;
import dev.crossvas.farming.utils.CustomTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CocoaCombineBlockEntity extends BaseBlockEntity {

    private int cocoaCounter;

    public CocoaCombineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.COCOA_COMBINE_TILE.get(), pPos, pBlockState);
        initCombineSidedCaps(this);
        this.ENERGY_USAGE = CrossFarmingConfig.COCOA_COMBINE_ENERGY_USAGE.get();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.cocoa.combine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CocoaCombineMenu(pContainerId, pPlayerInventory, this, this.data);
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
                return stack.is(CustomTags.ITEM_COCOA_HARVESTABLE);
            }
        };
    }

    @Override
    public void init() {
        super.init();
        cocoaCounter = 0;
    }

    @Override
    public void onTick() {
        super.onTick();

        if (hasEnergyToWork()) {
            tick++;
            if (tick == 20) {
                tick = 0;
                seconds++;
                List<BlockPos> COCOA_POSES = new ArrayList<>();
                for (BlockPos pos : getWorkingSpace(getBlockPos().above(2), farmRange)) {
                    BlockState posState = level.getBlockState(pos);
                    if (posState.getBlock() instanceof CocoaBlock) {
                        COCOA_POSES.add(pos.immutable());
                    }
                }

                if (seconds == CrossFarmingConfig.COCOA_COMBINE_SECONDS_TICK.get()) {
                    seconds = 0;
                    for (BlockPos workPos : COCOA_POSES) {
                        if (shouldReplace(workPos)) {
                            cocoaCounter++;
                            if (cocoaCounter == 9) {
                                cocoaCounter = 0;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean shouldReplace(BlockPos pos) {
        BlockState cocoaState = level.getBlockState(pos);
        if (cocoaState.getValue(CocoaBlock.AGE) == CocoaBlock.MAX_AGE) {
            collectDrops(pos, CustomTags.ITEM_COCOA_HARVESTABLE);
            this.extractEnergy();
            level.setBlockAndUpdate(pos, cocoaState.setValue(CocoaBlock.AGE, 0));
            return true;
        }

        return false;
    }
}
