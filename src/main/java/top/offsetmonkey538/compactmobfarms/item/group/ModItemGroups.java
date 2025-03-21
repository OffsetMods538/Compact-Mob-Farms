package top.offsetmonkey538.compactmobfarms.item.group;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import top.offsetmonkey538.compactmobfarms.item.ModItems;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public final class ModItemGroups {
    private ModItemGroups() {

    }

    public static final ItemGroup COMPACT_MOB_FARMS_ITEM_GROUP = register("compact_mob_farms_item_group",
            FabricItemGroup.builder()
                    .icon(ModItems.SPAWNER_SHARD::getDefaultStack)
                    .displayName(Text.translatable("itemGroup.compact_mob_farms.main_group"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.SPIRIT_BOTTLE);
                        entries.add(ModItems.SPAWNER_SHARD);
                        entries.add(ModItems.COMPACT_MOB_FARM);
                        entries.add(ModItems.SAMPLE_TAKER);
                        entries.add(ModItems.SPEED_UPGRADE);
                        entries.add(ModItems.DAMAGE_UPGRADE);
                        entries.add(ModItems.TIER_1_UPGRADE);
                        entries.add(ModItems.TIER_2_UPGRADE);
                        entries.add(ModItems.TIER_3_UPGRADE);
                        entries.add(ModItems.TIER_4_UPGRADE);
                    })
                    .build()
    );
    public static final ItemGroup FILLED_SAMPLE_TAKERS_ITEM_GROUP = register("filled_sample_takers_item_group",
            FabricItemGroup.builder()
                    .icon(ModItems.FILLED_SAMPLE_TAKER::getDefaultStack)
                    .displayName(Text.translatable("itemGroup.compact_mob_farms.filled_sample_takers_group"))
                    // todo: check if mob in EntityTiers.INSTANCE before adding
                    .entries((displayContext, entries) -> SpawnEggItem.getAll().forEach(spawnEgg -> entries.add(ModItems.FILLED_SAMPLE_TAKER.forEntity(spawnEgg.getEntityType(ItemStack.EMPTY)))))
                    .build()
    );


    private static <T extends ItemGroup> T register(String name, T group) {
        return Registry.register(Registries.ITEM_GROUP, id(name), group);
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // Registers item groups by loading the class.
    }
}
