package com.almuradev.paydirtwashplant.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
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

	@SideOnly(Side.CLIENT)
	IIcon back,front, side, front_active, side_active, unknown;

	public BlockWashPlant() {
		super(Material.iron);
		super.setCreativeTab(CreativeTabs.tabBlock);
		super.setBlockName("Paydirt Wash Plant");
		super.setBlockTextureName(PDWPMod.MODID + ":washplant");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		super.registerBlockIcons(register);
		back = register.registerIcon(getTextureName() + "_back");
		front = register.registerIcon(getTextureName() + "_front");
		side = register.registerIcon(getTextureName() + "_side");
		front_active = register.registerIcon(getTextureName() + "_front_active");
		side_active = register.registerIcon(getTextureName() + "_side_active");
		unknown = register.registerIcon(getTextureName() + "_unknown");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return retrieveIcon(side, 4);
	}

	public IIcon retrieveIcon(int side, int meta) {
		String s = DirectionHelper.getRelativeSide(ForgeDirection.getOrientation(side), meta/2);
		boolean b = meta%2 == 1;

		if(s.equals("front")) {
			return b ? front_active : front;
		} if(s.equals("back")) {
			return back;
		} if(s.equals("left") || s.equals("right")) {
			return b ? side_active : this.side;
		} if(s.equals("top") || s.equals("bottom")) {
			return blockIcon;
		}

		return unknown;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side)
	{
		return retrieveIcon(side, access.getBlockMetadata(x,y,z));
	}


	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileEntityWashplant();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			FMLNetworkHandler.openGui(player, PDWPMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@SideOnly(Side.CLIENT)
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		int l = MathHelper.absFloor((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		world.setBlockMetadataWithNotify(x,y,z,l*2,2);
	}

	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		((TileEntityWashplant) world.getTileEntity(x,y,z)).dropInventory();

		super.breakBlock(world, x, y, z, block, meta);
	}

	public static void updateBlockState(boolean isActive, World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x,y,z);

		if(meta%2 == 1) {
			if(!isActive) meta--;
		} else {
			if(isActive) meta++;
		}

		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
	}
}
