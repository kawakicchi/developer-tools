package com.github.kawakicchi.developer.grep;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Grep {

	public static void main(final String[] args) throws Exception {
		Grep grep = new Grep();

		grep.start();
		grep.start();
		grep.start();

		System.out.println("wait");
		grep.waitFor();

		System.out.println("end");
	}

	private List<GrepListener> listeners;

	private Boolean runningFlag;
	private Boolean stopFlag;

	private Thread threadMain;
	private Thread threadSearch;
	private List<Thread> threadGreps;

	private Queue<File> targetFiles;

	public Grep() {
		listeners = new ArrayList<GrepListener>();

		runningFlag = Boolean.FALSE;
		stopFlag = Boolean.FALSE;
	}

	public void addGrepListener(final GrepListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public boolean start() {
		boolean result = false;

		synchronized (runningFlag) {
			if (!runningFlag) {
				System.out.println("start.");
				runningFlag = Boolean.TRUE;
				stopFlag = Boolean.FALSE;

				threadMain = new Thread(new Runnable() {
					@Override
					public void run() {
						doMain();
					}
				});
				threadMain.start();
				result = true;
			} else {
				System.out.println("already start.");

			}
		}

		return result;
	}

	private boolean isStop() {
		if (threadSearch.isAlive())
			return false;
		for (Thread thread : threadGreps) {
			if (thread.isAlive())
				return false;
		}

		return true;
	}

	private boolean isSearchStop() {
		if (stopFlag)
			return true;

		return false;
	}

	private boolean isGrepStop() {
		if (stopFlag)
			return true;

		if (!threadSearch.isAlive()) {
			synchronized (targetFiles) {
				if (0 == targetFiles.size()) {
					return true;
				}
			}
		}

		return false;
	}

	public void stop() {
		synchronized (runningFlag) {
			stopFlag = Boolean.TRUE;
		}
	}

	public void waitFor() {
		while (runningFlag) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void findFile(final File file) {
		synchronized (targetFiles) {
			System.out.println("Find : " + file.getAbsolutePath());
			targetFiles.offer(file);
		}
	}

	private File getGrepFile() {
		File file = null;
		synchronized (targetFiles) {
			file = targetFiles.poll();
			if (null != file) {
				System.out.println("Grep : " + file.getAbsolutePath());
			}
		}
		return file;
	}

	private void doMain() {
		targetFiles = new LinkedList<File>();

		// Search thread
		threadSearch = new Thread(new SearchThread(this));
		threadSearch.start();
		// Grep thread
		threadGreps = new ArrayList<Thread>();
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new GrepThread(this));
			threadGreps.add(thread);
		}
		for (Thread thread : threadGreps) {
			thread.start();
		}

		// main loop
		while (!isStop()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		runningFlag = Boolean.FALSE;
	}

	private static class SearchThread implements Runnable {

		private Grep grep;

		public SearchThread(final Grep parent) {
			grep = parent;
		}

		public void run() {
			File file = new File(".\\");
			doDirectory(file);
		}

		private boolean doFile(final File file) {
			if (grep.isSearchStop()) {
				return false;
			}
			grep.findFile(file);
			return true;
		}

		private boolean doDirectory(final File directory) {
			if (grep.isSearchStop()) {
				return false;
			}

			File[] files = directory.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					if (!doDirectory(file)) {
						return false;
					}
				} else if (file.isFile()) {
					if (!doFile(file)) {
						return false;
					}
				}
			}
			return true;
		}
	}

	private static class GrepThread implements Runnable {

		private Grep grep;

		public GrepThread(final Grep parent) {
			grep = parent;
		}

		public void run() {
			while (!grep.isGrepStop()) {
				File file = grep.getGrepFile();
				if (null != file) {
					// TODO: grep
				} else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			}
		}

	}
}
