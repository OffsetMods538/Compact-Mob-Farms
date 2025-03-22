package top.offsetmonkey538.compactmobfarms.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import top.offsetmonkey538.compactmobfarms.api.datagen.EntityTiersProvider;

import java.util.concurrent.CompletableFuture;

public class ModEntityTiersProvider extends EntityTiersProvider {

    public ModEntityTiersProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, Identifier.ofVanilla("minecraft"));
    }

    @Override
    protected void generate(RegistryWrapper.WrapperLookup lookup) {
        addTier0(
                EntityType.CAT,
                EntityType.CHICKEN,
                EntityType.COD,
                EntityType.COW,
                EntityType.DONKEY,
                EntityType.HORSE,
                EntityType.MOOSHROOM,
                EntityType.MULE,
                EntityType.OCELOT,
                EntityType.PARROT,
                EntityType.PIG,
                EntityType.SALMON,
                EntityType.SHEEP,
                EntityType.SKELETON_HORSE,
                EntityType.SNOW_GOLEM,
                EntityType.TROPICAL_FISH,
                EntityType.DOLPHIN,
                EntityType.LLAMA,
                EntityType.PANDA,
                EntityType.POLAR_BEAR,
                EntityType.TRADER_LLAMA,
                EntityType.HOGLIN,
                EntityType.HUSK,
                EntityType.WARDEN,
                EntityType.ZOGLIN,
                EntityType.ZOMBIE,
                EntityType.ZOMBIE_VILLAGER
        );
        addTier1(
                EntityType.GLOW_SQUID,
                EntityType.SQUID,
                EntityType.PUFFERFISH,
                EntityType.CAVE_SPIDER,
                EntityType.SPIDER,
                EntityType.ENDERMAN,
                EntityType.BLAZE,
                EntityType.CREEPER,
                EntityType.DROWNED,
                EntityType.ELDER_GUARDIAN,
                EntityType.GUARDIAN,
                EntityType.MAGMA_CUBE,
                EntityType.PHANTOM,
                EntityType.PILLAGER,
                EntityType.RAVAGER,
                EntityType.SKELETON,
                EntityType.SLIME,
                EntityType.STRAY,
                EntityType.RABBIT
        );
        addTier2(
                EntityType.IRON_GOLEM,
                EntityType.ZOMBIFIED_PIGLIN,
                EntityType.GHAST,
                EntityType.SHULKER,
                EntityType.VINDICATOR,
                EntityType.WITCH
        );
        addTier3(
                EntityType.EVOKER,
                EntityType.WITHER_SKELETON
        );
        addTier4(
                EntityType.WITHER
        );
    }


    // Old, manually written one, used as reference:
    /*
    {
        "tier0": [
            "minecraft:allay",
            "minecraft:axolotl",
            "minecraft:bat",
            "minecraft:camel",
            "minecraft:cat",
            "minecraft:chicken",
            "minecraft:cod",
            "minecraft:cow",
            "minecraft:donkey",
            "minecraft:fox",
            "minecraft:horse",
            "minecraft:mooshroom",
            "minecraft:mule",
            "minecraft:ocelot",
            "minecraft:parrot",
            "minecraft:pig",
            "minecraft:salmon",
            "minecraft:sheep",
            "minecraft:skeleton_horse",
            "minecraft:sniffer",
            "minecraft:snow_golem",
            "minecraft:strider",
            "minecraft:tadpole",
            "minecraft:tropical_fish",
            "minecraft:turtle",
            "minecraft:villager",
            "minecraft:wandering_trader",
            "minecraft:bee",
            "minecraft:dolphin",
            "minecraft:goat",
            "minecraft:llama",
            "minecraft:panda",
            "minecraft:piglin",
            "minecraft:polar_bear",
            "minecraft:trader_llama",
            "minecraft:wolf",
            "minecraft:endermite",
            "minecraft:hoglin",
            "minecraft:husk",
            "minecraft:silverfish",
            "minecraft:vex",
            "minecraft:warden",
            "minecraft:zoglin",
            "minecraft:zombie",
            "minecraft:zombie_villager"
        ],
        "tier1": [
            "minecraft:glow_squid",
            "minecraft:squid",
            "minecraft:pufferfish",
            "minecraft:cave_spider",
            "minecraft:enderman",
            "minecraft:spider",
            "minecraft:blaze",
            "minecraft:creeper",
            "minecraft:drowned",
            "minecraft:elder_guardian",
            "minecraft:guardian",
            "minecraft:magma_cube",
            "minecraft:phantom",
            "minecraft:pillager",
            "minecraft:ravager",
            "minecraft:skeleton",
            "minecraft:slime",
            "minecraft:stray",
            "minecraft:rabbit"
        ],
        "tier2": [
            "minecraft:iron_golem",
            "minecraft:zombified_piglin",
            "minecraft:piglin_brute",
            "minecraft:ghast",
            "minecraft:shulker",
            "minecraft:vindicator",
            "minecraft:witch"
        ],
        "tier3": [
            "minecraft:evoker",
            "minecraft:wither_skeleton"
        ],
        "tier4": [
            "minecraft:wither"
        ],
        "unsupported": [

        ]
    }
     */


    // Also used as a reference:
    /*
        // Unsupported

        /// Actually unsupported
        this.register(EntityType.ALLAY, LootTable.builder());
        this.register(EntityType.ARMOR_STAND, LootTable.builder());
        this.register(EntityType.AXOLOTL, LootTable.builder()); // I think?
        this.register(EntityType.BAT, LootTable.builder());
        this.register(EntityType.CAMEL, LootTable.builder());
        this.register(EntityType.ENDERMITE, LootTable.builder());
        this.register(EntityType.FOX, LootTable.builder());
        this.register(EntityType.GIANT, LootTable.builder());
        this.register(EntityType.ILLUSIONER, LootTable.builder());
        this.register(EntityType.OCELOT, LootTable.builder());
        this.register(EntityType.PLAYER, LootTable.builder());
        this.register(EntityType.SILVERFISH, LootTable.builder());
        this.register(EntityType.TADPOLE, LootTable.builder());
        this.register(EntityType.VEX, LootTable.builder());
        this.register(EntityType.VILLAGER, LootTable.builder());
        this.register(EntityType.WANDERING_TRADER, LootTable.builder());
        this.register(EntityType.WOLF, LootTable.builder());
        this.register(EntityType.PIGLIN, LootTable.builder());
        this.register(EntityType.PIGLIN_BRUTE, LootTable.builder());

        /// Add datapack
        this.register(EntityType.ARMADILLO,       LootTable.builder()); // Scutes
        this.register(EntityType.BEE,             LootTable.builder()); // Honey bottles and/or honeycomb
        this.register(EntityType.ENDER_DRAGON,    LootTable.builder()); // Maybe make em drop dragon egg? Probably not... OOO DRAGONS BREATH
        this.register(EntityType.FROG,            LootTable.builder()); // Maybe have them drop the froglights?
        this.register(EntityType.GOAT,            LootTable.builder()); // Goat horns!
        this.register(EntityType.SNIFFER,         LootTable.builder()); // Flowers maybe?
        this.register(EntityType.TURTLE, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Blocks.SEAGRASS).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BOWL)).conditionally(DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().tag(TagPredicate.expected(DamageTypeTags.IS_LIGHTNING)))))); // Scutes

        /// Drop something through code
        this.register(EntityType.WITHER, LootTable.builder());


        // Supported
        this.register(EntityType.BLAZE, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BLAZE_ROD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).conditionally(KilledByPlayerLootCondition.builder())));
        this.register(EntityType.BOGGED, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BONE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)).withLimit(1)).apply(SetPotionLootFunction.builder(Potions.POISON))).conditionally(KilledByPlayerLootCondition.builder())));
        this.register(EntityType.CAT, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.STRING).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))))));
        this.register(EntityType.CAVE_SPIDER, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.STRING).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.SPIDER_EYE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(-1.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).conditionally(KilledByPlayerLootCondition.builder())));
        this.register(EntityType.CHICKEN, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.FEATHER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.CHICKEN).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.COD, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.COD).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BONE_MEAL)).conditionally(RandomChanceLootCondition.builder(0.05F))));
        this.register(EntityType.COW, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.LEATHER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BEEF).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.CREEPER, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.GUNPOWDER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().with(TagEntry.expandBuilder(ItemTags.CREEPER_DROP_MUSIC_DISCS)).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().type(EntityTypeTags.SKELETONS)))));
        this.register(EntityType.DOLPHIN, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.COD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())))));
        this.register(EntityType.DONKEY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.LEATHER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.DROWNED, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ROTTEN_FLESH).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.COPPER_INGOT)).conditionally(KilledByPlayerLootCondition.builder()).conditionally(RandomChanceWithEnchantedBonusLootCondition.builder( this.registryLookup, 0.11F, 0.02F))));
        this.register(EntityType.ELDER_GUARDIAN,  this.createElderGuardianTableBuilder());
        this.register(EntityType.ENDERMAN, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ENDER_PEARL).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.EVOKER, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.TOTEM_OF_UNDYING))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).conditionally(KilledByPlayerLootCondition.builder())));
        this.register(EntityType.BREEZE, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BREEZE_ROD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(1.0F, 2.0F)))).conditionally(KilledByPlayerLootCondition.builder())));
        this.register(EntityType.GHAST, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.GHAST_TEAR).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.GUNPOWDER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.GLOW_SQUID, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.GLOW_INK_SAC).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.GUARDIAN, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.PRISMARINE_SHARD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.COD).weight(2).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition()))).with(ItemEntry.builder(Items.PRISMARINE_CRYSTALS).weight(2).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).with(EmptyEntry.builder())).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(LootTableEntry.builder(LootTables.FISHING_FISH_GAMEPLAY).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition()))).conditionally(KilledByPlayerLootCondition.builder()).conditionally(RandomChanceWithEnchantedBonusLootCondition.builder( this.registryLookup, 0.025F, 0.01F))));
        this.register(EntityType.HORSE, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.LEATHER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.HUSK, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ROTTEN_FLESH).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.IRON_INGOT)).with(ItemEntry.builder(Items.CARROT)).with(ItemEntry.builder(Items.POTATO).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition()))).conditionally(KilledByPlayerLootCondition.builder()).conditionally(RandomChanceWithEnchantedBonusLootCondition.builder( this.registryLookup, 0.025F, 0.01F))));
        this.register(EntityType.RAVAGER, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.SADDLE).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))));
        this.register(EntityType.IRON_GOLEM, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Blocks.POPPY).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.IRON_INGOT).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 5.0F))))));
        this.register(EntityType.LLAMA, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.LEATHER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.MAGMA_CUBE, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.MAGMA_CREAM).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(-2.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))).conditionally( this.killedByFrog().invert()).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget. this, EntityPredicate.Builder.create().typeSpecific(SlimePredicate.of(NumberRange.IntRange.atLeast(2)))))).with(ItemEntry.builder(Items.PEARLESCENT_FROGLIGHT).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))).conditionally( this.killedByFrog(FrogVariant.WARM))).with(ItemEntry.builder(Items.VERDANT_FROGLIGHT).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))).conditionally( this.killedByFrog(FrogVariant.COLD))).with(ItemEntry.builder(Items.OCHRE_FROGLIGHT).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))).conditionally( this.killedByFrog(FrogVariant.TEMPERATE)))));
        this.register(EntityType.MULE, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.LEATHER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.MOOSHROOM, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.LEATHER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BEEF).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.PANDA, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Blocks.BAMBOO).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))));
        this.register(EntityType.PARROT, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.FEATHER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.PHANTOM, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.PHANTOM_MEMBRANE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).conditionally(KilledByPlayerLootCondition.builder())));
        this.register(EntityType.PIG, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.PORKCHOP).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.PILLAGER, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.OMINOUS_BOTTLE).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))).apply(SetOminousBottleAmplifierLootFunction.builder(UniformLootNumberProvider.create(0.0F, 4.0F)))).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget. this, EntityPredicate.Builder.create().typeSpecific(RaiderPredicate.CAPTAIN_WITHOUT_RAID)))));
        this.register(EntityType.POLAR_BEAR, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.COD).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).with(ItemEntry.builder(Items.SALMON).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.PUFFERFISH, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.PUFFERFISH).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BONE_MEAL)).conditionally(RandomChanceLootCondition.builder(0.05F))));
        this.register(EntityType.RABBIT, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.RABBIT_HIDE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.RABBIT).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.RABBIT_FOOT)).conditionally(KilledByPlayerLootCondition.builder()).conditionally(RandomChanceWithEnchantedBonusLootCondition.builder( this.registryLookup, 0.1F, 0.03F))));
        this.register(EntityType.SALMON, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.SALMON).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BONE_MEAL)).conditionally(RandomChanceLootCondition.builder(0.05F))));
        this.register(EntityType.SHEEP, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.MUTTON).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.SHULKER, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.SHULKER_SHELL)).conditionally(RandomChanceWithEnchantedBonusLootCondition.builder( this.registryLookup, 0.5F, 0.0625F))));
        this.register(EntityType.SKELETON, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BONE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.SKELETON_HORSE, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BONE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.SLIME, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.SLIME_BALL).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))).conditionally( this.killedByFrog().invert())).with(ItemEntry.builder(Items.SLIME_BALL).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))).conditionally( this.killedByFrog())).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget. this, EntityPredicate.Builder.create().typeSpecific(SlimePredicate.of(NumberRange.IntRange.exactly(1)))))));
        this.register(EntityType.SNOW_GOLEM, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.SNOWBALL).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 15.0F))))));
        this.register(EntityType.SPIDER, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.STRING).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.SPIDER_EYE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(-1.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).conditionally(KilledByPlayerLootCondition.builder())));
        this.register(EntityType.SQUID, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.INK_SAC).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.STRAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BONE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)).withLimit(1)).apply(SetPotionLootFunction.builder(Potions.SLOWNESS))).conditionally(KilledByPlayerLootCondition.builder())));
        this.register(EntityType.STRIDER, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.STRING).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.TRADER_LLAMA, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.LEATHER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.TROPICAL_FISH, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.TROPICAL_FISH).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BONE_MEAL)).conditionally(RandomChanceLootCondition.builder(0.05F))));
        this.register(EntityType.WARDEN, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.SCULK_CATALYST))));
        this.register(EntityType.VINDICATOR, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).conditionally(KilledByPlayerLootCondition.builder())));
        this.register(EntityType.WITCH, LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(1.0F, 3.0F)).with(ItemEntry.builder(Items.GLOWSTONE_DUST).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).with(ItemEntry.builder(Items.SUGAR).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).with(ItemEntry.builder(Items.SPIDER_EYE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).with(ItemEntry.builder(Items.GLASS_BOTTLE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).with(ItemEntry.builder(Items.GUNPOWDER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F)))).with(ItemEntry.builder(Items.STICK).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.REDSTONE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 8.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.WITHER_SKELETON, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.COAL).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(-1.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.BONE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Blocks.WITHER_SKELETON_SKULL)).conditionally(KilledByPlayerLootCondition.builder()).conditionally(RandomChanceWithEnchantedBonusLootCondition.builder( this.registryLookup, 0.025F, 0.01F))));
        this.register(EntityType.ZOGLIN, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ROTTEN_FLESH).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.ZOMBIE, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ROTTEN_FLESH).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.IRON_INGOT)).with(ItemEntry.builder(Items.CARROT)).with(ItemEntry.builder(Items.POTATO).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition()))).conditionally(KilledByPlayerLootCondition.builder()).conditionally(RandomChanceWithEnchantedBonusLootCondition.builder( this.registryLookup, 0.025F, 0.01F))));
        this.register(EntityType.ZOMBIE_HORSE, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ROTTEN_FLESH).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.ZOMBIFIED_PIGLIN, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ROTTEN_FLESH).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.GOLD_NUGGET).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.GOLD_INGOT)).conditionally(KilledByPlayerLootCondition.builder()).conditionally(RandomChanceWithEnchantedBonusLootCondition.builder( this.registryLookup, 0.025F, 0.01F))));
        this.register(EntityType.HOGLIN, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.PORKCHOP).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition())).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.LEATHER).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))));
        this.register(EntityType.ZOMBIE_VILLAGER, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ROTTEN_FLESH).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F))).apply(EnchantedCountIncreaseLootFunction.builder( this.registryLookup, UniformLootNumberProvider.create(0.0F, 1.0F))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.IRON_INGOT)).with(ItemEntry.builder(Items.CARROT)).with(ItemEntry.builder(Items.POTATO).apply(FurnaceSmeltLootFunction.builder().conditionally( this.createSmeltLootCondition()))).conditionally(KilledByPlayerLootCondition.builder()).conditionally(RandomChanceWithEnchantedBonusLootCondition.builder( this.registryLookup, 0.025F, 0.01F))));
     */
}
