package top.offsetmonkey538.compactmobfarms.item;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.component.ModComponents;

import java.util.List;

public class FilledSampleTakerItem extends Item {
    public static final String SAMPLED_ENTITY_KEY = "SampledEntity";

    public FilledSampleTakerItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        final EntityType<?> sampledEntity = getSampledEntityType(stack);

        if (sampledEntity == null) return;

        tooltip.add(Text.translatable(ModItems.SAMPLE_TAKER.getTranslationKey() + ".tooltip.type", sampledEntity.getName()));
    }

    public static void setSampledEntity(ItemStack stack, Identifier entity) {
        stack.set(ModComponents.SAMPLED_ENTITY, entity);
    }

    @Nullable
    public static Identifier getSampledEntityId(ItemStack stack) {
        return SampleTakerItem.getSampledEntityId(stack);
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
