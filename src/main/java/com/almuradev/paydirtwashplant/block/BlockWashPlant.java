package com.almuradev.paydirtwashplant.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.almuradev.paydirtwashplant.PDWPMod;
import com.almuradev.paydirtwashplant.tileentity.TileEntityWashplant;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWashPlant extends BlockContainer {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool ACTIVE = PropertyBool.create("active");

	public BlockWashPlant() {
		super(Material.IRON);
		setUnlocalizedName("washplant");
		setRegistryName(PDWPMod.MODID, "washplant");
		setCreativeTab(PDWPMod.TAB_WASHPLANT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ACTIVE);
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(ACTIVE, false);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(ACTIVE, false), 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {

		// TODO Need to actually figure out the state from the meta to account for ACTIVE property
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		// TODO Need to get the meta from the state including ACTIVE property
		return ((EnumFacing)state.getValue(FACING)).getIndex();
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
