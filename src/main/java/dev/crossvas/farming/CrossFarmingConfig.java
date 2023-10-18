package dev.crossvas.farming;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class CrossFarmingConfig {

    public static ForgeConfigSpec COMMON_CONFIG;

    static ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec.BooleanValue ENABLE_IC2_RECIPES;
    public static ForgeConfigSpec.BooleanValue ENABLE_VANILLA_RECIPES;

    public static ForgeConfigSpec.IntValue CROP_FARM_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue CROP_FARM_ENERGY_USAGE;
    public static ForgeConfigSpec.IntValue CROP_COMBINE_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue CROP_COMBINE_ENERGY_USAGE;

    public static ForgeConfigSpec.IntValue TREE_FARM_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue TREE_FARM_ENERGY_USAGE;
    public static ForgeConfigSpec.IntValue TREE_COMBINE_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue TREE_COMBINE_ENERGY_USAGE;

    public static ForgeConfigSpec.IntValue INFERNAL_FARM_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue INFERNAL_FARM_ENERGY_USAGE;
    public static ForgeConfigSpec.IntValue INFERNAL_COMBINE_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue INFERNAL_COMBINE_ENERGY_USAGE;

    public static ForgeConfigSpec.IntValue PEAT_FARM_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue PEAT_FARM_ENERGY_USAGE;
    public static ForgeConfigSpec.IntValue PEAT_COMBINE_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue PEAT_COMBINE_ENERGY_USAGE;

    public static ForgeConfigSpec.IntValue RESIN_FARM_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue RESIN_FARM_ENERGY_USAGE;
    public static ForgeConfigSpec.IntValue RESIN_COMBINE_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue RESIN_COMBINE_ENERGY_USAGE;

    public static ForgeConfigSpec.IntValue GOURD_COMBINE_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue GOURD_COMBINE_ENERGY_USAGE;
    public static ForgeConfigSpec.IntValue SUCCULENT_COMBINE_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue SUCCULENT_COMBINE_ENERGY_USAGE;
    public static ForgeConfigSpec.IntValue COCOA_COMBINE_SECONDS_TICK;
    public static ForgeConfigSpec.IntValue COCOA_COMBINE_ENERGY_USAGE;

    public static void initConfig() {
        initCommon();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG, "cross_farming.toml");
    }

    public static void initCommon() {
        builder.push(CrossFarming.ID);
        builder.comment("Compat Settings").push("compat");
        ENABLE_IC2_RECIPES = builder.comment("Use IC2 items for farming blocks.").define("enable_ic2_recipes", false);
        ENABLE_VANILLA_RECIPES = builder.comment("Use vanilla items for farming blocks.").define("enable_vanilla_recipes", true);
        builder.pop();
        builder.comment("Crop Farm Settings").push("crop");
        CROP_FARM_SECONDS_TICK = builder.defineInRange("crop_farm_seconds_tick", 5, 1, Integer.MAX_VALUE);
        CROP_FARM_ENERGY_USAGE = builder.defineInRange("crop_farm_energy_usage", 20, 0, Integer.MAX_VALUE);
        CROP_COMBINE_SECONDS_TICK = builder.defineInRange("crop_combine_seconds_tick", 10, 1, Integer.MAX_VALUE);
        CROP_COMBINE_ENERGY_USAGE = builder.defineInRange("crop_combine_energy_usage", 30, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.comment("Tree Farm Settings").push("tree");
        TREE_FARM_SECONDS_TICK = builder.defineInRange("tree_farm_seconds_tick", 5, 1, Integer.MAX_VALUE);
        TREE_FARM_ENERGY_USAGE = builder.defineInRange("tree_farm_energy_usage", 20, 0, Integer.MAX_VALUE);
        TREE_COMBINE_SECONDS_TICK = builder.defineInRange("tree_combine_seconds_tick", 10, 1, Integer.MAX_VALUE);
        TREE_COMBINE_ENERGY_USAGE = builder.defineInRange("tree_combine_energy_usage", 30, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.comment("Infernal Farm Settings").push("infernal");
        INFERNAL_FARM_SECONDS_TICK = builder.defineInRange("infernal_farm_seconds_tick", 5, 1, Integer.MAX_VALUE);
        INFERNAL_FARM_ENERGY_USAGE = builder.defineInRange("infernal_farm_energy_usage", 20, 0, Integer.MAX_VALUE);
        INFERNAL_COMBINE_SECONDS_TICK = builder.defineInRange("infernal_combine_seconds_tick", 10, 1, Integer.MAX_VALUE);
        INFERNAL_COMBINE_ENERGY_USAGE = builder.defineInRange("infernal_combine_energy_usage", 30, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.comment("Peat Farm Settings").push("peat");
        PEAT_FARM_SECONDS_TICK = builder.defineInRange("peat_farm_seconds_tick", 5, 1, Integer.MAX_VALUE);
        PEAT_FARM_ENERGY_USAGE = builder.defineInRange("peat_farm_energy_usage", 20, 0, Integer.MAX_VALUE);
        PEAT_COMBINE_SECONDS_TICK = builder.defineInRange("peat_combine_seconds_tick", 10, 1, Integer.MAX_VALUE);
        PEAT_COMBINE_ENERGY_USAGE = builder.defineInRange("peat_combine_energy_usage", 30, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.comment("Resin Farm Settings").push("resin");
        RESIN_FARM_SECONDS_TICK = builder.defineInRange("resin_farm_seconds_tick", 5, 1, Integer.MAX_VALUE);
        RESIN_FARM_ENERGY_USAGE = builder.defineInRange("resin_farm_energy_usage", 20, 0, Integer.MAX_VALUE);
        RESIN_COMBINE_SECONDS_TICK = builder.defineInRange("resin_combine_seconds_tick", 60, 1, Integer.MAX_VALUE);
        RESIN_COMBINE_ENERGY_USAGE = builder.defineInRange("resin_combine_energy_usage", 30, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.comment("Gourd Farm Settings").push("gourd");
        GOURD_COMBINE_SECONDS_TICK = builder.defineInRange("gourd_combine_seconds_tick", 20, 1, Integer.MAX_VALUE);
        GOURD_COMBINE_ENERGY_USAGE = builder.defineInRange("gourd_combine_energy_usage", 30, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.comment("Succulent Farm Settings").push("succulent");
        SUCCULENT_COMBINE_SECONDS_TICK = builder.defineInRange("succulent_combine_seconds_tick", 30, 1, Integer.MAX_VALUE);
        SUCCULENT_COMBINE_ENERGY_USAGE = builder.defineInRange("succulent_combine_energy_usage", 30, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.comment("Cocoa Farm Settings").push("cocoa");
        COCOA_COMBINE_SECONDS_TICK = builder.defineInRange("cocoa_combine_seconds_tick", 10, 1, Integer.MAX_VALUE);
        COCOA_COMBINE_ENERGY_USAGE = builder.defineInRange("cocoa_combine_energy_usage", 30, 0, Integer.MAX_VALUE);
        builder.pop();
        COMMON_CONFIG = builder.build();
    }
}
