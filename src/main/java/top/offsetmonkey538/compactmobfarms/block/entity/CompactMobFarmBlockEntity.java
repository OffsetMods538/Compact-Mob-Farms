package top.offsetmonkey538.compactmobfarms.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.accessor.EntityAccessor;
import top.offsetmonkey538.compactmobfarms.accessor.LivingEntityAccessor;
import top.offsetmonkey538.compactmobfarms.inventory.CompactMobFarmInventory;
import top.offsetmonkey538.compactmobfarms.item.FilledSampleTakerItem;
import top.offsetmonkey538.compactmobfarms.item.upgrade.CompactMobFarmUpgradeItem;
import top.offsetmonkey538.compactmobfarms.network.ModPackets;
import top.offsetmonkey538.compactmobfarms.screen.CompactMobFarmScreenHandler;

public class CompactMobFarmBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {
    public static final String DROP_INVENTORY_NBT_KEY = "DropInventory";
    public static final String SAMPLE_TAKER_NBT_KEY = "SampleTaker";
    public static final String UPGRADES_NBT_KEY = "Upgrades";
    public static final String SWORD_NBT_KEY = "Sword";
    public static final String TURNED_ON_NBT_KEY = "Sword";

    public static final int DEFAULT_ATTACK_SPEED = 30 * 20; // 30 seconds, multiplied by 20 because it needs to be in ticks.
    public static final Item NUGGET_OF_EXPERIENCE = Registries.ITEM.get(new Identifier("create:experience_nugget"));

    private int killTimer = 0;
    private boolean isTurnedOn = true;
    private float maxEntityHealth = -1;
    private float currentEntityHealth = -1;
    private LivingEntity currentEntity = null;
    private final List<BiConsumer<Identifier, PacketByteBuf>> packetSenders = new ArrayList<>();
    final CompactMobFarmInventory dropInventory = new CompactMobFarmInventory();
    private final SimpleInventory upgrades = new SimpleInventory(4) {
        @Override
        public int getMaxCountPerStack() {
            return 1;
        }

        @Override
        public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
            return false;
        }

        @Override
        public void markDirty() {
            super.markDirty();
            CompactMobFarmBlockEntity.this.markDirty();

            killTimer = Math.min(killTimer, getAttackSpeed(getSword(), currentEntity));

            sendAttackSpeedUpgradePacket();
            sendAttackDamageUpgradePacket();
        }
    };
    private final SimpleInventory sampleTaker = new SimpleInventory(1) {
        @Override
        public int getMaxCountPerStack() {
            return 1;
        }

        @Override
        public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
            return false;
        }

        @Override
        public void markDirty() {
            super.markDirty();
            CompactMobFarmBlockEntity.this.markDirty();

            CompactMobFarmBlockEntity.this.currentEntity = null;

            CompactMobFarmBlockEntity.this.resetHealth();

            if (CompactMobFarmBlockEntity.this.world == null) return;
            CompactMobFarmBlockEntity.this.world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    };
    private final SimpleInventory sword = new SimpleInventory(1) {
        @Override
        public int getMaxCountPerStack() {
            return 1;
        }

        @Override
        public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
            return false;
        }

        @Override
        public void markDirty() {
            super.markDirty();
            CompactMobFarmBlockEntity.this.markDirty();

            sendAttackDamageUpgradePacket();
        }
    };

    public CompactMobFarmBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.COMPACT_MOB_FARM, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, CompactMobFarmBlockEntity blockEntity) {
        if (world.isClient()) return;

        // Set entity if it doesn't exist
        if (blockEntity.currentEntity == null) {
            if (!blockEntity.setCurrentEntity()) return;

            blockEntity.setMaxEntityHealth(blockEntity.currentEntity.getMaxHealth());
            blockEntity.setCurrentEntityHealth(blockEntity.maxEntityHealth);
        }
        if (blockEntity.currentEntity == null && !blockEntity.setCurrentEntity()) return;

        if (!blockEntity.isTurnedOn) return;


        blockEntity.killTimer--;
        if (blockEntity.killTimer > 0) return;

        blockEntity.checkHealthAndKillEntity();

        blockEntity.killTimer = blockEntity.getAttackSpeed(blockEntity.getSword(), blockEntity.currentEntity);
    }

    private void checkHealthAndKillEntity() {
        if (!(world instanceof ServerWorld serverWorld)) return;

        final FakePlayer player = FakePlayer.get(serverWorld);
        if (this.getSword() != null) {
            player.setStackInHand(Hand.MAIN_HAND, this.getSword());
            if(EnchantmentHelper.getFireAspect(player) > 0) currentEntity.setFireTicks(1);
            if (this.getSword().damage(1, world.random, null)) this.sword.clear();
        }

        float attackDamage = getAttackDamage(this.getSword(), currentEntity);


        setCurrentEntityHealth(currentEntityHealth - attackDamage);
        if (currentEntityHealth > 0) return;

        killEntity(player);

        setCurrentEntityHealth(maxEntityHealth);
    }

    private boolean setCurrentEntity() {
        final ItemStack sampleTaker = this.getSampleTaker();
        if (sampleTaker == null) return false;

        final EntityType<?> livingEntityType = FilledSampleTakerItem.getSampledEntityType(sampleTaker);
        sendEntityUpdatePacket(livingEntityType);
        if (livingEntityType == null) return false;

        final Entity entity = livingEntityType.create(this.getWorld());
        if (!(entity instanceof LivingEntity livingEntity)) return false;

        this.currentEntity = livingEntity;
        return true;
    }

    private void sendEntityUpdatePacket(EntityType<?> newEntity) {
        if (newEntity == null) {
            sendPacket(ModPackets.GUI_ENTITY_REMOVED, PacketByteBufs.create());
            return;
        }


        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeRegistryValue(Registries.ENTITY_TYPE, newEntity);

        sendPacket(ModPackets.GUI_ENTITY_CHANGED, buf);
    }

    private void sendAttackSpeedUpgradePacket() {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeFloat(getAttackSpeed(getSword(), currentEntity));

        sendPacket(ModPackets.GUI_UPDATE_ATTACK_SPEED, buf);
    }

    private void sendAttackDamageUpgradePacket() {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeFloat(getAttackDamage(getSword(), currentEntity));

        sendPacket(ModPackets.GUI_UPDATE_ATTACK_DAMAGE, buf);
    }

    private void sendPacket(Identifier packet, PacketByteBuf buf) {
        for (BiConsumer<Identifier, PacketByteBuf> sender : packetSenders) {
            sender.accept(packet, buf);
        }
    }

    public void registerPacketSender(BiConsumer<Identifier, PacketByteBuf> sender) {
        this.packetSenders.add(sender);
    }

    public void removePacketSender(BiConsumer<Identifier, PacketByteBuf> sender) {
        this.packetSenders.remove(sender);
    }

    private int getAttackSpeed(@Nullable ItemStack sword, LivingEntity target) {
        int result = DEFAULT_ATTACK_SPEED;

        for (ItemStack upgradeStack : upgrades.stacks) {
            if (!(upgradeStack.getItem() instanceof CompactMobFarmUpgradeItem upgrade)) continue;

            result = upgrade.modifyAttackSpeed(result, sword, target, this);
        }

        return result;
    }

    private float getAttackDamage(@Nullable ItemStack sword, LivingEntity target) {
        float result = 1;


        if (sword != null && target != null) {
            result += EnchantmentHelper.getAttackDamage(sword, target.getGroup());
            result += sword.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_DAMAGE).stream()
                    .mapToDouble(EntityAttributeModifier::getValue).sum();
        }

        for (ItemStack upgradeStack : upgrades.stacks) {
            if (!(upgradeStack.getItem() instanceof CompactMobFarmUpgradeItem upgrade)) continue;

            result = upgrade.modifyAttackDamage(result, sword, target, this);
        }

        return result;
    }

    @SuppressWarnings("UnstableApiUsage")
    private void killEntity(PlayerEntity player) {
        ((EntityAccessor) currentEntity).compact_mob_farms$setDropMethod(stack -> {
            try (Transaction transaction = Transaction.openOuter()) {
                dropInventory.insert(ItemVariant.of(stack), stack.getCount(), transaction);
                transaction.commit();
            }
        });
        ((LivingEntityAccessor) currentEntity).compact_mob_farms$setXpDropMethod(this::dropXp);

        currentEntity.age = 1;
        currentEntity.setAttacker(player);
        currentEntity.setAttacking(player);

        currentEntity.drop(currentEntity.getDamageSources().playerAttack(player));
    }


    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CompactMobFarmScreenHandler(syncId, playerInventory, sampleTaker, upgrades, sword, ScreenHandlerContext.create(this.world, this.pos));
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBoolean(isTurnedOn);
        buf.writeFloat(currentEntityHealth);
        buf.writeFloat(maxEntityHealth);
        buf.writeFloat(getAttackSpeed(getSword(), currentEntity));
        buf.writeFloat(getAttackDamage(getSword(), currentEntity));
        buf.writeBoolean(currentEntity != null);
        if (currentEntity != null) buf.writeRegistryValue(Registries.ENTITY_TYPE, currentEntity.getType());
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put(DROP_INVENTORY_NBT_KEY, dropInventory.toNbtList());
        nbt.put(SAMPLE_TAKER_NBT_KEY, sampleTaker.toNbtList());
        nbt.put(UPGRADES_NBT_KEY, upgrades.toNbtList());
        nbt.put(SWORD_NBT_KEY, sword.toNbtList());
        nbt.putBoolean(TURNED_ON_NBT_KEY, isTurnedOn);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        dropInventory.fromNbt(nbt.getList(DROP_INVENTORY_NBT_KEY, NbtElement.COMPOUND_TYPE));
        sampleTaker.readNbtList(nbt.getList(SAMPLE_TAKER_NBT_KEY, NbtElement.COMPOUND_TYPE));
        upgrades.readNbtList(nbt.getList(UPGRADES_NBT_KEY, NbtElement.COMPOUND_TYPE));
        sword.readNbtList(nbt.getList(SWORD_NBT_KEY, NbtElement.COMPOUND_TYPE));
        isTurnedOn = nbt.getBoolean(TURNED_ON_NBT_KEY);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @SuppressWarnings("UnstableApiUsage")
    private void dropXp(int amount) {
        final ItemStack sword = getSword();

        if (EnchantmentHelper.get(sword).containsKey(Enchantments.MENDING) && sword.isDamaged()) {
            int repairAmount = Math.min(amount * 2, sword.getDamage());
            sword.setDamage(sword.getDamage() - repairAmount);

            amount -= repairAmount / 2;
        }

        try (Transaction transaction = Transaction.openOuter()) {
            dropInventory.insert(ItemVariant.of(NUGGET_OF_EXPERIENCE), amount / 3, transaction);
            transaction.commit();
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    public ItemStack getSampleTaker() {
        return sampleTaker.getStack(0);
    }

    public ItemStack getSword() {
        return sword.getStack(0);
    }

    public CompactMobFarmInventory getDropInventory() {
        return dropInventory;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.isTurnedOn = turnedOn;
    }

    public void resetHealth() {
        this.maxEntityHealth = -1;
        this.currentEntityHealth = -1;

        sendPacket(ModPackets.GUI_HEALTH_RESET, PacketByteBufs.create());
    }

    public void setMaxEntityHealth(float maxEntityHealth) {
        this.maxEntityHealth = maxEntityHealth;


        final PacketByteBuf buf = PacketByteBufs.create();

        buf.writeFloat(maxEntityHealth);

        sendPacket(ModPackets.GUI_MAX_HEALTH_CHANGED, buf);
    }

    public void setCurrentEntityHealth(float currentEntityHealth) {
        this.currentEntityHealth = currentEntityHealth;


        final PacketByteBuf buf = PacketByteBufs.create();

        buf.writeFloat(currentEntityHealth);

        sendPacket(ModPackets.GUI_HEALTH_CHANGED, buf);
    }
}
