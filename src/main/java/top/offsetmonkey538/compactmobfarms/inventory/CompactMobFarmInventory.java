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
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int amount) {
        if (getStack(slot).getCount() - amount <= 0) return getItems().remove(slot);

        ItemStack itemStack = Inventories.splitStack(getItems(), slot, amount);
        if (!itemStack.isEmpty()) {
            this.markDirty();
        }
        return itemStack;
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
