package org.waterpicker.paydirtwashplant.tileentity;

import ic2.api.energy.prefab.BasicSink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.waterpicker.paydirtwashplant.Config;
import org.waterpicker.paydirtwashplant.block.TileFluidHandlerWashPlant;
import org.waterpicker.paydirtwashplant.util.Voltage;

public class WashPlantTile extends TileEntity implements IFluidHandler {
    // new basic energy sink, 1000 EU buffer, tier 1 (32 EU/t, LV)
    private BasicSink sink = new BasicSink(this, Config.EU_BUFFER, Voltage.getVoltage(Config.VOLTAGE));
    private FluidTank tank = new FluidTank(Config.WATER_BUFFER);

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

    @Override
    public int fill(ForgeDirection direction, FluidStack resource, boolean doFill) {
        if(direction.equals(ForgeDirection.UP)) {
            if(resource.getFluid().equals(FluidRegistry.WATER)) {
                return tank.fill(resource, doFill);
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection direction, FluidStack fluidStack, boolean b) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection forgeDirection, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection forgeDirection, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection forgeDirection, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection forgeDirection) {
        return new FluidTankInfo[]{tank.getInfo()};
    }

    public void onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int sideHit, float hitX, float hitY, float hitZ) {
        player.addChatMessage(new ChatComponentText("EU: " + sink.getEnergyStored() + "Water: " + tank.getFluidAmount()));
    }
}