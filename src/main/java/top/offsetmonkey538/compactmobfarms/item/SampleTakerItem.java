package top.offsetmonkey538.compactmobfarms.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.component.ModComponents;
import top.offsetmonkey538.compactmobfarms.config.EntityTiers;
import top.offsetmonkey538.monkeylib538.utils.IdentifierUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SampleTakerItem extends Item {
    public static final String SAMPLED_ENTITY_KEY = "SampledEntity";
    public static final String SAMPLES_COLLECTED_KEY = "SamplesCollected";

    public SampleTakerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity targetEntity, Hand hand) {
        final Identifier sampledEntiyIdentifier = getSampledEntityId(stack);
        final Identifier targetEntityIdentifier = Registries.ENTITY_TYPE.getId(targetEntity.getType());
        final List<UUID> samplesCollected = getSamplesCollected(stack);
        final UUID targetUuid = targetEntity.getUuid();

        if (!EntityTiers.INSTANCE.isSupported(targetEntity.getType()) || samplesCollected.contains(targetUuid) || (sampledEntiyIdentifier != null && !sampledEntiyIdentifier.equals(targetEntityIdentifier))) {
            System.out.println((user.getWorld().isClient() ? "client: " : "server: ") + "samples: " + samplesCollected.size() + " canceled\n");
            return super.useOnEntity(stack, user, targetEntity, hand);
        }
        System.out.println((user.getWorld().isClient() ? "client: " : "server: ") + "samples: " + samplesCollected.size() + "\n");

        if (user.getWorld().isClient()) return ActionResult.SUCCESS;

        if (sampledEntiyIdentifier == null) setSampledEntity(stack, targetEntityIdentifier);
        samplesCollected.add(targetUuid);
        setSamplesCollected(stack, samplesCollected);

        if (samplesCollected.size() >= 10) {
            user.setStackInHand(hand, ModItems.FILLED_SAMPLE_TAKER.forEntity(sampledEntiyIdentifier));
            return ActionResult.CONSUME;
        }

        if (user.getAbilities().creativeMode) user.setStackInHand(hand, stack);

        return ActionResult.CONSUME;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.round(getSamplesCollected(stack).size() / 10f * 13f);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        float f = Math.max(0.0f, (float) getSamplesCollected(stack).size() / 10f);
        return MathHelper.hsvToRgb(f / 3.0f, 1.0f, 1.0f);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        final List<UUID> samplesCollected = getSamplesCollected(stack);
        final EntityType<?> sampledEntity = getSampledEntityType(stack);

        if (samplesCollected.isEmpty() || sampledEntity == null) return;

        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.amount", samplesCollected.size()));
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.type", sampledEntity.getName()));
    }

    public static void setSampledEntity(ItemStack stack, Identifier entity) {
        stack.set(ModComponents.SAMPLED_ENTITY, entity);
    }

    public static void setSamplesCollected(ItemStack stack, List<UUID> samplesCollected) {
        stack.set(ModComponents.SAMPLES_COLLECTED, samplesCollected);
    }

    @Nullable
    public static Identifier getSampledEntityId(ItemStack stack) {
        if (!stack.contains(ModComponents.SAMPLED_ENTITY)) return null;
        return stack.get(ModComponents.SAMPLED_ENTITY);
    }

    @Nullable
    public static EntityType<?> getSampledEntityType(ItemStack stack) {
        final Identifier id = getSampledEntityId(stack);

        if (id == null || !Registries.ENTITY_TYPE.containsId(id)) return null;

        return Registries.ENTITY_TYPE.get(id);
    }

    public static List<UUID> getSamplesCollected(ItemStack stack) {
        if (!stack.contains(ModComponents.SAMPLES_COLLECTED)) return new ArrayList<>();
        //noinspection DataFlowIssue
        return new ArrayList<>(stack.get(ModComponents.SAMPLES_COLLECTED));
    }
}
