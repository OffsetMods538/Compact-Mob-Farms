package top.offsetmonkey538.compactmobfarms.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import top.offsetmonkey538.compactmobfarms.item.ModItems;

@Mixin(SpawnerBlock.class)
public abstract class SpawnerBlockMixin extends Block {
    public SpawnerBlockMixin(Settings settings) {
        super(settings);
    }


    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        final ItemStack item = player.getStackInHand(hand);

        if (!item.isOf(Items.GLASS_BOTTLE)) return ActionResult.PASS;

        if (!player.getAbilities().creativeMode) item.decrement(1);
        player.giveItemStack(ModItems.SPIRIT_BOTTLE.getDefaultStack());

        return ActionResult.success(world.isClient());
    }
}
