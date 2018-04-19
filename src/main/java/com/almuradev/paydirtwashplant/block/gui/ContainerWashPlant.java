/*
 * This file is part of Paydirt-Washplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant.block.gui;

import com.almuradev.paydirtwashplant.block.gui.slots.FluidSlot;
import com.almuradev.paydirtwashplant.block.gui.slots.InputSlot;
import com.almuradev.paydirtwashplant.block.gui.slots.OutputSlot;
import com.almuradev.paydirtwashplant.tileentity.TileEntityWashplant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ContainerWashPlant extends Container {

    private final TileEntityWashplant washplant;

    public ContainerWashPlant(InventoryPlayer inventory, TileEntityWashplant washplant) {
        this.washplant = washplant;

        addSlotToContainer(new InputSlot(washplant, 0, 126, 17));
        addSlotToContainer(new OutputSlot(washplant, 1, 126, 62));
        addSlotToContainer(new FluidSlot(washplant, 2, 60, 17));
        addSlotToContainer(new OutputSlot(washplant, 3, 60, 62));


        for (int column = 0; column < 3; column++) {
            for (int row = 0; row < 9; ++row) {
                this.addSlotToContainer(new Slot(inventory, row + column * 9 + 9, 8 + row * 18, 84 + column * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            this.addSlotToContainer(new Slot(inventory, column, 8 + column * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        ItemStack stack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            if (slotID < this.washplant.getSizeInventory()) {
                if (!mergeItemStack(stackInSlot, this.washplant.getSizeInventory(), 36 + this.washplant.getSizeInventory(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            //places it into the tileEntity is possible since its in the player inventory
            else if (!this.mergeItemStack(stackInSlot, 0, this.washplant.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stackInSlot.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }
        return stack;
    }
}

