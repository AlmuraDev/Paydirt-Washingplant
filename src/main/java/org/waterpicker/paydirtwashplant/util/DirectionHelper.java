package org.waterpicker.paydirtwashplant.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class DirectionHelper {
    public static ForgeDirection getRelativeSide(int front, String side) {
        ForgeDirection dir = getDirection(front);

        switch (side) {
            case "front":
                return dir;
            case "back":
                return dir.getOpposite();
            case "left":
                return dir.getRotation(ForgeDirection.UP);
            case "right":
                return dir.getRotation(ForgeDirection.UP).getOpposite();
            case "top":
                return ForgeDirection.UP;
            case "bottom":
                return ForgeDirection.DOWN;
            default:
                return ForgeDirection.UNKNOWN;
        }
    }

    public static String getRelativeSide(ForgeDirection side, int front) {
        ForgeDirection dir = getDirection(front);

        if(side.equals(dir))
            return "front";
        if(side.equals(dir.getOpposite()))
            return "back";
        if(side.equals(dir.getRotation(ForgeDirection.UP)))
            return "left";
        if(side.equals(dir.getRotation(ForgeDirection.UP).getOpposite()))
            return "right";
        if(side.equals(ForgeDirection.UP))
            return "top";
        if(side.equals(ForgeDirection.DOWN))
            return "bottom";

        return "unknown";
    }

    private static ForgeDirection getDirection(int direction) {
        switch (direction) {
            case 0:
                return ForgeDirection.NORTH;
            case 1:
                return ForgeDirection.EAST;
            case 2:
                return ForgeDirection.SOUTH;
            case 3:
                return ForgeDirection.WEST;
            default:
                return ForgeDirection.UNKNOWN;
        }
    }
}
