package dev.crossvas.farming.utils.helpers;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class ItemHelper {

    public static ItemStack insertItemStacked(IItemHandler inventory, int startIndex, @Nonnull ItemStack stack, boolean simulate) {
        if (inventory != null && !stack.isEmpty()) {
            if (!stack.isStackable()) {
                return insertItem(inventory, stack, simulate);
            } else {
                int i;
                for(i = startIndex; i < inventory.getSlots(); i++) {
                    ItemStack slot = inventory.getStackInSlot(i);
                    if (ItemHandlerHelper.canItemStacksStackRelaxed(slot, stack)) {
                        stack = inventory.insertItem(i, stack, simulate);
                        if (stack.isEmpty()) {
                            break;
                        }
                    }
                }

                if (!stack.isEmpty()) {
                    for(i = startIndex; i < inventory.getSlots(); i++) {
                        if (inventory.getStackInSlot(i).isEmpty()) {
                            stack = inventory.insertItem(i, stack, simulate);
                            if (stack.isEmpty()) {
                                break;
                            }
                        }
                    }
                }

                return stack;
            }
        } else {
            return stack;
        }
    }

    public static ItemStack insertItem(IItemHandler dest, @Nonnull ItemStack stack, boolean simulate) {
        if (dest != null && !stack.isEmpty()) {
            for(int i = 0; i < dest.getSlots(); i++) {
                stack = dest.insertItem(i, stack, simulate);
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return stack;
    }
}
