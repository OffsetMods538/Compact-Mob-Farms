package top.offsetmonkey538.compactmobfarms.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public final class ModRecipes {
    private ModRecipes() {

    }

    public static final SpecialRecipeSerializer<FilledSampleTakerCloningRecipe> CLONE_FILLED_SAMPLE_TAKER = register("clone_filled_sample_taker", new SpecialRecipeSerializer<>(FilledSampleTakerCloningRecipe::new));

    @SuppressWarnings("SameParameterValue")
    private static <T extends RecipeSerializer<?>> T register(String name, T item) {
        return Registry.register(Registries.RECIPE_SERIALIZER, id(name), item);
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // Registers recipe serializers by loading the class.
    }
}
