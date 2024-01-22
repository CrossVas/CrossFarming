package dev.crossvas.farming;

import carbonconfiglib.CarbonConfig;
import carbonconfiglib.config.Config;
import carbonconfiglib.config.ConfigEntry;
import carbonconfiglib.config.ConfigHandler;
import carbonconfiglib.config.ConfigSection;
import carbonconfiglib.impl.ReloadMode;

public class CrossFarmingConfig {

    public static ConfigHandler HANDLER;

    public static ConfigEntry.BoolValue ENABLE_IC2_RECIPES;
    public static ConfigEntry.BoolValue ENABLE_VANILLA_RECIPES;

    public static ConfigEntry.IntValue CROP_FARM_SECONDS_TICK;
    public static ConfigEntry.IntValue CROP_FARM_ENERGY_USAGE;
    public static ConfigEntry.IntValue CROP_COMBINE_SECONDS_TICK;
    public static ConfigEntry.IntValue CROP_COMBINE_ENERGY_USAGE;

    public static ConfigEntry.IntValue TREE_FARM_SECONDS_TICK;
    public static ConfigEntry.IntValue TREE_FARM_ENERGY_USAGE;
    public static ConfigEntry.IntValue TREE_COMBINE_SECONDS_TICK;
    public static ConfigEntry.IntValue TREE_COMBINE_ENERGY_USAGE;

    public static ConfigEntry.IntValue INFERNAL_FARM_SECONDS_TICK;
    public static ConfigEntry.IntValue INFERNAL_FARM_ENERGY_USAGE;
    public static ConfigEntry.IntValue INFERNAL_COMBINE_SECONDS_TICK;
    public static ConfigEntry.IntValue INFERNAL_COMBINE_ENERGY_USAGE;

    public static ConfigEntry.IntValue PEAT_FARM_SECONDS_TICK;
    public static ConfigEntry.IntValue PEAT_FARM_ENERGY_USAGE;
    public static ConfigEntry.IntValue PEAT_COMBINE_SECONDS_TICK;
    public static ConfigEntry.IntValue PEAT_COMBINE_ENERGY_USAGE;

    public static ConfigEntry.IntValue RESIN_FARM_SECONDS_TICK;
    public static ConfigEntry.IntValue RESIN_FARM_ENERGY_USAGE;
    public static ConfigEntry.IntValue RESIN_COMBINE_ENERGY_USAGE;

    public static ConfigEntry.IntValue GOURD_COMBINE_SECONDS_TICK;
    public static ConfigEntry.IntValue GOURD_COMBINE_ENERGY_USAGE;
    public static ConfigEntry.IntValue SUCCULENT_COMBINE_SECONDS_TICK;
    public static ConfigEntry.IntValue SUCCULENT_COMBINE_ENERGY_USAGE;
    public static ConfigEntry.IntValue COCOA_COMBINE_SECONDS_TICK;
    public static ConfigEntry.IntValue COCOA_COMBINE_ENERGY_USAGE;

    public static void initConfig() {
        Config CONFIG = new Config(CrossFarming.ID);
        ConfigSection COMPAT = CONFIG.add("compat");
        COMPAT.setComment("Compat Settings");
        ENABLE_VANILLA_RECIPES = COMPAT.addBool("enable_vanilla_recipes", true).setRequiredReload(ReloadMode.WORLD);
        ENABLE_IC2_RECIPES = COMPAT.addBool("enable_ic2_recipes", true).setRequiredReload(ReloadMode.WORLD);

        HANDLER = CarbonConfig.CONFIGS.createConfig(CONFIG);
        HANDLER.register();

        ConfigSection CROP = CONFIG.add("crop");
        CROP.setComment("Crop Farm Config");
        CROP_FARM_SECONDS_TICK = CROP.addInt("crop_farm_seconds_tick", 5).setMin(0);
        CROP_FARM_ENERGY_USAGE = CROP.addInt("crop_farm_energy_usage", 20).setMin(0);
        CROP_COMBINE_SECONDS_TICK = CROP.addInt("crop_combine_seconds_tick", 10).setMin(0);
        CROP_COMBINE_ENERGY_USAGE = CROP.addInt("crop_combine_energy_usage", 30).setMin(0);

        ConfigSection TREE = CONFIG.add("tree");
        TREE.setComment("Tree Farm Config");
        TREE_FARM_SECONDS_TICK = TREE.addInt("tree_farm_seconds_tick", 5).setMin(0);
        TREE_FARM_ENERGY_USAGE = TREE.addInt("tree_farm_energy_usage", 20).setMin(0);
        TREE_COMBINE_SECONDS_TICK = TREE.addInt("tree_combine_seconds_tick", 2).setMin(0);
        TREE_COMBINE_ENERGY_USAGE = TREE.addInt("tree_combine_energy_usage", 30).setMin(0);

        ConfigSection INFERNAL = CONFIG.add("infernal");
        INFERNAL.setComment("Infernal Farm Config");
        INFERNAL_FARM_SECONDS_TICK = INFERNAL.addInt("infernal_farm_seconds_tick", 5).setMin(0);
        INFERNAL_FARM_ENERGY_USAGE = INFERNAL.addInt("infernal_farm_energy_usage", 20).setMin(0);
        INFERNAL_COMBINE_SECONDS_TICK = INFERNAL.addInt("infernal_combine_seconds_tick", 10).setMin(0);
        INFERNAL_COMBINE_ENERGY_USAGE = INFERNAL.addInt("infernal_combine_energy_usage", 30).setMin(0);

        ConfigSection PEAT = CONFIG.add("peat");
        PEAT.setComment("Resin Farm Config");
        PEAT_FARM_SECONDS_TICK = PEAT.addInt("peat_farm_seconds_tick", 5).setMin(0);
        PEAT_FARM_ENERGY_USAGE = PEAT.addInt("peat_farm_energy_usage", 20).setMin(0);
        PEAT_COMBINE_SECONDS_TICK = PEAT.addInt("peat_combine_seconds_tick", 10).setMin(0);
        PEAT_COMBINE_ENERGY_USAGE = PEAT.addInt("peat_combine_energy_usage", 30).setMin(0);

        ConfigSection RESIN = CONFIG.add("resin");
        RESIN.setComment("Resin Farm Config");
        RESIN_FARM_SECONDS_TICK = RESIN.addInt("resin_farm_seconds_tick", 5).setMin(0);
        RESIN_FARM_ENERGY_USAGE = RESIN.addInt("resin_farm_energy_usage", 20).setMin(0);
        RESIN_COMBINE_ENERGY_USAGE = RESIN.addInt("resin_combine_energy_usage", 30).setMin(0);

        ConfigSection GOURD = CONFIG.add("gourd");
        GOURD.setComment("Gourd Farm Config");
        GOURD_COMBINE_ENERGY_USAGE = GOURD.addInt("gourd_combine_seconds_tick", 20).setMin(0);
        GOURD_COMBINE_ENERGY_USAGE = GOURD.addInt("gourd_combine_energy_usage", 30).setMin(0);

        ConfigSection SUCCULENT = CONFIG.add("succulent");
        SUCCULENT.setComment("Succulent Farm Config");
        GOURD_COMBINE_ENERGY_USAGE = SUCCULENT.addInt("succulent_combine_seconds_tick", 30).setMin(0);
        GOURD_COMBINE_ENERGY_USAGE = SUCCULENT.addInt("succulent_combine_energy_usage", 30).setMin(0);

        ConfigSection COCOA = CONFIG.add("cocoa");
        COCOA.setComment("Cocoa Farm Config");
        COCOA_COMBINE_ENERGY_USAGE = COCOA.addInt("cocoa_combine_seconds_tick", 10).setMin(0);
        COCOA_COMBINE_ENERGY_USAGE = COCOA.addInt("cocoa_combine_energy_usage", 30).setMin(0);
    }
}
