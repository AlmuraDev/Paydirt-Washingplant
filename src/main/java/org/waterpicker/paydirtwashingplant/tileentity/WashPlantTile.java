package org.waterpicker.paydirtwashingplant.tileentity;

import ic2.api.energy.prefab.BasicSink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.waterpicker.paydirtwashingplant.Config;

public class WashPlantTile extends TileEntity {
    // new basic energy sink, 1000 EU buffer, tier 1 (32 EU/t, LV)
    private BasicSink sink = new BasicSink(this, Config.BLOCK_EU_BUFFER, 1);

    @Override
    public void invalidate() {
        sink.invalidate();
        super.invalidate(); // this is important for mc!
    }

    @Override
    public void onChunkUnload() {
        sink.onChunkUnload(); // notify the energy sink
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        sink.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        sink.writeToNBT(tag);
    }
}