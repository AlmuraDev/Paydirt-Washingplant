package com.almuradev.paydirtwashplant.block.gui.slots;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.ItemFluidContainer;

public class FluidSlot extends Slot {
	public FluidSlot(IInventory iinventory, int slotIndex, int posX, int posY) {
		super(iinventory, slotIndex, posX, posY);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return itemstack.getItem() instanceof ItemFluidContainer || itemstack.getItem().equals(Items.WATER_BUCKET);
	}
}
