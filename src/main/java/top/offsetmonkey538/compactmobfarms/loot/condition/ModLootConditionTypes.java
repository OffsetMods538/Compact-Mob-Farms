package top.offsetmonkey538.compactmobfarms.loot.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.id;

public final class ModLootConditionTypes {
    private ModLootConditionTypes() {

    }

    public static final LootConditionType FROM_FARM = register("from_farm", FromFarmLootCondition.CODEC);

    @SuppressWarnings("SameParameterValue")
    private static LootConditionType register(String name, MapCodec<? extends LootCondition> codec) {
        return Registry.register(Registries.LOOT_CONDITION_TYPE, id(name), new LootConditionType(codec));
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // Registers conditions by loading the class.
    }
}
