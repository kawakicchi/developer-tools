package com.github.kawakicchi.developer.explain;

import java.util.Locale;
import java.util.ResourceBundle;

public final class LabelManager {

	private static final LabelManager INSTANCE = new LabelManager();
	
	private ResourceBundle bundle;
	
	private LabelManager() {
		
	}
	
	public static LabelManager getInstance() {
		return INSTANCE;
	}
	
	public static String get(final String key) {
		return INSTANCE.bundle.getString(key);
	}
	
	public void load() {
		ResourceBundle.Control control = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT);
		bundle = ResourceBundle.getBundle("com.github.kawakicchi.developer.explain.Label", new Locale("ja", "JP"), control);
	}
}
