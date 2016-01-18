package com.github.kawakicchi.developer.svn.entity;

import java.util.ArrayList;
import java.util.List;

public class LogEntryEntity {

	private List<LogPathEntity> paths;

	public LogEntryEntity() {
		paths = new ArrayList<LogPathEntity>();
	}

	public void addPath(final LogPathEntity path) {
		paths.add(path);
	}

	public List<LogPathEntity> getPaths() {
		return paths;
	}
}
