package dev.crossvas.farming.utils.data;

import dev.crossvas.farming.utils.data.providers.BlockTagsDataProvider;
import dev.crossvas.farming.utils.data.providers.ItemTagsDataProvider;
import dev.crossvas.farming.utils.data.providers.LootTablesDataProvider;
import dev.crossvas.farming.utils.data.providers.RecipeDataProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DataGenerator {

    @SubscribeEvent
    public static void getData(GatherDataEvent e) {
        net.minecraft.data.DataGenerator gen = e.getGenerator();
        if (e.includeServer()) {
            gen.addProvider(e.includeServer(), new RecipeDataProvider(gen));
            gen.addProvider(e.includeServer(), new BlockTagsDataProvider(gen, e.getExistingFileHelper()));
            gen.addProvider(e.includeServer(), new ItemTagsDataProvider(gen, e.getExistingFileHelper()));
            gen.addProvider(e.includeServer(), new LootTablesDataProvider(gen));
        }
    }
}
