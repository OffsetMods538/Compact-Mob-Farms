package top.offsetmonkey538.compactmobfarms.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.function.CopyComponentsLootFunction;
import net.minecraft.registry.RegistryWrapper;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.component.ModComponents;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.addDrop(ModBlocks.COMPACT_MOB_FARM, this.drops(ModBlocks.COMPACT_MOB_FARM).apply(
                CopyComponentsLootFunction.builder(CopyComponentsLootFunction.Source.BLOCK_ENTITY)
                        .include(ModComponents.DROP_INVENTORY)
                        .include(ModComponents.SAMPLE_TAKER)
                        .include(ModComponents.TIER_UPGRADE)
                        .include(ModComponents.UPGRADES)
                        .include(ModComponents.SWORD)
                        .include(ModComponents.TURNED_ON)
        ));
    }
}
