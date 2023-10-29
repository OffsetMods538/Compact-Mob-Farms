package top.offsetmonkey538.compactmobfarms.client.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import java.util.List;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import top.offsetmonkey538.compactmobfarms.config.EntityTiers;
import top.offsetmonkey538.compactmobfarms.item.ModItems;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public class CompactMobFarmsEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(id("/spirit_bottle"))
                .leftInput(EmiStack.of(Items.GLASS_BOTTLE))
                .rightInput(EmiStack.of(Items.SPAWNER), true)
                .output(EmiStack.of(ModItems.SPIRIT_BOTTLE))
                .build()
        );

        for (EntityType<?> entity : EntityTiers.INSTANCE.getSupported()) {
            registry.addRecipe(new EmiInfoRecipe(
                    List.of(
                            EmiIngredient.of(Ingredient.ofItems(ModItems.SAMPLE_TAKER)),
                            EmiIngredient.of(Ingredient.ofStacks(ModItems.FILLED_SAMPLE_TAKER.forEntity(entity)))
                    ),
                    List.of(
                            Text.translatable(
                                    "emi_info.compact_mob_farms.sampling",
                                    Text.translatable(entity.getTranslationKey())
                            )
                    ),
                    id("/sampling/" + entity)
            ));
        }

        registry.setDefaultComparison(ModItems.FILLED_SAMPLE_TAKER, Comparison.compareNbt());
    }
}
