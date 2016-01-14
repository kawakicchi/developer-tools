package com.github.kawakicchi.developer.task;

import java.util.ArrayList;
import java.util.List;

public class TaskThread {

	private List<TaskThreadListener> listeners;
	private Task task;

	public TaskThread(final Task task) {
		listeners = new ArrayList<TaskThread.TaskThreadListener>();
		this.task = task;
	}

	public void addTaskThreadListener(final TaskThreadListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void execute() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (listeners) {
					for (TaskThreadListener listener : listeners) {
						listener.taskThreadStart(task);
					}
				}

				try {
					task.execute();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				synchronized (listeners) {
					for (TaskThreadListener listener : listeners) {
						listener.taskThreadFinished(task);
					}
				}
			}
		});
		thread.start();
	}

	public static interface TaskThreadListener {

		public void taskThreadStart(final Task task);

		public void taskThreadFinished(final Task task);
	}
}
