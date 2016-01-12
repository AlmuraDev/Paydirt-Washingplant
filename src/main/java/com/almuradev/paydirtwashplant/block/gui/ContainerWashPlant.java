package com.almuradev.paydirtwashplant.block.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

import com.almuradev.paydirtwashplant.block.gui.slots.FluidSlot;
import com.almuradev.paydirtwashplant.block.gui.slots.InputSlot;
import com.almuradev.paydirtwashplant.block.gui.slots.OutputSlot;
import com.almuradev.paydirtwashplant.tileentity.TileEntityWashplant;

public class ContainerWashPlant extends Container {
	private TileEntityWashplant washplant;

	private int washtime;
	private int power;
	private int fluid;

	public ContainerWashPlant(InventoryPlayer inventory, TileEntityWashplant washplant) {
		this.washplant = washplant;

		addSlotToContainer(new InputSlot(washplant, 0, 126, 17));
		addSlotToContainer(new OutputSlot(washplant, 1, 126, 62));
		addSlotToContainer(new FluidSlot(washplant, 2, 60,  17));
		addSlotToContainer(new OutputSlot(washplant, 3, 60,62));


		for (int column = 0; column < 3; ++column) {
			for (int row = 0; row < 9; ++row) {
				this.addSlotToContainer(new Slot(inventory, row + column * 9 + 9, 8 + row * 18, 84 + column * 18));
			}
		}

		for (int column = 0; column < 9; ++column) {
			this.addSlotToContainer(new Slot(inventory, column, 8 + column * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		ItemStack stack = null;
		Slot slot = (Slot) inventorySlots.get(slotID);

		if (slot != null && slot.getHasStack()) {
			ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();

			if (slotID < washplant.getSizeInventory()) {
				if (!mergeItemStack(stackInSlot, washplant.getSizeInventory(), 36 + washplant.getSizeInventory(), true)) {
					return null;
				}
			}
			//places it into the tileEntity is possible since its in the player inventory
			else if (!this.mergeItemStack(stackInSlot, 0, washplant.getSizeInventory(), false)) {
				return null;
			}

			if (stackInSlot.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}

	protected boolean mergeItemStack(ItemStack stack, int start, int end, boolean backwards) {
		boolean flag = false;
		int k = start;

		if (backwards) {
			k = end - 1;
		}

		Slot slot;
		ItemStack stack1;

		if (stack.isStackable()) {
			while (stack.stackSize > 0 && (!backwards && k < end || backwards && k >= start)) {
				slot = (Slot)this.inventorySlots.get(k);
				stack1 = slot.getStack();

				if (stack1 != null && stack1.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == stack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, stack1)) {
					int l = stack1.stackSize + stack.stackSize;

					if (l <= stack.getMaxStackSize()) {
						stack.stackSize = 0;
						stack1.stackSize = l;
						slot.onSlotChanged();
						flag = true;
					} else if (stack1.stackSize < stack.getMaxStackSize()) {
						stack.stackSize -= stack.getMaxStackSize() - stack1.stackSize;
						stack1.stackSize = stack.getMaxStackSize();
						slot.onSlotChanged();
						flag = true;
					}
				}

				if (backwards) {
					--k;
				} else {
					++k;
				}
			}
		}

		if (stack.stackSize > 0) {
			if (backwards) {
				k = end - 1;
			} else {
				k = start;
			}

			while (!backwards && k < end || backwards && k >= start) {
				slot = (Slot)this.inventorySlots.get(k);
				stack1 = slot.getStack();

				if (stack1 == null && slot.isItemValid(stack)) {
					slot.putStack(stack.copy());
					slot.onSlotChanged();
					stack.stackSize = 0;
					flag = true;
					break;
				}

				if (backwards) {
					--k;
				} else {
					++k;
				}
			}
		}

		return flag;
	}

	@Override
	public void addCraftingToCrafters (ICrafting par1ICrafting)
	{
		super.addCraftingToCrafters(par1ICrafting);
		par1ICrafting.sendProgressBarUpdate(this, 0, washplant.getWashTime());
		par1ICrafting.sendProgressBarUpdate(this, 1, washplant.getPowerLevel());
		par1ICrafting.sendProgressBarUpdate(this, 2, washplant.getFluidLevel());
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges () {
		super.detectAndSendChanges();

		for (Object craft : crafters) {
			ICrafting icrafting = (ICrafting) craft;

			if (this.washtime != washplant.getWashTime()) {
				icrafting.sendProgressBarUpdate(this, 0, washplant.getWashTime());
			}

			if (this.power != washplant.getPowerLevel()) {
				icrafting.sendProgressBarUpdate(this, 1, washplant.getPowerLevel());
			}

			if (this.fluid != washplant.getFluidLevel()) {
				icrafting.sendProgressBarUpdate(this, 2, washplant.getFluidLevel());
			}
		}

		this.washtime = washplant.getWashTime();
		this.power = washplant.getPowerLevel();
		this.fluid = washplant.getFluidLevel();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar (int id, int value)
	{
		if (id == 0) {
			washplant.setWashTime(value);
		}

		if (id == 1) {
			washplant.setPowerLevel(value);
		}

		if (id == 2) {
			washplant.setFluidLevel(value);
		}
	}
}

