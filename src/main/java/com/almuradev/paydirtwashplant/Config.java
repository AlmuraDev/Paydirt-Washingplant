/*
 * This file is part of Paydirt-Washplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.File;

import javax.annotation.Nullable;

public final class Config {

    public static String MINED_ITEM;
    public static String VOLTAGE_TIER;
    public static int EU_BUFFER;
    public static int WATER_BUFFER;
    public static int EU_PER_OPERATION;
    public static int WATER_PER_OPERATION;
    public static double COBBLESTONE_PERCENTAGE;
    public static double GRAVEL_PERCENTAGE;
    public static double DIRT_PERCENTAGE;
    public static int WASH_TIME;

    @Nullable
    private static ItemStack minedItem = null;

    private Config() {
    }

    /**
     * Lazily initialized washed item.
     *
     * @return The washing result
     */
    public static ItemStack minedItem() {
        if (minedItem == null) {
            final int hash = MINED_ITEM.indexOf("#");
            final int meta;
            final String registryName;
            if (hash == -1) {
                meta = 0;
                registryName = MINED_ITEM;
            } else {
                meta = Integer.parseInt(MINED_ITEM.substring(hash + 1));
                registryName = MINED_ITEM.substring(0, hash);
            }
            @Nullable final Item itemInstance = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
            if (itemInstance == null) {
                minedItem = ItemStack.EMPTY;
            } else {
                minedItem = new ItemStack(itemInstance, 1, meta);
            }
        }
        return minedItem;
    }

    public static void setup(File file) {
        Configuration config = new Configuration(file);

        MINED_ITEM = config.get(Configuration.CATEGORY_GENERAL, "mined item", "minecraft:gold_nugget:0").getString();
        VOLTAGE_TIER = config.get(Configuration.CATEGORY_GENERAL, "voltage_tier", "low").getString();
        EU_BUFFER = config.get(Configuration.CATEGORY_GENERAL, "power buffer", 100000).getInt();
        WATER_BUFFER = config.get(Configuration.CATEGORY_GENERAL, "water buffer", 10000).getInt();
        EU_PER_OPERATION = config.get(Configuration.CATEGORY_GENERAL, "eu per operation", 5000).getInt();
        WATER_PER_OPERATION = config.get(Configuration.CATEGORY_GENERAL, "water per operation", 500).getInt();
        COBBLESTONE_PERCENTAGE = config.get(Configuration.CATEGORY_GENERAL, "cobblestone percentage", 0.02).getDouble();
        GRAVEL_PERCENTAGE = config.get(Configuration.CATEGORY_GENERAL, "gravel percentage", 0.05).getDouble();
        DIRT_PERCENTAGE = config.get(Configuration.CATEGORY_GENERAL, "dirt percentage", 0.02).getDouble();
        WASH_TIME = config.get(Configuration.CATEGORY_GENERAL, "wash time", 20).getInt();

        config.save();
    }

}