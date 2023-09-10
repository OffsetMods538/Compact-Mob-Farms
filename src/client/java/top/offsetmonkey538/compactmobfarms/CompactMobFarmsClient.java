package top.offsetmonkey538.compactmobfarms;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.block.entity.ModBlockEntityTypes;
import top.offsetmonkey538.compactmobfarms.client.gui.screen.ingame.CompactMobFarmScreen;
import top.offsetmonkey538.compactmobfarms.item.ModItems;
import top.offsetmonkey538.compactmobfarms.item.SampleTakerItem;
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
	}
}
