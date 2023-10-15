package top.offsetmonkey538.compactmobfarms.recipe;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import top.offsetmonkey538.compactmobfarms.item.ModItems;
import top.offsetmonkey538.compactmobfarms.item.SampleTakerItem;

public class FilledSampleTakerCloningRecipe extends SpecialCraftingRecipe {
    public FilledSampleTakerCloningRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        boolean foundFilledSampleTaker = false;
        boolean foundEmptySampleTaker = false;
        boolean foundSpiritBottle = false;

        for (int i = 0; i < inventory.getInputStacks().size(); i++) {
            final ItemStack stack = inventory.getInputStacks().get(i);
            if (stack.isEmpty()) continue;

            if (stack.isOf(ModItems.FILLED_SAMPLE_TAKER)) {
                if (foundFilledSampleTaker) return false;
                foundFilledSampleTaker = true;
            }

            if (stack.isOf(ModItems.SAMPLE_TAKER)) {
                if (SampleTakerItem.getSamplesCollected(stack).size() > 0) return false;

                if (foundEmptySampleTaker) return false;
                foundEmptySampleTaker = true;
            }

            if (stack.isOf(ModItems.SPIRIT_BOTTLE)) {
                if (foundSpiritBottle) return false;
                foundSpiritBottle = true;
            }
        }

        return foundFilledSampleTaker &&
                foundEmptySampleTaker &&
                foundSpiritBottle;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        final ItemStack filledSampleTaker = inventory.getInputStacks().stream()
                .filter(stack -> stack.isOf(ModItems.FILLED_SAMPLE_TAKER))
                .toList().get(0);

        return filledSampleTaker.copyWithCount(2);
    }

    @Override
    public boolean fits(int width, int height) {
        return (width * height >= 3);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CLONE_FILLED_SAMPLE_TAKER;
    }
}
