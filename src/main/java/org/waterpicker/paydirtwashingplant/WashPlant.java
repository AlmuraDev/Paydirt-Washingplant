package org.waterpicker.paydirtwashingplant;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.waterpicker.paydirtwashingplant.tileentity.WashPlantTile;

/**
 * Created by Waterpician on 12/27/2015.
 */
public class WashPlant extends BlockContainer {

    protected WashPlant() {
        super(Material.iron);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new WashPlantTile();
    }
}
