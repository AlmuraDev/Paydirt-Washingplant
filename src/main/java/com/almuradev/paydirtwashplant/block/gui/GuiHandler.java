/*
 * This file is part of Paydirt-Washingplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant.block.gui;

import static com.google.common.base.Preconditions.checkNotNull;

import com.almuradev.paydirtwashplant.tileentity.TileEntityWashplant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public final class GuiHandler implements IGuiHandler {

    public static final int MOD_TILE_ENTITY_GUI = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(final int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case MOD_TILE_ENTITY_GUI:
                return new ContainerWashPlant(player.inventory, checkNotNull((TileEntityWashplant) world.getTileEntity(new BlockPos(x, y, z))));
            default:
                return null;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nullable
    public Object getClientGuiElement(final int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case MOD_TILE_ENTITY_GUI:
                return new GuiWashPlant(player.inventory, checkNotNull((TileEntityWashplant) world.getTileEntity(new BlockPos(x, y, z))));
            default:
                return null;
        }
    }
}

