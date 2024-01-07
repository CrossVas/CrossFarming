package dev.crossvas.farming.utils.data.providers;

import dev.crossvas.farming.CrossFarming;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blocks.BaseBlock;
import dev.crossvas.farming.utils.CustomTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
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
        this.tag(CustomTags.BLOCK_TREE_SOIL).addTag(BlockTags.DIRT);
        this.tag(CustomTags.BLOCK_TREE_HARVESTABLE).addTag(BlockTags.LOGS);
        this.tag(CustomTags.BLOCK_TREE_HARVESTABLE).addTag(BlockTags.SAPLINGS);
        this.tag(CustomTags.BLOCK_CROP_SOIL).addTag(BlockTags.DIRT);
        this.tag(CustomTags.BLOCK_CROP_HARVESTABLE).addTag(BlockTags.LOGS);
        this.tag(CustomTags.BLOCK_TREE_HARVESTABLE).addTag(BlockTags.SAPLINGS);
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
}
