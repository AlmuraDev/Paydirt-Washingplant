package com.almuradev.paydirtwashplant.proxy;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.almuradev.paydirtwashplant.Config;
import com.almuradev.paydirtwashplant.PDWPMod;
import com.almuradev.paydirtwashplant.block.gui.GuiHandler;
import com.almuradev.paydirtwashplant.tileentity.TileEntityWashplant;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

public class CommonProxy {
	public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
		initConfig(event);        
		registerBlocks();
		registerTileEntities();
	}

	public void fmlLifeCycleEvent(FMLInitializationEvent event) {
		registerRecipes();     
		registerGuiHandlers();
	}   

	private void initConfig(FMLPreInitializationEvent event) {
		Config.setup(new File(event.getModConfigurationDirectory(), "/paydirt.cfg"));
	}

	private void registerBlocks() {
		GameRegistry.registerBlock(PDWPMod.WASHPLANT, "paydirtwashplant");
	}

	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityWashplant.class, "washplanttile");
	}

	private void registerGuiHandlers() {
		NetworkRegistry.INSTANCE.registerGuiHandler(PDWPMod.instance, new GuiHandler());
	}

	private void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(PDWPMod.WASHPLANT), "XAX",
				"XBX",
				"XCX",
				'A', Blocks.REDSTONE_BLOCK, 'B', Blocks.IRON_BLOCK, 'C', Blocks.OBSIDIAN);
	}
}

