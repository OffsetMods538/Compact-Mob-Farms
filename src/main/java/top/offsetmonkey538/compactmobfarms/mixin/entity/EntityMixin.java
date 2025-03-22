package top.offsetmonkey538.compactmobfarms.mixin.entity;

import java.util.function.Consumer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.offsetmonkey538.compactmobfarms.accessor.EntityAccessor;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAccessor {
    @Unique
    private Consumer<ItemStack> compact_mob_farms$dropMethod = null;

    @Inject(
            method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ItemEntity;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V"
            ),
            cancellable = true
    )
    private void compact_mob_farms$replaceDropMethod(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> cir) {
        if (compact_mob_farms$dropMethod == null) return;

        compact_mob_farms$dropMethod.accept(stack);
        cir.setReturnValue(null);
    }

    @Override
    @Unique
    public void compact_mob_farms$setDropMethod(Consumer<ItemStack> dropMethod) {
        compact_mob_farms$dropMethod = dropMethod;
    }
}
