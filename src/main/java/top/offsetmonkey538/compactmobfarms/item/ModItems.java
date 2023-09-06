package top.offsetmonkey538.compactmobfarms.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.id;

public final class ModItems {
    private ModItems() {

    }

    public static final BlockItem       COMPACT_MOB_FARM = register(new BlockItem(ModBlocks.COMPACT_MOB_FARM, new FabricItemSettings().rarity(Rarity.UNCOMMON)), "compact_mob_farm");
    public static final SampleTakerItem SAMPLE_TAKER     = register(new SampleTakerItem(new FabricItemSettings().maxCount(1)), "sample_taker");

    private static <T extends Item> T register(T item, String name) {
        return Registry.register(Registries.ITEM, id(name), item);
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // Registers items by loading the class.
    }
}
