package dev.crossvas.farming.utils.data.providers;

import com.google.gson.JsonObject;
import dev.crossvas.farming.CrossFarming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class RecipeCondition implements ICondition {

    public static ResourceLocation CONDITION_ID;
    public static String ID;
    public static boolean CONDITION;

    public RecipeCondition(String id, boolean condition) {
        CONDITION_ID = new ResourceLocation(CrossFarming.ID, id);
        ID = id;
        CONDITION = condition;
    }

    @Override
    public ResourceLocation getID() {
        return CONDITION_ID;
    }

    @Override
    public boolean test(IContext context) {
        return CONDITION;
    }

    public IConditionSerializer<RecipeCondition> getSerializer(String id, boolean condition) {
        ResourceLocation ID = new ResourceLocation(CrossFarming.ID, id);
        return new IConditionSerializer<>() {
            @Override
            public void write(JsonObject json, RecipeCondition value) {
            }

            @Override
            public RecipeCondition read(JsonObject json) {
                return new RecipeCondition(id, condition);
            }

            @Override
            public ResourceLocation getID() {
                return ID;
            }
        };
    }
}
