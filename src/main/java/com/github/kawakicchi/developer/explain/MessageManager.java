package com.github.kawakicchi.developer.explain;

import java.util.Locale;
import java.util.ResourceBundle;

public final class MessageManager {

	private static final MessageManager INSTANCE = new MessageManager();
	
	private ResourceBundle bundle;
	
	private MessageManager() {
		
	}
	
	public static MessageManager getInstance() {
		return INSTANCE;
	}
	
	public static String get(final String key) {
		return INSTANCE.bundle.getString(key);
	}
	
	public static String format(final String key, final Object...args) {
		String message = INSTANCE.bundle.getString(key);
		return String.format(message, args);
	}
	
	public void load() {
		ResourceBundle.Control control = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT);
		bundle = ResourceBundle.getBundle("com.github.kawakicchi.developer.explain.Message", new Locale("ja", "JP"), control);
	}
}
