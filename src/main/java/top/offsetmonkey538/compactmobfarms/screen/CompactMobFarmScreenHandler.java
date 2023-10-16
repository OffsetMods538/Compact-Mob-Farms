package top.offsetmonkey538.compactmobfarms.screen;

import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity;
import top.offsetmonkey538.compactmobfarms.config.EntityTiers;
import top.offsetmonkey538.compactmobfarms.item.FilledSampleTakerItem;
import top.offsetmonkey538.compactmobfarms.item.TierUpgradeItem;
import top.offsetmonkey538.compactmobfarms.item.upgrade.CompactMobFarmUpgradeItem;
import top.offsetmonkey538.compactmobfarms.network.ModPackets;

public class CompactMobFarmScreenHandler extends ScreenHandler {
    private EntityType<?> entityType;
    private float entityHealth, maxEntityHealth, attackSpeed, attackDamage;
    private boolean turnedOn;
    private final ScreenHandlerContext context;
    private BiConsumer<Identifier, PacketByteBuf> sender = null;

    public CompactMobFarmScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(1), new SimpleInventory(1), new SimpleInventory(4), new SimpleInventory(1), ScreenHandlerContext.EMPTY);

        turnedOn = buf.readBoolean();
        entityHealth = buf.readFloat();
        maxEntityHealth = buf.readFloat();
        attackSpeed = buf.readFloat();
        attackDamage = buf.readFloat();
        if (buf.readBoolean()) this.entityType = buf.readRegistryValue(Registries.ENTITY_TYPE);
    }

    public CompactMobFarmScreenHandler(int syncId, PlayerInventory playerInventory, Inventory sampleTaker, Inventory tierUpgrade, Inventory upgrades, Inventory sword, ScreenHandlerContext context) {
        super(ModScreenHandlers.COMPACT_MOB_FARM_SCREEN_HANDLER, syncId);

        PlayerEntity player = playerInventory.player;

        this.context = context;
        context.run((world, pos) -> {
            if (!(world.getBlockEntity(pos) instanceof CompactMobFarmBlockEntity entity && player instanceof ServerPlayerEntity serverPlayer)) return;
            sender = (id, buf) -> {
                buf.writeByte(syncId);
                ServerPlayNetworking.send(serverPlayer, id, buf);
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

                    PacketByteBuf buf = PacketByteBufs.create();

                    buf.writeText(Text.translatable(ModBlocks.COMPACT_MOB_FARM.getTranslationKey() + ".unable_to_insert_sample_taker", Text.translatable(entity.getTranslationKey()), EntityTiers.INSTANCE.requiredTierFor(entity)));

                    blockEntity.sendPacket(ModPackets.GUI_DISPLAY_PROBLEM_MESSAGE, buf);

                    return false;
                }, false);
            }
        });

        this.addSlot(new Slot(sword, 0, 89, 16));
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

                    if (EntityTiers.INSTANCE.isSupported(currentType) && EntityTiers.INSTANCE.tier0Supports(currentType))
                        return true;

                    PacketByteBuf buf = PacketByteBufs.create();

                    buf.writeText(Text.translatable(ModBlocks.COMPACT_MOB_FARM.getTranslationKey() + ".unable_to_remove_tier_upgrade", Text.translatable(currentType.getTranslationKey())));

                    blockEntity.sendPacket(ModPackets.GUI_DISPLAY_PROBLEM_MESSAGE, buf);

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
        // fixme: it don't do the worky

        final Slot slot = this.getSlot(slotId);

        final ItemStack originalStack = slot.getStack();
        ItemStack newStack = originalStack.copy();

        if (slotId < 2 && !this.insertItem(originalStack, 1, this.slots.size(), true)) newStack = ItemStack.EMPTY;
        if (!this.insertItem(originalStack, 0, 1, false)) newStack = ItemStack.EMPTY;

        if (originalStack.isEmpty()) slot.setStack(ItemStack.EMPTY);

        slot.markDirty();

        return newStack;
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
}
