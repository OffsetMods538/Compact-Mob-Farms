package top.offsetmonkey538.compactmobfarms.item.upgrade;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity;

public abstract class CompactMobFarmUpgradeItem extends Item {
    public CompactMobFarmUpgradeItem() {
        super(new FabricItemSettings().maxCount(1));
    }

    /**
     * Modifies the attack damage.
     * <br>
     * Default implementation just returns the provided <code>initialDamage</code>.
     *
     * @param initialDamage The initial amount of damage that should be dealt, this might have been modified by other upgrades.
     * @param sword The item that is used to damage the entity, this may be null.
     * @param target The entity that will be damaged.
     * @return The amount of damage that should be done to the mob inside the farm, this may be further modified by other upgrades.
     */
    public float modifyAttackDamage(float initialDamage, @Nullable ItemStack sword, LivingEntity target, CompactMobFarmBlockEntity blockEntity) {
        return initialDamage;
    }

    /**
     * Modifies the attack speed. <strong>Measured in ticks!</strong>
     * <br>
     * Default implementation just returns the provided <code>initialAttackDamage</code>.
     *
     * @param initialAttackSpeed The initial number of ticks until the next attack, this might have been modified by other upgrades.
     * @param sword The item that is used to damage the entity, this may be null.
     * @param target The entity that will be damaged.
     * @return The number of ticks until the next attack, this may be further modified by other upgrades.
     */
    public int modifyAttackSpeed(int initialAttackSpeed, @Nullable ItemStack sword, LivingEntity target, CompactMobFarmBlockEntity blockEntity) {
        return initialAttackSpeed;
    }
}
