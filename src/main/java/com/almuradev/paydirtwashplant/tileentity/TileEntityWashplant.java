package com.almuradev.paydirtwashplant.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import com.almuradev.paydirtwashplant.Config;
import com.almuradev.paydirtwashplant.PDWPMod;
import com.almuradev.paydirtwashplant.block.BlockWashPlant;
import com.almuradev.paydirtwashplant.util.DirectionHelper;
import com.almuradev.paydirtwashplant.util.Voltage;

import java.util.Random;

public class TileEntityWashplant extends TileEntity implements IFluidHandler, ISidedInventory {

	public static Random rand = new Random();
	private boolean washing;
	private FluidTank tank;
	private Sink sink;
	private ItemStack[] slots = new ItemStack[4];
	private int[] leftslot = {0};
	private int[] rightslot = {1};
	private int washTime = 0;

	public TileEntityWashplant() {
		tank = new FluidTank(Config.WATER_BUFFER);
		sink = new Sink(this, Config.EU_BUFFER, Voltage.getVoltage(Config.VOLTAGE));
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		sink.readFromNBT(tag);
		tank.readFromNBT(tag);
		washTime = tag.getInteger("wash Time");
		washing = tag.getBoolean("Washing");

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
		sink.writeToNBT(tag);
		tank.writeToNBT(tag);

		tag.setInteger("Wash Time", washTime);
		tag.setBoolean("Washing", washing);

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
		sink.invalidate();
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		sink.onChunkUnload();
	}

	public void updateEntity() {
		sink.updateEntity();

		processFluid();

		boolean updateInventory = false;

		if (!this.worldObj.isRemote) {
			if (canWash()) {
				toggleWashing(true);

				++this.washTime;

				if(washTime % 20 == 0)
					worldObj.playSoundEffect(xCoord,yCoord,zCoord, PDWPMod.MODID + ":washplant",1,1);

				if (this.washTime == Config.WASH_TIME) {
					if (slots[1] == null || (slots[1].stackSize < slots[1].getMaxStackSize())) {
						this.washItem();
						updateInventory = true;
					} 
					this.washTime = 0;
				}
			} else {
				this.washTime = 0;
				toggleWashing(false);
			}
		}

		if (updateInventory) {
			this.markDirty();
		}
	}

	private void toggleWashing(boolean b) {
		if(!washing == b) {
			BlockWashPlant.updateBlockState(b, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			washing = b;
		}
	}

	public boolean isWashing() {
		return this.washTime > 0;
	}

	private boolean canWash() {
		return tank.getFluidAmount() > Config.WATER_PER_OPERATION && sink.canUseEnergy(Config.EU_PER_OPERATION) && slots[0] != null;
	}

	public void washItem() {
		tank.drain(Config.WATER_PER_OPERATION, true);
		sink.useEnergy(Config.EU_PER_OPERATION);

		if(success(Block.getBlockFromItem(slots[0].getItem()))) {

			ItemStack itemstack = new ItemStack((Item)Item.itemRegistry.getObject(Config.MINED_ITEM));

			if (this.slots[1] == null) {
				this.slots[1] = itemstack.copy();
			} else if (this.slots[1].getItem() == itemstack.getItem()) {
				this.slots[1].stackSize += itemstack.stackSize;
			}
		}

		--this.slots[0].stackSize;

		if (this.slots[0].stackSize <= 0) {
			this.slots[0] = null;
		}
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
		return r < b;
	}

	public boolean acceptsEnergyFrom(ForgeDirection direction) {
		return (direction.equals(DirectionHelper.getRelativeSide(worldObj.getBlockMetadata(xCoord,yCoord,zCoord)/2, "back"))
				|| direction.equals(DirectionHelper.getRelativeSide(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)/2, "bottom")));
	}

	// Fluid

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
		if(direction.equals(DirectionHelper.getRelativeSide(worldObj.getBlockMetadata(xCoord,yCoord,zCoord)/2, "top"))) {
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
		if (direction.equals(DirectionHelper.getRelativeSide(worldObj.getBlockMetadata(xCoord,yCoord,zCoord)/2, "left"))) {
			return leftslot;

		} if (direction.equals(DirectionHelper.getRelativeSide(worldObj.getBlockMetadata(xCoord,yCoord,zCoord)/2, "right"))) {
			return rightslot;
		}

		return new int[0];
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, int side) {
		if(ForgeDirection.getOrientation(side).equals(DirectionHelper.getRelativeSide(worldObj.getBlockMetadata(xCoord,yCoord,zCoord)/2,"left"))) {
			if (isItemValidForSlot(slot, item))
				return true;
		}

		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		return ForgeDirection.getOrientation(side).equals(DirectionHelper.getRelativeSide(worldObj.getBlockMetadata(xCoord,yCoord,zCoord)/2,"right"));
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
			if (block == null) {
				return false;
			}
			if(block.equals(Blocks.cobblestone) || block.equals(Blocks.gravel) || block.equals(Blocks.dirt)) {
				return true;
			}
		}
		return false;
	}

	public void dropInventory() {
		for(ItemStack itemstack : slots) {
			if (itemstack != null)
				worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, itemstack));
		}
	}

	public int getFluidLevel() {
		return tank.getFluidAmount();
	}

	public int getPowerLevel() {
		return (int) sink.getEnergyStored();
	}

	public int getWashTime() {
		return washTime;
	}

	public void setFluidLevel(int fluidLevel) {
		tank.setFluid(new FluidStack(FluidRegistry.WATER, fluidLevel));
	}

	public void setPowerLevel(int powerLevel) {
		sink.setEnergyStored(powerLevel);
	}

	public void setWashTime(int washtime) {
		washTime = washtime;
	}

	@Override
	public Packet getDescriptionPacket () {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public void onDataPacket (NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
		worldObj.func_147479_m(xCoord, yCoord, zCoord);
	}

	private void processFluid() {
		if(getStackInSlot(2) == null)return;
		ItemStack input = ItemStack.copyItemStack(getStackInSlot(2));
		input.stackSize = 1;
		ItemStack result = ItemStack.copyItemStack(getStackInSlot(3));

		boolean success = false;
		if(FluidContainerRegistry.isFilledContainer(input)){
			if(tank.fill(new FluidStack(FluidContainerRegistry.getFluidForFilledItem(input), FluidContainerRegistry.BUCKET_VOLUME), false) == FluidContainerRegistry.BUCKET_VOLUME) {
				if(!addStackToOutput(input.getItem().hasContainerItem(input) ? input.getItem().getContainerItem(input) : null, false))return;
				tank.fill(new FluidStack(FluidContainerRegistry.getFluidForFilledItem(input), FluidContainerRegistry.BUCKET_VOLUME), true);
				result = input.getItem().hasContainerItem(input) ? input.getItem().getContainerItem(input) : null;
				success = true;

			}
		} if(success) {
			getStackInSlot(2).stackSize--;
			if(getStackInSlot(2).stackSize == 0)slots[2] = null;
			addStackToOutput(result, true);
		}
	}

	private boolean addStackToOutput(ItemStack stack, boolean doPut){
		ItemStack output = getStackInSlot(3);
		if(stack == null){
			if(doPut)markDirty();
			return true;
		}
		if(output == null){
			if(doPut){
				setInventorySlotContents(3, stack);
			}
			return true;
		}
		else if(stack.isItemEqual(output) && (output.stackSize + stack.stackSize) <= output.getMaxStackSize()){
			if(doPut){
				incrStackSize(3, stack.stackSize > 0 ? stack.stackSize : 1, true);
			}
			return true;
		}
		else{
			return false;
		}
	}

	public boolean incrStackSize(int i, int j, boolean markDirty) {
		ItemStack itemstack = getStackInSlot(i);

		if(itemstack != null) {
			if (itemstack.stackSize >= itemstack.getMaxStackSize()) {
				itemstack.stackSize = itemstack.getMaxStackSize();
				slots[i] = itemstack;
				if(markDirty)markDirty();
			} else {
				itemstack.stackSize += j;
				slots[i] = itemstack;
				if(markDirty)markDirty();
				return true;
			}
		}
		return false;
	}

	public int washTimeScaled(int scale) {
		return (getWashTime() * scale) / Config.WASH_TIME;
	}

	public int fluidScaled(int scale) {
		return (getFluidLevel() * scale) / tank.getCapacity();
	}

	public int powerScaled(int scale) {
		return (getPowerLevel() * scale) / sink.getCapacity();
	}
}