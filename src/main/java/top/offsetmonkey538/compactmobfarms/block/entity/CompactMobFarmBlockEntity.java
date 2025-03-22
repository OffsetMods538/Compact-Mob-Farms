package top.offsetmonkey538.compactmobfarms.block.entity;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.accessor.EntityAccessor;
import top.offsetmonkey538.compactmobfarms.accessor.LivingEntityAccessor;
import top.offsetmonkey538.compactmobfarms.component.ModComponents;
import top.offsetmonkey538.compactmobfarms.inventory.CompactMobFarmInventory;
import top.offsetmonkey538.compactmobfarms.item.FilledSampleTakerItem;
import top.offsetmonkey538.compactmobfarms.item.upgrade.CompactMobFarmUpgradeItem;
import top.offsetmonkey538.compactmobfarms.network.ModPackets;
import top.offsetmonkey538.compactmobfarms.screen.CompactMobFarmScreenHandler;
import top.offsetmonkey538.monkeylib538.utils.IdentifierUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class CompactMobFarmBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<CompactMobFarmScreenHandler.OpeningData> {
    public static final GameProfile FAKE_PLAYER_PROFILE = new GameProfile(FakePlayer.DEFAULT_UUID, "[CompactMobFarms - Compact Mob Farm]");

    public static final String DROP_INVENTORY_NBT_KEY = "DropInventory";
    public static final String SAMPLE_TAKER_NBT_KEY = "SampleTaker";
    public static final String TIER_UPGRADE_NBT_KEY = "TierUpgrade";
    public static final String UPGRADES_NBT_KEY = "Upgrades";
    public static final String SWORD_NBT_KEY = "Sword";
    public static final String TURNED_ON_NBT_KEY = "IsTurnedOn";

    public static final int DEFAULT_ATTACK_SPEED = 30 * 20; // 30 seconds, multiplied by 20 because it needs to be in ticks.
    public static final Item NUGGET_OF_EXPERIENCE = Registries.ITEM.get(IdentifierUtils.INSTANCE.of("create:experience_nugget"));

    private int killTimer = 0;
    private boolean isTurnedOn = true;
    private float maxEntityHealth = -1;
    private float currentEntityHealth = -1;
    private LivingEntity currentEntity = null;
    private final List<Consumer< CustomPayload>> packetSenders = new ArrayList<>();
    private final CompactMobFarmInventory dropInventory = new CompactMobFarmInventory();
    private final SimpleInventory tierUpgrade = new SimpleInventory(1) {
        @Override
        public int getMaxCountPerStack() {
            return 1;
        }

        @Override
        public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
            return false;
        }
    };
    private final UpgradesInventory upgrades = new UpgradesInventory(4) {
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
            CompactMobFarmBlockEntity.this.sendAttackDamageUpgradePacket();

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

    public static void tick(World world, CompactMobFarmBlockEntity blockEntity) {
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

        final FakePlayer player = FakePlayer.get(serverWorld, FAKE_PLAYER_PROFILE);
        if (this.getSword() != null) {
            player.setStackInHand(Hand.MAIN_HAND, this.getSword());
            if(EnchantmentHelper.getLevel(serverWorld.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.FIRE_ASPECT).orElseThrow(), getSword()) > 0) currentEntity.setFireTicks(1);
            this.getSword().damage(1, serverWorld, player, item -> this.sword.clear());
        }

        float attackDamage = getAttackDamage(this.getSword(), currentEntity);


        setCurrentEntityHealth(currentEntityHealth - attackDamage);
        if (currentEntityHealth > 0) return;

        killEntity(player);

        setCurrentEntityHealth(maxEntityHealth);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean setCurrentEntity() {
        final ItemStack sampleTaker = this.getSampleTaker();
        if (sampleTaker == null) return false;

        final EntityType<?> livingEntityType = FilledSampleTakerItem.getSampledEntityType(sampleTaker);
        sendEntityUpdatePacket(livingEntityType);
        if (livingEntityType == null) return false;

        final Entity entity = livingEntityType.create(this.getWorld());
        if (!(entity instanceof LivingEntity livingEntity)) return false;

        this.currentEntity = livingEntity;

        CompactMobFarmBlockEntity.this.sendAttackDamageUpgradePacket();

        return true;
    }

    private void sendEntityUpdatePacket(EntityType<?> newEntity) {
        sendPacket(new ModPackets.GuiEntityChanged(newEntity));
    }

    private void sendAttackSpeedUpgradePacket() {
        sendPacket(new ModPackets.GuiAttackSpeedChanged(getAttackSpeed(getSword(), currentEntity)));
    }

    private void sendAttackDamageUpgradePacket() {
        sendPacket(new ModPackets.GuiAttackDamageChanged(getAttackDamage(getSword(), currentEntity)));
    }

    public void sendPacket(CustomPayload payload) {
        for (Consumer<CustomPayload> sender : packetSenders) {
            sender.accept(payload);
        }
    }

    public void registerPacketSender(Consumer<CustomPayload> sender) {
        this.packetSenders.add(sender);
    }

    public void removePacketSender(Consumer<CustomPayload> sender) {
        this.packetSenders.remove(sender);
    }

    private int getAttackSpeed(@Nullable ItemStack sword, LivingEntity target) {
        int result = DEFAULT_ATTACK_SPEED;

        for (ItemStack upgradeStack : upgrades.heldStacks) {
            if (!(upgradeStack.getItem() instanceof CompactMobFarmUpgradeItem upgrade)) continue;

            result = upgrade.modifyAttackSpeed(result, sword, target, this);
        }

        return result;
    }

    private float getAttackDamage(@Nullable ItemStack sword, LivingEntity target) {
        AtomicReference<Float> result = new AtomicReference<>((float) 1);


        if (sword != null && target != null && world instanceof ServerWorld serverWorld) {
            final FakePlayer player = FakePlayer.get(serverWorld, FAKE_PLAYER_PROFILE);
            player.setStackInHand(Hand.MAIN_HAND, sword);

            result.updateAndGet(v -> v + EnchantmentHelper.getDamage(serverWorld, sword, target, serverWorld.getDamageSources().playerAttack(player), 0));
            sword.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT).applyModifiers(AttributeModifierSlot.MAINHAND, (attribute, modifier) -> {
                if (attribute != EntityAttributes.GENERIC_ATTACK_DAMAGE) return;
                result.updateAndGet(v -> (float) (v + modifier.value()));
            });
        }

        for (ItemStack upgradeStack : upgrades.heldStacks) {
            if (!(upgradeStack.getItem() instanceof CompactMobFarmUpgradeItem upgrade)) continue;

            result.set(upgrade.modifyAttackDamage(result.get(), sword, target, this));
        }

        return result.get();
    }

    private void killEntity(PlayerEntity player) {
        if (!(world instanceof ServerWorld serverWorld)) return;

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

        currentEntity.drop(serverWorld, currentEntity.getDamageSources().playerAttack(player));
    }


    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CompactMobFarmScreenHandler(syncId, playerInventory, sampleTaker, tierUpgrade, upgrades, sword, ScreenHandlerContext.create(this.world, this.pos));
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public CompactMobFarmScreenHandler.OpeningData getScreenOpeningData(ServerPlayerEntity player) {
        return new CompactMobFarmScreenHandler.OpeningData(
                isTurnedOn,
                currentEntityHealth,
                maxEntityHealth,
                getAttackSpeed(getSword(), currentEntity),
                getAttackDamage(getSword(), currentEntity),
                Optional.ofNullable(currentEntity == null ? null : currentEntity.getType())
        );
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        dropInventory.fromNbt(nbt.getList(DROP_INVENTORY_NBT_KEY, NbtElement.COMPOUND_TYPE), registryLookup);
        sampleTaker.readNbtList(nbt.getList(SAMPLE_TAKER_NBT_KEY, NbtElement.COMPOUND_TYPE), registryLookup);
        tierUpgrade.readNbtList(nbt.getList(TIER_UPGRADE_NBT_KEY, NbtElement.COMPOUND_TYPE), registryLookup);
        upgrades.readNbtList(nbt.getList(UPGRADES_NBT_KEY, NbtElement.COMPOUND_TYPE), registryLookup);
        sword.readNbtList(nbt.getList(SWORD_NBT_KEY, NbtElement.COMPOUND_TYPE), registryLookup);
        isTurnedOn = nbt.getBoolean(TURNED_ON_NBT_KEY);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.put(DROP_INVENTORY_NBT_KEY, dropInventory.toNbtList(registryLookup));
        nbt.put(SAMPLE_TAKER_NBT_KEY, sampleTaker.toNbtList(registryLookup));
        nbt.put(TIER_UPGRADE_NBT_KEY, tierUpgrade.toNbtList(registryLookup));
        nbt.put(UPGRADES_NBT_KEY, upgrades.toNbtList(registryLookup));
        nbt.put(SWORD_NBT_KEY, sword.toNbtList(registryLookup));
        nbt.putBoolean(TURNED_ON_NBT_KEY, isTurnedOn);

        super.writeNbt(nbt, registryLookup);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);

        dropInventory.copyFrom(components.getOrDefault(ModComponents.DROP_INVENTORY, dropInventory));
        sampleTaker.setStack(0, components.getOrDefault(ModComponents.SAMPLE_TAKER, ItemStack.EMPTY));
        tierUpgrade.setStack(0, components.getOrDefault(ModComponents.TIER_UPGRADE, ItemStack.EMPTY));
        upgrades.fromStacks(components.getOrDefault(ModComponents.UPGRADES, List.of()));
        sword.setStack(0, components.getOrDefault(ModComponents.SWORD, ItemStack.EMPTY));
        setTurnedOn(components.getOrDefault(ModComponents.TURNED_ON, false));
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);

        componentMapBuilder.add(ModComponents.DROP_INVENTORY, dropInventory);
        componentMapBuilder.add(ModComponents.SAMPLE_TAKER, sampleTaker.getStack(0));
        componentMapBuilder.add(ModComponents.TIER_UPGRADE, tierUpgrade.getStack(0));
        componentMapBuilder.add(ModComponents.UPGRADES, upgrades.heldStacks);
        componentMapBuilder.add(ModComponents.SWORD, sword.getStack(0));
        componentMapBuilder.add(ModComponents.TURNED_ON, isTurnedOn);
    }

    @SuppressWarnings("deprecation") // Minecraft also overrides this in places
    @Override
    public void removeFromCopiedStackNbt(NbtCompound nbt) {
        super.removeFromCopiedStackNbt(nbt);

        nbt.remove(DROP_INVENTORY_NBT_KEY);
        nbt.remove(SAMPLE_TAKER_NBT_KEY);
        nbt.remove(TIER_UPGRADE_NBT_KEY);
        nbt.remove(UPGRADES_NBT_KEY);
        nbt.remove(SWORD_NBT_KEY);
        nbt.remove(TURNED_ON_NBT_KEY);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    private void dropXp(int amount) {
        final ItemStack sword = getSword();

        if (world instanceof ServerWorld serverWorld && EnchantmentHelper.getLevel(serverWorld.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.MENDING).orElseThrow(), sword) > 0 && sword.isDamaged()) {
            int repairAmount = Math.min(amount * 2, sword.getDamage());
            sword.setDamage(sword.getDamage() - repairAmount);

            amount -= repairAmount / 2;
        }

        try (Transaction transaction = Transaction.openOuter()) {
            dropInventory.insert(ItemVariant.of(NUGGET_OF_EXPERIENCE), amount / 3, transaction);
            transaction.commit();
        }
    }

    public ItemStack getSampleTaker() {
        return sampleTaker.getStack(0);
    }

    public ItemStack getTierUpgrade() {
        return tierUpgrade.getStack(0);
    }

    public ItemStack getSword() {
        return sword.getStack(0);
    }

    public CompactMobFarmInventory getDropInventory() {
        return dropInventory;
    }

    public LivingEntity getCurrentEntity() {
        return currentEntity;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.isTurnedOn = turnedOn;
    }

    public void resetHealth() {
        this.maxEntityHealth = -1;
        this.currentEntityHealth = -1;

        sendPacket(new ModPackets.GuiHealthChanged(null));
    }

    public void setMaxEntityHealth(float maxEntityHealth) {
        this.maxEntityHealth = maxEntityHealth;

        sendPacket(new ModPackets.GuiMaxHealthChanged(maxEntityHealth));
    }

    public void setCurrentEntityHealth(float currentEntityHealth) {
        this.currentEntityHealth = currentEntityHealth;

        sendPacket(new ModPackets.GuiHealthChanged(currentEntityHealth));
    }

    private static class UpgradesInventory extends SimpleInventory {
        public UpgradesInventory(int size) {
            super(size);
        }

        public void fromStacks(List<ItemStack> stacks) {
            clear();
            stacks.forEach(this::addStack);
            markDirty();
        }
    }
}
