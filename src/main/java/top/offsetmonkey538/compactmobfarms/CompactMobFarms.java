package top.offsetmonkey538.compactmobfarms;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.block.entity.ModBlockEntityTypes;
import top.offsetmonkey538.compactmobfarms.config.EntityTierResourceReloadListener;
import top.offsetmonkey538.compactmobfarms.item.ModItems;
import top.offsetmonkey538.compactmobfarms.item.group.ModItemGroups;
import top.offsetmonkey538.compactmobfarms.recipe.ModRecipes;
import top.offsetmonkey538.compactmobfarms.screen.ModScreenHandlers;

public class CompactMobFarms implements ModInitializer {
	public static final String MOD_ID = "compact-mob-farms";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.register();
		ModItems.register();
		ModItemGroups.register();
		ModBlockEntityTypes.register();
		ModScreenHandlers.register();
		ModRecipes.register();

		//noinspection UnstableApiUsage
		ItemStorage.SIDED.registerForBlockEntity((block, direction) -> block.getDropInventory(), ModBlockEntityTypes.COMPACT_MOB_FARM);

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (!Blocks.SPAWNER.getLootTableId().equals(id) || !source.isBuiltin()) return;

			LootPool.Builder pool = LootPool.builder()
					.with(ItemEntry.builder(ModItems.SPAWNER_SHARD))
					.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(6, 8)))
					.apply(ApplyBonusLootFunction.uniformBonusCount(Enchantments.FORTUNE, 5));

			tableBuilder.pool(pool);
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new EntityTierResourceReloadListener());
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
