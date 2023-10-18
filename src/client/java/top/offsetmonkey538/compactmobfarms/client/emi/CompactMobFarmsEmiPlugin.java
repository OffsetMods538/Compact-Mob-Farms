package top.offsetmonkey538.compactmobfarms.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
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


    }
}
