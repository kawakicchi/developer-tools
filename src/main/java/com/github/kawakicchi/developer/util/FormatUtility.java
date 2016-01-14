package com.github.kawakicchi.developer.util;

public class FormatUtility {

	public static String nanoTimeToString(final long time) {
		if (time < 1000000000) {
			return String.format("%.0f ms", (double) (time) / 1000000.f);
		} else {
			return String.format("%.2fs", (double) (time) / 1000000000.f);
		}
	}
}
