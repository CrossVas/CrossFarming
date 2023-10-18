package dev.crossvas.farming.blocks;

import dev.crossvas.farming.blockentities.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BaseCombineBlock extends BaseBlock {

    protected String ID;
    protected boolean hasGui;

    public BaseCombineBlock(String id, boolean hasGui) {
        super(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F));
        this.ID = id;
        this.hasGui = hasGui;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collContext) {
        return COMBINE_SHAPE;
    }

    @Override
    protected boolean hasGUI() {
        return this.hasGui;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return switch (this.ID) {
            case "crop_combine" -> new CropCombineBlockEntity(pPos, pState);
            case "tree_combine" -> new TreeCombineBlockEntity(pPos, pState);
            case "gourd_combine" -> new GourdCombineBlockEntity(pPos, pState);
            case "succulent_combine" -> new SucculentCombineBlockEntity(pPos, pState);
            case "peat_bog_combine" -> new PeatBogCombineBlockEntity(pPos, pState);
            case "cocoa_combine" -> new CocoaCombineBlockEntity(pPos, pState);
            case "infernal_combine" -> new InfernalCombineBlockEntity(pPos, pState);
            case "resin_combine" -> new ResinCombineBlockEntity(pPos, pState);
            default -> null;
        };
    }
}
