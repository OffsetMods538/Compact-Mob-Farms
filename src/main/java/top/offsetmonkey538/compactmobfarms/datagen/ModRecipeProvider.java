package top.offsetmonkey538.compactmobfarms.datagen;

import java.util.function.Consumer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.recipe.ComplexRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import top.offsetmonkey538.compactmobfarms.item.ModItems;
import top.offsetmonkey538.compactmobfarms.recipe.ModRecipes;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ComplexRecipeJsonBuilder
                .create(ModRecipes.CLONE_FILLED_SAMPLE_TAKER)
                .offerTo(exporter, "clone_filled_sample_taker");

        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, ModItems.SPAWNER_SHARD)
                .input(ModItems.SPIRIT_BOTTLE, 2)
                .input(Items.GOLD_NUGGET)
                .input(Items.IRON_BARS)
                .criterion("has_spirit_bottle_item", InventoryChangedCriterion.Conditions.items(ModItems.SPIRIT_BOTTLE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder
                .create(RecipeCategory.MISC, ModItems.COMPACT_MOB_FARM)
                .pattern("OSO")
                .pattern("SsS")
                .pattern("OSO")
                .input('O', Items.OBSIDIAN)
                .input('S', ModItems.SPAWNER_SHARD)
                .input('s', ItemTags.STONE_BRICKS)
                .criterion("has_spawner_shard_item", InventoryChangedCriterion.Conditions.items(ModItems.SPAWNER_SHARD))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder
                .create(RecipeCategory.TOOLS, ModItems.SAMPLE_TAKER)
                .pattern(" I")
                .pattern("N ")
                .input('I', Items.IRON_INGOT)
                .input('N', Items.IRON_NUGGET)
                .criterion("has_iron_ingot", InventoryChangedCriterion.Conditions.items(Items.IRON_INGOT))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder
                .create(RecipeCategory.MISC, ModItems.SPEED_UPGRADE)
                .pattern("ISI")
                .pattern("SsS")
                .pattern("ISI")
                .input('I', Items.IRON_NUGGET)
                .input('S', Items.SUGAR)
                .input('s', ModItems.SPAWNER_SHARD)
                .criterion("has_spawner_shard_item", InventoryChangedCriterion.Conditions.items(ModItems.SPAWNER_SHARD))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder
                .create(RecipeCategory.MISC, ModItems.DAMAGE_UPGRADE)
                .pattern("ISI")
                .pattern("SsS")
                .pattern("ISI")
                .input('I', Items.IRON_NUGGET)
                .input('S', Items.STONE_SWORD)
                .input('s', ModItems.SPAWNER_SHARD)
                .criterion("has_spawner_shard_item", InventoryChangedCriterion.Conditions.items(ModItems.SPAWNER_SHARD))
                .offerTo(exporter);
    }
}
