package dev.crossvas.farming.utils.data;

import com.google.gson.JsonObject;
import dev.crossvas.farming.CrossFarming;
import dev.crossvas.farming.CrossFarmingConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.fml.ModList;

public class RecipeConditions {

    public static ResourceLocation ENABLE_VANILLA_RECIPES_ID = new ResourceLocation(CrossFarming.ID, "enable_vanilla_recipes");
    public static ResourceLocation ENABLE_IC2_RECIPES_ID = new ResourceLocation(CrossFarming.ID, "enable_ic2_recipes");
    public static ResourceLocation IC2_LOADED = new ResourceLocation(CrossFarming.ID, "ic2_loaded");

    public static ICondition VANILLA_RECIPES = new CommonRecipeCondition(ENABLE_VANILLA_RECIPES_ID, CrossFarmingConfig.ENABLE_VANILLA_RECIPES.get());
    public static ICondition IC2_RECIPES = new CommonRecipeCondition(ENABLE_IC2_RECIPES_ID, CrossFarmingConfig.ENABLE_IC2_RECIPES.get());
    public static ICondition IC2 = new CommonRecipeCondition(IC2_LOADED, ModList.get().isLoaded("ic2"));

    public static void init() {
        CraftingHelper.register(new CommonConditionSerializer<>(VANILLA_RECIPES));
        CraftingHelper.register(new CommonConditionSerializer<>(IC2_RECIPES));
        CraftingHelper.register(new CommonConditionSerializer<>(IC2));
    }

    public static class CommonRecipeCondition implements ICondition {

        ResourceLocation ID;
        boolean CONDITION;

        public CommonRecipeCondition(ResourceLocation id, boolean condition) {
            this.ID = id;
            this.CONDITION = condition;
        }

        @Override
        public ResourceLocation getID() {
            return this.ID;
        }

        public boolean getCondition() {
            return this.CONDITION;
        }

        @Override
        public boolean test(IContext iContext) {
            return this.CONDITION;
        }
    }

    public static class CommonConditionSerializer<T extends ICondition> implements IConditionSerializer<T> {

        T COMMON_CONDITION;

        public CommonConditionSerializer(T recipeCondition) {
            this.COMMON_CONDITION = recipeCondition;
        }

        @Override
        public void write(JsonObject jsonObject, T commonRecipeCondition) {}

        @Override
        public T read(JsonObject jsonObject) {
            return this.COMMON_CONDITION;
        }

        @Override
        public ResourceLocation getID() {
            return this.COMMON_CONDITION.getID();
        }
    }
}
