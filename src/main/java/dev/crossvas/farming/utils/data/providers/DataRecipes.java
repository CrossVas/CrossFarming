package dev.crossvas.farming.utils.data.providers;

import dev.crossvas.farming.CrossFarmingData;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class DataRecipes extends RecipeProvider implements IConditionBuilder {

    public static ResourceLocation CROP_FARM_BLOCK = CrossFarmingData.CROP_FARM_BLOCK.getId();
    public static ResourceLocation CROP_COMBINE_BLOCK = CrossFarmingData.CROP_COMBINE_BLOCK.getId();
    public static ResourceLocation TREE_FARM_BLOCK = CrossFarmingData.TREE_FARM_BLOCK.getId();
    public static ResourceLocation TREE_COMBINE_BLOCK = CrossFarmingData.TREE_COMBINE_BLOCK.getId();
    public static ResourceLocation PEAT_BOG_FARM_BLOCK = CrossFarmingData.PEAT_BOG_FARM_BLOCK.getId();
    public static ResourceLocation PEAT_BOG_COMBINE_BLOCK = CrossFarmingData.PEAT_BOG_COMBINE_BLOCK.getId();
    public static ResourceLocation PEAT_BOG_BLOCK = CrossFarmingData.PEAT_BOG_BLOCK.getId();
    public static ResourceLocation RESIN_FARM_BLOCK = CrossFarmingData.RESIN_FARM_BLOCK.getId();
    public static ResourceLocation RESIN_COMBINE_BLOCK = CrossFarmingData.RESIN_COMBINE_BLOCK.getId();

    public DataRecipes(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        buildCommonRecipes(pFinishedRecipeConsumer);
        buildVanillaRecipes(pFinishedRecipeConsumer);
        buildIC2Recipes(pFinishedRecipeConsumer);
    }

    private void buildCommonRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shaped(CrossFarmingData.INFERNAL_FARM_BLOCK.get())
                .pattern("WGW")
                .pattern("GFG")
                .pattern("WGW")
                .define('G', Items.GLASS)
                .define('W', Items.NETHER_WART)
                .define('F', CrossFarmingData.CROP_FARM_BLOCK.get())
                .unlockedBy("crop_farm", InventoryChangeTrigger.TriggerInstance.hasItems(CrossFarmingData.CROP_FARM_BLOCK.get())).save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(CrossFarmingData.INFERNAL_COMBINE_BLOCK.get())
                .pattern("WGW")
                .pattern("GFG")
                .pattern("WGW")
                .define('G', Items.GLASS)
                .define('W', Items.NETHER_WART)
                .define('F', CrossFarmingData.CROP_COMBINE_BLOCK.get())
                .unlockedBy("crop_combine", InventoryChangeTrigger.TriggerInstance.hasItems(CrossFarmingData.CROP_COMBINE_BLOCK.get())).save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(CrossFarmingData.GOURD_COMBINE_BLOCK.get())
                .pattern("MGP")
                .pattern("GFG")
                .pattern("PGM")
                .define('G', Items.GLASS)
                .define('M', Blocks.MELON)
                .define('P', Blocks.PUMPKIN)
                .define('F', CrossFarmingData.TREE_COMBINE_BLOCK.get())
                .unlockedBy("tree_combine", InventoryChangeTrigger.TriggerInstance.hasItems(CrossFarmingData.TREE_COMBINE_BLOCK.get())).save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(CrossFarmingData.SUCCULENT_COMBINE_BLOCK.get())
                .pattern("CGC")
                .pattern("GFG")
                .pattern("CGC")
                .define('G', Items.GLASS)
                .define('C', Blocks.CACTUS)
                .define('F', CrossFarmingData.TREE_COMBINE_BLOCK.get())
                .unlockedBy("tree_combine", InventoryChangeTrigger.TriggerInstance.hasItems(CrossFarmingData.TREE_COMBINE_BLOCK.get())).save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(CrossFarmingData.COCOA_COMBINE_BLOCK.get())
                .pattern("CSC")
                .pattern("GFG")
                .pattern("CGC")
                .define('G', Items.GLASS)
                .define('C', Items.COCOA_BEANS)
                .define('S', Items.SHEARS)
                .define('F', CrossFarmingData.TREE_COMBINE_BLOCK.get())
                .unlockedBy("tree_combine", InventoryChangeTrigger.TriggerInstance.hasItems(CrossFarmingData.TREE_COMBINE_BLOCK.get())).save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(CrossFarmingData.PEAT_BOG_BLOCK.get(), 6)
                .pattern("DSD")
                .pattern("SWS")
                .pattern("DSD")
                .define('D', Blocks.DIRT)
                .define('S', Blocks.SAND)
                .define('W', Fluids.WATER.getBucket())
                .unlockedBy("sand", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.SAND)).save(pFinishedRecipeConsumer);
    }

    public static void buildIC2Recipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ICondition IC2_RECIPES = new RecipeConditions.IC2RecipeCondition();
        ICondition IC2_LOADED = new RecipeConditions.IC2LoadedRecipeCondition();
        ConditionalRecipe.builder().addCondition(IC2_RECIPES).addCondition(IC2_LOADED).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.CROP_FARM_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("M#M")
                        .define('G', Blocks.GLASS)
                        .define('D', Items.DIAMOND)
                        .define('S', Items.WHEAT_SEEDS)
                        .define('P', Blocks.PISTON)
                        .define('M', ic2.core.platform.registries.IC2Blocks.MACHINE_BLOCK)
                        .define('#', ic2.core.platform.registries.IC2Items.ADVANCED_CIRCUIT)
                        .unlockedBy("hoe", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_HOE)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, modLoc(CROP_FARM_BLOCK));

        ConditionalRecipe.builder().addCondition(IC2_RECIPES).addCondition(IC2_LOADED).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.CROP_COMBINE_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("M#M")
                        .define('G', Blocks.GLASS)
                        .define('D', ic2.core.platform.registries.IC2Items.MAGNET)
                        .define('S', Items.GOLDEN_HOE)
                        .define('P', Blocks.STICKY_PISTON)
                        .define('M', ic2.core.platform.registries.IC2Blocks.MACHINE_BLOCK)
                        .define('#', ic2.core.platform.registries.IC2Items.ADVANCED_CIRCUIT)
                        .unlockedBy("hoe", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_HOE)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, modLoc(CROP_COMBINE_BLOCK));

        ConditionalRecipe.builder().addCondition(IC2_RECIPES).addCondition(IC2_LOADED).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.TREE_FARM_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("M#M")
                        .define('G', Blocks.GLASS)
                        .define('D', Items.DIAMOND)
                        .define('S', ItemTags.SAPLINGS)
                        .define('P', Blocks.PISTON)
                        .define('M', ic2.core.platform.registries.IC2Blocks.MACHINE_BLOCK)
                        .define('#', ic2.core.platform.registries.IC2Items.ADVANCED_CIRCUIT)
                        .unlockedBy("axe", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_AXE)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, modLoc(TREE_FARM_BLOCK));

        ConditionalRecipe.builder().addCondition(IC2_RECIPES).addCondition(IC2_LOADED).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.TREE_COMBINE_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("M#M")
                        .define('G', Blocks.GLASS)
                        .define('D', ic2.core.platform.registries.IC2Items.MAGNET)
                        .define('S', Items.GOLDEN_AXE)
                        .define('P', Blocks.STICKY_PISTON)
                        .define('M', ic2.core.platform.registries.IC2Blocks.MACHINE_BLOCK)
                        .define('#', ic2.core.platform.registries.IC2Items.ADVANCED_CIRCUIT)
                        .unlockedBy("axe", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_AXE)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, modLoc(TREE_COMBINE_BLOCK));

        ConditionalRecipe.builder().addCondition(IC2_RECIPES).addCondition(IC2_LOADED).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.PEAT_BOG_FARM_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("M#M")
                        .define('G', Blocks.GLASS)
                        .define('D', Items.DIAMOND)
                        .define('S', CrossFarmingData.PEAT_ITEM.get())
                        .define('P', Blocks.PISTON)
                        .define('M', ic2.core.platform.registries.IC2Blocks.MACHINE_BLOCK)
                        .define('#', ic2.core.platform.registries.IC2Items.ADVANCED_CIRCUIT)
                        .unlockedBy("shovel", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_SHOVEL)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, modLoc(PEAT_BOG_FARM_BLOCK));

        ConditionalRecipe.builder().addCondition(IC2_RECIPES).addCondition(IC2_LOADED).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.PEAT_BOG_COMBINE_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("M#M")
                        .define('G', Blocks.GLASS)
                        .define('D', ic2.core.platform.registries.IC2Items.MAGNET)
                        .define('S', Items.GOLDEN_SHOVEL)
                        .define('P', Blocks.STICKY_PISTON)
                        .define('M', ic2.core.platform.registries.IC2Blocks.MACHINE_BLOCK)
                        .define('#', ic2.core.platform.registries.IC2Items.ADVANCED_CIRCUIT)
                        .unlockedBy("shovel", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLDEN_SHOVEL)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, modLoc(PEAT_BOG_COMBINE_BLOCK));

        ConditionalRecipe.builder().addCondition(IC2_RECIPES).addCondition(IC2_LOADED).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.PEAT_BOG_BLOCK.get(), 8)
                        .pattern("DSD")
                        .pattern("SWS")
                        .pattern("DSD")
                        .define('D', Blocks.DIRT)
                        .define('S', Blocks.SAND)
                        .define('W', ic2.core.platform.registries.IC2Items.CELL_WATER)
                        .unlockedBy("sand", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.SAND)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, modLoc(PEAT_BOG_BLOCK));

        ConditionalRecipe.builder().addCondition(IC2_LOADED).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.RESIN_FARM_BLOCK.get())
                        .pattern("TGT")
                        .pattern("GFG")
                        .pattern("TGT")
                        .define('G', Blocks.GLASS)
                        .define('F', CrossFarmingData.CROP_FARM_BLOCK.get())
                        .define('T', ic2.core.platform.registries.IC2Items.TREETAP)
                        .unlockedBy("treetap", InventoryChangeTrigger.TriggerInstance.hasItems(ic2.core.platform.registries.IC2Items.TREETAP)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, modLoc(RESIN_FARM_BLOCK));

        ConditionalRecipe.builder().addCondition(IC2_LOADED).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.RESIN_COMBINE_BLOCK.get())
                        .pattern("TGT")
                        .pattern("GFG")
                        .pattern("TGT")
                        .define('G', Blocks.GLASS)
                        .define('F', CrossFarmingData.RESIN_FARM_BLOCK.get())
                        .define('T', ic2.core.platform.registries.IC2Items.ELECTRIC_TREETAP)
                        .unlockedBy("e_treetap", InventoryChangeTrigger.TriggerInstance.hasItems(ic2.core.platform.registries.IC2Items.ELECTRIC_TREETAP)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, modLoc(RESIN_COMBINE_BLOCK));
    }

    public static void buildVanillaRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ICondition VANILLA_RECIPE = new RecipeConditions.VanillaRecipeCondition();
        ConditionalRecipe.builder().addCondition(VANILLA_RECIPE).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.CROP_FARM_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("CRC")
                        .define('G', Items.GLASS)
                        .define('D', Tags.Items.GEMS_DIAMOND)
                        .define('R', Items.OXIDIZED_COPPER)
                        .define('P', Items.PISTON)
                        .define('S', Items.WHEAT_SEEDS)
                        .define('C', Items.STONE)
                        .unlockedBy("hoe", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_HOE)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, CROP_FARM_BLOCK);

        ConditionalRecipe.builder().addCondition(VANILLA_RECIPE).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.CROP_COMBINE_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("CRC")
                        .define('G', Items.GLASS)
                        .define('D', Tags.Items.GEMS_DIAMOND)
                        .define('R', Items.OXIDIZED_COPPER)
                        .define('P', Items.STICKY_PISTON)
                        .define('S', Items.GOLDEN_HOE)
                        .define('C', Items.STONE)
                        .unlockedBy("hoe", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_HOE)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, CROP_COMBINE_BLOCK);

        ConditionalRecipe.builder().addCondition(VANILLA_RECIPE).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.TREE_FARM_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("CRC")
                        .define('G', Items.GLASS)
                        .define('D', Tags.Items.GEMS_DIAMOND)
                        .define('R', Items.OXIDIZED_COPPER)
                        .define('P', Items.PISTON)
                        .define('S', ItemTags.SAPLINGS)
                        .define('C', Items.STONE)
                        .unlockedBy("axe", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_AXE)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, TREE_FARM_BLOCK);

        ConditionalRecipe.builder().addCondition(VANILLA_RECIPE).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.TREE_COMBINE_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("CRC")
                        .define('G', Items.GLASS)
                        .define('D', Tags.Items.GEMS_DIAMOND)
                        .define('R', Items.OXIDIZED_COPPER)
                        .define('P', Items.STICKY_PISTON)
                        .define('S', Items.GOLDEN_AXE)
                        .define('C', Items.STONE)
                        .unlockedBy("axe", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_AXE)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, TREE_COMBINE_BLOCK);

        ConditionalRecipe.builder().addCondition(VANILLA_RECIPE).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.PEAT_BOG_FARM_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("CRC")
                        .define('G', Items.GLASS)
                        .define('D', Tags.Items.GEMS_DIAMOND)
                        .define('R', Items.OXIDIZED_COPPER)
                        .define('P', Items.PISTON)
                        .define('S', CrossFarmingData.PEAT_ITEM.get())
                        .define('C', Items.STONE)
                        .unlockedBy("shovel", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_SHOVEL)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, PEAT_BOG_FARM_BLOCK);

        ConditionalRecipe.builder().addCondition(VANILLA_RECIPE).addRecipe(finishedRecipeConsumer ->
                ShapedRecipeBuilder.shaped(CrossFarmingData.PEAT_BOG_COMBINE_BLOCK.get())
                        .pattern("GDG")
                        .pattern("SPS")
                        .pattern("CRC")
                        .define('G', Items.GLASS)
                        .define('D', Tags.Items.GEMS_DIAMOND)
                        .define('R', Items.OXIDIZED_COPPER)
                        .define('P', Items.STICKY_PISTON)
                        .define('S', Items.GOLDEN_SHOVEL)
                        .define('C', Items.STONE)
                        .unlockedBy("shovel", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLDEN_SHOVEL)).save(finishedRecipeConsumer)
        ).build(pFinishedRecipeConsumer, PEAT_BOG_COMBINE_BLOCK);

    }

    public static ResourceLocation modLoc(ResourceLocation info) {
        return new ResourceLocation(info.getNamespace(), info.getPath() + "_ic2");
    }
}
