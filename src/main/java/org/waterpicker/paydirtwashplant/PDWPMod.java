package org.waterpicker.paydirtwashplant;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import org.waterpicker.paydirtwashplant.proxy.CommonProxy;

@Mod(modid = PDWPMod.MODID, version = PDWPMod.VERSION)

public class PDWPMod {
    public static final String MODID = "paydirtwashplant";
    public static final String VERSION = "1.0";

    @SidedProxy(
            clientSide="org.waterpicker.paydirtwashplant.proxy.ClientProxy",
            serverSide="org.waterpicker.paydirtwashplant.proxy.CommonProxy"
    )

    public static CommonProxy proxy;

    @EventHandler
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
        proxy.fmlLifeCycleEvent(event);
    }

    @EventHandler
    public void fmlLifeCycleEvent(FMLInitializationEvent event) {
        proxy.fmlLifeCycleEvent(event);
    }

    @EventHandler
    public void fmlLifeCycle(FMLPostInitializationEvent event) {
        proxy.fmlLifeCycleEvent(event);
    }

    @EventHandler
    public void fmlLifeCycle(FMLServerAboutToStartEvent event) {
        proxy.fmlLifeCycleEvent(event);
    }

    @EventHandler
    public void fmlLifeCycle(FMLServerStartingEvent event) {
        proxy.fmlLifeCycleEvent(event);
    }

    @EventHandler
    public void fmlLifeCycle(FMLServerStartedEvent event) {
        proxy.fmlLifeCycleEvent(event);
    }

    @EventHandler
    public void fmlLifeCycle(FMLServerStoppingEvent event) {
        proxy.fmlLifeCycleEvent(event);
    }

    @EventHandler
    public void fmlLifeCycle(FMLServerStoppedEvent event) {
        proxy.fmlLifeCycleEvent(event);
    }
}
