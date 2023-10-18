package dev.crossvas.farming.gui.menus;

import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blockentities.SucculentCombineBlockEntity;
import dev.crossvas.farming.gui.menus.base.IEnergyHolderMenu;
import dev.crossvas.farming.gui.slots.SlotReadOnly;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class SucculentCombineMenu extends AbstractContainerMenu implements IEnergyHolderMenu {

    public final SucculentCombineBlockEntity SUCCULENT_COMBINE_TILE;
    public final Level level;
    public final ContainerData data;

    public SucculentCombineMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public SucculentCombineMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(CrossFarmingData.SUCCULENT_COMBINE_MENU.get(), id);
        checkContainerSize(inv, 21);
        SUCCULENT_COMBINE_TILE = (SucculentCombineBlockEntity) entity;
        this.level = inv.player.level;
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.SUCCULENT_COMBINE_TILE.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlotsTest(handler, 8, 17, 3, 7, 0);
        });
        this.addDataSlots(data);
    }

    protected void addSlotsTest(IItemHandler handler, int x, int y, int slotRow, int slotColumn, int previousIndexes) {
        for(int row = 0; row < slotRow; ++row) {
            for(int column = 0; column < slotColumn; ++column) {
                this.addSlot(new SlotReadOnly(handler, row * slotColumn + column + previousIndexes, x + column * 18, y + row * 18));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, SUCCULENT_COMBINE_TILE.getBlockPos()), player, CrossFarmingData.SUCCULENT_COMBINE_BLOCK.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
