package com.almuradev.paydirtwashplant.util;

import net.minecraft.util.EnumFacing;

public class DirectionHelper {
	public static EnumFacing getRelativeSide(EnumFacing front, String side) {
		switch (side) {
		case "front":
			return front;
		case "back":
			return front.getOpposite();
		case "left":
			return front.rotateY();
		case "right":
			return front.rotateY().getOpposite();
		case "top":
			return EnumFacing.UP;
		case "bottom":
			return EnumFacing.DOWN;
		default:
			return EnumFacing.UP;
		}
	}

	public static String getRelativeSide(EnumFacing side, EnumFacing front) {
		if(side.equals(front))
			return "front";
		if(side.equals(front.getOpposite()))
			return "back";
		if(side.equals(front.rotateY()))
			return "left";
		if(side.equals(front.rotateY().getOpposite()))
			return "right";
		if(side.equals(EnumFacing.UP))
			return "top";
		if(side.equals(EnumFacing.DOWN))
			return "bottom";

		return "unknown";
	}

	private static EnumFacing getDirection(int direction) {
		switch (direction) {
		case 0:
			return EnumFacing.NORTH;
		case 1:
			return EnumFacing.EAST;
		case 2:
			return EnumFacing.SOUTH;
		case 3:
			return EnumFacing.WEST;
		default:
			return EnumFacing.UP;
		}
	}
}
