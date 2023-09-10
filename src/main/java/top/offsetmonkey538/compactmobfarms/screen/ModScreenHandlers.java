package top.offsetmonkey538.compactmobfarms.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public final class ModScreenHandlers {
    private ModScreenHandlers() {

    }

    public static final ScreenHandlerType<CompactMobFarmScreenHandler> COMPACT_MOB_FARM_SCREEN_HANDLER = register(CompactMobFarmScreenHandler::new, "sample_taker");

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(ScreenHandlerType.Factory<T> factory, String name) {
        return Registry.register(Registries.SCREEN_HANDLER, id(name), new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // Registers items by loading the class.
    }
}
