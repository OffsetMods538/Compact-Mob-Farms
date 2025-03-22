package top.offsetmonkey538.compactmobfarms.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public class EntityTierResourceReloadListener extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final String NAME = MOD_ID + "/entity_tiers";
    public static final Identifier ID = id(NAME);

    private static final Path OVERRIDES_PATH = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + "/overrides.json");

    private final RegistryOps<JsonElement> ops;

    public EntityTierResourceReloadListener(RegistryWrapper.WrapperLookup lookup) {
        super(new Gson(), NAME);
        this.ops = lookup.getOps(JsonOps.INSTANCE);
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        LOGGER.info("Loading entity tiers...");

        // Get list of entity tier configs
        // Sort list by priority value
        // Apply tier configs in order to a map of entity to tier. This way each entity will keep the last tier added.
        // Turn map into an actual EntityTiers instance storing lists for each tier.

        // Get list of entity tier configs
        final List<EntityTiers> tierLists = new ArrayList<>();
        final Map<Identifier, List<String>> failed = new LinkedHashMap<>();
        for (Map.Entry<Identifier, JsonElement> resource : prepared.entrySet()) {
            final Identifier identifier = resource.getKey();

            final DataResult<EntityTiers> dataResult = EntityTiers.FILE_CODEC.parse(ops, resource.getValue());
            final Optional<EntityTiers> optional = dataResult.resultOrPartial(error -> addToListMap(failed, identifier, error));

            if (optional.isEmpty()) {
                addToListMap(failed, identifier, "Failed to load!");
                continue;
            }

            tierLists.add(optional.get());
        }
        LOGGER.info("Loaded {} entity tier configs from datapacks.", prepared.size() - failed.size());

        if (!failed.isEmpty()) {
            LOGGER.error("{} entity tier configs failed to load:", failed.size());
            for (Map.Entry<Identifier, List<String>> fileEntry : failed.entrySet()) {
                LOGGER.error("\t- File '{}'", fileEntry.getKey());
                for (String error : fileEntry.getValue()) {
                    LOGGER.error("\t\t- {}", error);
                }
            }
        }

        // Load overrides config
        LOGGER.info("Loading overrides config from '{}'...", OVERRIDES_PATH);

        EntityTiers overrides = new EntityTiers(Integer.MAX_VALUE, ImmutableList.of(), ImmutableList.of(), ImmutableList.of(), ImmutableList.of(), ImmutableList.of(), ImmutableList.of());
        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        try {
            final JsonElement json = JsonHelper.deserialize(gson, Files.newBufferedReader(OVERRIDES_PATH), JsonElement.class);
            overrides = EntityTiers.FILE_CODEC.parse(ops, json).resultOrPartial(LOGGER::error).orElse(overrides);
            tierLists.add(overrides);
        } catch (IOException e) {
            LOGGER.error("Failed to load overrides config!", e);
        } catch (JsonParseException e) {
            LOGGER.error("Failed to parse overrides config!", e);
        } finally {
            LOGGER.info("Overrides config loaded.");

            LOGGER.info("Saving overrides config to '{}'.", OVERRIDES_PATH);
            final DataResult<JsonElement> result = EntityTiers.FILE_CODEC.encodeStart(ops, overrides);
            final JsonElement json = result.resultOrPartial(LOGGER::error).orElse(new JsonObject());
            try {
                Files.createDirectories(OVERRIDES_PATH.getParent());
                Files.writeString(OVERRIDES_PATH, gson.toJson(json));
            } catch (IOException e) {
                LOGGER.error("Failed to save overrides config!", e);
            }
        }

        // Sort list by priority value
        tierLists.sort(Comparator.comparingInt(entityTiers -> {
            assert entityTiers.priority() != null; // Loaded using EntityTiers.FILE_CODEC, which should ensure it's not null.
            return entityTiers.priority();
        }));

        // Apply tier configs in order to a map of entity to tier. This way each entity will keep the last tier added.
        final Map<EntityType<?>, EntityTiers.Tier> entityMap = new HashMap<>();
        for (EntityTiers tierList : tierLists) {
            assert tierList.unsupported() != null; // Loaded from codec as before, should have at least an empty list, I think.
            tierList.unsupported().forEach(entityMap::remove);
            tierList.tier0().forEach(entity -> entityMap.put(entity, EntityTiers.Tier.TIER_0));
            tierList.tier1().forEach(entity -> entityMap.put(entity, EntityTiers.Tier.TIER_1));
            tierList.tier2().forEach(entity -> entityMap.put(entity, EntityTiers.Tier.TIER_2));
            tierList.tier3().forEach(entity -> entityMap.put(entity, EntityTiers.Tier.TIER_3));
            tierList.tier4().forEach(entity -> entityMap.put(entity, EntityTiers.Tier.TIER_4));
        }

        // Turn map into an actual EntityTiers instance storing lists for each tier.
        final List<EntityType<?>> tier0 = new ArrayList<>();
        final List<EntityType<?>> tier1 = new ArrayList<>();
        final List<EntityType<?>> tier2 = new ArrayList<>();
        final List<EntityType<?>> tier3 = new ArrayList<>();
        final List<EntityType<?>> tier4 = new ArrayList<>();
        for (Map.Entry<EntityType<?>, EntityTiers.Tier> entry : entityMap.entrySet()) {
            switch (entry.getValue()) {
                case TIER_0 -> tier0.add(entry.getKey());
                case TIER_1 -> tier1.add(entry.getKey());
                case TIER_2 -> tier2.add(entry.getKey());
                case TIER_3 -> tier3.add(entry.getKey());
                case TIER_4 -> tier4.add(entry.getKey());
            }
        }
        EntityTiers.setInstance(new EntityTiers(null, null, tier0, tier1, tier2, tier3, tier4));
    }

    private <K, V> void addToListMap(Map<K, List<V>> map, K key, V value) {
        if (!map.containsKey(key)) {
            map.put(key, List.of(value));
            return;
        }

        List<V> list = map.get(key);
        list = Stream.concat(Stream.of(list).flatMap(List::stream), Stream.of(value)).toList();
        map.put(key, list);
    }
}
