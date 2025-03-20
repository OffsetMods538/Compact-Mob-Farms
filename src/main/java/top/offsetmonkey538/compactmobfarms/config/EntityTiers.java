package top.offsetmonkey538.compactmobfarms.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;

public record EntityTiers(
        List<EntityType<?>> unsupported,
        List<EntityType<?>> tier0,
        List<EntityType<?>> tier1,
        List<EntityType<?>> tier2,
        List<EntityType<?>> tier3,
        List<EntityType<?>> tier4
        ) {
    public static final EntityTiers INSTANCE = new EntityTiers();
    public static final Codec<EntityTiers> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("unsupported").forGetter(EntityTiers::unsupported),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier0").forGetter(EntityTiers::tier0),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier1").forGetter(EntityTiers::tier1),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier2").forGetter(EntityTiers::tier2),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier3").forGetter(EntityTiers::tier3),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier4").forGetter(EntityTiers::tier4)
    ).apply(instance, EntityTiers::new));

    public EntityTiers() {
        this(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public void add(EntityTiers instance) {
        unsupported.addAll(instance.unsupported);
        tier0.addAll(instance.tier0);
        tier1.addAll(instance.tier1);
        tier2.addAll(instance.tier2);
        tier3.addAll(instance.tier3);
        tier4.addAll(instance.tier4);
    }

    public void addOverride(EntityTiers override) {
        for (EntityType<?> entity : override.unsupported) {
            removeFromAllTiers(entity);
            unsupported.add(entity);
        }

        for (EntityType<?> entity : override.tier0) {
            removeFromAllTiers(entity);
            tier0.add(entity);
        }

        for (EntityType<?> entity : override.tier1) {
            removeFromAllTiers(entity);
            tier1.add(entity);
        }

        for (EntityType<?> entity : override.tier2) {
            removeFromAllTiers(entity);
            tier2.add(entity);
        }

        for (EntityType<?> entity : override.tier3) {
            removeFromAllTiers(entity);
            tier3.add(entity);
        }

        for (EntityType<?> entity : override.tier4) {
            removeFromAllTiers(entity);
            tier4.add(entity);
        }
    }

    public void clear() {
        unsupported.clear();
        tier0.clear();
        tier1.clear();
        tier2.clear();
        tier3.clear();
        tier4.clear();
    }

    private void removeFromAllTiers(EntityType<?> entity) {
        unsupported.remove(entity);
        tier0.remove(entity);
        tier1.remove(entity);
        tier2.remove(entity);
        tier3.remove(entity);
        tier4.remove(entity);
    }

    public List<EntityType<?>> getSupported() {
        return Stream.of(tier0, tier1, tier2, tier3, tier4)
                .flatMap(List::stream)
                .toList();
    }

    public int requiredTierFor(EntityType<?> entity) {
        if (!isSupported(entity)) return -1;
        if (tier0.contains(entity)) return 0;
        if (tier1.contains(entity)) return 1;
        if (tier2.contains(entity)) return 2;
        if (tier3.contains(entity)) return 3;
        if (tier4.contains(entity)) return 4;

        return -1;
    }

    public boolean isSupported(EntityType<?> entity) {
        if (unsupported.contains(entity)) return false;
        return anySupports(entity);
    }

    public boolean tier0Supports(EntityType<?> entity) {
        return tier0.contains(entity);
    }

    public boolean tier1Supports(EntityType<?> entity) {
        return tier0Supports(entity)
                || tier1.contains(entity);
    }

    public boolean tier2Supports(EntityType<?> entity) {
        return tier1Supports(entity)
                || tier2.contains(entity);
    }

    public boolean tier3Supports(EntityType<?> entity) {
        return tier2Supports(entity)
                || tier3.contains(entity);
    }

    public boolean tier4Supports(EntityType<?> entity) {
        return tier3Supports(entity)
                || tier4.contains(entity);
    }

    public boolean anySupports(EntityType<?> entity) {
        return tier4Supports(entity);
    }

    public List<EntityType<?>> getUnsupported() {
        return unsupported;
    }

    public List<EntityType<?>> getTier0() {
        return tier0;
    }

    public List<EntityType<?>> getTier1() {
        return tier1;
    }

    public List<EntityType<?>> getTier2() {
        return tier2;
    }

    public List<EntityType<?>> getTier3() {
        return tier3;
    }

    public List<EntityType<?>> getTier4() {
        return tier4;
    }

    @Override
    public String toString() {
        return  "\n" + "Unsupported: " + unsupported +
                "\n" + "Tier 0: " + tier0 +
                "\n" + "Tier 1: " + tier1 +
                "\n" + "Tier 2: " + tier2 +
                "\n" + "Tier 3: " + tier3 +
                "\n" + "Tier 4: " + tier4;
    }
}
