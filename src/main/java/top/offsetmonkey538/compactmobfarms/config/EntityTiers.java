package top.offsetmonkey538.compactmobfarms.config;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.EntityType;

public class EntityTiers {
    public static final EntityTiers INSTANCE = new EntityTiers();

    public List<EntityType<?>> UNSUPPORTED = new ArrayList<>();
    public List<EntityType<?>> TIER_0 = new ArrayList<>();
    public List<EntityType<?>> TIER_1 = new ArrayList<>();
    public List<EntityType<?>> TIER_2 = new ArrayList<>();
    public List<EntityType<?>> TIER_3 = new ArrayList<>();
    public List<EntityType<?>> TIER_4 = new ArrayList<>();

    public EntityTiers() {

    }

    public void add(EntityTiers instance) {
        UNSUPPORTED.addAll(instance.UNSUPPORTED);
        TIER_0.addAll(instance.TIER_0);
        TIER_1.addAll(instance.TIER_1);
        TIER_2.addAll(instance.TIER_2);
        TIER_3.addAll(instance.TIER_3);
        TIER_4.addAll(instance.TIER_4);
    }

    public void addOverride(EntityTiers override) {
        for (EntityType<?> entity : override.UNSUPPORTED) {
            removeFromAllTiers(entity);
            UNSUPPORTED.add(entity);
        }

        for (EntityType<?> entity : override.TIER_0) {
            removeFromAllTiers(entity);
            TIER_0.add(entity);
        }

        for (EntityType<?> entity : override.TIER_1) {
            removeFromAllTiers(entity);
            TIER_1.add(entity);
        }

        for (EntityType<?> entity : override.TIER_2) {
            removeFromAllTiers(entity);
            TIER_2.add(entity);
        }

        for (EntityType<?> entity : override.TIER_3) {
            removeFromAllTiers(entity);
            TIER_3.add(entity);
        }

        for (EntityType<?> entity : override.TIER_4) {
            removeFromAllTiers(entity);
            TIER_4.add(entity);
        }
    }

    public void clear() {
        UNSUPPORTED.clear();
        TIER_0.clear();
        TIER_1.clear();
        TIER_2.clear();
        TIER_3.clear();
        TIER_4.clear();
    }

    private void removeFromAllTiers(EntityType<?> entity) {
        UNSUPPORTED.remove(entity);
        TIER_0.remove(entity);
        TIER_1.remove(entity);
        TIER_2.remove(entity);
        TIER_3.remove(entity);
        TIER_4.remove(entity);
    }

    public int requiredTierFor(EntityType<?> entity) {
        if (!isSupported(entity)) return -1;
        if (TIER_0.contains(entity)) return 0;
        if (TIER_1.contains(entity)) return 1;
        if (TIER_2.contains(entity)) return 2;
        if (TIER_3.contains(entity)) return 3;
        if (TIER_4.contains(entity)) return 4;

        return -1;
    }

    public boolean isSupported(EntityType<?> entity) {
        if (UNSUPPORTED.contains(entity)) return false;
        return anySupports(entity);
    }

    public boolean tier0Supports(EntityType<?> entity) {
        return TIER_0.contains(entity);
    }

    public boolean tier1Supports(EntityType<?> entity) {
        return tier0Supports(entity)
                || TIER_1.contains(entity);
    }

    public boolean tier2Supports(EntityType<?> entity) {
        return tier1Supports(entity)
                || TIER_2.contains(entity);
    }

    public boolean tier3Supports(EntityType<?> entity) {
        return tier2Supports(entity)
                || TIER_3.contains(entity);
    }

    public boolean tier4Supports(EntityType<?> entity) {
        return tier3Supports(entity)
                || TIER_4.contains(entity);
    }

    public boolean anySupports(EntityType<?> entity) {
        return tier4Supports(entity);
    }

    @Override
    public String toString() {
        return  "\n" + "Unsupported: " + UNSUPPORTED +
                "\n" + "Tier 0: " + TIER_0 +
                "\n" + "Tier 1: " + TIER_1 +
                "\n" + "Tier 2: " + TIER_2 +
                "\n" + "Tier 3: " + TIER_3 +
                "\n" + "Tier 4: " + TIER_4;
    }
}
