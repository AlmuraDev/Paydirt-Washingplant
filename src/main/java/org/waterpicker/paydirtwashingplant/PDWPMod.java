package org.waterpicker.paydirtwashingplant;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.inventory.ISidedInventory;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.core.config.JSONConfigurationFactory;

@Mod(modid = PDWPMod.MODID, version = PDWPMod.VERSION)
public class PDWPMod
{
    public static final String MODID = "paydirtwashingplant";
    public static final String VERSION = "1.0";



    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        Config.setup(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
}
