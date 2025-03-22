package top.offsetmonkey538.compactmobfarms.mixin.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import java.util.function.Consumer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import top.offsetmonkey538.compactmobfarms.accessor.LivingEntityAccessor;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityAccessor {

    @Unique
    private Consumer<Integer> compact_mob_farms$xpDropMethod = null;

    @WrapWithCondition(
            method = "dropXp",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"
            )
    )
    private boolean compact_mob_farms$replaceDropMethod(ServerWorld world, Vec3d pos, int amount) {
        if (compact_mob_farms$xpDropMethod == null) return true;

        compact_mob_farms$xpDropMethod.accept(amount);
        return false;
    }

    @Override
    @Unique
    public void compact_mob_farms$setXpDropMethod(Consumer<Integer> dropMethod) {
        compact_mob_farms$xpDropMethod = dropMethod;
    }
}
