package org.waterpicker.paydirtwashingplant;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.List;

/**
 * Created by Waterpician on 12/26/2015.
 */
public class Config {
    public static String BLOCK_VOLTAGE;
    public static int BLOCK_EU_BUFFER;
    public static int NEEDED_EU_PER_OPERATION;
    public static int WATER_NEEDED_PER_OPERATION;
    public static double COBBLESTONE_PERCENTAGE;
    public static double GRAVEL_PERCENTAGE;
    public static double DIRT_PERCENTAGE;

    public static void setup(File file) {
        Configuration config = new Configuration(file);
        BLOCK_VOLTAGE = config.get(Configuration.CATEGORY_GENERAL, "voltage", "low").getString();
        BLOCK_EU_BUFFER = config.get(Configuration.CATEGORY_GENERAL, "buffer", 1000).getInt();
        NEEDED_EU_PER_OPERATION = config.get(Configuration.CATEGORY_GENERAL, "eu per operation", 50).getInt();
        WATER_NEEDED_PER_OPERATION = config.get(Configuration.CATEGORY_GENERAL, "water (mb) per operation", 50).getInt();
    }
}
