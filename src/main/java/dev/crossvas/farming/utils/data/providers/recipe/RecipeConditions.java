package dev.crossvas.farming.utils.data.providers.recipe;

import dev.crossvas.farming.CrossFarming;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class RecipeConditions {

    public static ResourceLocation ENABLE_VANILLA_RECIPES_ID = new ResourceLocation(CrossFarming.ID, "enable_vanilla_recipes");
    public static ResourceLocation ENABLE_IC2_RECIPES_ID = new ResourceLocation(CrossFarming.ID, "enable_ic2_recipes");
    public static ResourceLocation IC2_LOADED = new ResourceLocation(CrossFarming.ID, "ic2_loaded");

    public static ICondition VANILLA_RECIPES = new RecipeConditionVanilla();
    public static ICondition IC2_RECIPES = new RecipeConditionIC2();
    public static ICondition IC2 = new RecipeConditionIC2Loaded();

    public static void init() {
        CraftingHelper.register(new RecipeConditionVanilla.Serializer());
        CraftingHelper.register(new RecipeConditionIC2.Serializer());
        CraftingHelper.register(new RecipeConditionIC2Loaded.Serializer());
    }
}
