/*
 * This file is part of Paydirt-Washplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant.block;

import buildcraft.api.blocks.ICustomRotationHandler;
import com.almuradev.paydirtwashplant.PDWPMod;
import com.almuradev.paydirtwashplant.tileentity.TileEntityWashplant;
import com.almuradev.paydirtwashplant.util.BlockAccessHelper;
import ic2.api.tile.IWrenchable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import java.util.List;

import javax.annotation.Nullable;

public final class BlockWashPlant extends BlockContainer implements IWrenchable, ICustomRotationHandler {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockWashPlant() {
        super(Material.IRON);
        setUnlocalizedName("washplant");
        setRegistryName(PDWPMod.BLOCK_ID);
        setCreativeTab(PDWPMod.getInstance().getTabWashplant());
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        final TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityWashplant) {
            ((TileEntityWashplant) tile).setFacing(placer.getHorizontalFacing().getOpposite());
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final TileEntity tile = BlockAccessHelper.getTile(worldIn, pos);
        IBlockState result = super.getActualState(state, worldIn, pos);
        if (tile instanceof TileEntityWashplant) {
            final TileEntityWashplant washplant = (TileEntityWashplant) tile;
            result = result.withProperty(FACING, washplant.getFacing()).withProperty(ACTIVE, washplant.isActive());
        }
        return result;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return super.getBlockLayer();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityWashplant();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX,
        float hitY, float hitZ) {
        if (!world.isRemote) {
            super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
            FMLNetworkHandler.openGui(player, PDWPMod.getInstance(), 0, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        @Nullable final TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityWashplant) {
            ((TileEntityWashplant) tile).dropInventory();
        }
        super.breakBlock(world, pos, state);
    }

    /**
     * Get direction the block is facing.
     *
     * The direction typically refers to the front/main/functionally dominant side of a block.
     *
     * @param world World containing the block.
     * @param pos The block's current position in the world.
     * @return Current block facing.
     */
    @Override
    public EnumFacing getFacing(final World world, final BlockPos pos) {
        @Nullable final TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityWashplant) {
            return ((TileEntityWashplant) tile).getFacing();
        }
        return EnumFacing.NORTH;
    }

    /**
     * Set the block's facing to face towards the specified direction.
     *
     * Contrary to Block.rotateBlock the block should always face the requested direction after
     * successfully processing this method.
     *
     * @param world World containing the block.
     * @param pos The block's current position in the world.
     * @param newDirection Requested facing, see {@link #getFacing}.
     * @param player Player causing the action, may be null.
     * @return true if successful, false otherwise.
     */
    @Override
    public boolean setFacing(final World world, final BlockPos pos, final EnumFacing newDirection, final EntityPlayer player) {
        @Nullable final TileEntity tile = world.getTileEntity(pos);
        return (tile instanceof TileEntityWashplant) && ((TileEntityWashplant) tile).setFacing(newDirection);
    }

    /**
     * Determine if the wrench can be used to remove the block.
     *
     * @param world World containing the block.
     * @param pos The block's current position in the world.
     * @param player Player causing the action, may be null.
     * @return true if allowed, false otherwise.
     */
    @Override
    public boolean wrenchCanRemove(final World world, final BlockPos pos, final EntityPlayer player) {
        //TODO permissions
        return true;
    }

    /**
     * Determine the items the block will drop when the wrenching is successful.
     *
     * The ItemStack will be copied before creating the EntityItem.
     *
     * @param world World containing the block.
     * @param pos The block's current position in the world.
     * @param state The block's block state before removal.
     * @param te The block's tile entity before removal, if any, may be null.
     * @param player Player removing the block, may be null.
     * @param fortune Fortune level for drop calculation.
     * @return ItemStacks to drop, may be empty.
     */
    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getWrenchDrops(World world, BlockPos pos, IBlockState state, TileEntity te, EntityPlayer player,
        int fortune) {
        //TODO remove regular drops
        return getDrops(world, pos, state, fortune);
    }

    @Override
    public EnumActionResult attemptRotation(World world, BlockPos pos, IBlockState state, EnumFacing sideWrenched) {
        @Nullable final TileEntity tile = world.getTileEntity(pos);
        return (tile instanceof TileEntityWashplant) && ((TileEntityWashplant) tile).setFacing(sideWrenched) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }
}
