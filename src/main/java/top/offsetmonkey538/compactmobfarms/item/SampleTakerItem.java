package top.offsetmonkey538.compactmobfarms.item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

        if (samplesCollected.size() >= 10 || samplesCollected.contains(targetUuid) || (sampledEntiyIdentifier != null && !sampledEntiyIdentifier.equals(targetEntityIdentifier))) return super.useOnEntity(stack, user, targetEntity, hand);


        if (user.getWorld().isClient()) return ActionResult.SUCCESS;

        if (sampledEntiyIdentifier == null) setSampledEntity(stack, targetEntityIdentifier);
        samplesCollected.add(targetUuid);
        setSamplesCollected(stack, samplesCollected);

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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        final List<UUID> samplesCollected = getSamplesCollected(stack);
        final EntityType<?> sampledEntity = getSampledEntityType(stack);

        if (samplesCollected.isEmpty() || sampledEntity == null) return;

        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.amount", samplesCollected.size()));
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.type", sampledEntity.getName()));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        if (getSamplesCollected(stack).size() >= 10) return super.getTranslationKey(stack) + ".filled";
        return super.getTranslationKey(stack);
    }

    public static void setSampledEntity(ItemStack stack, Identifier entity) {
        stack.getOrCreateNbt().putString(SAMPLED_ENTITY_KEY, entity.toString());
    }

    public static void setSamplesCollected(ItemStack stack, List<UUID> samplesCollected) {
        NbtList nbtList = new NbtList();

        samplesCollected.forEach(uuid -> nbtList.add(NbtHelper.fromUuid(uuid)));

        stack.getOrCreateNbt().put(SAMPLES_COLLECTED_KEY, nbtList);
    }

    public static Identifier getSampledEntityId(ItemStack stack) {
        if (stack.getNbt() == null || !stack.getNbt().contains(SAMPLED_ENTITY_KEY)) return null;
        return new Identifier(stack.getNbt().getString(SAMPLED_ENTITY_KEY));
    }

    public static EntityType<?> getSampledEntityType(ItemStack stack) {
        final Identifier id = getSampledEntityId(stack);

        if (id == null || !Registries.ENTITY_TYPE.containsId(id)) return null;

        return Registries.ENTITY_TYPE.get(id);
    }

    public static List<UUID> getSamplesCollected(ItemStack stack) {
        List<UUID> result = new ArrayList<>();

        if (stack.getNbt() == null || !stack.getNbt().contains(SAMPLES_COLLECTED_KEY)) return result;

        NbtList nbtList = stack.getNbt().getList(SAMPLES_COLLECTED_KEY, NbtElement.INT_ARRAY_TYPE);
        nbtList.forEach(nbtUuid -> result.add(NbtHelper.toUuid(nbtUuid)));

        return result;
    }
}
