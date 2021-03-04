package dev.frydae.emcutils.features.vaultButtons;

import dev.frydae.emcutils.features.VaultButtons;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class VaultScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final int rows;

    public static VaultScreenHandler createGeneric9x7(int syncId, PlayerInventory playerInventory) {
        return new VaultScreenHandler(VaultButtons.GENERIC_9X7, syncId, playerInventory, 7);
    }

    private VaultScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, int rows) {
        this(type, syncId, playerInventory, new SimpleInventory(9 * rows), rows);
    }

    public VaultScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows) {
        super(type, syncId);
        checkSize(inventory, rows * 9);
        this.inventory = inventory;
        this.rows = rows;
        inventory.onOpen(playerInventory.player);
        int i = (6 - 3) * 18;

        for(int row = 0; row < 6; ++row) {
            for(int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(inventory, column + row * 9, 8 + column * 18, 18 + row * 18));
            }
        }

        for(int row = 0; row < 3; ++row) {
            for(int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 103 + row * 18 + i));
            }
        }

        for(int row = 0; row < 9; ++row) {
            this.addSlot(new Slot(playerInventory, row, 8 + row * 18, 161 + i));
        }
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();

            if (index < this.rows * 9) {
                if (!this.insertItem(itemStack2, this.rows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, this.rows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }

    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }
}
