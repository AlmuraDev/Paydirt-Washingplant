/*
 * This file is part of Paydirt-Washingplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;

/**
 * A helper class for getting an entity from a chunk cache/world.
 */
public final class BlockAccessHelper {

    private BlockAccessHelper() {}

    /**
     * Gets the tile entity in a world without possibility of causing concurrent
     * modification exception.
     *
     * @param access The world or chunk cache
     * @param pos The target position
     * @return The tile entity at that spot, or {@code null} if there is no tile at that spot
     */
    @Nullable
    public static TileEntity getTile(final IBlockAccess access, final BlockPos pos) {
        return access instanceof ChunkCache ? ((ChunkCache) access).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : access.getTileEntity(pos);
    }

}
