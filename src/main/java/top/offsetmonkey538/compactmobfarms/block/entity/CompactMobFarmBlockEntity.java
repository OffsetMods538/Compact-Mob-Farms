package top.offsetmonkey538.compactmobfarms.block.entity;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.compactmobfarms.inventory.CompactMobFarmInventory;
import top.offsetmonkey538.compactmobfarms.item.SampleTakerItem;

public class CompactMobFarmBlockEntity extends BlockEntity implements CompactMobFarmInventory {
    final List<ItemStack> dropInventory = new ArrayList<>(1);
    private final SimpleInventory sampleTaker = new SimpleInventory(1) {
        @Override
        public int getMaxCountPerStack() {
            return 1;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return SampleTakerItem.getSamplesCollected(stack).size() >= 10 && super.canInsert(stack);
        }

        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return canInsert(stack);
        }

        @Override
        public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
            return false;
        }
    };
    int timer = 0;

    public CompactMobFarmBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.COMPACT_MOB_FARM, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, CompactMobFarmBlockEntity blockEntity) {
        // TODO
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
}
