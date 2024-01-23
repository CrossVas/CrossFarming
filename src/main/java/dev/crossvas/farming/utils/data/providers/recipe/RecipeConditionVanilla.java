package dev.crossvas.farming.utils.data.providers.recipe;

import com.google.gson.JsonObject;
import dev.crossvas.farming.CrossFarmingConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class RecipeConditionVanilla extends RecipeConditionBase {

    public RecipeConditionVanilla() {
        super(RecipeConditions.ENABLE_VANILLA_RECIPES_ID, CrossFarmingConfig.ENABLE_VANILLA_RECIPES.get());
    }

    public static class Serializer implements IConditionSerializer<RecipeConditionVanilla> {

        @Override
        public void write(JsonObject jsonObject, RecipeConditionVanilla ic2RecipeCondition) {}

        @Override
        public RecipeConditionVanilla read(JsonObject jsonObject) {
            return new RecipeConditionVanilla();
        }

        @Override
        public ResourceLocation getID() {
            return RecipeConditions.ENABLE_VANILLA_RECIPES_ID;
        }
    }
}
