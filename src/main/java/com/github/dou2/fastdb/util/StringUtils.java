package com.github.dou2.fastdb.util;

public class StringUtils {
	public static int parseInt(String str, int defaultValue) {
		try {
			return Integer.valueOf(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static boolean parseBoolean(String str, boolean defaultValue) {
		try {
			return Boolean.valueOf(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
