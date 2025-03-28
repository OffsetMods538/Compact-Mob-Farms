package top.offsetmonkey538.compactmobfarms.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public final class ModBlockEntityTypes {
    private ModBlockEntityTypes() {

    }

    public static final BlockEntityType<CompactMobFarmBlockEntity> COMPACT_MOB_FARM = register(BlockEntityType.Builder.create(CompactMobFarmBlockEntity::new, ModBlocks.COMPACT_MOB_FARM).build(), "compact_mob_farm");

    @SuppressWarnings("SameParameterValue")
    private static <T extends BlockEntityType<?>> T register(T block, String name) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id(name), block);
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // Registers blocks by loading the class.
    }
}
