package org.waterpicker.paydirtwashplant.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
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

public class BasicSink extends TileEntity implements IEnergySink {
    // **********************************
    // *** Methods for use by the mod ***
    // **********************************

    /**
     * Constructor for a new BasicSink delegate.
     *
     * @param capacity1 Maximum amount of eu to store.
     * @param tier1 IC2 tier, 1 = LV, 2 = MV, ...
     */
    public BasicSink(int capacity1, int tier1) {
        this.capacity = capacity1;
        this.tier = tier1;
    }

    // in-world te forwards	>>

    /**
     * Forward for the base TileEntity's updateEntity(), used for creating the energy net link.
     * Either updateEntity or onLoaded have to be used.
     */
    @Override
    public void updateEntity() {
        if (!addedToEnet) onLoaded();
    }

    /**
     * Notification that the base TileEntity finished loaded, for advanced uses.
     * Either updateEntity or onLoaded have to be used.
     */
    public void onLoaded() {
        if (!addedToEnet &&
                !FMLCommonHandler.instance().getEffectiveSide().isClient() &&
                Info.isIc2Available()) {

            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

            addedToEnet = true;
        }
    }

    /**
     * Forward for the base TileEntity's invalidate(), used for destroying the energy net link.
     * Both invalidate and onChunkUnload have to be used.
     */
    @Override
    public void invalidate() {
        super.invalidate();

        onChunkUnload();
    }

    /**
     * Forward for the base TileEntity's onChunkUnload(), used for destroying the energy net link.
     * Both invalidate and onChunkUnload have to be used.
     */
    @Override
    public void onChunkUnload() {
        if (addedToEnet &&
                Info.isIc2Available()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

            addedToEnet = false;
        }
    }

    /**
     * Forward for the base TileEntity's readFromNBT(), used for loading the state.
     *
     * @param tag Compound tag as supplied by TileEntity.readFromNBT()
     */
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagCompound data = tag.getCompoundTag("IC2BasicSink");

        energyStored = data.getDouble("energy");
    }

    /**
     * Forward for the base TileEntity's writeToNBT(), used for saving the state.
     *
     * @param tag Compound tag as supplied by TileEntity.writeToNBT()
     */
    @Override
    public void writeToNBT(NBTTagCompound tag) {
        try {
            super.writeToNBT(tag);
        } catch (RuntimeException e) {
            // happens if this is a delegate, ignore
        }

        NBTTagCompound data = new NBTTagCompound();

        data.setDouble("energy", energyStored);

        tag.setTag("IC2BasicSink", data);
    }

    // << in-world te forwards
    // methods for using this adapter >>

    /**
     * Get the maximum amount of energy this sink can hold in its buffer.
     *
     * @return Capacity in EU.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Set the maximum amount of energy this sink can hold in its buffer.
     *
     * @param capacity1 Capacity in EU.
     */
    public void setCapacity(int capacity1) {
        this.capacity = capacity1;
    }

    /**
     * Get the IC2 energy tier for this sink.
     *
     * @return IC2 Tier.
     */
    public int getTier() {
        return tier;
    }

    /**
     * Set the IC2 energy tier for this sink.
     *
     * @param tier1 IC2 Tier.
     */
    public void setTier(int tier1) {
        this.tier = tier1;
    }

    /**
     * Determine the energy stored in the sink's input buffer.
     *
     * @return amount in EU, may be above capacity
     */
    public double getEnergyStored() {
        return energyStored;
    }

    /**
     * Set the stored energy to the specified amount.
     *
     * This is intended for server -> client synchronization, e.g. to display the stored energy in
     * a GUI through getEnergyStored().
     *
     * @param amount
     */
    public void setEnergyStored(double amount) {
        energyStored = amount;
    }

    /**
     * Determine if the specified amount of energy is available.
     *
     * @param amount in EU
     * @return true if the amount is available
     */
    public boolean canUseEnergy(double amount) {
        return energyStored >= amount;
    }

    /**
     * Use the specified amount of energy, if available.
     *
     * @param amount amount to use
     * @return true if the amount was available
     */
    public boolean useEnergy(double amount) {
        if (canUseEnergy(amount) && !FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            energyStored -= amount;

            return true;
        }
        return false;
    }

    /**
     * Discharge the supplied ItemStack into this sink's energy buffer.
     *
     * @param stack ItemStack to discharge (null is ignored)
     * @param limit Transfer limit, values <= 0 will use the battery's limit
     * @return true if energy was transferred
     */
    public boolean discharge(ItemStack stack, int limit) {
        if (stack == null || !Info.isIc2Available()) return false;

        double amount = capacity - energyStored;
        if (amount <= 0) return false;

        if (limit > 0 && limit < amount) amount = limit;

        amount = ElectricItem.manager.discharge(stack, amount, tier, limit > 0, true, false);

        energyStored += amount;

        return amount > 0;
    }

    // << methods for using this adapter

    // backwards compatibility (ignore these) >>

    @Deprecated
    public void onUpdateEntity() {
        updateEntity();
    }

    @Deprecated
    public void onInvalidate() {
        invalidate();
    }

    @Deprecated
    public void onOnChunkUnload() {
        onChunkUnload();
    }

    @Deprecated
    public void onReadFromNbt(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Deprecated
    public void onWriteToNbt(NBTTagCompound tag) {
        writeToNBT(tag);
    }

    // << backwards compatibility

    // ******************************
    // *** Methods for use by ic2 ***
    // ******************************

    // energy net interface >>

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        if(direction.equals(DirectionHelper.getRelativeSide(this, "back")) || direction.equals(DirectionHelper.getRelativeSide(this, "bottom")))
            return true;
            
        return false;
    }

    @Override
    public double getDemandedEnergy() {
        return Math.max(0, capacity - energyStored);
    }

    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        energyStored += amount;

        return 0;
    }

    @Override
    public int getSinkTier() {
        return tier;
    }

    // << energy net interface

    protected int capacity;
    protected int tier;
    protected double energyStored;
    protected boolean addedToEnet;
}
