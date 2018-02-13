/*
 * This file is part of Paydirt-Washingplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant.util;

public class Voltage_Tier {

    // Important: https://wiki.industrial-craft.net/index.php?title=Tier_System

    public static int getVoltage(String voltage) {
        switch (voltage.toLowerCase().trim()) {
            case "low":
                return 1;
            case "medium":
                return 2;
            case "high":
                return 3;
            case "extreme":
                return 4;
            case "insane":
                return 5;

            default:
                return 1;
        }
    }
}
