package dev.crossvas.farming;

import dev.crossvas.farming.utils.data.DataGenerator;
import dev.crossvas.farming.utils.data.providers.RecipeConditions;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CrossFarming.ID)
public class CrossFarming {

    public static final String ID = "cross_farming";

    public static final CreativeModeTab TAB = new CreativeModeTab(CrossFarming.ID) {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(CrossFarmingData.CROP_FARM_BLOCK.get());
        }
    };

    public CrossFarming() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        CrossFarmingData.initData(bus);
        CrossFarmingConfig.initConfig();
        RecipeConditions.init();
        bus.addListener(DataGenerator::getData);
    }
}
