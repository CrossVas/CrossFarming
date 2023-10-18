package dev.crossvas.farming.items;

import dev.crossvas.farming.CrossFarming;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class PeatItem extends Item {

    public PeatItem() {
        super(new Item.Properties().tab(CrossFarming.TAB));
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 2000;
    }
}
