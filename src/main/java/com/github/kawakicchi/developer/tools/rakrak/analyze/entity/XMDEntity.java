package com.github.kawakicchi.developer.tools.rakrak.analyze.entity;

import java.util.ArrayList;
import java.util.List;

public class XMDEntity {

	private List<MSGEntity> msgList;

	public XMDEntity() {
		msgList = new ArrayList<MSGEntity>();
	}

	public void add(final MSGEntity entity) {
		msgList.add(entity);
	}

	public List<MSGEntity> getMSGList() {
		return msgList;
	}
}
