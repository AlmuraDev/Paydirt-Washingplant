package org.waterpicker.paydirtwashplant.block;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.TileFluidHandler;

/**
 * Created by Waterpician on 12/27/2015.
 */
public class TileFluidHandlerWashPlant extends TileFluidHandler {
    public TileFluidHandlerWashPlant(int capacity) {
        super();
        tank.setCapacity(capacity);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        if(from.equals(ForgeDirection.UP))
            return true;
        return false;
    }
}
