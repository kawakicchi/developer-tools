package com.github.kawakicchi.developer.tools.rakrak.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServletLog {

	public static void main(final String[] args) {
		File logDir = new File( args[0] );
		File file = Paths.get(logDir.getAbsolutePath(), "trace\\Servlet\\20151228.txt").toFile();
		ServletLog analyze = new ServletLog();
		analyze.analyze(file);
	}

	private static final Pattern PTN_LOG = Pattern
			.compile("^\\[([0-9]{2}:[0-9]{2}:[0-9]{2}) lib21\\] ([+|-])([0-9]+),([0-9]+.[0-9]+.[0-9]+.[0-9]+),([^,]*),([^,]*),([^,]*),([^,]*)(,([^,]*),([^,]*)){0,1}$");

	public void analyze(final File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Windows-31J"));
			String line = null;
			while (null != (line = reader.readLine())) {
				System.out.println(line);
				Matcher m = PTN_LOG.matcher(line);
				if (m.find()) {
					System.out.println(String.format("%s %s %s %s %s %s %s %s %s %s", m.group(1), m.group(2), m.group(3), m.group(4), m.group(5),
							m.group(6), m.group(7), m.group(8), m.group(10), m.group(11)));
					String time = m.group(1);
					String plmi = m.group(2);
					String ip = m.group(4);
					String path = m.group(8);
					if (null != m.group(9)) {
						String ptime = m.group(10);
						String user = m.group(11);
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
