package com.github.kawakicchi.developer.svn.entity;

public class LogEntity {

	private LogEntryEntity logEntry;

	public LogEntity() {

	}

	public void setLogEntry(final LogEntryEntity logEntry) {
		this.logEntry = logEntry;
	}

	public LogEntryEntity getLogEntry() {
		return this.logEntry;
	}
}
