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

import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.Constants;

import net.minecraftforge.fluids.*;

import com.almuradev.paydirtwashplant.Config;
import com.almuradev.paydirtwashplant.PDWPMod;
import com.almuradev.paydirtwashplant.block.BlockWashPlant;
import com.almuradev.paydirtwashplant.util.DirectionHelper;
import com.almuradev.paydirtwashplant.util.Voltage;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Random;

public class TileEntityWashplant extends TileEntity implements IFluidHandler, ISidedInventory, ITickable {

	public static Random rand = new Random();
	private boolean washing;
	private FluidTank tank;
	private Sink sink;
	private ItemStack[] slots = new ItemStack[4];
	private int[] leftslot = {0};
	private int[] rightslot = {1};
	private int washTime = 0;

	public TileEntityWashplant() {
		tank = new WashPlantTank(Config.WATER_BUFFER);
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
				slots[slot] = new ItemStack(item);
			}
		}

		super.readFromNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
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

		return super.writeToNBT(tag);
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

	@Override
	public void markDirty() {
	    if (world != null && !world.isRemote) {
	        world.markChunkDirty(pos, this);
	    }
	    super.markDirty();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	private void toggleWashing(boolean b) {
		if (!washing == b) {
			BlockWashPlant.updateBlockState(b, this.world, this.pos);
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

		if (success(Block.getBlockFromItem(slots[0].getItem()))) {

			final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Config.MINED_ITEM));

			if (item == null) {
				return;
			}

			ItemStack itemstack = new ItemStack(item);

			if (this.slots[1] == null) {
				this.slots[1] = itemstack.copy();
			} else if (this.slots[1].getItem() == itemstack.getItem()) {
				this.slots[1].setCount(slots[1].getCount() + itemstack.getCount());
			}
		}

		this.slots[0].shrink(1);
	}

	private boolean success(Block block) {
		double r = rand.nextDouble();

		double b = -1;

		if (Blocks.COBBLESTONE.equals(block)) {
			b = Config.COBBLESTONE_PERCENTAGE;
		} else if(Blocks.GRAVEL.equals(block)) {
			b = Config.GRAVEL_PERCENTAGE;
		} else if(Blocks.DIRT.equals(block)) {
			b = Config.DIRT_PERCENTAGE;
		}

		return r < b;
	}

	public boolean acceptsEnergyFrom(EnumFacing direction) {
		return (direction.equals(DirectionHelper.getRelativeSide(world.getBlockState(pos).getValue(BlockWashPlant.FACING), "back"))
				|| direction.equals(DirectionHelper.getRelativeSide(world.getBlockState(pos).getValue(BlockWashPlant.FACING), "bottom")));
	}

	// Fluid

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return tank.fill(resource,doFill);
	}

	@Override
	public FluidStack drain(FluidStack fluidStack, boolean b) {
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return this.tank.getTankProperties();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing direction) {
		if (direction.equals(DirectionHelper.getRelativeSide(world.getBlockState(pos).getValue(BlockWashPlant.FACING), "left"))) {
			return leftslot;

		} if (direction.equals(DirectionHelper.getRelativeSide(world.getBlockState(pos).getValue(BlockWashPlant.FACING), "right"))) {
			return rightslot;
		}

		return new int[0];
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, EnumFacing side) {
		return side.equals(DirectionHelper.getRelativeSide(world.getBlockState(pos).getValue(BlockWashPlant.FACING), "left")) && isItemValidForSlot(
				slot, item);

	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, EnumFacing side) {
		return side.equals(DirectionHelper.getRelativeSide(world.getBlockState(pos).getValue(BlockWashPlant.FACING),"right"));
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i > slots.length) {
			return ItemStack.EMPTY;
		}
		ItemStack stack = slots[i];
		if (stack == null) {
			stack = ItemStack.EMPTY;
		}
		return stack;
	}

	@Override
	public ItemStack decrStackSize(int slot, int i) {
		if (this.slots[slot] != null) {
			ItemStack itemstack;

			if (this.slots[slot].getCount() <= i) {
				itemstack = this.slots[slot];
				this.slots[slot] = null;
				this.markDirty();
				return itemstack;
			} else {
				itemstack = this.slots[slot].splitStack(i);

				if (this.slots[slot].isEmpty()) {
					this.slots[slot] = null;
				}

				this.markDirty();
				return itemstack;
			}
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Waterpicker, need to do this
		// TODO DO NOT RETURN NULL, look how null stacks were handled elsewhere in here
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		this.slots[slot] = itemstack;

		if (!itemstack.isEmpty() && itemstack.getCount() > this.getInventoryStackLimit()) {
			itemstack.setCount(this.getInventoryStackLimit());
		}

		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if(slot == 0) {
			Block block = Block.getBlockFromItem(itemstack.getItem());
			return block != Blocks.AIR && (block.equals(Blocks.COBBLESTONE) || block.equals(Blocks.GRAVEL) || block.equals(Blocks.DIRT));
		}
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {

	}

	public void dropInventory() {
		for (ItemStack itemstack : slots) {
			if (itemstack != null)
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemstack));
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
	public SPacketUpdateTileEntity getUpdatePacket () {
		NBTTagCompound tag = new NBTTagCompound();
		return new SPacketUpdateTileEntity(pos, 1, tag);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

	private void processFluid() {
		if (getStackInSlot(2).isEmpty()) {
			return;
		}
		ItemStack input = getStackInSlot(2).copy();
		input.setCount(1);
		ItemStack result = getStackInSlot(3).copy();

		boolean success = false;

		// TODO Waterpicker, I've never touched Fluid's before and much of this changed...can you work on this?
		final FluidStack container = FluidUtil.getFluidContained(input);


		if (container != null) {
			if (tank.fill(new FluidStack(container.getFluid(), 5000), false) == 5000) {  //5000 indicates the input rate of the tank.
				if(!addStackToOutput(input.getItem().hasContainerItem(input) ? input.getItem().getContainerItem(input) : null, false))return;
				tank.fill(new FluidStack(container.getFluid(), 5000), true);
				result = input.getItem().hasContainerItem(input) ? input.getItem().getContainerItem(input) : null;
				success = true;

			}
		} if (success) {
			getStackInSlot(2).shrink(1);
			if (getStackInSlot(2).isEmpty()) {
				slots[2].setCount(0);
			}
			addStackToOutput(result, true);
		}
	}

	private boolean addStackToOutput(ItemStack stack, boolean doPut){
		if (stack.isEmpty()) {
			if (doPut) {
				markDirty();
			}
			return true;
		}

		ItemStack output = getStackInSlot(3);
		if (output.isEmpty()) {
			if (doPut) {
				setInventorySlotContents(3, stack);
			}
			return true;
		}
		else if (stack.isItemEqual(output) && (output.getCount() + stack.getCount()) <= output.getMaxStackSize()){
			if (doPut) {
				incrStackSize(3, stack.getCount() > 0 ? stack.getCount() : 1, true);
			}
			return true;
		}
		else {
			return false;
		}
	}

	public boolean incrStackSize(int i, int j, boolean markDirty) {
		ItemStack itemstack = getStackInSlot(i);

		if (!itemstack.isEmpty()) {
			if (itemstack.getCount() >= itemstack.getMaxStackSize()) {
				itemstack.setCount(itemstack.getMaxStackSize());
				slots[i] = itemstack;
				if (markDirty) {
					markDirty();
				}
			} else {
				itemstack.setCount(itemstack.getCount() + j);
				slots[i] = itemstack;
				if (markDirty) {
					markDirty();
				}
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
		return (int) ((getPowerLevel() * scale) / sink.getCapacity());
	}

	@Override
	public void update() {
		sink.update();

		processFluid();

		boolean updateInventory = false;

		if (!this.world.isRemote) {
			if (canWash()) {
				toggleWashing(true);

				++this.washTime;

				if (washTime % 20 == 0) {
					world.playSound(null, pos, PDWPMod.SOUND_WASHING, SoundCategory.BLOCKS, 1, 1);
				}

				if (this.washTime == Config.WASH_TIME) {
					if (slots[1] == null || (slots[1].getCount() < slots[1].getMaxStackSize())) {
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

	@Override
	public String getName() {
		return "Washplant";
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}
}