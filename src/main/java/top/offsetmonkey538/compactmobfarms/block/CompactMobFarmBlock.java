package top.offsetmonkey538.compactmobfarms.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity;
import top.offsetmonkey538.compactmobfarms.block.entity.ModBlockEntityTypes;

public class CompactMobFarmBlock extends BlockWithEntity {
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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntityTypes.COMPACT_MOB_FARM, CompactMobFarmBlockEntity::tick);
    }
}
