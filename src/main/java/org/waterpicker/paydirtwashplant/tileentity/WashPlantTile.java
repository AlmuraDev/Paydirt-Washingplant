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

public class WashPlantTile extends BasicSink implements IFluidHandler, ISidedInventory {
    // new basic energy sink, 1000 EU buffer, tier 1 (32 EU/t, LV)
    private FluidTank tank = new FluidTank(Config.WATER_BUFFER);
    private ItemStack[] slots = new ItemStack[2];
    private int[] leftslot = {0};
    private int[] rightslot = {1};

    private int ticks = 1;

    public WashPlantTile() {
        super(Config.EU_BUFFER, 1);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);

        NBTTagList items = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < items.tagCount(); ++i) {
            NBTTagCompound item = (NBTTagCompound) items.getCompoundTagAt(i);

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

    public void updateEntity() {
        super.updateEntity();
        if(ticks > 20) {
            if(tank.getFluidAmount() > Config.WATER_PER_OPERATION && canUseEnergy(Config.EU_PER_OPERATION)) {
                tank.drain(Config.EU_PER_OPERATION, true);
                useEnergy(Config.EU_PER_OPERATION);

                if(decrStackSize(0, 1) != null) {
                    if (slots[1] == null) {
                        slots[1] = new ItemStack(Items.gold_nugget);
                    } else {
                        slots[1] = new ItemStack(slots[1].getItem(), slots[1].stackSize + 1);
                    }

                    markDirty();
                }

                ticks = 1;
                return;
            }
            return;
        }
        ticks++;
    }

    public void onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int sideHit, float hitX, float hitY, float hitZ) {
        player.addChatMessage(new ChatComponentText("EU: " + getEnergyStored() + "Water: " + tank.getFluidAmount()));
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
        if(ForgeDirection.getOrientation(side).equals(DirectionHelper.getRelativeSide(this,"right")))
            return true;

        return false;
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
        if (this.slots[slot] != null)
        {
            ItemStack itemstack;

            if (this.slots[slot].stackSize <= i)
            {
                itemstack = this.slots[slot];
                this.slots[slot] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.slots[slot].splitStack(i);

                if (this.slots[slot].stackSize == 0)
                {
                    this.slots[slot] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        if (this.slots[p_70304_1_] != null)
        {
            ItemStack itemstack = this.slots[p_70304_1_];
            this.slots[p_70304_1_] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.slots[p_70299_1_] = p_70299_2_;

        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit())
        {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
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
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
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
        for(int i = 0; i < slots.length; i++) {
            if (slots[i] != null)
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, slots[i]));
        }
    }
}