package org.waterpicker.paydirtwashplant;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

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

        VOLTAGE = log("voltage", config.get(Configuration.CATEGORY_GENERAL, "voltage", "low").getString());
        EU_BUFFER = log("power buffer", config.get(Configuration.CATEGORY_GENERAL, "power buffer", 1000).getInt());
        WATER_BUFFER = log("water buffer", config.get(Configuration.CATEGORY_GENERAL, "water buffer", 1000).getInt());
        EU_PER_OPERATION = log("eu per operation", config.get(Configuration.CATEGORY_GENERAL, "eu per operation", 50).getInt());
        WATER_PER_OPERATION = log("water per operation", config.get(Configuration.CATEGORY_GENERAL, "water per operation", 50).getInt());
        COBBLESTONE_PERCENTAGE = log("cobblestone percentage", config.get(Configuration.CATEGORY_GENERAL, "cobblestone percentage", 0.7).getDouble());
        GRAVEL_PERCENTAGE = log("gravel percentage", config.get(Configuration.CATEGORY_GENERAL, "gravel percentage", 0.1).getDouble());
        DIRT_PERCENTAGE = log("dirt percentage", config.get(Configuration.CATEGORY_GENERAL, "dirt percentage", 0.2).getDouble());
        config.save();
    }

    public static int log(String s, int m) {
        System.out.println(s + ": " + m);
        return m;
    }

    public static String log(String s, String m) {
        System.out.println(s + ": " + m);
        return m;
    }

    public static double log(String s, double m) {
        System.out.println(s + ": " + m);
        return m;
    }
}