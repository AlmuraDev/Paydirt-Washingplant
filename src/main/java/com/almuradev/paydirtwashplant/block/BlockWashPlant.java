package com.almuradev.paydirtwashplant.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.almuradev.paydirtwashplant.PDWPMod;
import com.almuradev.paydirtwashplant.tileentity.TileEntityWashplant;
import com.almuradev.paydirtwashplant.util.DirectionHelper;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWashPlant extends BlockContainer {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool ACTIVE = PropertyBool.create("active");

	public BlockWashPlant() {
		super(Material.IRON);
		setRegistryName(PDWPMod.MODID, "paydirtwashplant");
	}


	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileEntityWashplant();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
			FMLNetworkHandler.openGui(player, PDWPMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@SideOnly(Side.CLIENT)
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
		EnumFacing facing =  EnumFacing.fromAngle((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D);
		world.setBlockState(pos,state.withProperty(FACING, facing),2);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		((TileEntityWashplant) world.getTileEntity(pos)).dropInventory();
		super.breakBlock(world, pos, state);
	}

	public static void updateBlockState(boolean isActive, World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos).withProperty(ACTIVE, isActive);

		world.setBlockState(pos, state, 2);
	}
}
