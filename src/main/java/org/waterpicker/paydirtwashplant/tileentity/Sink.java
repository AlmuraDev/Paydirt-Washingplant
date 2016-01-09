package org.waterpicker.paydirtwashplant.tileentity;

import ic2.api.energy.prefab.BasicSink;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class Sink extends BasicSink {
    private TileEntityWashplant tile;

    public Sink(TileEntityWashplant parent, int capacity1, int tier) {
        super(parent, capacity1, tier);
        tile = parent;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        return tile.acceptsEnergyFrom(direction);
    }

}