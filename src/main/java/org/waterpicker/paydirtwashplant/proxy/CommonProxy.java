package org.waterpicker.paydirtwashplant.proxy;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import org.waterpicker.paydirtwashplant.Config;
import org.waterpicker.paydirtwashplant.block.BlockWashPlant;
import org.waterpicker.paydirtwashplant.tileentity.WashPlantTile;

import java.io.File;

public class CommonProxy {
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
        // load configuration before doing anything else, because might use

        // config values during registration
        initConfig(event);

        // register stuff
        registerBlocks();
        registerTileEntities();
    }

    public void fmlLifeCycleEvent(FMLInitializationEvent event) {
        // register custom event listeners
        registerEventListeners();

        // register recipes here to allow use of items from other mods
        registerRecipes();

        // register achievements here to allow use of items from other mods
        registerAchievements();

        // register gui handlers
        registerGuiHandlers();
    }

    public void fmlLifeCycleEvent(FMLPostInitializationEvent event) {
    }

    public void fmlLifeCycleEvent(FMLServerAboutToStartEvent event) {
    }


    public void fmlLifeCycleEvent(FMLServerStartedEvent event) {
    }


    public void fmlLifeCycleEvent(FMLServerStoppingEvent event) {
    }


    public void fmlLifeCycleEvent(FMLServerStoppedEvent event) {
    }


    public void fmlLifeCycleEvent(FMLServerStartingEvent event)
    {
        // register server commands
    }

    private void initConfig(FMLPreInitializationEvent event) {
        Config.setup(new File(event.getModConfigurationDirectory(), "/paydirt.config"));
    }

    private void registerBlocks() {
        GameRegistry.registerBlock(new BlockWashPlant(), "paydirtwashplant");
    }

    private void registerTileEntities() {
        GameRegistry.registerTileEntity(WashPlantTile.class, "washplanttile");
    }

    private void registerAchievements() {
    }

    private void registerGuiHandlers() {
    }

    private void registerRecipes() {
    }

    private void registerEventListeners() {
    }
}

