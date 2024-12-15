package top.offsetmonkey538.compactmobfarms.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.config.EntityTiers;

import java.util.List;
import java.util.Optional;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.id;

public final class ModPackets {
    private ModPackets() {

    }

    public static void register() {
        final PayloadTypeRegistry<RegistryByteBuf> registry = PayloadTypeRegistry.playS2C();

        registry.register(GuiEntityChanged.ID, GuiEntityChanged.CODEC);
        registry.register(GuiHealthChanged.ID, GuiHealthChanged.CODEC);
        registry.register(GuiMaxHealthChanged.ID, GuiMaxHealthChanged.CODEC);
        registry.register(GuiAttackSpeedChanged.ID, GuiAttackSpeedChanged.CODEC);
        registry.register(GuiAttackDamageChanged.ID, GuiAttackDamageChanged.CODEC);
        registry.register(GuiDisplayProblemMessage.ID, GuiDisplayProblemMessage.CODEC);
        registry.register(EntityTierListChanged.ID, EntityTierListChanged.CODEC);
    }

    public record GuiEntityChanged(int syncId, Optional<EntityType<?>> newEntity) implements CustomPayload {
        public static final CustomPayload.Id<GuiEntityChanged> ID = new CustomPayload.Id<>(id("gui_entity_changed"));
        public static final PacketCodec<RegistryByteBuf, GuiEntityChanged> CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT,
                GuiEntityChanged::syncId,

                PacketCodecs.optional(PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE)),
                GuiEntityChanged::newEntity,

                GuiEntityChanged::new
        );

        public GuiEntityChanged(@Nullable EntityType<?> newEntity) {
            this(-1, Optional.ofNullable(newEntity));
        }

        public GuiEntityChanged setSyncId(Integer syncId) {
            return new GuiEntityChanged(
                    syncId,
                    newEntity
            );
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record GuiHealthChanged(int syncId, Optional<Float> newHealth) implements CustomPayload {
        public static final CustomPayload.Id<GuiHealthChanged> ID = new CustomPayload.Id<>(id("gui_health_changed"));
        public static final PacketCodec<RegistryByteBuf, GuiHealthChanged> CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT,
                GuiHealthChanged::syncId,

                PacketCodecs.optional(PacketCodecs.FLOAT),
                GuiHealthChanged::newHealth,

                GuiHealthChanged::new
        );

        public GuiHealthChanged(@Nullable Float newHealth) {
            this(-1, Optional.ofNullable(newHealth));
        }

        public GuiHealthChanged setSyncId(Integer syncId) {
            return new GuiHealthChanged(
                    syncId,
                    newHealth
            );
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record GuiMaxHealthChanged(int syncId, float newMaxHealth) implements CustomPayload {
        public static final CustomPayload.Id<GuiMaxHealthChanged> ID = new CustomPayload.Id<>(id("gui_max_health_changed"));
        public static final PacketCodec<RegistryByteBuf, GuiMaxHealthChanged> CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT,
                GuiMaxHealthChanged::syncId,

                PacketCodecs.FLOAT,
                GuiMaxHealthChanged::newMaxHealth,

                GuiMaxHealthChanged::new
        );

        public GuiMaxHealthChanged(float newMaxHealth) {
            this(-1, newMaxHealth);
        }

        public GuiMaxHealthChanged setSyncId(Integer syncId) {
            return new GuiMaxHealthChanged(
                    syncId,
                    newMaxHealth
            );
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record GuiAttackSpeedChanged(int syncId, float newAttackSpeed) implements CustomPayload {
        public static final CustomPayload.Id<GuiAttackSpeedChanged> ID = new CustomPayload.Id<>(id("gui_attack_speed_changed"));
        public static final PacketCodec<RegistryByteBuf, GuiAttackSpeedChanged> CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT,
                GuiAttackSpeedChanged::syncId,

                PacketCodecs.FLOAT,
                GuiAttackSpeedChanged::newAttackSpeed,

                GuiAttackSpeedChanged::new
        );

        public GuiAttackSpeedChanged(float newAttackSpeed) {
            this(-1, newAttackSpeed);
        }

        public GuiAttackSpeedChanged setSyncId(Integer syncId) {
            return new GuiAttackSpeedChanged(
                    syncId,
                    newAttackSpeed
            );
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record GuiAttackDamageChanged(int syncId, float newAttackDamage) implements CustomPayload {
        public static final CustomPayload.Id<GuiAttackDamageChanged> ID = new CustomPayload.Id<>(id("gui_attack_damage_changed"));
        public static final PacketCodec<RegistryByteBuf, GuiAttackDamageChanged> CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT,
                GuiAttackDamageChanged::syncId,

                PacketCodecs.FLOAT,
                GuiAttackDamageChanged::newAttackDamage,

                GuiAttackDamageChanged::new
        );

        public GuiAttackDamageChanged(float newAttackDamage) {
            this(-1, newAttackDamage);
        }

        public GuiAttackDamageChanged setSyncId(Integer syncId) {
            return new GuiAttackDamageChanged(
                    syncId,
                    newAttackDamage
            );
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record GuiDisplayProblemMessage(int syncId, Text problemMessage) implements CustomPayload {
        public static final CustomPayload.Id<GuiDisplayProblemMessage> ID = new CustomPayload.Id<>(id("gui_display_problem_message"));
        public static final PacketCodec<RegistryByteBuf, GuiDisplayProblemMessage> CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT,
                GuiDisplayProblemMessage::syncId,

                TextCodecs.REGISTRY_PACKET_CODEC,
                GuiDisplayProblemMessage::problemMessage,

                GuiDisplayProblemMessage::new
        );

        public GuiDisplayProblemMessage(Text problemMessage) {
            this(-1, problemMessage);
        }

        public GuiDisplayProblemMessage setSyncId(Integer syncId) {
            return new GuiDisplayProblemMessage(
                    syncId,
                    problemMessage
            );
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record EntityTierListChanged(
            List<EntityType<?>> unsupported,
            List<EntityType<?>> tier0,
            List<EntityType<?>> tier1,
            List<EntityType<?>> tier2,
            List<EntityType<?>> tier3,
            List<EntityType<?>> tier4
    ) implements CustomPayload {
        public static final CustomPayload.Id<EntityTierListChanged> ID = new CustomPayload.Id<>(id("entity_tier_list_changed"));
        public static final PacketCodec<RegistryByteBuf, EntityTierListChanged> CODEC = PacketCodec.tuple(
                PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE).collect(PacketCodecs.toList()),
                EntityTierListChanged::unsupported,

                PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE).collect(PacketCodecs.toList()),
                EntityTierListChanged::tier0,

                PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE).collect(PacketCodecs.toList()),
                EntityTierListChanged::tier1,

                PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE).collect(PacketCodecs.toList()),
                EntityTierListChanged::tier2,

                PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE).collect(PacketCodecs.toList()),
                EntityTierListChanged::tier3,

                PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE).collect(PacketCodecs.toList()),
                EntityTierListChanged::tier4,

                EntityTierListChanged::new
        );

        public EntityTierListChanged(EntityTiers tiers) {
            this(
                    tiers.UNSUPPORTED,
                    tiers.TIER_0,
                    tiers.TIER_1,
                    tiers.TIER_2,
                    tiers.TIER_3,
                    tiers.TIER_4
            );
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
