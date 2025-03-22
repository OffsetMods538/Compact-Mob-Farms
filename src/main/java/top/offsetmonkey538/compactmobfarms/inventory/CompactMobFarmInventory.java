package top.offsetmonkey538.compactmobfarms.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

public class CompactMobFarmInventory implements SlottedStorage<ItemVariant> {
    public static final Codec<CompactMobFarmInventory> CODEC = Codec.list(ItemStack.CODEC).xmap(CompactMobFarmInventory::new, inventory -> inventory.slots.stream().map(slot -> slot.stack).toList());
    public static final PacketCodec<RegistryByteBuf, CompactMobFarmInventory> PACKET_CODEC = ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(CompactMobFarmInventory::new, inventory -> inventory.slots.stream().map(slot -> slot.stack).toList());

    private final List<Slot> slots = new ArrayList<>();

    public CompactMobFarmInventory() {}
    public CompactMobFarmInventory(List<ItemStack> stacks) {
        stacks.forEach(stack -> slots.add(new Slot(stack)));
    }

    @Override
    public int getSlotCount() {
        return slots.size();
    }

    @Override
    public SingleSlotStorage<ItemVariant> getSlot(int slot) {
        return slots.get(slot);
    }

    @Override
    public boolean supportsInsertion() {
        return false;
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        if (resource.isBlank()) return 0;

        for (Slot slot : slots) {
            final ItemStack storedStack = slot.getStack();
            if (!ItemStack.areItemsAndComponentsEqual(storedStack, resource.toStack())) continue;

            int oldCount = storedStack.getCount();
            int newCount = (int) Math.min(64, oldCount + maxAmount);

            storedStack.setCount(newCount);
            slot.setStack(storedStack);

            return newCount - oldCount;
        }


        // None of the already stored stacks match the new stack.

        int count = (int) Math.min(64, maxAmount);
        final ItemStack addedStack = resource.toStack(count);

        slots.add(new Slot(addedStack));

        return count;
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        if (resource.isBlank()) return 0;

        for (Slot slot : slots) {
            final ItemStack storedStack = slot.getStack();
            if (!ItemStack.areItemsAndComponentsEqual(storedStack, resource.toStack())) continue;

            int oldCount = storedStack.getCount();
            int newCount = (int) Math.min(0, oldCount - maxAmount);

            storedStack.setCount(newCount);
            slot.setStack(storedStack);
            if (newCount == 0) slots.remove(slot);

            return oldCount - newCount;
        }

        return 0;
    }

    @Override
    @NotNull
    public Iterator<StorageView<ItemVariant>> iterator() {
        return new Iterator<>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return CompactMobFarmInventory.this.slots.size() > i;
            }

            @Override
            public StorageView<ItemVariant> next() {
                return CompactMobFarmInventory.this.slots.get(i++);
            }
        };
    }

    public NbtList toNbtList(RegistryWrapper.WrapperLookup registryLookup) {
        final NbtList nbt = new NbtList();

        slots.forEach((Slot slot) -> nbt.add(slot.toNbt(registryLookup)));

        return nbt;
    }

    public void fromNbt(NbtList nbtList, RegistryWrapper.WrapperLookup registryLookup) {
        nbtList.forEach(nbt -> slots.add(new Slot((NbtCompound) nbt, registryLookup)));
    }

    public void copyFrom(CompactMobFarmInventory original) {
        slots.clear();
        slots.addAll(original.slots);
    }

    private class Slot extends SingleStackStorage {
        private ItemStack stack;

        public Slot(ItemStack stack) {
            this.stack = stack;
        }
        public Slot(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
            this(ItemStack.fromNbt(registryLookup, nbt).orElse(ItemStack.EMPTY));
        }

        @Override
        protected ItemStack getStack() {
            return stack;
        }

        @Override
        protected void setStack(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        protected void onFinalCommit() {
            if (stack.isEmpty()) CompactMobFarmInventory.this.slots.remove(this);
        }

        public NbtCompound toNbt(RegistryWrapper.WrapperLookup registryLookup) {
            return (NbtCompound) stack.encode(registryLookup);
        }
    }
}
