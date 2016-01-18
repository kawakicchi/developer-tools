package com.github.kawakicchi.developer.svn.entity;

public class LogPathEntity {
	private String action;
	private String kind;
	private String path;

	public void setAction(final String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setKind(final String kind) {
		this.kind = kind;
	}

	public String getKind() {
		return kind;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
