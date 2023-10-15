package top.offsetmonkey538.compactmobfarms.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;

import static net.minecraft.item.BlockItem.*;
import static top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity.*;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        this.addDrop(ModBlocks.COMPACT_MOB_FARM, this.drops(ModBlocks.COMPACT_MOB_FARM).apply(
                CopyNbtLootFunction.builder(ContextLootNbtProvider.BLOCK_ENTITY)
                        .withOperation(DROP_INVENTORY_NBT_KEY, BLOCK_ENTITY_TAG_KEY + "." + DROP_INVENTORY_NBT_KEY)
                        .withOperation(SAMPLE_TAKER_NBT_KEY, BLOCK_ENTITY_TAG_KEY + "." + SAMPLE_TAKER_NBT_KEY)
                        .withOperation(TIER_UPGRADE_NBT_KEY, BLOCK_ENTITY_TAG_KEY + "." + TIER_UPGRADE_NBT_KEY)
                        .withOperation(UPGRADES_NBT_KEY, BLOCK_ENTITY_TAG_KEY + "." + UPGRADES_NBT_KEY)
                        .withOperation(SWORD_NBT_KEY, BLOCK_ENTITY_TAG_KEY + "." + SWORD_NBT_KEY)
                        .withOperation(TURNED_ON_NBT_KEY, BLOCK_ENTITY_TAG_KEY + "." + TURNED_ON_NBT_KEY)
        ));
    }
}
