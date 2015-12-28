package org.waterpicker.paydirtwashplant.util;

/**
 * Created by Waterpician on 12/27/2015.
 */
public class Voltage {
    public static int getVoltage(String voltage) {
        switch (voltage) {
            case "low":
                return 1;
            case "medium":
                return 2;
            case "high":
                return 3;
            default:
                return 1;
        }
    }
}
