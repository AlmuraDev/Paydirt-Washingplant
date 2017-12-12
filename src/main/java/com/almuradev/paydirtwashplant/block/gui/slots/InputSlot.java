package com.almuradev.paydirtwashplant.block.gui.slots;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class InputSlot extends Slot {
	public InputSlot(IInventory inventory, int slotIndex, int posX, int posY) {
		super(inventory, slotIndex, posX, posY);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		Block block = Block.getBlockFromItem(stack.getItem());
		if(block == null) {
			return false;
		}
		// Input Block Types.
		if(block.equals(Blocks.COBBLESTONE) || block.equals(Blocks.GRAVEL) || block.equals(Blocks.DIRT)) {
			return true;
		}

		return false;
	}
}