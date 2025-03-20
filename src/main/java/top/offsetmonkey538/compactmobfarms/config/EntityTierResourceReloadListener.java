package top.offsetmonkey538.compactmobfarms.config;

import com.google.gson.*;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
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

        EntityTiers.INSTANCE.clear();

        final Map<Identifier, List<String>> failed = new LinkedHashMap<>();
        for (Map.Entry<Identifier, JsonElement> resource : prepared.entrySet()) {
            final Identifier identifier = resource.getKey();

            final DataResult<EntityTiers> result = EntityTiers.CODEC.parse(ops, resource.getValue());
            final Optional<EntityTiers> optional = result.resultOrPartial(error -> addToListMap(failed, identifier, error));

            if (optional.isEmpty()) {
                addToListMap(failed, identifier, "Failed to load!");
                continue;
            }

            EntityTiers.INSTANCE.add(optional.get());
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

        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        EntityTiers overrides = new EntityTiers();
        try {
            final JsonElement json = JsonHelper.deserialize(gson, Files.newBufferedReader(OVERRIDES_PATH), JsonElement.class);
            overrides = EntityTiers.CODEC.parse(ops, json).resultOrPartial(LOGGER::error).orElse(new EntityTiers());
            EntityTiers.INSTANCE.addOverride(overrides);

        } catch (IOException e) {
            LOGGER.error("Failed to load overrides config!", e);
        } catch (JsonParseException e) {
            LOGGER.error("Failed to parse overrides config!", e);
        } finally {
            LOGGER.info("Overrides config loaded.");

            LOGGER.info("Saving overrides config to '{}'.", OVERRIDES_PATH);
            final DataResult<JsonElement> result = EntityTiers.CODEC.encodeStart(ops, overrides);
            final JsonElement json = result.resultOrPartial(LOGGER::error).orElse(new JsonObject());
            try {
                Files.createDirectories(OVERRIDES_PATH.getParent());
                Files.writeString(OVERRIDES_PATH, gson.toJson(json));
            } catch (IOException e) {
                LOGGER.error("Failed to save overrides config!", e);
            }
        }
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
