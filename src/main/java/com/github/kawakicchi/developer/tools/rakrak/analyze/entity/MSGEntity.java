package com.github.kawakicchi.developer.tools.rakrak.analyze.entity;

public class MSGEntity {

	private String id;
	private String mode;
	private String lang;
	private String value;

	public void setId(final String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	public void setLang(final String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public String toString() {
		String ls = "\n";
		try {
			ls = System.getProperty("line.separator");
		} catch (SecurityException e) {
		}

		StringBuilder s = new StringBuilder();
		s.append(String.format("MSG : %s", value)).append(ls);
		s.append(String.format("  ID : %s", id)).append(ls);
		s.append(String.format("  MODE : %s", mode)).append(ls);
		s.append(String.format("  LANG : %s", lang)).append(ls);
		return s.toString();
	}

}
