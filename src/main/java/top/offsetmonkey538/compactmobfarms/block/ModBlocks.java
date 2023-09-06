package top.offsetmonkey538.compactmobfarms.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.id;

public final class ModBlocks {
    private ModBlocks() {

    }

    public static final Block COMPACT_MOB_FARM = register(new CompactMobFarmBlock(FabricBlockSettings.create().mapColor(MapColor.IRON_GRAY).instrument(Instrument.IRON_XYLOPHONE).sounds(BlockSoundGroup.METAL).strength(50.0f, 1200.0f).nonOpaque().notSolid().requiresTool()), "compact_mob_farm");

    private static <T extends Block> T register(T block, String name) {
        return Registry.register(Registries.BLOCK, id(name), block);
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // Registers blocks by loading the class.
    }
}
