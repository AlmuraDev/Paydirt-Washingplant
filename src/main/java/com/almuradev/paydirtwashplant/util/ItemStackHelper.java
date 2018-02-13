/*
 * This file is part of Paydirt-Washingplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.spongepowered.api.util.Tuple;

import javax.annotation.Nullable;

/**
 * Utility on item stacks.
 */
public final class ItemStackHelper {

    private ItemStackHelper() {
    }

    /**
     * Utility method for checking if two item stacks can merge.
     *
     * @param first The first item stack
     * @param second The second item stack
     * @return True if they can merge
     */
    public static boolean canMerge(final ItemStack first, final ItemStack second) {
        return first.isEmpty() || second.isEmpty() || (first.isItemEqual(second) && first.getCount() + second.getCount() <= first.getMaxStackSize()
            && ItemStack.areItemStackTagsEqual(second, first));
    }

    /**
     * Merges two item stacks. Two original item stacks are unmodified.
     *
     * @param first The first item stack
     * @param second The second item stack
     * @return A new item stack
     */
    public static ItemStack merge(final ItemStack first, final ItemStack second) {
        if (first.isEmpty()) {
            return second.copy();
        }
        final ItemStack result = first.copy();
        result.grow(second.getCount());
        return result;
    }

    /**
     * Utility method for checking if two item stacks can flood together.
     *
     * @param first The first item stack
     * @param second The second item stack
     * @return True if they can merge
     */
    public static boolean canFlood(final ItemStack first, final ItemStack second) {
        return first.isEmpty() || second.isEmpty() || (first.isItemEqual(second) && ItemStack.areItemStackTagsEqual(second, first));
    }

    /**
     * Combines two item stacks. Two original item stacks are unmodified.
     *
     * @param first The first item stack
     * @param second The second item stack
     * @return A tuple containing the merged and the extra stack if the merged reached max size
     */
    public static Tuple<ItemStack, ItemStack> flood(final ItemStack first, final ItemStack second) {
        if (first.isEmpty()) {
            return new Tuple<>(second.copy(), ItemStack.EMPTY);
        }

        final ItemStack resultFirst = first.copy();
        final ItemStack resultSecond;
        final int t = first.getCount() + second.getCount() - first.getMaxStackSize();
        if (t > 0) {
            resultFirst.setCount(first.getMaxStackSize());
            resultSecond = first.copy();
            resultSecond.setCount(t);
        } else {
            resultFirst.grow(second.getCount());
            resultSecond = ItemStack.EMPTY;
        }

        return new Tuple<>(resultFirst, resultSecond);
    }

    /**
     * Gets the actual left over container after a drain.
     *
     * @param container The original container
     * @param toDrain The amount to drain
     * @return The actual leftover
     */
    public static ItemStack getActualLeftOver(final ItemStack container, FluidStack toDrain) {
        final ItemStack test = container.copy();
        @Nullable final IFluidHandlerItem handler = test.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        if (handler == null) {
            return ItemStack.EMPTY;
        }
        handler.drain(toDrain, true);
        return test;
    }

}
