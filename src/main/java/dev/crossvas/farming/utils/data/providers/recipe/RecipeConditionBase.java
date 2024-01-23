package dev.crossvas.farming.utils.data.providers.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class RecipeConditionBase implements ICondition {

    ResourceLocation ID;
    boolean CONDITION;

    public RecipeConditionBase(ResourceLocation id, boolean condition) {
        this.ID = id;
        this.CONDITION = condition;
    }

    @Override
    public ResourceLocation getID() {
        return this.ID;
    }

    @Override
    public boolean test(IContext iContext) {
        return this.CONDITION;
    }
}
