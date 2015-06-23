package com.github.kawakicchi.developer.tools.rakrak.analyze.entity;

import java.util.ArrayList;
import java.util.List;

public class DDEntity {

	private String name;
	private List<DDTitleEntity> titles;
	private String dbField;
	private String size;
	private String inputType;

	private String sql;
	private String refWindow;
	private String plugin;

	public DDEntity() {
		name = null;
		titles = new ArrayList<DDTitleEntity>();
		dbField = null;
		size = null;
		inputType = null;
		sql = null;
		refWindow = null;
		plugin = null;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addTitle(final DDTitleEntity title) {
		titles.add(title);
	}

	public List<DDTitleEntity> getTitleList() {
		return titles;
	}

	public void setDbfield(final String dbField) {
		this.dbField = dbField;
	}

	public String getDbfield() {
		return dbField;
	}

	public void setSize(final String size) {
		this.size = size;
	}

	public String getSize() {
		return size;
	}

	public void setInputType(final String value) {
		inputType = value;
	}

	public String getInputType() {
		return inputType;
	}

	public void setSql(final String value) {
		sql = value;
	}
	
	public String getSql() {
		return sql;
	}
	
	public void setRefWindow(final String value) {
		refWindow = value;
	}

	public String getRefWindow() {
		return refWindow;
	}
	
	public void setPlugin(final String value) {
		plugin = value;
	}
	
	public String getPlugin() {
		return plugin;
	}

	public String toString() {
		String ls = "\n";
		try {
			ls = System.getProperty("line.separator");
		} catch (SecurityException e) {
		}

		StringBuilder s = new StringBuilder();
		s.append(String.format("DD : %s", name)).append(ls);
		for (DDTitleEntity title : titles) {
			s.append(String.format("  TITLE : %s %s", title.getLang(), title.getValue())).append(ls);
		}
		s.append(String.format("  DBFIELD : %s", dbField)).append(ls);
		s.append(String.format("  SIZE : %s", size)).append(ls);
		s.append(String.format("  INPUTTYPE : %s", inputType)).append(ls);
		if (null != refWindow) {
			s.append(String.format("  REFWINDOW : %s", refWindow)).append(ls);
		}
		return s.toString();
	}
}
