package top.offsetmonkey538.compactmobfarms.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import top.offsetmonkey538.compactmobfarms.datagen.language.ModEnglishLanguageProvider;
import top.offsetmonkey538.compactmobfarms.datagen.language.ModEstonianLanguageProvider;

public class CompactMobFarmsDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModBlockLootTableProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModRecipeProvider::new);

        pack.addProvider(ModEnglishLanguageProvider::new);
        pack.addProvider(ModEstonianLanguageProvider::new);
    }
}
