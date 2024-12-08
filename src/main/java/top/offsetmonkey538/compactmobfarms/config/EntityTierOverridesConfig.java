package top.offsetmonkey538.compactmobfarms.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonPrimitive;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import top.offsetmonkey538.monkeylib538.config.Config;
import top.offsetmonkey538.monkeylib538.utils.IdentifierUtils;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.MOD_ID;

public class EntityTierOverridesConfig extends Config {

    public EntityTiers tiers = new EntityTiers();

    @Override
    protected String getName() {
        return MOD_ID + "/overrides.json";
    }

    @Override
    protected Jankson.Builder configureJankson(Jankson.Builder builder) {
        builder.registerSerializer(EntityType.class, (entityType, marshaller) -> marshaller.serialize(Registries.ENTITY_TYPE.getId(entityType)));
        builder.registerDeserializer(JsonPrimitive.class, EntityType.class, (json, marshaller) -> Registries.ENTITY_TYPE.get(IdentifierUtils.INSTANCE.of(marshaller.marshall(String.class, json))));

        return super.configureJankson(builder);
    }
}
