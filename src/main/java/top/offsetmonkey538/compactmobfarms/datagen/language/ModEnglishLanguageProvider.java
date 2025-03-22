package top.offsetmonkey538.compactmobfarms.datagen.language;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("emi_info.compact_mob_farms.sampling", "Use a sample taker on a(n) %s 10 times.");

        translationBuilder.add("itemGroup.compact_mob_farms.main_group", "Compact Mob Farms");
        translationBuilder.add("itemGroup.compact_mob_farms.filled_sample_takers_group", "Sample Takers");

        translationBuilder.add(ModBlocks.COMPACT_MOB_FARM.getTranslationKey() + ".unable_to_remove_tier_upgrade", "Can't take, \"%s\" needs it.");
        translationBuilder.add(ModBlocks.COMPACT_MOB_FARM.getTranslationKey() + ".unable_to_insert_sample_taker", "Can't put, \"%s\" needs tier %s.");

        translationBuilder.add(ModItems.SPIRIT_BOTTLE, "Bottle of Spirits");
        translationBuilder.add(ModItems.SPAWNER_SHARD, "Spawner Shard");
        translationBuilder.add(ModItems.COMPACT_MOB_FARM, "Compact Mob Farm");
        translationBuilder.add(ModItems.SPEED_UPGRADE, "Speed Upgrade");
        translationBuilder.add(ModItems.DAMAGE_UPGRADE, "Damage Upgrade");
        translationBuilder.add(ModItems.TIER_1_UPGRADE, "Tier 1 Upgrade");
        translationBuilder.add(ModItems.TIER_2_UPGRADE, "Tier 2 Upgrade");
        translationBuilder.add(ModItems.TIER_3_UPGRADE, "Tier 3 Upgrade");
        translationBuilder.add(ModItems.TIER_4_UPGRADE, "Tier 4 Upgrade");

        translationBuilder.add(ModItems.SAMPLE_TAKER, "Sample Taker");
        translationBuilder.add(ModItems.FILLED_SAMPLE_TAKER, "Filled Sample Taker");

        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.amount", "Samples taken: %s");
        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.type", "Sample type: %s");
        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.required_tier", "Requires tier: %s");

        translationBuilder.add("general.compact_mob_farms.unsupported", "Unsupported");
    }
}
