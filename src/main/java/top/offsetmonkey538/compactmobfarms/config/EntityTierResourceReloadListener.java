package top.offsetmonkey538.compactmobfarms.config;

import blue.endless.jankson.Jankson;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import top.offsetmonkey538.monkeylib538.config.ConfigManager;

import java.io.InputStream;
import java.util.Map;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.LOGGER;
import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.id;

public class EntityTierResourceReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return id("entity_tiers");
    }

    @Override
    public void reload(ResourceManager manager) {
        EntityTiers.INSTANCE.clear();

        // Load overrides config.
        final EntityTierOverridesConfig overridesConfig = ConfigManager.init(new EntityTierOverridesConfig(), LOGGER::error);
        final Jankson jankson = overridesConfig.configureJankson(Jankson.builder()).build();


        // Add entries from datapacks.
        for (Map.Entry<Identifier, Resource> resourceEntry : manager.findResources("entity_tiers", path -> path.toString().endsWith(".json")).entrySet()) {
            final Identifier resourceId = resourceEntry.getKey();
            final Resource resource = resourceEntry.getValue();

            try (InputStream stream = resource.getInputStream()) {
                EntityTiers.INSTANCE.add(jankson.fromJson(jankson.load(stream), EntityTiers.class));
            } catch (Exception e) {
                LOGGER.error("Unable to load entity tiers from '" + resourceId + "'", e);
            }
        }

        // Add the entries from the config file.
        EntityTiers.INSTANCE.addOverride(overridesConfig.tiers);

        LOGGER.info(EntityTiers.INSTANCE.toString());
    }
}
