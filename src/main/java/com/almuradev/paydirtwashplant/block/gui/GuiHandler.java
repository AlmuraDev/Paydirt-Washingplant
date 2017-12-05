package com.almuradev.paydirtwashplant.block.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.almuradev.paydirtwashplant.tileentity.TileEntityWashplant;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {
	public static final int MOD_TILE_ENTITY_GUI = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == MOD_TILE_ENTITY_GUI)
			return new ContainerWashPlant(player.inventory, (TileEntityWashplant) world.getTileEntity(new BlockPos(x,y,z)));

		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == MOD_TILE_ENTITY_GUI)
			return new GuiWashPlant(player.inventory, (TileEntityWashplant) world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}
}

