package top.offsetmonkey538.compactmobfarms.mixin.block;

import net.minecraft.block.BlockState;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.offsetmonkey538.compactmobfarms.item.ModItems;

@Mixin(SpawnerBlock.class)
public abstract class SpawnerBlockMixin extends AbstractBlockMixin {
    @Override
    protected void compact_mob_farms$obtainSpiritBottleFromSpawner(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        final ItemStack item = player.getStackInHand(Hand.MAIN_HAND);

        if (!item.isOf(Items.GLASS_BOTTLE)) {
            cir.setReturnValue(ActionResult.PASS);
            return;
        }

        if (!player.getAbilities().creativeMode) item.decrement(1);
        if (!player.giveItemStack(ModItems.SPIRIT_BOTTLE.getDefaultStack())) {
            player.dropItem(ModItems.SPIRIT_BOTTLE.getDefaultStack(), true);
        }

        cir.setReturnValue(ActionResult.success(world.isClient()));
    }
}
