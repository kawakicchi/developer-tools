package com.github.kawakicchi.developer.tools.rakrak.analyze.entity;

import java.util.ArrayList;
import java.util.List;

public class ProgramOptionEntity {

	private List<ProgramOptionParamEntity> paramList;

	public ProgramOptionEntity() {
		paramList = new ArrayList<ProgramOptionParamEntity>();
	}

	public void addParam(final ProgramOptionParamEntity param) {
		paramList.add(param);
	}

	public List<ProgramOptionParamEntity> getParamList() {
		return paramList;
	}
}
