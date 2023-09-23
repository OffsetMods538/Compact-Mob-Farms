package top.offsetmonkey538.compactmobfarms.datagen.language;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import top.offsetmonkey538.compactmobfarms.item.ModItems;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.SPIRIT_BOTTLE, "Bottle of Spirits");

        translationBuilder.add(ModItems.SAMPLE_TAKER, "Sample taker");
        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".filled", "Filled sample taker");

        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.amount", "Samples taken: %s");
        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.type", "Sample type: %s");
    }
}
