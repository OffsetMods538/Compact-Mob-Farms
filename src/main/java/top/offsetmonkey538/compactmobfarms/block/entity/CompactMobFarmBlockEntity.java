package top.offsetmonkey538.compactmobfarms.block.entity;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.accessor.EntityAccessor;
import top.offsetmonkey538.compactmobfarms.inventory.CompactMobFarmInventory;
import top.offsetmonkey538.compactmobfarms.item.SampleTakerItem;
import top.offsetmonkey538.compactmobfarms.screen.CompactMobFarmScreenHandler;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public class CompactMobFarmBlockEntity extends BlockEntity implements CompactMobFarmInventory, NamedScreenHandlerFactory {
    private int killTimer = 0;
    private float maxEntityHealth = -1;
    private float currentEntityHealth = 0;
    private LivingEntity currentEntity = null;
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


        blockEntity.killTimer++;
        if (blockEntity.killTimer < 20) return;

        blockEntity.checkHealthAndKillEntity();

        blockEntity.killTimer = 0;
    }

    private void checkHealthAndKillEntity() {
        if (!(world instanceof ServerWorld serverWorld)) return;

        // Set entity if it doesn't exist
        if (this.currentEntity == null && !setCurrentEntity()) return;

        if (maxEntityHealth == -1) maxEntityHealth = currentEntity.getMaxHealth();

        final FakePlayer player = FakePlayer.get(serverWorld);
        if (this.getSword() != null) player.setStackInHand(Hand.MAIN_HAND, this.getSword());

        float attackDamage = getAttackDamage(this.getSword(), player, currentEntity);


        currentEntityHealth -= attackDamage;
        if (currentEntityHealth > 0) return;

        killEntity(player);

        currentEntityHealth = maxEntityHealth;
    }

    private boolean setCurrentEntity() {
        final ItemStack sampleTaker = this.getSampleTaker();
        if (sampleTaker == null) return false;

        final EntityType<?> livingEntityType = SampleTakerItem.getSampledEntityType(sampleTaker);
        if (livingEntityType == null) return false;

        final Entity entity = livingEntityType.create(this.getWorld());
        if (!(entity instanceof LivingEntity livingEntity)) return false;

        this.currentEntity = livingEntity;
        return true;
    }

    private float getAttackDamage(ItemStack sword, PlayerEntity player, LivingEntity target) {
        float result = (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);

        if (sword == null) return result;

        return result + EnchantmentHelper.getAttackDamage(sword, target.getGroup());
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
    protected void writeNbt(NbtCompound nbt) {
        NbtList dropInventoryNbt = new NbtList();
        this.dropInventory.forEach(item -> dropInventoryNbt.add(item.writeNbt(new NbtCompound())));
        nbt.put("DropInventory", dropInventoryNbt);
        nbt.put("SampleTaker", sampleTaker.toNbtList());
        nbt.put("Sword", sword.toNbtList());

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        nbt.getList("DropInventory", NbtElement.COMPOUND_TYPE)
                .forEach(itemNbt -> this.dropInventory.add(ItemStack.fromNbt((NbtCompound) itemNbt)));
        sampleTaker.readNbtList(nbt.getList("SampleTaker", NbtElement.COMPOUND_TYPE));
        sword.readNbtList(nbt.getList("Sword", NbtElement.COMPOUND_TYPE));
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
    }

    public ItemStack getSampleTaker() {
        return sampleTaker.getStack(0);
    }

    public ItemStack getSword() {
        return sword.getStack(0);
    }
}
