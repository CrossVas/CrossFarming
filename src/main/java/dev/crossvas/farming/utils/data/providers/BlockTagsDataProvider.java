package dev.crossvas.farming.utils.data.providers;

import dev.crossvas.farming.CrossFarming;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blocks.BaseBlock;
import dev.crossvas.farming.utils.CustomTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class BlockTagsDataProvider extends BlockTagsProvider {

    public BlockTagsDataProvider(DataGenerator dataGen, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGen, CrossFarming.ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        addCompatTags();
        this.tag(CustomTags.BLOCK_TREE_SOIL).addTag(BlockTags.DIRT);
        this.tag(CustomTags.BLOCK_TREE_HARVESTABLE).addTag(BlockTags.LOGS);
        this.tag(CustomTags.BLOCK_TREE_PLANTABLE).add(Blocks.ACACIA_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.OAK_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.SPRUCE_SAPLING);
        this.tag(CustomTags.BLOCK_CROP_SOIL).addTag(BlockTags.DIRT);
        this.tag(CustomTags.BLOCK_CROP_FARMLAND).add(Blocks.FARMLAND);
        this.tag(CustomTags.BLOCK_CROP_HARVESTABLE).add(Blocks.WHEAT, Blocks.BEETROOTS, Blocks.POTATOES, Blocks.CARROTS);
        this.tag(CustomTags.BLOCK_COCOA_HARVESTABLE).add(Blocks.COCOA);
        this.tag(CustomTags.BLOCK_GOURD_HARVESTABLE).add(Blocks.MELON, Blocks.PUMPKIN);
        this.tag(CustomTags.BLOCK_SUCCULENT_HARVESTABLE).add(Blocks.CACTUS, Blocks.SUGAR_CANE, Blocks.BAMBOO);
        this.tag(CustomTags.BLOCK_PEAT_SOIL).add(CrossFarmingData.PEAT_BOG_BLOCK.get());
        this.tag(CustomTags.BLOCK_PEAT_HARVESTABLE).add(CrossFarmingData.PEAT_BLOCK.get(), Blocks.DIRT, Blocks.SAND);
        this.tag(CustomTags.BLOCK_INFERNAL_SOIL).add(Blocks.SOUL_SAND);
        this.tag(CustomTags.BLOCK_INFERNAL_HARVESTABLE).add(Blocks.NETHER_WART);
        CrossFarmingData.BLOCKS.getEntries().stream()
                .filter(regObject -> regObject.get() instanceof BaseBlock)
                .map(RegistryObject::get).toList().forEach(block -> this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block));
    }

    public void addCompatTags() {
        String ma = "mysticalagriculture";
        this.tag(CustomTags.BLOCK_CROP_FARMLAND).addOptional(new ResourceLocation(ma, "inferium_farmland"));
        this.tag(CustomTags.BLOCK_CROP_FARMLAND).addOptional(new ResourceLocation(ma, "prudentium_farmland"));
        this.tag(CustomTags.BLOCK_CROP_FARMLAND).addOptional(new ResourceLocation(ma, "tertium_farmland"));
        this.tag(CustomTags.BLOCK_CROP_FARMLAND).addOptional(new ResourceLocation(ma, "imperium_farmland"));
        this.tag(CustomTags.BLOCK_CROP_FARMLAND).addOptional(new ResourceLocation(ma, "supremium_farmland"));
        this.tag(CustomTags.BLOCK_CROP_HARVESTABLE).addOptionalTag(new ResourceLocation(ma, "essences"));

        this.tag(CustomTags.BLOCK_TREE_PLANTABLE).addOptional(new ResourceLocation("ic2", "rubber_sapling"));
    }
}
