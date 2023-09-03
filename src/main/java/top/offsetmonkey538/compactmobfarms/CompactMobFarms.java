package top.offsetmonkey538.compactmobfarms;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.offsetmonkey538.compactmobfarms.block.ModBlocks;

import top.offsetmonkey538.compactmobfarms.item.ModItems;

public class CompactMobFarms implements ModInitializer {
	public static final String MOD_ID = "compact-mob-farms";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Do stuff
		ModBlocks.register();
		ModItems.register();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
