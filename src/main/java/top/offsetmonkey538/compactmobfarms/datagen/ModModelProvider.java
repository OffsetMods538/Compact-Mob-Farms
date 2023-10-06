package top.offsetmonkey538.compactmobfarms.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.SimpleModelSupplier;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.item.ModItems;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleState(ModBlocks.COMPACT_MOB_FARM);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SPIRIT_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SPAWNER_SHARD, Models.GENERATED);
        Models.GENERATED.upload(
                ModelIds.getItemModelId(ModItems.FILLED_SAMPLE_TAKER),
                TextureMap
                        .layer0(ModItems.FILLED_SAMPLE_TAKER)
                        .register(TextureKey.LAYER1, TextureMap.getSubId(ModItems.FILLED_SAMPLE_TAKER, "_layer_1"))
                        .register(TextureKey.LAYER2, TextureMap.getSubId(ModItems.FILLED_SAMPLE_TAKER, "_overlay")),
                itemModelGenerator.writer
        );

        itemModelGenerator.writer.accept(ModelIds.getItemModelId(ModItems.COMPACT_MOB_FARM), new SimpleModelSupplier(ModelIds.getBlockModelId(ModBlocks.COMPACT_MOB_FARM)));
        Models.GENERATED.upload(ModelIds.getItemModelId(ModItems.SAMPLE_TAKER), TextureMap.layer0(ModItems.SAMPLE_TAKER), itemModelGenerator.writer, this::sampleTakerJsonGenerator);
        for (int i = 1; i <= 9; i++) {
            itemModelGenerator.register(ModItems.SAMPLE_TAKER, "_filled_" + i, Models.GENERATED);
        }
    }

    private JsonObject sampleTakerJsonGenerator(Identifier id, Map<TextureKey, Identifier> textures) {
        JsonObject json = Models.GENERATED.createJson(id, textures);

        JsonArray overrides = new JsonArray();
        for (int filled = 1; filled <= 9; filled++) {
            JsonObject override = new JsonObject();
            JsonObject predicate = new JsonObject();

            predicate.addProperty(id("filled").toString(), filled  / 10f);
            override.add("predicate", predicate);

            override.addProperty("model", id.withSuffixedPath("_filled_" + filled).toString());

            overrides.add(override);
        }
        json.add("overrides", overrides);

        return json;
    }
}
