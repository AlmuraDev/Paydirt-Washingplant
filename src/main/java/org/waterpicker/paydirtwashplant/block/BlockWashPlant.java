package org.waterpicker.paydirtwashplant.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.waterpicker.paydirtwashplant.tileentity.WashPlantTile;

/**
 * Created by Waterpician on 12/27/2015.
 */
public class BlockWashPlant extends BlockContainer {

    private boolean isActive;

    @SideOnly(Side.CLIENT)
    IIcon back,front, side, front_active, side_active;

    public BlockWashPlant() {
        super(Material.iron);
        super.setCreativeTab(CreativeTabs.tabBlock);
        super.setBlockName("washplant");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        blockIcon = register.registerIcon("washplant");
        back = register.registerIcon("washplant_back");
        front = register.registerIcon("washplant_front");
        side = register.registerIcon("washplant_side");
        front_active = register.registerIcon("washplant_front_active");
        side_active = register.registerIcon("washplant_side_active");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return blockIcon;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new WashPlantTile();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int sideHit, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && world.getTileEntity(x, y, z) instanceof WashPlantTile) {
            ((WashPlantTile) world.getTileEntity(x, y, z)).onBlockActivated(world, x, y, z, entityPlayer, sideHit, hitX, hitY, hitZ);
        }
        return false;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        int meta = world.getBlockMetadata(x,y,z);
        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }

        if (l == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }

        if (l == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }

        if (l == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        ((WashPlantTile) world.getTileEntity(x,y,z)).dropInventory();

        super.breakBlock(world, x, y, z, block, meta);
    }

    public static void updateBlockState(boolean isActive, World world, int x, int y, int z) {
        Block block = world.getBlock(x,y,z);
        //TileEntity tile =
        if(block != null) {
            if(block instanceof BlockWashPlant) {
                BlockWashPlant washplant = (BlockWashPlant) block;

                //washplant
            }
        }
    }
}
