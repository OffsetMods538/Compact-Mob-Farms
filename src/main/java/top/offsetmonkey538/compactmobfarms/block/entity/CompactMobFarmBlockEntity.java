package top.offsetmonkey538.compactmobfarms.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
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
import top.offsetmonkey538.compactmobfarms.inventory.CompactMobFarmInventory;
import top.offsetmonkey538.compactmobfarms.item.SampleTakerItem;
import top.offsetmonkey538.compactmobfarms.network.ModPackets;
import top.offsetmonkey538.compactmobfarms.screen.CompactMobFarmScreenHandler;

public class CompactMobFarmBlockEntity extends BlockEntity implements CompactMobFarmInventory, ExtendedScreenHandlerFactory {
    private int killTimer = 0;
    private boolean isTurnedOn = true;
    private float maxEntityHealth = -1;
    private float currentEntityHealth = 0;
    private LivingEntity currentEntity = null;
    private final List<BiConsumer<Identifier, PacketByteBuf>> packetSenders = new ArrayList<>();
    final List<ItemStack> dropInventory = new ArrayList<>(1);
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
        }
    };

    public CompactMobFarmBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.COMPACT_MOB_FARM, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, CompactMobFarmBlockEntity blockEntity) {
        if (world.isClient()) return;

        // Set entity if it doesn't exist
        if (blockEntity.currentEntity == null && !blockEntity.setCurrentEntity()) return;

        if (!blockEntity.isTurnedOn) return;


        blockEntity.killTimer++;
        if (blockEntity.killTimer < 10) return;

        blockEntity.checkHealthAndKillEntity();

        blockEntity.killTimer = 0;
    }

    private void checkHealthAndKillEntity() {
        if (!(world instanceof ServerWorld serverWorld)) return;

        if (maxEntityHealth == -1) maxEntityHealth = currentEntity.getMaxHealth();
        if (currentEntityHealth == -1) currentEntityHealth = maxEntityHealth;

        final FakePlayer player = FakePlayer.get(serverWorld);
        if (this.getSword() != null) {
            player.setStackInHand(Hand.MAIN_HAND, this.getSword());
            if(EnchantmentHelper.getFireAspect(player) > 0) currentEntity.setFireTicks(1);
            if (this.getSword().damage(1, world.random, null)) this.sword.clear();
        }

        float attackDamage = getAttackDamage(this.getSword(), currentEntity);


        currentEntityHealth -= attackDamage;
        if (currentEntityHealth > 0) return;

        killEntity(player);

        currentEntityHealth = maxEntityHealth;
    }

    private boolean setCurrentEntity() {
        final ItemStack sampleTaker = this.getSampleTaker();
        if (sampleTaker == null) return false;

        final EntityType<?> livingEntityType = SampleTakerItem.getSampledEntityType(sampleTaker);
        sendUpdatePacket(livingEntityType);
        if (livingEntityType == null) return false;

        final Entity entity = livingEntityType.create(this.getWorld());
        if (!(entity instanceof LivingEntity livingEntity)) return false;

        this.currentEntity = livingEntity;
        return true;
    }

    private void sendUpdatePacket(EntityType<?> newEntity) {
        if (newEntity == null) {
            sendPacket(ModPackets.GUI_ENTITY_REMOVED, PacketByteBufs.create());
            return;
        }


        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeRegistryValue(Registries.ENTITY_TYPE, newEntity);

        sendPacket(ModPackets.GUI_ENTITY_CHANGED, buf);
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

    private float getAttackDamage(@Nullable ItemStack sword, LivingEntity target) {
        float result = 1;


        if (sword != null && target != null) {
            result += EnchantmentHelper.getAttackDamage(sword, target.getGroup());
            result += sword.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_DAMAGE).stream()
                    .mapToDouble(EntityAttributeModifier::getValue).sum();
        }

        return result;
    }

    private void killEntity(PlayerEntity player) {
        ((EntityAccessor) currentEntity).compact_mob_farms$setDropMethod(this::addStack);

        currentEntity.drop(currentEntity.getDamageSources().playerAttack(player));
    }


    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CompactMobFarmScreenHandler(syncId, playerInventory, sampleTaker, sword, ScreenHandlerContext.create(this.world, this.pos));
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBoolean(isTurnedOn);
        buf.writeBoolean(currentEntity != null);
        if (currentEntity != null) buf.writeRegistryValue(Registries.ENTITY_TYPE, currentEntity.getType());
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        NbtList dropInventoryNbt = new NbtList();
        this.dropInventory.forEach(item -> dropInventoryNbt.add(item.writeNbt(new NbtCompound())));
        nbt.put("DropInventory", dropInventoryNbt);
        nbt.put("SampleTaker", sampleTaker.toNbtList());
        nbt.put("Sword", sword.toNbtList());
        nbt.putBoolean("TurnedOn", isTurnedOn);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        nbt.getList("DropInventory", NbtElement.COMPOUND_TYPE)
                .forEach(itemNbt -> this.dropInventory.add(ItemStack.fromNbt((NbtCompound) itemNbt)));
        sampleTaker.readNbtList(nbt.getList("SampleTaker", NbtElement.COMPOUND_TYPE));
        sword.readNbtList(nbt.getList("Sword", NbtElement.COMPOUND_TYPE));
        isTurnedOn = nbt.getBoolean("TurnedOn");
    }

    @Override
    public List<ItemStack> getItems() {
        return dropInventory;
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

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.world == null) return;

        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        currentEntity = null;
        maxEntityHealth = -1;
        currentEntityHealth = -1;
    }

    public ItemStack getSampleTaker() {
        return sampleTaker.getStack(0);
    }

    public ItemStack getSword() {
        return sword.getStack(0);
    }

    public void setTurnedOn(boolean turnedOn) {
        this.isTurnedOn = turnedOn;
    }
}
