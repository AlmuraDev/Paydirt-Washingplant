/*
 * This file is part of Paydirt-Washingplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant.proxy;

import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.almuradev.paydirtwashplant.Config;
import com.almuradev.paydirtwashplant.PDWPMod;
import com.almuradev.paydirtwashplant.block.gui.GuiHandler;
import com.almuradev.paydirtwashplant.tileentity.TileEntityWashplant;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

import javax.annotation.Nullable;

public abstract class CommonProxy {

	protected CommonProxy() {}

	public void fmlLifeCycleEvent(FMLConstructionEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
		initConfig(event);
		registerTileEntities();
	}

	public void fmlLifeCycleEvent(FMLInitializationEvent event) {
		registerGuiHandlers();
	}   

	private void initConfig(FMLPreInitializationEvent event) {
		Config.setup(new File(event.getModConfigurationDirectory(), "/paydirt.cfg"));
	}

	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(PDWPMod.getInstance().getBlockWashplant());
	}

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(PDWPMod.getInstance().getBlockWashplant()).setRegistryName(PDWPMod.BLOCK_ID));
    }

	@SubscribeEvent
	public void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
		@Nullable final ItemStack machineCasing = IC2Items.getItemAPI().getItemStack("resource", "machine");
		@Nullable final ItemStack circuit = IC2Items.getItemAPI().getItemStack("crafting", "circuit");
		GameRegistry.addShapedRecipe(new ResourceLocation(PDWPMod.MODID, "washplant"), new ResourceLocation(PDWPMod.MODID, "washplant"), new
				ItemStack(PDWPMod.getInstance().getBlockWashplant()),
				"XAX",
				"XBX",
				"XCX",
				'A', circuit == null ? Blocks.REDSTONE_BLOCK : circuit,
				'B', machineCasing == null ? Blocks.IRON_BLOCK : machineCasing,
				'C', Blocks.OBSIDIAN,
				'X', Blocks.AIR
				);
	}

	@SubscribeEvent
	public void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(PDWPMod.getInstance().getWashingSoundEvent());
	}

	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityWashplant.class, PDWPMod.MODID + ":washplant");
	}

	private void registerGuiHandlers() {
		NetworkRegistry.INSTANCE.registerGuiHandler(PDWPMod.getInstance(), new GuiHandler());
	}
}

