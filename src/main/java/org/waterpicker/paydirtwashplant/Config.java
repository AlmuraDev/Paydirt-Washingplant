package org.waterpicker.paydirtwashplant;
import net.minecraftforge.common.config.Configuration;

import java.io.File;;

/**
 * Created by Waterpician on 12/26/2015.
 */
public class Config {
    public static String VOLTAGE;
    public static int EU_BUFFER;
    public static int WATER_BUFFER;
    public static int EU_PER_OPERATION;
    public static int WATER_PER_OPERATION;
    public static double COBBLESTONE_PERCENTAGE;
    public static double GRAVEL_PERCENTAGE;
    public static double DIRT_PERCENTAGE;

    public static void setup(File file) {
        Configuration config = new Configuration(file);
        config.load();
        VOLTAGE = config.get(Configuration.CATEGORY_GENERAL, "voltage", "low").getString();
        EU_BUFFER = config.get(Configuration.CATEGORY_GENERAL, "power buffer", 1000).getInt();
        WATER_BUFFER = config.get(Configuration.NEW_LINE, "water buffer", 1000).getInt();
        EU_PER_OPERATION = config.get(Configuration.CATEGORY_GENERAL, "eu per operation", 50).getInt();
        WATER_PER_OPERATION = config.get(Configuration.CATEGORY_GENERAL, "water per operation", 50).getInt();
        COBBLESTONE_PERCENTAGE = config.get(Configuration.CATEGORY_GENERAL, "cobblestone percentage", 0.05d).getDouble();
        GRAVEL_PERCENTAGE = config.get(Configuration.CATEGORY_GENERAL, "gravel percentage", 0.05d).getDouble();
        DIRT_PERCENTAGE = config.get(Configuration.CATEGORY_GENERAL, "dirt percentage", 0.05d).getDouble();
        config.save();
    }
}
