package com.github.kawakicchi.developer.tools.rakrak.analyze.entity;

import java.util.ArrayList;
import java.util.List;

public class XPDEntity {

	private List<ProgramEntity> programList;

	public XPDEntity() {
		programList = new ArrayList<ProgramEntity>();
	}

	public void add(final ProgramEntity entity) {
		programList.add(entity);
	}

	public List<ProgramEntity> getProgramList() {
		return programList;
	}
}
