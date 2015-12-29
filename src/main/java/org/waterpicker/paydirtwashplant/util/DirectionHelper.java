package org.waterpicker.paydirtwashplant.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Waterpician on 12/29/2015.
 */
public class DirectionHelper {
    public static ForgeDirection getRelativeSide(TileEntity entity, String side) {
        ForgeDirection front = ForgeDirection.getOrientation(entity.getWorldObj().getBlockMetadata(entity.xCoord,entity.yCoord,entity.zCoord));

        switch (side) {
            case "front":
                return front;
            case "back":
                return front.getOpposite();
            case "left":
                return front.getRotation(ForgeDirection.UP);
            case "right":
                return front.getRotation(ForgeDirection.UP).getOpposite();
            case "top":
                return ForgeDirection.UP;
            case "bottom":
                return ForgeDirection.DOWN;
            default:
                return ForgeDirection.UNKNOWN;
        }
    }
}
