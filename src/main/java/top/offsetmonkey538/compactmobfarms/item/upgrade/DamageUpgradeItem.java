package top.offsetmonkey538.compactmobfarms.item.upgrade;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity;

public class DamageUpgradeItem extends CompactMobFarmUpgradeItem {
    public DamageUpgradeItem(FabricItemSettings settings) {
        super(settings);
    }

    @Override
    public float modifyAttackDamage(float initialDamage, @Nullable ItemStack sword, LivingEntity target, CompactMobFarmBlockEntity blockEntity) {
        return initialDamage + 2;
    }
}
