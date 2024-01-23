package dev.crossvas.farming.utils.data.providers.recipe;

import com.google.gson.JsonObject;
import dev.crossvas.farming.CrossFarmingConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class RecipeConditionIC2 extends RecipeConditionBase {

    public RecipeConditionIC2() {
        super(RecipeConditions.ENABLE_IC2_RECIPES_ID, CrossFarmingConfig.ENABLE_IC2_RECIPES.get());
    }

    public static class Serializer implements IConditionSerializer<RecipeConditionIC2> {

        @Override
        public void write(JsonObject jsonObject, RecipeConditionIC2 ic2RecipeCondition) {}

        @Override
        public RecipeConditionIC2 read(JsonObject jsonObject) {
            return new RecipeConditionIC2();
        }

        @Override
        public ResourceLocation getID() {
            return RecipeConditions.ENABLE_IC2_RECIPES_ID;
        }
    }
}
