package com.github.kawakicchi.developer.tools.rakrak.analyze.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProgramPPEntity {

	private static final Pattern PTN_KEY = Pattern.compile("([*~=]{0,1})[ \\t]*([0-9a-z_]+)");

	private String page;
	private String plugin;
	private ProgramOptionEntity option;

	private Set<String> allKeyFields;

	private List<String> keyFields;
	private List<String> keyFields2;
	private List<String> keyFields3;
	private List<String> keyFields4;
	private List<String> listKeyFields;
	private List<String> listKeyFields2;
	private List<String> listKeyFields3;
	private List<String> listKeyFields4;
	private List<String> listFields;
	private List<String> listFields2;
	private List<String> listFields3;
	private List<String> listFields4;

	private List<String> inqPrintFields;
	private List<String> inqPrintFields2;
	private List<String> inqPrintFields3;
	private List<String> inqPrintFields4;
	private List<String> inqHiddenFields;
	private List<String> inqHiddenFields2;
	private List<String> inqHiddenFields3;
	private List<String> inqHiddenFields4;
	private List<String> entInputFields;
	private List<String> entInputFields2;
	private List<String> entInputFields3;
	private List<String> entInputFields4;
	private List<String> entDBFields;
	private List<String> entDBFields2;
	private List<String> entDBFields3;
	private List<String> entDBFields4;
	private List<String> entHiddenFields;
	private List<String> entHiddenFields2;
	private List<String> entHiddenFields3;
	private List<String> entHiddenFields4;
	private List<String> updPrintFields;
	private List<String> updPrintFields2;
	private List<String> updPrintFields3;
	private List<String> updPrintFields4;
	private List<String> updInputFields;
	private List<String> updInputFields2;
	private List<String> updInputFields3;
	private List<String> updInputFields4;
	private List<String> updDBFields;
	private List<String> updDBFields2;
	private List<String> updDBFields3;
	private List<String> updDBFields4;
	private List<String> updHiddenFields;
	private List<String> updHiddenFields2;
	private List<String> updHiddenFields3;
	private List<String> updHiddenFields4;
	private List<String> delPrintFields;
	private List<String> delPrintFields2;
	private List<String> delPrintFields3;
	private List<String> delPrintFields4;
	private List<String> delHiddenFields;
	private List<String> delHiddenFields2;
	private List<String> delHiddenFields3;
	private List<String> delHiddenFields4;
	private List<String> fileInputFields;
	private List<String> fileInputFields2;
	private List<String> fileInputFields3;
	private List<String> fileInputFields4;

	private List<String> rptPrintFields;
	private List<String> rptPrintFields2;
	private List<String> rptPrintFields3;
	private List<String> rptPrintFields4;

	private List<String> hiddenFields;
	private List<String> hiddenFields2;
	private List<String> hiddenFields3;
	private List<String> hiddenFields4;

	public ProgramPPEntity() {
		allKeyFields = new HashSet<String>();

		keyFields = new ArrayList<String>();
		keyFields2 = new ArrayList<String>();
		keyFields3 = new ArrayList<String>();
		keyFields4 = new ArrayList<String>();

		listKeyFields = new ArrayList<String>();
		listKeyFields2 = new ArrayList<String>();
		listKeyFields3 = new ArrayList<String>();
		listKeyFields4 = new ArrayList<String>();

		listFields = new ArrayList<String>();
		listFields2 = new ArrayList<String>();
		listFields3 = new ArrayList<String>();
		listFields4 = new ArrayList<String>();

		inqPrintFields = new ArrayList<String>();
		inqPrintFields2 = new ArrayList<String>();
		inqPrintFields3 = new ArrayList<String>();
		inqPrintFields4 = new ArrayList<String>();

		inqHiddenFields = new ArrayList<String>();
		inqHiddenFields2 = new ArrayList<String>();
		inqHiddenFields3 = new ArrayList<String>();
		inqHiddenFields4 = new ArrayList<String>();

		entInputFields = new ArrayList<String>();
		entInputFields2 = new ArrayList<String>();
		entInputFields3 = new ArrayList<String>();
		entInputFields4 = new ArrayList<String>();

		entDBFields = new ArrayList<String>();
		entDBFields2 = new ArrayList<String>();
		entDBFields3 = new ArrayList<String>();
		entDBFields4 = new ArrayList<String>();

		entHiddenFields = new ArrayList<String>();
		entHiddenFields2 = new ArrayList<String>();
		entHiddenFields3 = new ArrayList<String>();
		entHiddenFields4 = new ArrayList<String>();

		updPrintFields = new ArrayList<String>();
		updPrintFields2 = new ArrayList<String>();
		updPrintFields3 = new ArrayList<String>();
		updPrintFields4 = new ArrayList<String>();

		updInputFields = new ArrayList<String>();
		updInputFields2 = new ArrayList<String>();
		updInputFields3 = new ArrayList<String>();
		updInputFields4 = new ArrayList<String>();

		updDBFields = new ArrayList<String>();
		updDBFields2 = new ArrayList<String>();
		updDBFields3 = new ArrayList<String>();
		updDBFields4 = new ArrayList<String>();

		updHiddenFields = new ArrayList<String>();
		updHiddenFields2 = new ArrayList<String>();
		updHiddenFields3 = new ArrayList<String>();
		updHiddenFields4 = new ArrayList<String>();

		delPrintFields = new ArrayList<String>();
		delPrintFields2 = new ArrayList<String>();
		delPrintFields3 = new ArrayList<String>();
		delPrintFields4 = new ArrayList<String>();

		delHiddenFields = new ArrayList<String>();
		delHiddenFields2 = new ArrayList<String>();
		delHiddenFields3 = new ArrayList<String>();
		delHiddenFields4 = new ArrayList<String>();

		fileInputFields = new ArrayList<String>();
		fileInputFields2 = new ArrayList<String>();
		fileInputFields3 = new ArrayList<String>();
		fileInputFields4 = new ArrayList<String>();

		rptPrintFields = new ArrayList<String>();
		rptPrintFields2 = new ArrayList<String>();
		rptPrintFields3 = new ArrayList<String>();
		rptPrintFields4 = new ArrayList<String>();

		hiddenFields = new ArrayList<String>();
		hiddenFields2 = new ArrayList<String>();
		hiddenFields3 = new ArrayList<String>();
		hiddenFields4 = new ArrayList<String>();
	}

	public void setPage(final String value) {
		page = value;
	}

	public String getPage() {
		return page;
	}

	public void setPlugin(final String value) {
		plugin = value;
	}

	public String getPlugin() {
		return plugin;
	}

	public void setOption(final ProgramOptionEntity value) {
		option = value;
	}

	public ProgramOptionEntity getOption() {
		return option;
	}

	public void setKeyFields(final String value) {
		setConditionKeyFields(value, keyFields);
	}

	public void setKeyFields2(final String value) {
		setConditionKeyFields(value, keyFields2);
	}

	public void setKeyFields3(final String value) {
		setConditionKeyFields(value, keyFields3);
	}

	public void setKeyFields4(final String value) {
		setConditionKeyFields(value, keyFields4);
	}

	public void setListKeyFields(final String value) {
		setKeyList(value, listKeyFields);
	}

	public void setListKeyFields2(final String value) {
		setKeyList(value, listKeyFields2);
	}

	public void setListKeyFields3(final String value) {
		setKeyList(value, listKeyFields3);
	}

	public void setListKeyFields4(final String value) {
		setKeyList(value, listKeyFields4);
	}

	public void setListFields(final String value) {
		setKeyList(value, listFields);
	}

	public void setListFields2(final String value) {
		setKeyList(value, listFields2);
	}

	public void setListFields3(final String value) {
		setKeyList(value, listFields3);
	}

	public void setListFields4(final String value) {
		setKeyList(value, listFields4);
	}

	public void setInqPrintFields(final String value) {
		setKeyList(value, inqPrintFields);
	}

	public void setInqPrintFields2(final String value) {
		setKeyList(value, inqPrintFields2);
	}

	public void setInqPrintFields3(final String value) {
		setKeyList(value, inqPrintFields3);
	}

	public void setInqPrintFields4(final String value) {
		setKeyList(value, inqPrintFields4);
	}

	public void setInqHiddenFields(final String value) {
		setKeyList(value, inqHiddenFields);
	}

	public void setInqHiddenFields2(final String value) {
		setKeyList(value, inqHiddenFields2);
	}

	public void setInqHiddenFields3(final String value) {
		setKeyList(value, inqHiddenFields3);
	}

	public void setInqHiddenFields4(final String value) {
		setKeyList(value, inqHiddenFields4);
	}

	public void setEntInputFields(final String value) {
		setKeyList(value, entInputFields);
	}

	public void setEntInputFields2(final String value) {
		setKeyList(value, entInputFields2);
	}

	public void setEntInputFields3(final String value) {
		setKeyList(value, entInputFields3);
	}

	public void setEntInputFields4(final String value) {
		setKeyList(value, entInputFields4);
	}

	public void setEntDBFields(final String value) {
		setKeyList(value, entDBFields);
	}

	public void setEntDBFields2(final String value) {
		setKeyList(value, entDBFields2);
	}

	public void setEntDBFields3(final String value) {
		setKeyList(value, entDBFields3);
	}

	public void setEntDBFields4(final String value) {
		setKeyList(value, entDBFields4);
	}

	public void setEntHiddenFields(final String value) {
		setKeyList(value, entHiddenFields);
	}

	public void setEntHiddenFields2(final String value) {
		setKeyList(value, entHiddenFields2);
	}

	public void setEntHiddenFields3(final String value) {
		setKeyList(value, entHiddenFields3);
	}

	public void setEntHiddenFields4(final String value) {
		setKeyList(value, entHiddenFields4);
	}

	public void setUpdPrintFields(final String value) {
		setKeyList(value, updPrintFields);
	}

	public void setUpdPrintFields2(final String value) {
		setKeyList(value, updPrintFields2);
	}

	public void setUpdPrintFields3(final String value) {
		setKeyList(value, updPrintFields3);
	}

	public void setUpdPrintFields4(final String value) {
		setKeyList(value, updPrintFields4);
	}

	public void setUpdInputFields(final String value) {
		setKeyList(value, updInputFields);
	}

	public void setUpdInputFields2(final String value) {
		setKeyList(value, updInputFields2);
	}

	public void setUpdInputFields3(final String value) {
		setKeyList(value, updInputFields3);
	}

	public void setUpdInputFields4(final String value) {
		setKeyList(value, updInputFields4);
	}

	public void setUpdDBFields(final String value) {
		setKeyList(value, updDBFields);
	}

	public void setUpdDBFields2(final String value) {
		setKeyList(value, updDBFields2);
	}

	public void setUpdDBFields3(final String value) {
		setKeyList(value, updDBFields3);
	}

	public void setUpdDBFields4(final String value) {
		setKeyList(value, updDBFields4);
	}

	public void setUpdHiddenFields(final String value) {
		setKeyList(value, updHiddenFields);
	}

	public void setUpdHiddenFields2(final String value) {
		setKeyList(value, updHiddenFields2);
	}

	public void setUpdHiddenFields3(final String value) {
		setKeyList(value, updHiddenFields3);
	}

	public void setUpdHiddenFields4(final String value) {
		setKeyList(value, updHiddenFields4);
	}

	public void setDelPrintFields(final String value) {
		setKeyList(value, delPrintFields);
	}

	public void setDelPrintFields2(final String value) {
		setKeyList(value, delPrintFields2);
	}

	public void setDelPrintFields3(final String value) {
		setKeyList(value, delPrintFields3);
	}

	public void setDelPrintFields4(final String value) {
		setKeyList(value, delPrintFields4);
	}

	public void setDelHiddenFields(final String value) {
		setKeyList(value, delHiddenFields);
	}

	public void setDelHiddenFields2(final String value) {
		setKeyList(value, delHiddenFields2);
	}

	public void setDelHiddenFields3(final String value) {
		setKeyList(value, delHiddenFields3);
	}

	public void setDelHiddenFields4(final String value) {
		setKeyList(value, delHiddenFields4);
	}

	public void setFileInputFields(final String value) {
		setKeyList(value, fileInputFields);
	}

	public void setFileInputFields2(final String value) {
		setKeyList(value, fileInputFields2);
	}

	public void setFileInputFields3(final String value) {
		setKeyList(value, fileInputFields3);
	}

	public void setFileInputFields4(final String value) {
		setKeyList(value, fileInputFields4);
	}

	public void setRptPrintFields(final String value) {
		setKeyList(value, rptPrintFields);
	}

	public void setRptPrintFields2(final String value) {
		setKeyList(value, rptPrintFields2);
	}

	public void setRptPrintFields3(final String value) {
		setKeyList(value, rptPrintFields3);
	}

	public void setRptPrintFields4(final String value) {
		setKeyList(value, rptPrintFields4);
	}

	public void setHiddenFields(final String value) {
		setKeyList(value, hiddenFields);
	}

	public void setHiddenFields2(final String value) {
		setKeyList(value, hiddenFields2);
	}

	public void setHiddenFields3(final String value) {
		setKeyList(value, hiddenFields3);
	}

	public void setHiddenFields4(final String value) {
		setKeyList(value, hiddenFields4);
	}

	public boolean isKeyField(final String key) {
		return allKeyFields.contains(key);
	}

	private void setConditionKeyFields(final String value, final List<String> keys) {
		keys.clear();
		if (null != value && 0 < value.length()) {
			for (String conditionKey : value.split("[ ,\\t\\n\\r]{1,}")) {
				Matcher m = PTN_KEY.matcher(conditionKey);
				if (m.find()) {
					System.out.println(String.format("%s : %s", m.group(1), m.group(2)));
					String condition = m.group(1);
					String key = m.group(2);

					keys.add(key);
					allKeyFields.add(key);
				}
			}
		}
	}

	private void setKeyList(final String value, final List<String> keys) {
		keys.clear();
		if (null != value && 0 < value.length()) {
			for (String key : value.split("[ ,\\t\\n\\r]{1,}")) {
				keys.add(key);
				allKeyFields.add(key);
			}
		}
	}
}
