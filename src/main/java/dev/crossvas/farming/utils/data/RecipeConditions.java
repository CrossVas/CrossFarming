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

    public static void init() {
        CraftingHelper.register(new VanillaRecipeCondition.Serializer());
        CraftingHelper.register(new IC2RecipeCondition.Serializer());
        CraftingHelper.register(new IC2LoadedRecipeCondition.Serializer());
    }

    public static class VanillaRecipeCondition implements ICondition {

        @Override
        public ResourceLocation getID() {
            return ENABLE_VANILLA_RECIPES_ID;
        }

        @Override
        public boolean test(IContext context) {
            return CrossFarmingConfig.ENABLE_VANILLA_RECIPES.get();
        }

        public static class Serializer implements IConditionSerializer<VanillaRecipeCondition> {

            @Override
            public void write(JsonObject json, VanillaRecipeCondition value) {}

            @Override
            public VanillaRecipeCondition read(JsonObject json) {
                return new VanillaRecipeCondition();
            }

            @Override
            public ResourceLocation getID() {
                return ENABLE_VANILLA_RECIPES_ID;
            }
        }
    }

    public static class IC2RecipeCondition implements ICondition {

        @Override
        public ResourceLocation getID() {
            return ENABLE_IC2_RECIPES_ID;
        }

        @Override
        public boolean test(IContext context) {
            return CrossFarmingConfig.ENABLE_IC2_RECIPES.get();
        }

        public static class Serializer implements IConditionSerializer<IC2RecipeCondition> {

            @Override
            public void write(JsonObject json, IC2RecipeCondition value) {}

            @Override
            public IC2RecipeCondition read(JsonObject json) {
                return new IC2RecipeCondition();
            }

            @Override
            public ResourceLocation getID() {
                return ENABLE_IC2_RECIPES_ID;
            }
        }
    }

    public static class IC2LoadedRecipeCondition implements ICondition {

        @Override
        public ResourceLocation getID() {
            return IC2_LOADED;
        }

        @Override
        public boolean test(IContext context) {
            return ModList.get().isLoaded("ic2");
        }

        public static class Serializer implements IConditionSerializer<IC2LoadedRecipeCondition> {

            @Override
            public void write(JsonObject json, IC2LoadedRecipeCondition value) {}

            @Override
            public IC2LoadedRecipeCondition read(JsonObject json) {
                return new IC2LoadedRecipeCondition();
            }

            @Override
            public ResourceLocation getID() {
                return IC2_LOADED;
            }
        }
    }
}
