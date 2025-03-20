package top.offsetmonkey538.compactmobfarms.screen;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity;
import top.offsetmonkey538.compactmobfarms.config.EntityTiers;
import top.offsetmonkey538.compactmobfarms.item.FilledSampleTakerItem;
import top.offsetmonkey538.compactmobfarms.item.ModItems;
import top.offsetmonkey538.compactmobfarms.item.TierUpgradeItem;
import top.offsetmonkey538.compactmobfarms.item.upgrade.CompactMobFarmUpgradeItem;
import top.offsetmonkey538.compactmobfarms.network.ModPackets;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.LOGGER;

public class CompactMobFarmScreenHandler extends ScreenHandler {
    private EntityType<?> entityType;
    private float entityHealth, maxEntityHealth, attackSpeed, attackDamage;
    private boolean turnedOn;
    private final ScreenHandlerContext context;
    private Consumer<CustomPayload> sender = null;

    public CompactMobFarmScreenHandler(int syncId, PlayerInventory playerInventory, OpeningData data) {
        this(syncId, playerInventory, new SimpleInventory(1), new SimpleInventory(1), new SimpleInventory(4), new SimpleInventory(1), ScreenHandlerContext.EMPTY);

        turnedOn = data.turnedOn();
        entityHealth = data.entityHealth();
        maxEntityHealth = data.maxEntityHealth();
        attackSpeed = data.attackSpeed();
        attackDamage = data.attackDamage();
        this.entityType = data.entityType.orElse(null);
    }

    public CompactMobFarmScreenHandler(int syncId, PlayerInventory playerInventory, Inventory sampleTaker, Inventory tierUpgrade, Inventory upgrades, Inventory sword, ScreenHandlerContext context) {
        super(ModScreenHandlers.COMPACT_MOB_FARM_SCREEN_HANDLER, syncId);

        PlayerEntity player = playerInventory.player;

        this.context = context;
        // Oh my god this whole thing is so stupid but i mean if it works it works... tho it doesn't work yet as i'm
        //  still implementing it. this is a bad idea.
        context.run((world, pos) -> {
            if (!(world.getBlockEntity(pos) instanceof CompactMobFarmBlockEntity entity && player instanceof ServerPlayerEntity serverPlayer)) return;
            sender = payload -> {
                try {
                    payload = (CustomPayload) payload.getClass().getDeclaredMethod("setSyncId", Integer.class).invoke(payload, syncId);
                } catch (IllegalAccessException e) {
                    LOGGER.error("Couldn't access 'setSyncId' method on class '" + payload.getClass() + "'!", e);
                } catch (InvocationTargetException e) {
                    LOGGER.error("Couldn't execute 'setSyncId' method on class '" + payload.getClass() + "'!", e);
                } catch (NoSuchMethodException e) {
                    LOGGER.error("Class '" + payload.getClass() + "' doesn't have a 'setSyncId' method!", e);
                }
                ServerPlayNetworking.send(serverPlayer, payload);
            };

            entity.registerPacketSender(sender);
        });
        sampleTaker.onOpen(player);
        sword.onOpen(player);
        upgrades.onOpen(player);


        this.addSlot(new Slot(sampleTaker, 0, 35, 16) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return context.get((world, pos) -> {
                    if (!(stack.getItem() instanceof FilledSampleTakerItem)) return false;
                    if (!(world.getBlockEntity(pos) instanceof CompactMobFarmBlockEntity blockEntity)) return false;

                    EntityType<?> entity = FilledSampleTakerItem.getSampledEntityType(stack);
                    if (entity == null) return false; // Prolly shouldn't happen, but I guess I gotta make IntelliJ happy

                    if (TierUpgradeItem.isSupported(blockEntity.getTierUpgrade(), entity)) return true;

                    blockEntity.sendPacket(new ModPackets.GuiDisplayProblemMessage(
                            Text.translatable(ModBlocks.COMPACT_MOB_FARM.getTranslationKey() + ".unable_to_insert_sample_taker", Text.translatable(entity.getTranslationKey()), EntityTiers.instance.requiredTierFor(entity))
                    ));

                    return false;
                }, false);
            }
        });

        this.addSlot(new Slot(tierUpgrade, 0, 12, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof TierUpgradeItem;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return context.get((world, pos) -> {
                    if (!(world.getBlockEntity(pos) instanceof CompactMobFarmBlockEntity blockEntity)) return false;

                    if (blockEntity.getCurrentEntity() == null) return true;
                    final EntityType<?> currentType = blockEntity.getCurrentEntity().getType();

                    if (EntityTiers.instance.anySupports(currentType) && EntityTiers.instance.tier0Supports(currentType))
                        return true;

                    blockEntity.sendPacket(new ModPackets.GuiDisplayProblemMessage(
                            Text.translatable(ModBlocks.COMPACT_MOB_FARM.getTranslationKey() + ".unable_to_remove_tier_upgrade", Text.translatable(currentType.getTranslationKey()))
                    ));

                    return false;
                }, false);
            }
        });

        // The 4 upgrade slots
        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(upgrades, i, 35 + (i * 18), 53) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.getItem() instanceof CompactMobFarmUpgradeItem;
                }
            });
        }

        // The sword slot should be the last
        //  farm slot because we want it to be
        //  tried last when quick moving items.
        this.addSlot(new Slot(sword, 0, 89, 16));


        // The player inventory
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
        // The player Hotbar
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotId) {
        final Slot slot = this.getSlot(slotId);
        final ItemStack originalStack = slot.getStack();

        // If the item isn't *from* the player inventory, try inserting into the player inventory.
        if (!(slot.inventory instanceof PlayerInventory)) return wrapInsertItem(slot, 7, 42, true);

        // First try inserting into specific slots ...
        if (originalStack.isOf(ModItems.FILLED_SAMPLE_TAKER)) return wrapInsertItem(slot, 0);
        if (originalStack.getItem() instanceof TierUpgradeItem) return wrapInsertItem(slot, 1);
        if (originalStack.getItem() instanceof CompactMobFarmUpgradeItem) return wrapInsertItem(slot, 2, 5, false);

        // ... then to the sword slot. This way a tier upgrade wouldn't be used as a sword when quick moving.
        return wrapInsertItem(slot, 6);
    }

    /*
        The 'insertItem' method is kinda weird in my opinion. The start and end indexes are used like this:
        "...inserting to slots from startIndex to endIndex - 1 (both inclusive) until the entire stack is used."
        So for only adding to one slot, the start index would be x and end index x+1. Not sure why it's not just from
        the start index to the end index but yeah it is what it is.

        That's why my wrapper method increments the end index by one, Makes it easier for me to wrap my head around.
     */
    private ItemStack wrapInsertItem(Slot slot, int startIndex, int endIndex, boolean fromLast) {
        if (insertItem(slot.getStack(), startIndex, endIndex + 1, fromLast)) {
            slot.markDirty();
            return slot.getStack();
        }
        return ItemStack.EMPTY;
    }
    private ItemStack wrapInsertItem(Slot slot, int index) {
        return wrapInsertItem(slot, index, index, false);
    }

    private ItemStack tryInsertInSlot(ItemStack stackToInsert, int slotId) {
        final Slot insertionSlot = this.getSlot(slotId);
        final ItemStack existingStack = insertionSlot.getStack();
        if (existingStack.getCount() < insertionSlot.getMaxItemCount() && insertionSlot.canInsert(stackToInsert)) {
            if (stackToInsert.getCount() > insertionSlot.getMaxItemCount()) {
                insertionSlot.setStack(stackToInsert.split(insertionSlot.getMaxItemCount()));
            } else {
                insertionSlot.setStack(stackToInsert.split(stackToInsert.getCount()));
            }
            insertionSlot.markDirty();
            return stackToInsert;
        }

        return ItemStack.EMPTY;
    }

    private ItemStack tryInsertInSlots(ItemStack stackToInsert, int startSlotId, int endSlotId) {
        for (int insertionSlotId = startSlotId; insertionSlotId <= endSlotId; insertionSlotId++) {
            ItemStack newStack = tryInsertInSlot(stackToInsert, insertionSlotId);

            if (newStack.isEmpty()) continue;

            return newStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(context, player, ModBlocks.COMPACT_MOB_FARM);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        context.run((world, pos) -> {
            if (!(world.getBlockEntity(pos) instanceof CompactMobFarmBlockEntity entity)) return;
            entity.removePacketSender(sender);
        });
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id > 1) return false;

        context.run(((world, pos) -> {
            if (!(world.getBlockEntity(pos) instanceof CompactMobFarmBlockEntity blockEntity)) return;
            blockEntity.setTurnedOn(id == 1);
        }));

        return true;
    }

    public boolean isTurnedOn() {
        return turnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.turnedOn = turnedOn;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public float getEntityHealth() {
        return entityHealth;
    }

    public float getMaxEntityHealth() {
        return maxEntityHealth;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public float getAttackDamage() {
        return attackDamage;
    }


    public record OpeningData(
            boolean turnedOn,
            float entityHealth,
            float maxEntityHealth,
            float attackSpeed,
            float attackDamage,
            Optional<EntityType<?>> entityType
    ) {
        public static final PacketCodec<RegistryByteBuf, OpeningData> PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.BOOL,
                OpeningData::turnedOn,

                PacketCodecs.FLOAT,
                OpeningData::entityHealth,

                PacketCodecs.FLOAT,
                OpeningData::maxEntityHealth,

                PacketCodecs.FLOAT,
                OpeningData::attackSpeed,

                PacketCodecs.FLOAT,
                OpeningData::attackDamage,

                PacketCodecs.optional(PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE)),
                OpeningData::entityType,

                OpeningData::new
        );
    }
}
