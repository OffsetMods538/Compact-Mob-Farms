package top.offsetmonkey538.compactmobfarms.datagen;

import java.util.function.Consumer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import top.offsetmonkey538.compactmobfarms.item.ModItems;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
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
    }
}
