package com.github.kawakicchi.developer.task;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTask implements Task {

	private List<TaskListener> listeners;
	
	private long executeTime;

	public AbstractTask() {
		listeners = new ArrayList<TaskListener>();
		executeTime = -1;
	}

	public void addTaskListener(final TaskListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	/**
	 * 
	 * @return nan sec
	 */
	public long getExecuteTime() {
		return executeTime;
	}
	
	@Override
	public void execute() {
		callTaskStart();

		long start = System.nanoTime();
		try {
			doExecute();

			long end = System.nanoTime();
			executeTime = end - start;

			callTaskSuccess();
		} catch (Exception ex) {

			long end = System.nanoTime();
			executeTime = end - start;

			callTaskError();
		}
		callTaskFinished();
	}

	protected abstract void doExecute();

	private final void callTaskStart() {
		synchronized (listeners) {
			for (TaskListener listener : listeners) {
				listener.taskStart(this);
			}
		}
	}

	private final void callTaskSuccess() {
		synchronized (listeners) {
			for (TaskListener listener : listeners) {
				listener.taskSuccess(this);
			}
		}
	}

	private final void callTaskError() {
		synchronized (listeners) {
			for (TaskListener listener : listeners) {
				listener.taskError(this);
			}
		}
	}

	private final void callTaskFinished() {
		synchronized (listeners) {
			for (TaskListener listener : listeners) {
				listener.taskFinished(this);
			}
		}
	}
}
