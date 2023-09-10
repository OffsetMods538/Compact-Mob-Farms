package top.offsetmonkey538.compactmobfarms.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import top.offsetmonkey538.compactmobfarms.block.ModBlocks;
import top.offsetmonkey538.compactmobfarms.item.SampleTakerItem;

public class CompactMobFarmScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private final Inventory inventory;

    public CompactMobFarmScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1), ScreenHandlerContext.EMPTY);
    }

    public CompactMobFarmScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.COMPACT_MOB_FARM_SCREEN_HANDLER, syncId);

        this.context = context;
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);


        this.addSlot(new Slot(inventory, 0, 10, 10) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return context.get((world, pos) -> SampleTakerItem.getSamplesCollected(stack).size() >= 10, false);
            }
        });


        // The player inventory
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
        // The player Hotbar
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotId) {
        final Slot slot = this.getSlot(slotId);

        final ItemStack originalStack = slot.getStack();
        ItemStack newStack = originalStack.copy();

        if (slotId < this.inventory.size() && !this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) newStack = ItemStack.EMPTY;
        if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) newStack = ItemStack.EMPTY;

        if (originalStack.isEmpty()) slot.setStack(ItemStack.EMPTY);

        slot.markDirty();

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player) && canUse(context, player, ModBlocks.COMPACT_MOB_FARM);
    }
}
