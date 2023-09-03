package top.offsetmonkey538.compactmobfarms.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.id;

public final class ModItems {
    private ModItems() {

    }

    /*
     Example usage:
     public static final Item MY_ITEM = register(new Item(new FabricItemSettings()), "my_item");
     */

    private static <T extends Item> T register(T item, String name) {
        return Registry.register(Registries.ITEM, id(name), item);
    }

    public static void register() {
        // Registers items by loading the class.
    }
}
