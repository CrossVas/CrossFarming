package dev.crossvas.farming.utils.helpers;

import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blocks.BaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class BlockHelper {

    public static boolean shouldBlueprintBreakBlock(Level world, BlockPos blockPosition) {
        Block block = world.getBlockState(blockPosition).getBlock();
        return new ItemStack(block).is(CrossFarmingData.CustomTags.FARM_SOIL) && !(block instanceof BaseBlock);
    }
}
