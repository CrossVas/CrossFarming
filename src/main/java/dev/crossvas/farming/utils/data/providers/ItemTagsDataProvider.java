package dev.crossvas.farming.utils.data.providers;

import dev.crossvas.farming.CrossFarming;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.utils.CustomTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ItemTagsDataProvider extends ItemTagsProvider {

    public ItemTagsDataProvider(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper) {
        super(gen, new BlockTagsDataProvider(gen, existingFileHelper), CrossFarming.ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        addCompatTags();
        this.tag(CustomTags.ITEM_TREE_SOIL).addTag(ItemTags.DIRT);
        this.tag(CustomTags.ITEM_TREE_HARVESTABLE).add(Items.ACACIA_LOG, Items.BIRCH_LOG, Items.JUNGLE_LOG, Items.OAK_LOG, Items.DARK_OAK_LOG, Items.SPRUCE_LOG);
        this.tag(CustomTags.ITEM_TREE_HARVESTABLE).add(Items.APPLE, Items.STICK);
        this.tag(CustomTags.ITEM_TREE_PLANTABLE).add(Items.ACACIA_SAPLING, Items.BIRCH_SAPLING, Items.JUNGLE_SAPLING, Items.DARK_OAK_SAPLING, Items.OAK_SAPLING, Items.SPRUCE_SAPLING);

        this.tag(CustomTags.ITEM_CROP_SOIL).addTag(ItemTags.DIRT);
        this.tag(CustomTags.ITEM_CROP_FARMLAND).add(Items.FARMLAND);
        this.tag(CustomTags.ITEM_CROP_PLANTABLE).add(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.POTATO, Items.CARROT);
        this.tag(CustomTags.ITEM_CROP_HARVESTABLE).add(Items.WHEAT, Items.BEETROOT, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS, Items.WHEAT_SEEDS);

        this.tag(CustomTags.ITEM_COCOA_HARVESTABLE).add(Items.COCOA_BEANS);
        this.tag(CustomTags.ITEM_GOURD_HARVESTABLE).add(Items.MELON_SLICE, Items.PUMPKIN);
        this.tag(CustomTags.ITEM_SUCCULENT_HARVESTABLE).add(Items.CACTUS, Items.SUGAR_CANE, Items.BAMBOO);
        this.tag(CustomTags.ITEM_PEAT_SOIL).add(CrossFarmingData.PEAT_BOG_BLOCK.get().asItem());
        this.tag(CustomTags.ITEM_PEAT_HARVESTABLE).add(CrossFarmingData.PEAT_ITEM.get(), Items.DIRT, Items.SAND);
        this.tag(CustomTags.ITEM_PEAT_WASTE).add(Items.DIRT, Items.SAND);
        this.tag(CustomTags.ITEM_INFERNAL_SOIL).add(Items.SOUL_SAND);
        this.tag(CustomTags.ITEM_INFERNAL_HARVESTABLE).add(Items.NETHER_WART);
        this.tag(ItemTags.COALS).add(CrossFarmingData.PEAT_ITEM.get());
    }

    public void addCompatTags() {
        String ma = "mysticalagriculture";
        this.tag(CustomTags.ITEM_CROP_PLANTABLE).addOptionalTag(new ResourceLocation(ma, "seeds"));
        this.tag(CustomTags.ITEM_CROP_FARMLAND).addOptional(new ResourceLocation(ma, "inferium_farmland"));
        this.tag(CustomTags.ITEM_CROP_FARMLAND).addOptional(new ResourceLocation(ma, "prudentium_farmland"));
        this.tag(CustomTags.ITEM_CROP_FARMLAND).addOptional(new ResourceLocation(ma, "tertium_farmland"));
        this.tag(CustomTags.ITEM_CROP_FARMLAND).addOptional(new ResourceLocation(ma, "imperium_farmland"));
        this.tag(CustomTags.ITEM_CROP_FARMLAND).addOptional(new ResourceLocation(ma, "supremium_farmland"));
        this.tag(CustomTags.ITEM_CROP_HARVESTABLE).addOptionalTag(new ResourceLocation(ma, "essences"));
        this.tag(CustomTags.ITEM_CROP_HARVESTABLE).addOptionalTag(new ResourceLocation(ma, "seeds"));

        this.tag(CustomTags.ITEM_TREE_HARVESTABLE).addOptionalTag(new ResourceLocation("ic2", "rubber_wood"));
        this.tag(CustomTags.ITEM_RESIN_HARVESTABLE).addOptional(new ResourceLocation("ic2", "sticky_resin"));
    }
}
