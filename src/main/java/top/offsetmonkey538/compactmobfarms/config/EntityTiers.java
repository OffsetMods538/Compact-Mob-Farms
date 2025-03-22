package top.offsetmonkey538.compactmobfarms.config;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public record EntityTiers(
        @Nullable Integer priority,
        @Nullable ImmutableList<EntityType<?>> unsupported,
        @NotNull ImmutableList<EntityType<?>> tier0,
        @NotNull ImmutableList<EntityType<?>> tier1,
        @NotNull ImmutableList<EntityType<?>> tier2,
        @NotNull ImmutableList<EntityType<?>> tier3,
        @NotNull ImmutableList<EntityType<?>> tier4
) {
    public static EntityTiers instance;
    public static final Codec<EntityTiers> FILE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("priority").forGetter(EntityTiers::priority),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("unsupported").forGetter(EntityTiers::unsupported),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier0").forGetter(EntityTiers::tier0),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier1").forGetter(EntityTiers::tier1),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier2").forGetter(EntityTiers::tier2),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier3").forGetter(EntityTiers::tier3),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier4").forGetter(EntityTiers::tier4)
            ).apply(instance, EntityTiers::new)
    );
    public static final Codec<EntityTiers> EXPORT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier0").forGetter(EntityTiers::tier0),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier1").forGetter(EntityTiers::tier1),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier2").forGetter(EntityTiers::tier2),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier3").forGetter(EntityTiers::tier3),
            Registries.ENTITY_TYPE.getCodec().listOf().fieldOf("tier4").forGetter(EntityTiers::tier4)
    ).apply(instance, ((tier0, tier1, tier2, tier3, tier4) -> new EntityTiers(null, null, tier0, tier1, tier2, tier3, tier4))));

    public EntityTiers(
            @Nullable Integer priority,
            @Nullable List<EntityType<?>> unsupported,
            @NotNull List<EntityType<?>> tier0,
            @NotNull List<EntityType<?>> tier1,
            @NotNull List<EntityType<?>> tier2,
            @NotNull List<EntityType<?>> tier3,
            @NotNull List<EntityType<?>> tier4
    ) {
        this(
                priority,
                unsupported == null ? null : ImmutableList.copyOf(unsupported),
                ImmutableList.copyOf(tier0),
                ImmutableList.copyOf(tier1),
                ImmutableList.copyOf(tier2),
                ImmutableList.copyOf(tier3),
                ImmutableList.copyOf(tier4)

        );
    }

    public enum Tier {
        TIER_0,
        TIER_1,
        TIER_2,
        TIER_3,
        TIER_4
    }

    public static final class Builder {
        private Integer priority = null;
        private List<EntityType<?>> unsupported = new ArrayList<>();
        private List<EntityType<?>> tier0 = new ArrayList<>();
        private List<EntityType<?>> tier1 = new ArrayList<>();
        private List<EntityType<?>> tier2 = new ArrayList<>();
        private List<EntityType<?>> tier3 = new ArrayList<>();
        private List<EntityType<?>> tier4 = new ArrayList<>();

        public EntityTiers build() {
            return new EntityTiers(priority, unsupported, tier0, tier1, tier2, tier3, tier4);
        }

        public Builder setPriority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public Builder addUnsupported(EntityType<?> entity) {
            unsupported.add(entity);
            return this;
        }
        public Builder addTier0(EntityType<?> entity) {
            tier0.add(entity);
            return this;
        }
        public Builder addTier1(EntityType<?> entity) {
            tier1.add(entity);
            return this;
        }
        public Builder addTier2(EntityType<?> entity) {
            tier2.add(entity);
            return this;
        }
        public Builder addTier3(EntityType<?> entity) {
            tier3.add(entity);
            return this;
        }
        public Builder addTier4(EntityType<?> entity) {
            tier4.add(entity);
            return this;
        }

        public Builder addAllUnsupported(Collection<EntityType<?>> entities) {
            unsupported.addAll(entities);
            return this;
        }
        public Builder addAllTier0(Collection<EntityType<?>> entities) {
            tier0.addAll(entities);
            return this;
        }
        public Builder addAllTier1(Collection<EntityType<?>> entities) {
            tier1.addAll(entities);
            return this;
        }
        public Builder addAllTier2(Collection<EntityType<?>> entities) {
            tier2.addAll(entities);
            return this;
        }
        public Builder addAllTier3(Collection<EntityType<?>> entities) {
            tier3.addAll(entities);
            return this;
        }
        public Builder addAllTier4(Collection<EntityType<?>> entities) {
            tier4.addAll(entities);
            return this;
        }
    }

    public static void setInstance(EntityTiers instance) {
        EntityTiers.instance = instance;
    }

    public List<EntityType<?>> getSupported() {
        return Stream.of(tier0, tier1, tier2, tier3, tier4)
                .flatMap(List::stream)
                .toList();
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
    public Tier requiredTierFor(EntityType<?> entity) {
        if (!anySupports(entity)) return null;
        if (tier0.contains(entity)) return Tier.TIER_0;
        if (tier1.contains(entity)) return Tier.TIER_1;
        if (tier2.contains(entity)) return Tier.TIER_2;
        if (tier3.contains(entity)) return Tier.TIER_3;
        if (tier4.contains(entity)) return Tier.TIER_4;
        return null;
    }

    @Override
    public String toString() {
        return  "\n" + "Priority: " + (priority == null ? "null" : priority) +
                "\n" + "Unsupported: " + (unsupported == null ? "null" : unsupported) +
                "\n" + "Tier 0: " + tier0 +
                "\n" + "Tier 1: " + tier1 +
                "\n" + "Tier 2: " + tier2 +
                "\n" + "Tier 3: " + tier3 +
                "\n" + "Tier 4: " + tier4;
    }
}
