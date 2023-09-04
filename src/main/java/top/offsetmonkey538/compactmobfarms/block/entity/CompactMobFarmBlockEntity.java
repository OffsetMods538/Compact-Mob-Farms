package top.offsetmonkey538.compactmobfarms.block.entity;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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

public class CompactMobFarmBlockEntity extends BlockEntity implements CompactMobFarmInventory {
    List<ItemStack> dropInventory = new ArrayList<>(1);

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

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        nbt.getList("DropInventory", NbtElement.COMPOUND_TYPE)
                .forEach(itemNbt -> this.dropInventory.add(ItemStack.fromNbt((NbtCompound) itemNbt)));
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
