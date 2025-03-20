package top.offsetmonkey538.compactmobfarms.item;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import top.offsetmonkey538.compactmobfarms.config.EntityTiers;


public class TierUpgradeItem extends Item {
    public TierUpgradeItem(Settings settings) {
        super(settings);
    }

    public static boolean isSupported(ItemStack tier, EntityType<?> entity) {
        if (!EntityTiers.instance.anySupports(entity)) return false;

        if (tier == null || tier.isEmpty()) return EntityTiers.instance.tier0Supports(entity);
        if (tier.isOf(ModItems.TIER_1_UPGRADE)) return EntityTiers.instance.tier1Supports(entity);
        if (tier.isOf(ModItems.TIER_2_UPGRADE)) return EntityTiers.instance.tier2Supports(entity);
        if (tier.isOf(ModItems.TIER_3_UPGRADE)) return EntityTiers.instance.tier3Supports(entity);
        if (tier.isOf(ModItems.TIER_4_UPGRADE)) return EntityTiers.instance.tier4Supports(entity);

        return false;
    }
}
