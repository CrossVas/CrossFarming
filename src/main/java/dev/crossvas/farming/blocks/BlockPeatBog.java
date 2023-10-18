package dev.crossvas.farming.blocks;

import dev.crossvas.farming.CrossFarmingData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.IPlantable;

public class BlockPeatBog extends Block {

    private static final int MATURITY_STAGE = 3;
    private static final IntegerProperty MATURITY = IntegerProperty.create("maturity", 0, MATURITY_STAGE);

    public BlockPeatBog() {
        super(BlockBehaviour.Properties.of(Material.DIRT).randomTicks().strength(0.5F).sound(SoundType.GRAVEL));
        registerDefaultState(this.getStateDefinition().any().setValue(MATURITY, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(MATURITY);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.isClientSide || pLevel.random.nextInt(13) != 0) {
            return;
        }

        int maturity = pState.getValue(MATURITY);
        if (isMoistened(pLevel, pPos)) {
            if (maturity == MATURITY_STAGE - 1) {
                pLevel.setBlock(pPos, CrossFarmingData.PEAT_BLOCK.get().defaultBlockState(), Block.UPDATE_ALL);
            } else {
                pLevel.setBlock(pPos, pState.setValue(MATURITY, maturity + 1), Block.UPDATE_ALL);
            }
        }
    }

    private static boolean isMoistened(Level world, BlockPos pos) {
        for (BlockPos waterPos : BlockPos.betweenClosed(pos.offset(-2, -2, -2), pos.offset(2, 2, 2))) {
            BlockState blockState = world.getBlockState(waterPos);
            Block block = blockState.getBlock();
            if (block == Blocks.WATER || block == CrossFarmingData.PEAT_BLOCK.get()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return false;
    }
}
