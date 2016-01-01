package org.waterpicker.paydirtwashplant.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;
import net.minecraftforge.fluids.*;
import org.waterpicker.paydirtwashplant.Config;
import org.waterpicker.paydirtwashplant.util.DirectionHelper;
import org.waterpicker.paydirtwashplant.util.Voltage;

import java.util.Random;

public class WashPlantTile extends TileEntity implements IFluidHandler, ISidedInventory {

    public static Random rand = new Random();

    private FluidTank tank;
    private Sink sink;
    private ItemStack[] slots = new ItemStack[2];
    private int[] leftslot = {0};
    private int[] rightslot = {1};

    private int ticks = 0;

    public WashPlantTile() {
        tank = new FluidTank(Config.WATER_BUFFER);
        sink = new Sink(this, Config.EU_BUFFER, Voltage.getVoltage(Config.VOLTAGE));
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);

        NBTTagList items = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < items.tagCount(); ++i) {
            NBTTagCompound item = items.getCompoundTagAt(i);

            int slot = item.getInteger("Slot");
            if (slot >= 0 && slot < getSizeInventory()) {
                slots[slot] = ItemStack.loadItemStackFromNBT(item);
            }
        }

        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tank.writeToNBT(tag);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < slots.length; ++i) {
            if (slots[i] != null){
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                slots[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        tag.setTag("Items", nbttaglist);

        super.writeToNBT(tag);
    }
    @Override
    public void invalidate() {
        sink.invalidate(); // notify the energy source
        super.invalidate(); // this is important for mc!
    }

    @Override
    public void onChunkUnload() {
        sink.onChunkUnload(); // notify the energy source
    }

    public void updateEntity() {
        sink.updateEntity();

        if(ticks > 20) {
            if(tank.getFluidAmount() > Config.WATER_PER_OPERATION && sink.canUseEnergy(Config.EU_PER_OPERATION) && slots[0] != null) {
                tank.drain(Config.EU_PER_OPERATION, true);
                sink.useEnergy(Config.EU_PER_OPERATION);

                Block block = Block.getBlockFromItem(slots[0].getItem());

                if (decrStackSize(0, 1) != null) {
                    if (success(block)) {
                        if (slots[1] == null) {
                            slots[1] = new ItemStack(Items.gold_nugget);
                        } else {
                            slots[1] = new ItemStack(slots[1].getItem(), slots[1].stackSize + 1);
                        }
                    }
                    markDirty();
                }

                ticks = 0;
                return;
            }

            return;
        }

        ticks++;
    }


    private boolean success(Block block) {
        double r = rand.nextDouble();

        double b = -1;

        if(Blocks.cobblestone.equals(block))
            b = Config.COBBLESTONE_PERCENTAGE;
        else if(Blocks.gravel.equals(block))
            b = Config.GRAVEL_PERCENTAGE;
        else if(Blocks.dirt.equals(block))
            b = Config.DIRT_PERCENTAGE;

        return r <= b;
    }

    public boolean acceptsEnergyFrom(ForgeDirection direction) {
        return (direction.equals(DirectionHelper.getRelativeSide(this, "back")) || direction.equals(DirectionHelper.getRelativeSide(this, "bottom")));
    }

    public void onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int sideHit, float hitX, float hitY, float hitZ) {
        int inventory = slots[0] != null ? slots[0].stackSize : 0;

        player.addChatMessage(new ChatComponentText("EU: " + sink.getEnergyStored() + " Water: " + tank.getFluidAmount() + " Inventory: " + inventory));
    }

    // FLuid

    @Override
    public int fill(ForgeDirection direction, FluidStack resource, boolean doFill) {
        if(canFill(direction, resource.getFluid()))
                return tank.fill(resource, doFill);
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
    public boolean canFill(ForgeDirection direction, Fluid fluid) {
        if(direction.equals(DirectionHelper.getRelativeSide(this, "top"))) {
            if (fluid.equals(FluidRegistry.WATER)) {
                return true;
            }
        }
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

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        if (direction.equals(DirectionHelper.getRelativeSide(this, "left"))) {
            return leftslot;

        } if (direction.equals(DirectionHelper.getRelativeSide(this, "right"))) {
            return rightslot;
        }

        return null;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        if(ForgeDirection.getOrientation(side).equals(DirectionHelper.getRelativeSide(this,"left"))) {
            if (isItemValidForSlot(slot, item))
                return true;
        }

        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return ForgeDirection.getOrientation(side).equals(DirectionHelper.getRelativeSide(this,"right"));
    }

    @Override
    public int getSizeInventory() {
        return slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return slots[i];
    }

    @Override
    public ItemStack decrStackSize(int slot, int i) {
        if (this.slots[slot] != null) {
            ItemStack itemstack;

            if (this.slots[slot].stackSize <= i) {
                itemstack = this.slots[slot];
                this.slots[slot] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.slots[slot].splitStack(i);

                if (this.slots[slot].stackSize == 0) {
                    this.slots[slot] = null;
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.slots[slot] != null) {
            ItemStack itemstack = this.slots[slot];
            this.slots[slot] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        this.slots[slot] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        if(slot == 0) {
            Block block = Block.getBlockFromItem(itemstack.getItem());

            if(Blocks.cobblestone.equals(block))
                return true;
            if(Blocks.gravel.equals(block))
                return true;
            if(Blocks.dirt.equals(block))
                return true;
        }
        return false;
    }

    public void dropInventory() {
        for(ItemStack itemstack : slots) {
            if (itemstack != null)
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, itemstack));
        }
    }
}