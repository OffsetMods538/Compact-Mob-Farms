package top.offsetmonkey538.compactmobfarms.item;

import java.util.List;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FilledSampleTakerItem extends Item {
    public static final String SAMPLED_ENTITY_KEY = "SampledEntity";

    public FilledSampleTakerItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        final EntityType<?> sampledEntity = getSampledEntityType(stack);

        if (sampledEntity == null) return;

        tooltip.add(Text.translatable(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.type", sampledEntity.getName()));
    }

    public static void setSampledEntity(ItemStack stack, Identifier entity) {
        stack.getOrCreateNbt().putString(SAMPLED_ENTITY_KEY, entity.toString());
    }

    @Nullable
    public static Identifier getSampledEntityId(ItemStack stack) {
        if (stack.getNbt() == null || !stack.getNbt().contains(SAMPLED_ENTITY_KEY)) return null;
        return new Identifier(stack.getNbt().getString(SAMPLED_ENTITY_KEY));
    }

    @Nullable
    public static EntityType<?> getSampledEntityType(ItemStack stack) {
        final Identifier id = getSampledEntityId(stack);

        if (id == null || !Registries.ENTITY_TYPE.containsId(id)) return null;

        return Registries.ENTITY_TYPE.get(id);
    }

    public ItemStack forEntity(@Nullable EntityType<?> entity) {
        return forEntity(Registries.ENTITY_TYPE.getId(entity));
    }

    public ItemStack forEntity(Identifier entityId) {
        final ItemStack stack = new ItemStack(this);

        setSampledEntity(stack, entityId);

        return stack;
    }

    @Override
    public ItemStack getDefaultStack() {
        return forEntity(Registries.ENTITY_TYPE.getDefaultId());
    }
}
