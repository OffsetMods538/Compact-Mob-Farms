package top.offsetmonkey538.compactmobfarms.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity;
import top.offsetmonkey538.compactmobfarms.block.entity.ModBlockEntityTypes;

public class CompactMobFarmBlock extends BlockWithEntity {
    public static final VoxelShape SHAPE = VoxelShapes.union(
            VoxelShapes.cuboid(0.05625f, 0.0625f, 0.05625f, 0.94375f, 0.9375f, 0.94375f),

            VoxelShapes.cuboid(0f,     0f, 0f,     0.125f,1f, 0.125f),
            VoxelShapes.cuboid(0.875f, 0f, 0f,     1f,    1f, 0.125f),
            VoxelShapes.cuboid(0.875f, 0f, 0.875f, 1f,    1f, 1f),
            VoxelShapes.cuboid(0f,     0f, 0.875f, 0.125f,1f, 1f)
    );

    public CompactMobFarmBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CompactMobFarmBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntityTypes.COMPACT_MOB_FARM, CompactMobFarmBlockEntity::tick);
    }
}
