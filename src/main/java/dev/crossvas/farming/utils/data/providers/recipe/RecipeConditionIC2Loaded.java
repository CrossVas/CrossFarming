package dev.crossvas.farming.utils.data.providers.recipe;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.fml.ModList;

public class RecipeConditionIC2Loaded extends RecipeConditionBase {

    public RecipeConditionIC2Loaded() {
        super(RecipeConditions.IC2_LOADED, ModList.get().isLoaded("ic2"));
    }

    public static class Serializer implements IConditionSerializer<RecipeConditionIC2Loaded> {

        @Override
        public void write(JsonObject jsonObject, RecipeConditionIC2Loaded ic2RecipeCondition) {}

        @Override
        public RecipeConditionIC2Loaded read(JsonObject jsonObject) {
            return new RecipeConditionIC2Loaded();
        }

        @Override
        public ResourceLocation getID() {
            return RecipeConditions.IC2_LOADED;
        }
    }
}
