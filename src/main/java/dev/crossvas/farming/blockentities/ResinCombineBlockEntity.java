package dev.crossvas.farming.blockentities;

import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.base.BaseBlockEntity;
import dev.crossvas.farming.gui.menus.ResinCombineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResinCombineBlockEntity extends BaseBlockEntity implements MenuProvider {

    public int seconds;

    public ResinCombineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrossFarmingData.RESIN_COMBINE_TILE.get(), pPos, pBlockState);
        initCombineSidedCaps();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.farm.resin.combine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ResinCombineMenu(pContainerId, pPlayerInventory, this, this.data);
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
                return stack.is(CrossFarmingData.CustomTags.RESIN_CROPS);
            }
        };
    }

    @Override
    public void onTick() {
        super.onTick();
    }
}
