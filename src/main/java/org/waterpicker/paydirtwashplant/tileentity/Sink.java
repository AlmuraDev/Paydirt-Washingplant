package org.waterpicker.paydirtwashplant.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import org.waterpicker.paydirtwashplant.util.DirectionHelper;

public class Sink extends BasicSink {
    private WashPlantTile tile;

    public Sink(WashPlantTile parent, int capacity1, int tier) {
        super(parent, capacity1, tier);
        tile = parent;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        return tile.acceptsEnergyFrom(direction);
    }

}