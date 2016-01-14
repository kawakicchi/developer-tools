package com.github.kawakicchi.developer.tools.rakrak.analyze.entity;

import java.util.ArrayList;
import java.util.List;

public class DDEntity {

	private String name;
	private List<DDTitleEntity> titles;
	private String dbField;
	private String type;
	private String size;
	private Integer length;
	private String inputType;
	private String align;

	private String sql;

	private String refButton;
	private String refWindow;
	private String refTable;
	private String plugin;

	public DDEntity() {
		name = null;
		titles = new ArrayList<DDTitleEntity>();
		dbField = null;
		type = null;
		size = null;
		length = null;
		inputType = null;
		align = null;

		sql = null;

		refButton = null;
		refWindow = null;
		refTable = null;

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

	public String getTitle() {
		String title = null;
		for (DDTitleEntity t : titles) {
			if (null == t.getLang() || 0 == t.getLang().length()) {
				title = t.getValue();
				break;
			}
		}
		if (null == title && 0 < titles.size()) {
			title = titles.get(0).getValue();
		}
		return title;
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

	public void setType(final String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setSize(final String size) {
		this.size = size;
	}

	public String getSize() {
		return size;
	}

	public void setLength(final Integer length) {
		this.length = length;
	}
	
	public Integer getLength() {
		return length;
	}
	
	public void setInputType(final String value) {
		inputType = value;
	}

	public String getInputType() {
		return inputType;
	}

	public void setAlign(final String value) {
		align = value;
	}

	public String getAlign() {
		return align;
	}

	public void setSql(final String value) {
		sql = value;
	}

	public String getSql() {
		return sql;
	}

	public void setRefButtonValue(final String value) {
		refButton = value;
	}

	public String getRefButton() {
		return refButton;
	}

	public boolean isRefButton() {
		if (null != refButton && "Y".equals(refButton.toUpperCase())) {
			return true;
		}
		return false;
	}

	public void setRefWindow(final String value) {
		refWindow = value;
	}

	public String getRefWindow() {
		return refWindow;
	}

	public void setRefTable(final String value) {
		refTable = value;
	}

	public String getRefTable() {
		return refTable;
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
