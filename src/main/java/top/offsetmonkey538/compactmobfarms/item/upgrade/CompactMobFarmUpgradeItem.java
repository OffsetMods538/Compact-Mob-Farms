package top.offsetmonkey538.compactmobfarms.item.upgrade;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class CompactMobFarmUpgradeItem extends Item {
    public CompactMobFarmUpgradeItem() {
        super(new FabricItemSettings().maxCount(1));
    }

    /**
     * Modifies the attack damage.
     * <br>
     * Default implementation just returns the provided <code>initialDamage</code>.
     *
     * @param initialDamage The amou
     * @param sword The item that is used to damage the entity, this may be null.
     * @param target The entity that will be damaged.
     * @return The amount of damage that should be done to the mob inside the farm, this may be further modified by other upgrades.
     */
    public float modifyAttackDamage(float initialDamage, @Nullable ItemStack sword, LivingEntity target) {
        return initialDamage;
    }
}
