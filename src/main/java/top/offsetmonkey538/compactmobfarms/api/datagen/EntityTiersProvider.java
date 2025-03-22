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
    private final int priority;
    private EntityTiers.Builder builder;

    public EntityTiersProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, Identifier id, int priority) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK, EntityTierResourceReloadListener.NAME, EntityTiers.FILE_CODEC);
        this.id = id;
        this.priority = priority;
    }
    public EntityTiersProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, Identifier id) {
        this(dataOutput, registriesFuture, id, 0);
    }


    @Override
    protected void configure(BiConsumer<Identifier, EntityTiers> consumer, RegistryWrapper.WrapperLookup lookup) {
        builder = new EntityTiers.Builder().setPriority(priority);
        generate(lookup);
        consumer.accept(id, builder.build());
    }

    @Override
    public String getName() {
        return "%s/%s/%s".formatted(id.getNamespace(), EntityTierResourceReloadListener.NAME, id.getPath());
    }

    protected abstract void generate(RegistryWrapper.WrapperLookup lookup);

    protected void addUnsupported(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.builder.addAllUnsupported(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }

    protected void addTier0(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.builder.addAllTier0(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }

    protected void addTier1(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.builder.addAllTier1(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }

    protected void addTier2(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.builder.addAllTier2(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }

    protected void addTier3(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.builder.addAllTier3(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }

    protected void addTier4(EntityType<?> entity, EntityType<?>... entityAdditional) {
        this.builder.addAllTier4(Stream.concat(Stream.of(entity), Stream.of(entityAdditional)).toList());
    }
}
