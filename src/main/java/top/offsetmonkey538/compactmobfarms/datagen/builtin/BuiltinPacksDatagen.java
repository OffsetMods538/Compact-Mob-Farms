package top.offsetmonkey538.compactmobfarms.datagen.builtin;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.TagEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.api.datagen.EntityTiersProvider;
import top.offsetmonkey538.compactmobfarms.loot.condition.FromFarmLootCondition;
import top.offsetmonkey538.loottablemodifier.api.datagen.LootModifierProvider;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.MOD_ID;
import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.id;

public class BuiltinPacksDatagen {

    private enum BuiltinPack {
        //this.register(EntityType.ARMADILLO,       LootTable.builder()); // Scutes
        //this.register(EntityType.BEE,             LootTable.builder()); // Honey bottles and/or honeycomb
        //this.register(EntityType.ENDER_DRAGON,    LootTable.builder()); // Maybe make em drop dragon egg? Probably not... OOO DRAGONS BREATH
        //this.register(EntityType.FROG,            LootTable.builder()); // Maybe have them drop the froglights?
        //this.register(EntityType.GOAT,            LootTable.builder()); // Goat horns!
        //this.register(EntityType.SNIFFER,         LootTable.builder()); // Flowers maybe?
        //this.register(EntityType.TURTLE,          LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Blocks.SEAGRASS).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BOWL)).conditionally(DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().tag(TagPredicate.expected(DamageTypeTags.IS_LIGHTNING)))))); // Scutes

        ARMADILLO_SCUTE("armadillo", Text.literal("CMF - Armadillo - Scutes"), 1, 2, EntityType.ARMADILLO, 0, Items.ARMADILLO_SCUTE),
        TURTLE("turtle", Text.literal("CMF - Turtle - Scutes"), 1, 2, EntityType.TURTLE, 1, Items.TURTLE_SCUTE),
        BEE("bee", Text.literal("CMF - Bee - Honey and -combs"), 1, 1, EntityType.BEE, 0, Items.HONEY_BOTTLE, Items.HONEYCOMB),
        ENDER_DRAGON("ender_dragon", Text.literal("CMF - Ender Dragon - Breath"), 1, 2, EntityType.ENDER_DRAGON, 2, Items.DRAGON_BREATH),
        FROG("frog", Text.literal("CMF - Frog - Froglights"), 1, 3, EntityType.FROG, 0, Items.OCHRE_FROGLIGHT, Items.VERDANT_FROGLIGHT, Items.PEARLESCENT_FROGLIGHT),
        GOAT("goat", Text.literal("CMF - Goat - Horns"), 1, 2, EntityType.GOAT, 3, Items.GOAT_HORN),
        SNIFFER("sniffer", Text.literal("CMF - Sniffer - Flowers"), 1, 5, EntityType.SNIFFER, 1, ItemTags.FLOWERS),
        WANDERING_TRADER("wandering_trader", Text.literal("CMF - Wandering Trader - Leads"), 1, 2, EntityType.WANDERING_TRADER, 1, Items.LEAD);


        public final String id;
        public final Text displayName;
        public final int minDrop, maxDrop;
        public final EntityType<?> entity;
        public final int entityTier;
        public final Item[] dropsItem;
        public final TagKey<Item>[] dropsTag;

        BuiltinPack(String id, Text displayName, int minDrop, int maxDrop, EntityType<?> entity, int entityTier, Item... drops) {
            this(
                    id,
                    displayName,
                    minDrop,
                    maxDrop,
                    entity,
                    entityTier,
                    drops,
                    null
            );
        }

        @SafeVarargs
        BuiltinPack(String id, Text displayName, int minDrop, int maxDrop, EntityType<?> entity, int entityTier, TagKey<Item>... drops){
            this(
                    id,
                    displayName,
                    minDrop,
                    maxDrop,
                    entity,
                    entityTier,
                    null,
                    drops
            );
        }
        BuiltinPack(String id, Text displayName, int minDrop, int maxDrop, EntityType<?> entity, int entityTier, @Nullable Item[] dropsItem, @Nullable TagKey<Item>[] dropsTag) {
            this.id = id;
            this.displayName = displayName;
            this.minDrop = minDrop;
            this.maxDrop = maxDrop;
            this.entity = entity;
            this.entityTier = entityTier;
            this.dropsItem = dropsItem == null ? new Item[]{} : dropsItem;
            //noinspection unchecked
            this.dropsTag = dropsTag == null ? new TagKey[]{} : dropsTag;
        }

        public Identifier getId() {
            return id(id);
        }
    }

    public static void runDatagen(FabricDataGenerator fabricDataGenerator) {
        for (BuiltinPack pack : BuiltinPack.values()) {
            final FabricDataGenerator.Pack fabricPack = fabricDataGenerator.createBuiltinResourcePack(pack.getId());

            fabricPack.addProvider((fabricDataOutput, completableFuture) -> new LootModifier(fabricDataOutput, completableFuture, pack));
            fabricPack.addProvider((fabricDataOutput, completableFuture) -> new BuiltinEntityTiersProvider(fabricDataOutput, completableFuture, pack));
        }
    }

    public static void registerBuiltinPacks() {
        for (BuiltinPack pack : BuiltinPack.values()) ResourceManagerHelper.registerBuiltinResourcePack(
                pack.getId(),
                FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                pack.displayName,
                ResourcePackActivationType.NORMAL
        );
    }

    private static class LootModifier extends LootModifierProvider {
        private final BuiltinPack pack;

        public LootModifier(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, BuiltinPack pack) {
            super(dataOutput, registriesFuture);
            this.pack = pack;
        }

        @Override
        protected void generate(RegistryWrapper.WrapperLookup wrapperLookup) {
            addModifier(
                    pack.getId(),
                    Stream.concat(
                            Arrays.stream(pack.dropsItem)
                                    .map(item -> LootPool
                                            .builder()
                                            .rolls(ConstantLootNumberProvider.create(1))
                                            .with(
                                                    ItemEntry
                                                            .builder(item)
                                                            .apply(
                                                                    SetCountLootFunction
                                                                            .builder(UniformLootNumberProvider.create(pack.minDrop, pack.maxDrop))
                                                            )
                                                            .conditionally(
                                                                    FromFarmLootCondition.builder()
                                                            )
                                            )
                                    ),
                            Arrays.stream(pack.dropsTag)
                                    .map(tag -> LootPool
                                            .builder()
                                            .rolls(ConstantLootNumberProvider.create(1))
                                            .with(
                                                    TagEntry
                                                            .builder(tag)
                                                            .apply(
                                                                    SetCountLootFunction
                                                                            .builder(UniformLootNumberProvider.create(pack.minDrop, pack.maxDrop))
                                                            )
                                                            .conditionally(
                                                                    FromFarmLootCondition.builder()
                                                            )
                                            )
                                    )
                            )
                            .toList(),
                    pack.entity
            );
        }
    }

    private static class BuiltinEntityTiersProvider extends EntityTiersProvider {
        private final BuiltinPack pack;

        public BuiltinEntityTiersProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, BuiltinPack pack) {
            super(dataOutput, registriesFuture, pack.getId());
            this.pack = pack;
        }

        @Override
        protected void generate(RegistryWrapper.WrapperLookup lookup) {
            switch (pack.entityTier) {
                case 0: addTier0(pack.entity); break;
                case 1: addTier1(pack.entity); break;
                case 2: addTier2(pack.entity); break;
                case 3: addTier3(pack.entity); break;
                case 4: addTier4(pack.entity); break;
                default: throw new IllegalArgumentException("Builtin pack has no valid entity tier set! Pack:" + pack);
            }
        }
    }
}
