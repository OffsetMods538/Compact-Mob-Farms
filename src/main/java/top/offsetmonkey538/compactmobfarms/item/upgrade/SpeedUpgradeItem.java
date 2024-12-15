package top.offsetmonkey538.compactmobfarms.item.upgrade;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity;

public class SpeedUpgradeItem extends CompactMobFarmUpgradeItem {
    public SpeedUpgradeItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public int modifyAttackSpeed(int initialAttackSpeed, @Nullable ItemStack sword, LivingEntity target, CompactMobFarmBlockEntity blockEntity) {
        return initialAttackSpeed - 130; // 130 ticks = 6.5 seconds
    }
}
