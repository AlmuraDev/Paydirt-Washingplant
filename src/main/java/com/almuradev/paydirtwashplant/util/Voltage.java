package com.almuradev.paydirtwashplant.util;

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
