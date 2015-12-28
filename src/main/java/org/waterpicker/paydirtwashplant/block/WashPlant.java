package org.waterpicker.paydirtwashplant.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
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
}
