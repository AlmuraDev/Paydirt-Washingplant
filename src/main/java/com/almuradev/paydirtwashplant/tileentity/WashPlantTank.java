package com.almuradev.paydirtwashplant.tileentity;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class WashPlantTank extends FluidTank {
    public WashPlantTank(int capacity) {
        super(capacity);
    }

    public boolean canFillFluidType(FluidStack fluid)
    {
        return canFill();
    }

    public boolean canDrainFluidType(@Nullable FluidStack fluid) {
        return fluid != null && fluid.getFluid().equals(FluidRegistry.WATER) && canDrain();
    }
}
