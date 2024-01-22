package dev.crossvas.farming.utils;

import dev.crossvas.farming.CrossFarming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CustomTags {

    // Cocoa farm
    public static final TagKey<Block> BLOCK_COCOA_HARVESTABLE = createBlockTag("cocoa/harvestable");
    public static final TagKey<Item> ITEM_COCOA_HARVESTABLE = createItemTag("cocoa/harvestable");

    // Crop farm
    public static final TagKey<Block> BLOCK_CROP_SOIL = createBlockTag("crop/soil");
    public static final TagKey<Block> BLOCK_CROP_FARMLAND = createBlockTag("crop/farmland");
    public static final TagKey<Item> ITEM_CROP_SOIL = createItemTag("crop/soil");
    public static final TagKey<Item> ITEM_CROP_FARMLAND = createItemTag("crop/farmland");
    public static final TagKey<Item> ITEM_CROP_PLANTABLE = createItemTag("crop/plantable");
    public static final TagKey<Block> BLOCK_CROP_HARVESTABLE = createBlockTag("crop/harvestable");
    public static final TagKey<Item> ITEM_CROP_HARVESTABLE = createItemTag("crop/harvestable");

    // Resin farm
    public static final TagKey<Item> ITEM_RESIN_HARVESTABLE = createItemTag("resin/harvestable");

    // Tree farm
    public static final TagKey<Block> BLOCK_TREE_SOIL = createBlockTag("tree/soil");
    public static final TagKey<Item> ITEM_TREE_SOIL = createItemTag("tree/soil");
    public static final TagKey<Block> BLOCK_TREE_PLANTABLE = createBlockTag("tree/plantable");
    public static final TagKey<Item> ITEM_TREE_PLANTABLE = createItemTag("tree/plantable");
    public static final TagKey<Block> BLOCK_TREE_HARVESTABLE = createBlockTag("tree/harvestable");
    public static final TagKey<Item> ITEM_TREE_HARVESTABLE = createItemTag("tree/harvestable");

    // Gourd farm
    public static final TagKey<Block> BLOCK_GOURD_HARVESTABLE = createBlockTag("gourd/harvestable");
    public static final TagKey<Item> ITEM_GOURD_HARVESTABLE = createItemTag("gourd/harvestable");

    // Succulent farm
    public static final TagKey<Block> BLOCK_SUCCULENT_HARVESTABLE = createBlockTag("succulent/harvestable");
    public static final TagKey<Item> ITEM_SUCCULENT_HARVESTABLE = createItemTag("succulent/harvestable");

    // Peat farm
    public static final TagKey<Block> BLOCK_PEAT_SOIL = createBlockTag("peat/soil");
    public static final TagKey<Item> ITEM_PEAT_SOIL = createItemTag("peat/soil");
    public static final TagKey<Block> BLOCK_PEAT_HARVESTABLE = createBlockTag("peat/harvestable");
    public static final TagKey<Item> ITEM_PEAT_HARVESTABLE = createItemTag("peat/harvestable");
    public static final TagKey<Item> ITEM_PEAT_WASTE = createItemTag("peat/waste");

    // Infernal farm
    public static final TagKey<Block> BLOCK_INFERNAL_SOIL = createBlockTag("infernal/soil");
    public static final TagKey<Item> ITEM_INFERNAL_SOIL = createItemTag("infernal/soil");
    public static final TagKey<Block> BLOCK_INFERNAL_HARVESTABLE = createBlockTag("infernal/harvestable");
    public static final TagKey<Item> ITEM_INFERNAL_HARVESTABLE = createItemTag("infernal/harvestable");

    private static TagKey<Item> createItemTag(String path) {
        return ItemTags.create(new ResourceLocation(CrossFarming.ID, path));
    }

    private static TagKey<Block> createBlockTag(String path) {
        return BlockTags.create(new ResourceLocation(CrossFarming.ID, path));
    }
}
