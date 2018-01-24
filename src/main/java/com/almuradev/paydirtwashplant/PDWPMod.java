package com.almuradev.paydirtwashplant;

import com.almuradev.paydirtwashplant.block.BlockWashPlant;
import com.almuradev.paydirtwashplant.proxy.CommonProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = PDWPMod.MODID, version = PDWPMod.VERSION)

public class PDWPMod {
	public static final String MODID = "paydirtwashplant";
	public static final String VERSION = "1.0";
	public static PDWPMod instance;

	public static BlockWashPlant WASHPLANT = new BlockWashPlant();

	public static SoundEvent SOUND_WASHING = new SoundEvent(new ResourceLocation(MODID, "washplant")).setRegistryName(new ResourceLocation(MODID, "washplant"));
	
	@SidedProxy(
			serverSide="com.almuradev.paydirtwashplant.proxy.CommonProxy",
			clientSide="com.almuradev.paydirtwashplant.proxy.ClientProxy"
			)

	public static CommonProxy proxy;

	@Mod.EventHandler
	public void fmlLifeCycleEvent(FMLConstructionEvent event) {
		instance = this;
		proxy.fmlLifeCycleEvent(event);
	}

	@Mod.EventHandler
	public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
		proxy.fmlLifeCycleEvent(event);
	}

	@Mod.EventHandler
	public void fmlLifeCycleEvent(FMLInitializationEvent event) {
		proxy.fmlLifeCycleEvent(event);
	}
}
