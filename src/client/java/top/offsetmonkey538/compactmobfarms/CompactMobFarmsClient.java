package top.offsetmonkey538.compactmobfarms;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.block.entity.ModBlockEntityTypes;
import top.offsetmonkey538.compactmobfarms.client.gui.screen.ingame.CompactMobFarmScreen;
import top.offsetmonkey538.compactmobfarms.item.FilledSampleTakerItem;
import top.offsetmonkey538.compactmobfarms.item.ModItems;
import top.offsetmonkey538.compactmobfarms.item.SampleTakerItem;
import top.offsetmonkey538.compactmobfarms.network.ModPackets;
import top.offsetmonkey538.compactmobfarms.render.block.entity.CompactMobFarmBlockEntityRenderer;
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

			return spawnEgg.getColor(tintIndex);
		}, ModItems.FILLED_SAMPLE_TAKER);

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.GUI_ENTITY_CHANGED, (client, handler, buf, responseSender) -> {
			if (!(client.currentScreen instanceof CompactMobFarmScreen screen)) return;

			EntityType<?> livingEntityType = buf.readRegistryValue(Registries.ENTITY_TYPE);

			if (screen.getScreenHandler().syncId != buf.readUnsignedByte()) return;

			screen.setEntity(livingEntityType);
		});

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.GUI_ENTITY_REMOVED, (client, handler, buf, responseSender) -> {
			if (!(client.currentScreen instanceof CompactMobFarmScreen screen)) return;
			if (screen.getScreenHandler().syncId != buf.readUnsignedByte()) return;

			screen.clearEntity();
		});


		ClientPlayNetworking.registerGlobalReceiver(ModPackets.GUI_HEALTH_RESET, (client, handler, buf, responseSender) -> {
			if (!(client.currentScreen instanceof CompactMobFarmScreen screen)) return;
			if (screen.getScreenHandler().syncId != buf.readUnsignedByte()) return;

			screen.resetEntityHealth();
		});

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.GUI_HEALTH_CHANGED, (client, handler, buf, responseSender) -> {
			if (!(client.currentScreen instanceof CompactMobFarmScreen screen)) return;

			float newHealth = buf.readFloat();

			if (screen.getScreenHandler().syncId != buf.readUnsignedByte()) return;

			screen.setEntityHealth(newHealth);
		});

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.GUI_MAX_HEALTH_CHANGED, (client, handler, buf, responseSender) -> {
			if (!(client.currentScreen instanceof CompactMobFarmScreen screen)) return;

			float newMaxHealth = buf.readFloat();

			if (screen.getScreenHandler().syncId != buf.readUnsignedByte()) return;

			screen.setMaxEntityHealth(newMaxHealth);
		});
	}
}
