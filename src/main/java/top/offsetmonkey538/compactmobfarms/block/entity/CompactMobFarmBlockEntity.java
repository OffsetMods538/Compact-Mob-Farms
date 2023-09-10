package top.offsetmonkey538.compactmobfarms.block.entity;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.inventory.CompactMobFarmInventory;
import top.offsetmonkey538.compactmobfarms.screen.CompactMobFarmScreenHandler;

public class CompactMobFarmBlockEntity extends BlockEntity implements CompactMobFarmInventory, NamedScreenHandlerFactory {
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

    public CompactMobFarmBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.COMPACT_MOB_FARM, pos, state);
    }

    // int timer = 0;
    public static void tick(World world, BlockPos pos, BlockState state, CompactMobFarmBlockEntity blockEntity) {
        // // fixme: this is temporary and just for testing

        // if (world.isClient()) return;


        // blockEntity.timer++;
        // if (blockEntity.timer < 20) return;

        // final ItemStack sampleTaker = blockEntity.getSampleTaker();
        // if (sampleTaker == null) return;

        // final EntityType<?> livingEntityType = SampleTakerItem.getSampledEntityType(sampleTaker);
        // if (livingEntityType == null) return;

        // final Entity entity = livingEntityType.create(blockEntity.getWorld());
        // if (!(entity instanceof LivingEntity livingEntity)) return;

        // if (!(world instanceof ServerWorld serverWorld)) return;


        // FakePlayer player = FakePlayer.get(serverWorld);

        // DamageSource damageSource = livingEntity.getDamageSources().playerAttack(player);
        // Identifier identifier = livingEntity.getLootTable();
        // LootTable lootTable = world.getServer().getLootManager().getLootTable(identifier);
        // LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(serverWorld).add(LootContextParameters.THIS_ENTITY, livingEntity).add(LootContextParameters.ORIGIN, livingEntity.getPos()).add(LootContextParameters.DAMAGE_SOURCE, damageSource).addOptional(LootContextParameters.KILLER_ENTITY, damageSource.getAttacker()).addOptional(LootContextParameters.DIRECT_KILLER_ENTITY, damageSource.getSource());
        // builder = builder.add(LootContextParameters.LAST_DAMAGE_PLAYER, player).luck(player.getLuck());
        // LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
        // lootTable.generateLoot(lootContextParameterSet, livingEntity.getLootTableSeed(), blockEntity::addStack);
        // blockEntity.timer = 0;
    }


    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CompactMobFarmScreenHandler(syncId, playerInventory, sampleTaker, ScreenHandlerContext.create(this.world, this.pos));
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

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        nbt.getList("DropInventory", NbtElement.COMPOUND_TYPE)
                .forEach(itemNbt -> this.dropInventory.add(ItemStack.fromNbt((NbtCompound) itemNbt)));
        sampleTaker.readNbtList(nbt.getList("SampleTaker", NbtElement.COMPOUND_TYPE));
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
    }

    public ItemStack getSampleTaker() {
        return sampleTaker.getStack(0);
    }
}
