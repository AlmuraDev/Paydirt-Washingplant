/*
 * This file is part of Paydirt-Washplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant.proxy;

import com.almuradev.paydirtwashplant.PDWPMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The proxy for the client side.
 */
@SideOnly(Side.CLIENT)
public final class ClientProxy extends CommonProxy {

    @Override
    public void fmlLifeCycleEvent(FMLInitializationEvent event) {
        super.fmlLifeCycleEvent(event);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
            .register(Item.getItemFromBlock(PDWPMod.getInstance().getBlockWashplant()), 0, new ModelResourceLocation(PDWPMod.BLOCK_ID, "inventory"));
    }
}