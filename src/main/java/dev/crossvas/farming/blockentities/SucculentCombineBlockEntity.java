package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingConfig;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.SucculentCombineMenu;
import dev.crossvas.farming.utils.CustomTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SucculentCombineBlockEntity extends BaseBlockEntity {

    public SucculentCombineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.SUCCULENT_COMBINE_TILE.get(), pPos, pBlockState);
        initCombineSidedCaps(this);
        this.ENERGY_USAGE = CrossFarmingConfig.SUCCULENT_COMBINE_ENERGY_USAGE.get();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.succulent.combine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new SucculentCombineMenu(pContainerId, pPlayerInventory, this, this.data);
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
                return stack.is(CustomTags.ITEM_SUCCULENT_HARVESTABLE);
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

            if (!hasEnergyToWork()) {
                return;
            }

            if (seconds == CrossFarmingConfig.SUCCULENT_COMBINE_SECONDS_TICK.get()) {
                seconds = 0;
                for (BlockPos workPos : getWorkingSpace(getBlockPos().above(), farmRange)) {
                    if (hasEnergyToWork() && shouldHarvest(workPos.above())) {
                        this.extractEnergy();
                    }
                }
            }
        }
    }

    public boolean shouldHarvest(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(CustomTags.BLOCK_SUCCULENT_HARVESTABLE)) {
            collectDrops(pos, CustomTags.ITEM_SUCCULENT_HARVESTABLE);
            this.extractEnergy();
            level.destroyBlock(pos, false);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            return true;
        }
        return false;
    }
}
