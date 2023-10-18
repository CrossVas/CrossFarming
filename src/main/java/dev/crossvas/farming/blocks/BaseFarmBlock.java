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

public class BaseFarmBlock extends BaseBlock {


    protected String ID;
    protected boolean hasGui;

    public BaseFarmBlock(String id, boolean hasGui) {
        super(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F));
        this.ID = id;
        this.hasGui = hasGui;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return ID.equals("resin_farm") ? RESIN_FARM_SHAPE : FARM_SHAPE;
    }

    @Override
    protected boolean hasGUI() {
        return this.hasGui;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {

        return switch (this.ID) {
            case "crop_farm" -> new CropFarmBlockEntity(pPos, pState);
            case "tree_farm" -> new TreeFarmBlockEntity(pPos, pState);
            case "peat_bog_farm" -> new PeatBogFarmBlockEntity(pPos, pState);
            case "infernal_farm" -> new InfernalFarmBlockEntity(pPos, pState);
            case "resin_farm" -> new ResinFarmBlockEntity(pPos, pState);
            default -> null;
        };
    }
}
