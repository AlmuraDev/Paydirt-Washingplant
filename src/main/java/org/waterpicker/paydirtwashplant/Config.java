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
    public static float COBBLESTONE_PERCENTAGE;
    public static float GRAVEL_PERCENTAGE;
    public static float DIRT_PERCENTAGE;

    public static void setup(File file) {
        Configuration config = new Configuration(file);
        config.load();
        log("voltage", VOLTAGE, config.get(Configuration.CATEGORY_GENERAL, "voltage", "low").getString());
        log("power buffer", EU_BUFFER, config.get(Configuration.CATEGORY_GENERAL, "power buffer", 1000).getInt());
        log("water buffer", WATER_BUFFER, config.get(Configuration.NEW_LINE, "water buffer", 1000).getInt());
        log("eu per operation", EU_PER_OPERATION, config.get(Configuration.CATEGORY_GENERAL, "eu per operation", 50).getInt());
        log("water per operation", WATER_PER_OPERATION, config.get(Configuration.CATEGORY_GENERAL, "water per operation", 50).getInt());
        log("cobblestone percentage", COBBLESTONE_PERCENTAGE, (float) config.get(Configuration.CATEGORY_GENERAL, "cobblestone percentage", 0.7).getDouble());
        log("gravel percentage", GRAVEL_PERCENTAGE, (float) config.get(Configuration.CATEGORY_GENERAL, "gravel percentage", 0.1).getDouble());
        log("dirt percentage", DIRT_PERCENTAGE, (float) config.get(Configuration.CATEGORY_GENERAL, "dirt percentage", 0.2).getDouble());
        config.save();
    }

    public static void log(String s, Object m, Object l) {
        System.out.println(s + ": " + m);
        m = l;
    }
}
