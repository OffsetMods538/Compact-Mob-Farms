package top.offsetmonkey538.compactmobfarms.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity;
import top.offsetmonkey538.compactmobfarms.item.upgrade.CompactMobFarmUpgradeItem;
import top.offsetmonkey538.compactmobfarms.item.upgrade.DamageUpgradeItem;
import top.offsetmonkey538.compactmobfarms.item.upgrade.SpeedUpgradeItem;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.id;

public final class ModItems {
    private ModItems() {

    }

    public static final BlockItem             COMPACT_MOB_FARM    = register("compact_mob_farm",    new BlockItem(ModBlocks.COMPACT_MOB_FARM, new Item.Settings().rarity(Rarity.RARE)));
    public static final SampleTakerItem       SAMPLE_TAKER        = register("sample_taker",        new SampleTakerItem(new Item.Settings().maxCount(1)));
    public static final FilledSampleTakerItem FILLED_SAMPLE_TAKER = register("filled_sample_taker", new FilledSampleTakerItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON)));
    public static final Item                  SPIRIT_BOTTLE       = register("spirit_bottle",       new Item(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON).recipeRemainder(Items.GLASS_BOTTLE)));
    public static final Item                  SPAWNER_SHARD       = register("spawner_shard",       new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final TierUpgradeItem       TIER_1_UPGRADE      = register("tier_1_upgrade",      new TierUpgradeItem(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final TierUpgradeItem       TIER_2_UPGRADE      = register("tier_2_upgrade",      new TierUpgradeItem(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final TierUpgradeItem       TIER_3_UPGRADE      = register("tier_3_upgrade",      new TierUpgradeItem(new Item.Settings().rarity(Rarity.RARE)));
    public static final TierUpgradeItem       TIER_4_UPGRADE      = register("tier_4_upgrade",      new TierUpgradeItem(new Item.Settings().rarity(Rarity.EPIC)));
    public static final SpeedUpgradeItem      SPEED_UPGRADE       = register("speed_upgrade",       new SpeedUpgradeItem(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final DamageUpgradeItem     DAMAGE_UPGRADE      = register("damage_upgrade",      new DamageUpgradeItem(new Item.Settings().rarity(Rarity.UNCOMMON)));

    public static final CompactMobFarmUpgradeItem DEBUG_SPEED_UPGRADE  = register("debug_speed_upgrade",  new CompactMobFarmUpgradeItem(new Item.Settings()) {
        @Override
        public int modifyAttackSpeed(int initialAttackSpeed, @Nullable ItemStack sword, LivingEntity target, CompactMobFarmBlockEntity blockEntity) {
            return initialAttackSpeed - 2000;
        }
    });
    public static final CompactMobFarmUpgradeItem DEBUG_DAMAGE_UPGRADE = register("debug_damage_upgrade", new CompactMobFarmUpgradeItem(new Item.Settings()) {
        @Override
        public float modifyAttackDamage(float initialDamage, @Nullable ItemStack sword, LivingEntity target, CompactMobFarmBlockEntity blockEntity) {
            return initialDamage + 100;
        }
    });

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, id(name), item);
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // Registers items by loading the class.
    }
}
