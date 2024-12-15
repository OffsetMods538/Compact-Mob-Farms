package top.offsetmonkey538.compactmobfarms.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import top.offsetmonkey538.compactmobfarms.inventory.CompactMobFarmInventory;

import java.util.List;
import java.util.UUID;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.id;

public final class ModComponents {
    private ModComponents() {

    }

    public static final ComponentType<Identifier>              SAMPLED_ENTITY    = register(ComponentType.<Identifier>builder().codec(Identifier.CODEC).packetCodec(Identifier.PACKET_CODEC).build(), "sampled_entity");
    public static final ComponentType<List<UUID>>              SAMPLES_COLLECTED = register(ComponentType.<List<UUID>>builder().codec(Codec.list(Uuids.CODEC)).packetCodec(Uuids.PACKET_CODEC.collect(PacketCodecs.toList())).build(), "samples_collected");

    // For CompactMobFarm block item
    public static final ComponentType<CompactMobFarmInventory> DROP_INVENTORY    = register(ComponentType.<CompactMobFarmInventory>builder().codec(CompactMobFarmInventory.CODEC).packetCodec(CompactMobFarmInventory.PACKET_CODEC).build(), "drop_inventory");
    public static final ComponentType<ItemStack>               SAMPLE_TAKER      = register(ComponentType.<ItemStack>builder().codec(ItemStack.OPTIONAL_CODEC).packetCodec(ItemStack.OPTIONAL_PACKET_CODEC).build(), "sample_taker");
    public static final ComponentType<ItemStack>               TIER_UPGRADE      = register(ComponentType.<ItemStack>builder().codec(ItemStack.OPTIONAL_CODEC).packetCodec(ItemStack.OPTIONAL_PACKET_CODEC).build(), "tier_upgrade");
    public static final ComponentType<List<ItemStack>>         UPGRADES          = register(ComponentType.<List<ItemStack>>builder().codec(Codec.list(ItemStack.OPTIONAL_CODEC)).packetCodec(ItemStack.OPTIONAL_PACKET_CODEC.collect(PacketCodecs.toList())).build(), "upgrades");
    public static final ComponentType<ItemStack>               SWORD             = register(ComponentType.<ItemStack>builder().codec(ItemStack.OPTIONAL_CODEC).packetCodec(ItemStack.OPTIONAL_PACKET_CODEC).build(), "sword");
    public static final ComponentType<Boolean>                 TURNED_ON         = register(ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build(), "turned_on");

    private static <T> ComponentType<T> register(ComponentType<T> component, String name) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id(name), component);
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // Registers components by loading the class.
    }
}
