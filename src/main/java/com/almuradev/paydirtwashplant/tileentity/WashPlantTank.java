/*
 * This file is part of Paydirt-Washingplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant.tileentity;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public final class WashPlantTank extends FluidTank {

    WashPlantTank(final int capacity) {
        super(capacity);
    }

    @Override
    public boolean canFillFluidType(final FluidStack fluidStack) {
        return fluidStack.getFluid() == FluidRegistry.WATER && canFill();
    }

    @Override
    public boolean canDrainFluidType(@Nullable final FluidStack fluid) {
        return fluid != null && fluid.getFluid() == FluidRegistry.WATER && canDrain();
    }
}
