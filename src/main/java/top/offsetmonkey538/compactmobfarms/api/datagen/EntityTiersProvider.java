package top.offsetmonkey538.compactmobfarms.api.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import top.offsetmonkey538.compactmobfarms.config.EntityTierResourceReloadListener;
import top.offsetmonkey538.compactmobfarms.config.EntityTiers;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public abstract class EntityTiersProvider extends FabricCodecDataProvider<EntityTiers> {
    private final Identifier id;
    private EntityTiers tiers;

    public EntityTiersProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, Identifier id) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK, EntityTierResourceReloadListener.NAME, EntityTiers.CODEC);
        this.id = id;
    }

    @Override
    protected void configure(BiConsumer<Identifier, EntityTiers> consumer, RegistryWrapper.WrapperLookup lookup) {
        tiers = new EntityTiers();
        generate(lookup);
        consumer.accept(id, tiers);
    }

    @Override
    public String getName() {
        return EntityTierResourceReloadListener.NAME;
    }

    protected abstract void generate(RegistryWrapper.WrapperLookup lookup);

    protected void addUnsupported(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.tiers.getUnsupported().addAll(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }

    protected void addTier0(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.tiers.getTier0().addAll(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }

    protected void addTier1(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.tiers.getTier1().addAll(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }

    protected void addTier2(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.tiers.getTier2().addAll(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }

    protected void addTier3(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.tiers.getTier3().addAll(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }

    protected void addTier4(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.tiers.getTier4().addAll(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }
}
