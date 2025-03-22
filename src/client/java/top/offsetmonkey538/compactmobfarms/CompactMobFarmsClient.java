package top.offsetmonkey538.compactmobfarms;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.math.ColorHelper;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.block.entity.ModBlockEntityTypes;
import top.offsetmonkey538.compactmobfarms.client.gui.screen.ingame.CompactMobFarmScreen;
import top.offsetmonkey538.compactmobfarms.client.render.block.entity.CompactMobFarmBlockEntityRenderer;
import top.offsetmonkey538.compactmobfarms.config.EntityTiers;
import top.offsetmonkey538.compactmobfarms.item.FilledSampleTakerItem;
import top.offsetmonkey538.compactmobfarms.item.ModItems;
import top.offsetmonkey538.compactmobfarms.item.SampleTakerItem;
import top.offsetmonkey538.compactmobfarms.network.ModPackets;
import top.offsetmonkey538.compactmobfarms.screen.ModScreenHandlers;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public class CompactMobFarmsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModelPredicateProviderRegistry.register(ModItems.SAMPLE_TAKER, id("filled"), (stack, world, entity, seed) -> SampleTakerItem.getSamplesCollected(stack).size() / 10f);
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COMPACT_MOB_FARM, RenderLayer.getTranslucent());
		BlockEntityRendererFactories.register(ModBlockEntityTypes.COMPACT_MOB_FARM, CompactMobFarmBlockEntityRenderer::new);
		HandledScreens.register(ModScreenHandlers.COMPACT_MOB_FARM_SCREEN_HANDLER, CompactMobFarmScreen::new);

		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex >= 2) return -1;

			final SpawnEggItem spawnEgg = SpawnEggItem.forEntity(FilledSampleTakerItem.getSampledEntityType(stack));

			if (spawnEgg == null) return -1;

			return ColorHelper.Argb.fullAlpha(spawnEgg.getColor(tintIndex));
		}, ModItems.FILLED_SAMPLE_TAKER);

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.GuiEntityChanged.ID, (payload, context) -> {
			// IntelliJ thinks I should run '.close()' on the MinecraftClient and close the game as it inherits
			//  from AutoCloseable. Mr. IntelliJ IDEA is totally right.
			//  noinspection resource
            if (!(context.client().currentScreen instanceof CompactMobFarmScreen screen)) return;
			if (screen.getScreenHandler().syncId != payload.syncId()) return;

			screen.setEntity(payload.newEntity().orElse(null));
		});

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.GuiHealthChanged.ID, (payload, context) -> {
			// IntelliJ thinks I should run '.close()' on the MinecraftClient and close the game as it inherits
			//  from AutoCloseable. Mr. IntelliJ IDEA is totally right.
			//  noinspection resource
			if (!(context.client().currentScreen instanceof CompactMobFarmScreen screen)) return;
			if (screen.getScreenHandler().syncId != payload.syncId()) return;

			if (payload.newHealth().isEmpty()) screen.resetEntityHealth();
			else screen.setEntityHealth(payload.newHealth().get());
		});

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.GuiMaxHealthChanged.ID, (payload, context) -> {
			// IntelliJ thinks I should run '.close()' on the MinecraftClient and close the game as it inherits
			//  from AutoCloseable. Mr. IntelliJ IDEA is totally right.
			//  noinspection resource
			if (!(context.client().currentScreen instanceof CompactMobFarmScreen screen)) return;
			if (screen.getScreenHandler().syncId != payload.syncId()) return;

			screen.setMaxEntityHealth(payload.newMaxHealth());
		});

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.GuiAttackSpeedChanged.ID, (payload, context) -> {
			// IntelliJ thinks I should run '.close()' on the MinecraftClient and close the game as it inherits
			//  from AutoCloseable. Mr. IntelliJ IDEA is totally right.
			//  noinspection resource
			if (!(context.client().currentScreen instanceof CompactMobFarmScreen screen)) return;
			if (screen.getScreenHandler().syncId != payload.syncId()) return;

			screen.setAttackSpeed(payload.newAttackSpeed());
		});

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.GuiAttackDamageChanged.ID, (payload, context) -> {
			// IntelliJ thinks I should run '.close()' on the MinecraftClient and close the game as it inherits
			//  from AutoCloseable. Mr. IntelliJ IDEA is totally right.
			//  noinspection resource
			if (!(context.client().currentScreen instanceof CompactMobFarmScreen screen)) return;
			if (screen.getScreenHandler().syncId != payload.syncId()) return;

			screen.setAttackDamage(payload.newAttackDamage());
		});

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.GuiDisplayProblemMessage.ID, (payload, context) -> {
			// IntelliJ thinks I should run '.close()' on the MinecraftClient and close the game as it inherits
			//  from AutoCloseable. Mr. IntelliJ IDEA is totally right.
			//  noinspection resource
			if (!(context.client().currentScreen instanceof CompactMobFarmScreen screen)) return;
			if (screen.getScreenHandler().syncId != payload.syncId()) return;

			screen.displayProblemMessage(payload.problemMessage());
		});

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.EntityTierListChanged.ID, (payload, context) -> {
			EntityTiers.setInstance(new EntityTiers(
					null, null,
					payload.tier0(),
					payload.tier1(),
					payload.tier2(),
					payload.tier3(),
					payload.tier4()
			));
		});
	}
}
