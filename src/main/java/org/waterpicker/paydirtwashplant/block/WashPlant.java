package org.waterpicker.paydirtwashplant.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.waterpicker.paydirtwashplant.tileentity.WashPlantTile;

/**
 * Created by Waterpician on 12/27/2015.
 */
public class WashPlant extends BlockContainer {

    public WashPlant() {
        super(Material.iron);
        super.setCreativeTab(CreativeTabs.tabBlock);
        super.setBlockName("Paydirt Washplant");
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
}
