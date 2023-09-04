package top.offsetmonkey538.compactmobfarms.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity;

public class CompactMobFarmBlock extends Block implements BlockEntityProvider {
    public CompactMobFarmBlock(Settings settings) {
        super(settings);
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CompactMobFarmBlockEntity(pos, state);
    }
}
