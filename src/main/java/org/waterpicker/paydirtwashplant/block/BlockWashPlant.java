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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.waterpicker.paydirtwashplant.PDWPMod;
import org.waterpicker.paydirtwashplant.tileentity.WashPlantTile;
import org.waterpicker.paydirtwashplant.util.DirectionHelper;

public class BlockWashPlant extends BlockContainer {

    @SideOnly(Side.CLIENT)
    IIcon back,front, side, front_active, side_active, unknown;

    public BlockWashPlant() {
        super(Material.iron);
        super.setCreativeTab(CreativeTabs.tabBlock);
        super.setBlockName("Wash Plant");
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
        String s = DirectionHelper.getRelativeSide(ForgeDirection.getOrientation(side),meta/2);
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

        world.setBlockMetadataWithNotify(x,y,z,l*2,2);
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        ((WashPlantTile) world.getTileEntity(x,y,z)).dropInventory();

        super.breakBlock(world, x, y, z, block, meta);
    }

    public static void updateBlockState(boolean isActive, World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x,y,z);

        if(meta%2 == 1) {
            if(!isActive)
                meta--;
        } else {
            if(isActive)
                meta++;
        }

        world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }
}
