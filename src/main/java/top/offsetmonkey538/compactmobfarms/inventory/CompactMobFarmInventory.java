package top.offsetmonkey538.compactmobfarms.inventory;

import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface CompactMobFarmInventory extends Inventory {

    List<ItemStack> getItems();

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        return getItems().isEmpty();
    }

    @Override
    default ItemStack getStack(int slot) {
        if (slot >= size()) return ItemStack.EMPTY;
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int amount) {
        if (slot >= size()) return ItemStack.EMPTY;
        ItemStack removedStack;

        if (getStack(slot).getCount() - amount <= 0) {
            removedStack = getItems().remove(slot);
        } else {
            removedStack = Inventories.splitStack(getItems(), slot, amount);
        }

        if (!removedStack.isEmpty()) {
            this.markDirty();
        }
        return removedStack;
    }

    @Override
    default ItemStack removeStack(int slot) {
        this.markDirty();
        return getItems().remove(slot);
    }

    default void addStack(ItemStack stack) {
        if (stack.isEmpty()) return;

        for (int i = 0; i < size(); i++) {
            ItemStack storedStack = getStack(i);

            if (!storedStack.isOf(stack.getItem()) || (stack.getNbt() != null && !stack.getNbt().equals(storedStack.getNbt()))) continue;

            int newCount = stack.getCount() + storedStack.getCount();
            if (newCount > getMaxCountPerStack()) newCount = getMaxCountPerStack();

            storedStack.setCount(newCount);
            getItems().set(i, storedStack);
            return;
        }

        getItems().add(stack);
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        if (slot >= size()) return;
        if (stack.isEmpty()) {
            getItems().remove(slot);
            return;
        }
        getItems().set(slot, stack);
        this.markDirty();
    }

    @Override
    default boolean isValid(int slot, ItemStack stack) {
        // We don't want hoppers or anything to put items in here
        return false;
    }

    @Override
    void markDirty();

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    default void clear() {
        getItems().clear();
    }
}
