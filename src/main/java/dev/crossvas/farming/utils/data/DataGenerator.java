package dev.crossvas.farming.utils.data;

import dev.crossvas.farming.utils.data.providers.DataRecipes;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DataGenerator {

    @SubscribeEvent
    public static void getData(GatherDataEvent e) {
        net.minecraft.data.DataGenerator gen = e.getGenerator();
        if (e.includeServer()) {
            gen.addProvider(e.includeServer(), new DataRecipes(gen));
        }
    }
}
