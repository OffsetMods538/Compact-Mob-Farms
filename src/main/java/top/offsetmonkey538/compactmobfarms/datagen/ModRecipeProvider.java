package top.offsetmonkey538.compactmobfarms.datagen;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import top.offsetmonkey538.compactmobfarms.item.ModItems;
import top.offsetmonkey538.compactmobfarms.recipe.FilledSampleTakerCloningRecipe;
import top.offsetmonkey538.compactmobfarms.recipe.ModRecipes;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ComplexRecipeJsonBuilder
                .create(FilledSampleTakerCloningRecipe::new)
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

        ShapedRecipeJsonBuilder
                .create(RecipeCategory.MISC, ModItems.TIER_1_UPGRADE)
                .pattern("SIS")
                .pattern("IsI")
                .pattern("SIS")
                .input('S', ModItems.SPIRIT_BOTTLE)
                .input('I', Items.IRON_INGOT)
                .input('s', ModItems.SPAWNER_SHARD)
                .criterion("has_spawner_shard_item", InventoryChangedCriterion.Conditions.items(ModItems.SPAWNER_SHARD))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder
                .create(RecipeCategory.MISC, ModItems.TIER_2_UPGRADE)
                .pattern("SGS")
                .pattern("GTG")
                .pattern("SGS")
                .input('S', ModItems.SPIRIT_BOTTLE)
                .input('G', Items.GOLD_INGOT)
                .input('T', ModItems.TIER_1_UPGRADE)
                .criterion("has_tier_1_upgrade", InventoryChangedCriterion.Conditions.items(ModItems.TIER_1_UPGRADE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder
                .create(RecipeCategory.MISC, ModItems.TIER_3_UPGRADE)
                .pattern("SDS")
                .pattern("DTD")
                .pattern("SDS")
                .input('S', ModItems.SPIRIT_BOTTLE)
                .input('D', Items.DIAMOND)
                .input('T', ModItems.TIER_2_UPGRADE)
                .criterion("has_tier_2_upgrade", InventoryChangedCriterion.Conditions.items(ModItems.TIER_2_UPGRADE))
                .offerTo(exporter);

        SmithingTransformRecipeJsonBuilder
                .create(
                        Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.ofItems(ModItems.TIER_3_UPGRADE),
                        Ingredient.ofItems(Items.NETHERITE_INGOT),
                        RecipeCategory.MISC,
                        ModItems.TIER_4_UPGRADE)
                .criterion("has_tier_3_upgrade", InventoryChangedCriterion.Conditions.items(ModItems.TIER_3_UPGRADE))
                .offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(ModItems.TIER_4_UPGRADE));
    }
}
