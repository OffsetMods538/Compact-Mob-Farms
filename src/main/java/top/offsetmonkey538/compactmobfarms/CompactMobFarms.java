package top.offsetmonkey538.compactmobfarms;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.block.entity.ModBlockEntityTypes;
import top.offsetmonkey538.compactmobfarms.component.ModComponents;
import top.offsetmonkey538.compactmobfarms.config.EntityTierResourceReloadListener;
import top.offsetmonkey538.compactmobfarms.config.EntityTiers;
import top.offsetmonkey538.compactmobfarms.datagen.builtin.BuiltinPacksDatagen;
import top.offsetmonkey538.compactmobfarms.item.ModItems;
import top.offsetmonkey538.compactmobfarms.item.group.ModItemGroups;
import top.offsetmonkey538.compactmobfarms.loot.condition.ModLootConditionTypes;
import top.offsetmonkey538.compactmobfarms.network.ModPackets;
import top.offsetmonkey538.compactmobfarms.recipe.ModRecipes;
import top.offsetmonkey538.compactmobfarms.screen.ModScreenHandlers;
import top.offsetmonkey538.monkeylib538.utils.IdentifierUtils;

public class CompactMobFarms implements ModInitializer {
	public static final String MOD_ID = "compact-mob-farms";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.register();
		ModComponents.register();
		ModItems.register();
		ModItemGroups.register();
		ModBlockEntityTypes.register();
		ModScreenHandlers.register();
		ModRecipes.register();
		ModPackets.register();
		ModLootConditionTypes.register();

        ItemStorage.SIDED.registerForBlockEntity((block, direction) -> block.getDropInventory(), ModBlockEntityTypes.COMPACT_MOB_FARM);

		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (Blocks.SPAWNER.getLootTableKey() != key || !source.isBuiltin()) return;

			LootPool.Builder pool = LootPool.builder()
					.with(ItemEntry.builder(ModItems.SPAWNER_SHARD))
					.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(6, 8)))
					.apply(ApplyBonusLootFunction.uniformBonusCount(registries.createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), 5));

			tableBuilder.pool(pool);
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(EntityTierResourceReloadListener.ID, EntityTierResourceReloadListener::new);

		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> {
			ServerPlayNetworking.send(player, new ModPackets.EntityTierListChanged(
					EntityTiers.instance
			));
		});


		BuiltinPacksDatagen.registerBuiltinPacks();
	}

	public static Identifier id(String path) {
		return IdentifierUtils.INSTANCE.of(MOD_ID, path);
	}
}
