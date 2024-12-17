package top.offsetmonkey538.compactmobfarms.loot.condition;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity;

import java.util.Set;
//todo: test if actually works
public class FromFarmLootCondition implements LootCondition {
    private static final FromFarmLootCondition INSTANCE = new FromFarmLootCondition();
    public static final MapCodec<FromFarmLootCondition> CODEC = MapCodec.unit(INSTANCE);

    private FromFarmLootCondition() {

    }

    @Override
    public LootConditionType getType() {
        return ModLootConditionTypes.FROM_FARM;
    }

    @Override
    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.ATTACKING_ENTITY);
    }

    @Override
    public boolean test(LootContext lootContext) {
        final Entity attacker = lootContext.get(LootContextParameters.ATTACKING_ENTITY);

        if (!(attacker instanceof FakePlayer fakePlayer)) return false;

        return CompactMobFarmBlockEntity.FAKE_PLAYER_PROFILE.equals(fakePlayer.getGameProfile());
    }

    public static LootCondition.Builder builder() {
        return () -> INSTANCE;
    }
}
