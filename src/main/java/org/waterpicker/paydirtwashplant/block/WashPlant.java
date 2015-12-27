package org.waterpicker.paydirtwashplant.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
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
}
