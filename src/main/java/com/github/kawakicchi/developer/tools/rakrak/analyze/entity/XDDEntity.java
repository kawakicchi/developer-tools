package com.github.kawakicchi.developer.tools.rakrak.analyze.entity;

import java.util.ArrayList;
import java.util.List;

public class XDDEntity {

	private List<DDEntity> ddList;

	public XDDEntity() {
		ddList = new ArrayList<DDEntity>();
	}

	public void add(final DDEntity entity) {
		ddList.add(entity);
	}

	public List<DDEntity> getDDList() {
		return ddList;
	}
}
