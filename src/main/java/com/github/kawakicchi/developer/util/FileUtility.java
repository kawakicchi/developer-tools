package com.github.kawakicchi.developer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtility {

	public static String read(final File file, final String charset) {
		StringBuffer sb = new StringBuffer();
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file), charset);
			char[] buffer = new char[1024];
			int size = -1;
			while (-1 != (size = reader.read(buffer, 0, 1024))) {
				sb.append(buffer, 0, size);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}
}
