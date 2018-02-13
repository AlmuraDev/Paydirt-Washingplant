/*
 * This file is part of Paydirt-Washingplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant;

import com.almuradev.paydirtwashplant.block.BlockWashPlant;
import com.almuradev.paydirtwashplant.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = PDWPMod.MODID, version = PDWPMod.VERSION)
public final class PDWPMod {

    public static final String MODID = "paydirtwashplant";
    public static final String VERSION = "1.0";
    public static final ResourceLocation BLOCK_ID = new ResourceLocation(MODID, "washplant");
    private static final PDWPMod INSTANCE = new PDWPMod();

    private CreativeTabs tabWashplant;
    BlockWashPlant blockWashplant; // Made package since the creative tab inner class needs this

    private final SoundEvent
        washingSoundEvent = new SoundEvent(new ResourceLocation(MODID, "washplant")).setRegistryName(new ResourceLocation(MODID, "washplant"));

    @SidedProxy(
        serverSide = "com.almuradev.paydirtwashplant.proxy.ServerProxy",
        clientSide = "com.almuradev.paydirtwashplant.proxy.ClientProxy"
    )
    public static CommonProxy proxy;

    @Mod.InstanceFactory
    public static PDWPMod getInstance() {
        return INSTANCE;
    }

    private PDWPMod() {
    }

    public CreativeTabs getTabWashplant() {
        return this.tabWashplant;
    }

    public BlockWashPlant getBlockWashplant() {
        return this.blockWashplant;
    }

    public SoundEvent getWashingSoundEvent() {
        return this.washingSoundEvent;
    }

    @Mod.EventHandler
    public void fmlLifeCycleEvent(FMLConstructionEvent event) {
        proxy.fmlLifeCycleEvent(event);
    }

    @Mod.EventHandler
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
        this.tabWashplant = new CreativeTabs("paydirtwashplant.paydirtwashplant") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Item.getItemFromBlock(PDWPMod.this.blockWashplant));
            }
        };
        this.blockWashplant = new BlockWashPlant();
        proxy.fmlLifeCycleEvent(event);
    }

    @Mod.EventHandler
    public void fmlLifeCycleEvent(FMLInitializationEvent event) {
        proxy.fmlLifeCycleEvent(event);
    }
}
