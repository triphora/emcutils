package coffee.waffle.emcutils.feature;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class VaultScreenHandler extends AbstractContainerMenu {
	private final Container inventory = new SimpleContainer(9 * 6);
	private final int rows = 6;

	public VaultScreenHandler(int syncId, Inventory playerInventory) {
		super(VaultScreen.GENERIC_9X7, syncId);
		checkContainerSize(inventory, rows * 9);
		inventory.startOpen(playerInventory.player);
		int i = (6 - 3) * 18;

		for (int row = 0; row < 6; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(inventory, column + row * 9, 8 + column * 18, 18 + row * 18));
			}
		}

		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 103 + row * 18 + i));
			}
		}

		for (int row = 0; row < 9; ++row) {
			this.addSlot(new Slot(playerInventory, row, 8 + row * 18, 161 + i));
		}
	}

	public boolean stillValid(Player player) {
		return this.inventory.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();

			if (index < this.rows * 9) {
				if (!this.moveItemStackTo(itemStack2, this.rows * 9, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemStack2, 0, this.rows * 9, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return itemStack;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.inventory.stopOpen(player);
	}
}
