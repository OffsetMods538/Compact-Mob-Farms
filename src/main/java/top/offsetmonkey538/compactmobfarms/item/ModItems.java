package top.offsetmonkey538.compactmobfarms.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
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

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.id;

public final class ModItems {
    private ModItems() {

    }

    public static final BlockItem       COMPACT_MOB_FARM = register("compact_mob_farm", new BlockItem(ModBlocks.COMPACT_MOB_FARM, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final SampleTakerItem SAMPLE_TAKER     = register("sample_taker",     new SampleTakerItem(new FabricItemSettings().maxCount(1)));
    public static final Item            SPIRIT_BOTTLE    = register("spirit_bottle",    new Item(new FabricItemSettings().maxCount(16).rarity(Rarity.COMMON).recipeRemainder(Items.GLASS_BOTTLE)));
    public static final Item            SPAWNER_SHARD    = register("spawner_shard",    new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON)));

    public static final CompactMobFarmUpgradeItem DEBUG_SPEED_UPGRADE  = register("debug_speed_upgrade",  new CompactMobFarmUpgradeItem() {
        @Override
        public int modifyAttackSpeed(int initialAttackSpeed, @Nullable ItemStack sword, LivingEntity target, CompactMobFarmBlockEntity blockEntity) {
            return initialAttackSpeed - 2000;
        }
    });
    public static final CompactMobFarmUpgradeItem DEBUG_DAMAGE_UPGRADE = register("debug_damage_upgrade", new CompactMobFarmUpgradeItem() {
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
