package com.github.kawakicchi.developer.task;

public interface TaskManagerListener {

	public void taskManagerTaskStart(final Task task);
	
	public void taskManagerTaskFinished(final Task task);
}
