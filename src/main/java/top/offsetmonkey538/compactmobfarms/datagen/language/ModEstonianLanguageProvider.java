package top.offsetmonkey538.compactmobfarms.datagen.language;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import top.offsetmonkey538.compactmobfarms.item.ModItems;

public class ModEstonianLanguageProvider extends FabricLanguageProvider {
    public ModEstonianLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "et_ee");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.SPIRIT_BOTTLE, "Pudel vaime");
        translationBuilder.add(ModItems.SPAWNER_SHARD, "Tekitaja kild");

        translationBuilder.add(ModItems.SAMPLE_TAKER, "Proovivõtja");
        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".filled", "Täidetud proovivõtja");

        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.amount", "Proove võetud: %s");
        translationBuilder.add(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.type", "Proovi tüüp: %s");
    }
}
