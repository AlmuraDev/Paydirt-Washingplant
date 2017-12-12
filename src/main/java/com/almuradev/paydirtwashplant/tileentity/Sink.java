package com.almuradev.paydirtwashplant.tileentity;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyEmitter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class Sink extends BasicSink {
    private TileEntityWashplant tile;
 
    public Sink(TileEntityWashplant parent, int capacity1, int tier) {
        super(parent, capacity1, tier);
        tile = parent;
    }
 
    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing direction) {
        return tile.acceptsEnergyFrom(direction);
    }
 
    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        double energy = super.injectEnergy(directionFrom, amount, voltage);
        tile.markDirty();
        return energy;
    }
}