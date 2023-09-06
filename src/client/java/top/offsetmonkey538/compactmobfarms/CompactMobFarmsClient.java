package top.offsetmonkey538.compactmobfarms;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import top.offsetmonkey538.compactmobfarms.item.ModItems;
import top.offsetmonkey538.compactmobfarms.item.SampleTakerItem;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public class CompactMobFarmsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModelPredicateProviderRegistry.register(ModItems.SAMPLE_TAKER, id("filled"), (stack, world, entity, seed) -> SampleTakerItem.getSamplesCollected(stack).size() / 10f);
	}
}
