package com.github.kawakicchi.developer.task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TaskManager {

	private static final TaskManager INSTANCE = new TaskManager();

	private List<TaskManagerListener> listeners;
	private Thread thread;
	private Queue<TaskThread> taskThreads;
	private boolean stopFlag;

	private TaskThread activeTaskThread;

	private TaskManager() {
		listeners = new ArrayList<TaskManagerListener>();
		
		activeTaskThread = null;
		taskThreads = new LinkedList<TaskThread>();
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!stopFlag) {
					if (null == activeTaskThread) {
						synchronized (taskThreads) {
							activeTaskThread = taskThreads.poll();
							if (null != activeTaskThread) {
								activeTaskThread.addTaskThreadListener(new TaskThread.TaskThreadListener() {
									@Override
									public void taskThreadStart(final Task task) {
										synchronized (listeners) {
											for (TaskManagerListener listener : listeners) {
												listener.taskManagerTaskStart(task);
											}
										}
									}

									@Override
									public void taskThreadFinished(final Task task) {
										activeTaskThread = null;
										synchronized (listeners) {
											for (TaskManagerListener listener : listeners) {
												listener.taskManagerTaskFinished(task);
											}
										}
									}
								});
								
								activeTaskThread.execute();
							}
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}
	
	public static TaskManager getInstance() {
		return INSTANCE;
	}

	public static void queueTask(final Task task) {
		INSTANCE._queueTask(task);
	}
	
	public void addTaskManagerListener(final TaskManagerListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void start() {
		if (!thread.isAlive()) {
			stopFlag = false;
			thread.start();
		}
	}

	public void stop() {
		stopFlag = true;
	}

	public boolean isStop() {
		return (!thread.isAlive() && null == activeTaskThread);
	}

	private void _queueTask(final Task task) {
		synchronized (taskThreads) {
			TaskThread taskThread = new TaskThread(task);
			taskThreads.offer(taskThread);
		}
	}
}
