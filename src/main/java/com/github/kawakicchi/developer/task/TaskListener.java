package com.github.kawakicchi.developer.task;

public interface TaskListener {

	public void taskStart(final Task task);
	
	public void taskSuccess(final Task task);

	public void taskError(final Task task);
	
	public void taskFinished(final Task task);
}
