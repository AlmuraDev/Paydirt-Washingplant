package com.almuradev.paydirtwashplant;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;

import com.almuradev.paydirtwashplant.block.BlockWashPlant;
import com.almuradev.paydirtwashplant.proxy.CommonProxy;

@Mod(modid = PDWPMod.MODID, version = PDWPMod.VERSION)

public class PDWPMod {
	public static final String MODID = "paydirtwashplant";
	public static final String VERSION = "1.0";
	public static PDWPMod instance;

	public static BlockWashPlant WASHPLANT = new BlockWashPlant();

	@SidedProxy(
			serverSide="com.almuradev.paydirtwashplant.proxy.CommonProxy",
			clientSide="com.almuradev.paydirtwashplant.proxy.ClientProxy"
			)

	public static CommonProxy proxy;

	@EventHandler
	public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
		instance = this;
		proxy.fmlLifeCycleEvent(event);
	}

	@EventHandler
	public void fmlLifeCycleEvent(FMLInitializationEvent event) {
		proxy.fmlLifeCycleEvent(event);
	}
}
