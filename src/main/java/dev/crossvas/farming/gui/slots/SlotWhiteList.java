package dev.crossvas.farming.gui.slots;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SlotWhiteList extends SlotItemHandler {

    TagKey<Item> ALLOWED_TAG;

    public SlotWhiteList(IItemHandler itemHandler, int index, int xPosition, int yPosition, TagKey<Item> allowed_tag) {
        super(itemHandler, index, xPosition, yPosition);
        this.ALLOWED_TAG = allowed_tag;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is(this.ALLOWED_TAG);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return true;
    }
}
