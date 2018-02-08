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

    private int washtime;
    private int power;
    private int fluid;

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

//    protected boolean mergeItemStack(ItemStack stack, int start, int end, boolean backwards) {
//        boolean flag = false;
//        int k = start;
//
//        if (backwards) {
//            k = end - 1;
//        }
//
//        Slot slot;
//        ItemStack stack1;
//
//        if (stack.isStackable()) {
//            while (stack.getCount() > 0 && (!backwards && k < end || backwards && k >= start)) {
//                slot = this.inventorySlots.get(k);
//                stack1 = slot.getStack();
//
//                if (!stack1.isEmpty() && stack1.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() ==
//                    stack1.getItemDamage())
//                    && ItemStack.areItemStackTagsEqual(stack, stack1)) {
//                    int l = stack1.getCount() + stack.getCount();
//
//                    if (l <= stack.getMaxStackSize()) {
//                        stack.setCount(0);
//                        stack1.setCount(l);
//                        slot.onSlotChanged();
//                        flag = true;
//                    } else if (stack1.getCount() < stack.getMaxStackSize()) {
//                        int value = stack.getCount();
//                        value -= stack.getMaxStackSize() - stack1.getCount();
//                        stack.setCount(value);
//                        stack1.setCount(stack.getMaxStackSize());
//                        slot.onSlotChanged();
//                        flag = true;
//                    }
//                }
//
//                if (backwards) {
//                    --k;
//                } else {
//                    ++k;
//                }
//            }
//        }
//
//        if (stack.getCount() > 0) {
//            if (backwards) {
//                k = end - 1;
//            } else {
//                k = start;
//            }
//
//            while (!backwards && k < end || backwards && k >= start) {
//                slot = this.inventorySlots.get(k);
//                stack1 = slot.getStack();
//
//                if (stack1.isEmpty() && slot.isItemValid(stack)) {
//                    slot.putStack(stack.copy());
//                    slot.onSlotChanged();
//                    stack.setCount(0);
//                    flag = true;
//                    break;
//                }
//
//                if (backwards) {
//                    --k;
//                } else {
//                    ++k;
//                }
//            }
//        }
//
//        return flag;
//    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);

        listener.sendWindowProperty(this, 0, this.washplant.getWashTime());
        listener.sendWindowProperty(this, 1, this.washplant.getPowerLevel());
        listener.sendWindowProperty(this, 2, this.washplant.getFluidLevel());
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {
            if (this.washtime != this.washplant.getWashTime()) {
                listener.sendWindowProperty(this, 0, this.washplant.getWashTime());
            }

            if (this.power != this.washplant.getPowerLevel()) {
                listener.sendWindowProperty(this, 1, this.washplant.getPowerLevel());
            }

            if (this.fluid != this.washplant.getFluidLevel()) {
                listener.sendWindowProperty(this, 2, this.washplant.getFluidLevel());
            }
        }

        this.washtime = this.washplant.getWashTime();
        this.power = this.washplant.getPowerLevel();
        this.fluid = this.washplant.getFluidLevel();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
        switch (id) {
            case 0:
                this.washplant.setWashTime(value);
                break;
            case 1:
                this.washplant.setPowerLevel(value);
                break;
            case 2:
                this.washplant.setFluidLevel(value);
                break;
            default:
                //NOOP
        }
    }
}

