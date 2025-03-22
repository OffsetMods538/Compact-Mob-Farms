package top.offsetmonkey538.compactmobfarms.datagen.language;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModEstonianLanguageProvider extends FabricLanguageProvider {
    public ModEstonianLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "et_ee", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("emi_info.compact_mob_farms.sampling", "Kasuta proovivõtjat olendi \"%s\" peal 10 korda.");

        translationBuilder.add("itemGroup.compact_mob_farms.main_group", "Kompaktsed Olendi Farmid");
        translationBuilder.add("itemGroup.compact_mob_farms.filled_sample_takers_group", "Proovivõtjad");

        translationBuilder.add(ModBlocks.COMPACT_MOB_FARM.getTranslationKey() + ".unable_to_remove_tier_upgrade", "Ei saa, \"%s\" vajab seda.");
        translationBuilder.add(ModBlocks.COMPACT_MOB_FARM.getTranslationKey() + ".unable_to_insert_sample_taker", "Ei saa, \"%s\" vajab taset %s.");

        translationBuilder.add(ModItems.SPIRIT_BOTTLE, "Pudel vaime");
        translationBuilder.add(ModItems.SPAWNER_SHARD, "Tekitaja kild");
        translationBuilder.add(ModItems.COMPACT_MOB_FARM, "Kompaktne Olendi Farm");
        translationBuilder.add(ModItems.SPEED_UPGRADE, "Kiirendaja");
        translationBuilder.add(ModItems.DAMAGE_UPGRADE, "Löögikahju Täiendaja");
        translationBuilder.add(ModItems.TIER_1_UPGRADE, "Taseme kõrgendaja 1");
        translationBuilder.add(ModItems.TIER_2_UPGRADE, "Taseme kõrgendaja 2");
        translationBuilder.add(ModItems.TIER_3_UPGRADE, "Taseme kõrgendaja 3");
        translationBuilder.add(ModItems.TIER_4_UPGRADE, "Taseme kõrgendaja 4");

        translationBuilder.add(ModItems.SAMPLE_TAKER, "Proovivõtja");
        translationBuilder.add(ModItems.FILLED_SAMPLE_TAKER, "Täidetud proovivõtja");

        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.amount", "Proove võetud: %s");
        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.type", "Proovi tüüp: %s");
        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.required_tier", "Vajab taset: %s");

        translationBuilder.add("general.compact_mob_farms.unsupported", "Pole toetatud");
    }
}
