package top.offsetmonkey538.compactmobfarms.block;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.id;

public final class ModBlocks {
    private ModBlocks() {

    }

    /*
     Example usage:
     public static final Block MY_BLOCK = register(new Block(FabricBlockSettings.copyOf(Blocks.STONE)), "my_block");
     */

    private static <T extends Block> T register(T block, String name) {
        return Registry.register(Registries.BLOCK, id(name), block);
    }

    public static void register() {
        // Registers blocks by loading the class.
    }
}
