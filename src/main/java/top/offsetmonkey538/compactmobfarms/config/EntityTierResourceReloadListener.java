package top.offsetmonkey538.compactmobfarms.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonPrimitive;
import java.io.InputStream;
import java.util.Map;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import top.offsetmonkey538.monkeyconfig538.ConfigManager;
import top.offsetmonkey538.monkeyconfig538.serializer.defaultSerializers.RegistrySerializer;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public class EntityTierResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    private static final Jankson JANKSON = Jankson.builder()
            .registerDeserializer(JsonPrimitive.class, EntityType.class, new RegistrySerializer<>(Registries.ENTITY_TYPE)::fromJson)
            .build();

    @Override
    public Identifier getFabricId() {
        return id("entity_tiers");
    }

    @Override
    public void reload(ResourceManager manager) {
        EntityTiers.INSTANCE.clear();

        for (Map.Entry<Identifier, Resource> resourceEntry : manager.findResources("entity_tiers", path -> path.toString().endsWith(".json")).entrySet()) {
            final Identifier resourceId = resourceEntry.getKey();
            final Resource resource = resourceEntry.getValue();

            try (InputStream stream = resource.getInputStream()) {
                EntityTiers.INSTANCE.add(JANKSON.fromJson(JANKSON.load(stream), EntityTiers.class));
            } catch (Exception e) {
                LOGGER.error("Unable to load entity tiers from '" + resourceId + "'", e);
            }
        }

        // Add the entries from the config file.
        // TODO: My stupid ass config library doesn't actually
        //  add the loaded config into the 'configs' map when
        //  you call 'ConfigManager.load(...)' so I just have
        //  to do this I guess. Stupid me and my config library.
        ConfigManager.init(new EntityTierOverridesConfig(), MOD_ID + "/overrides");
        EntityTiers.INSTANCE.addOverride(((EntityTierOverridesConfig) ConfigManager.get(MOD_ID + "/overrides")).tiers);

        LOGGER.info(EntityTiers.INSTANCE.toString());
    }
}
