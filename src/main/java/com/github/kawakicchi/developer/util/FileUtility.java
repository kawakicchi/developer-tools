package com.github.kawakicchi.developer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

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
			release(reader);
		}
		return sb.toString();
	}

	public static boolean write(final File file, final String data, final String charset) {
		boolean result = false;
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(file), charset);
			writer.write(data);
			writer.flush();
			result = true;
		} catch (IOException ex) {

		} finally {
			release(writer);
		}
		return result;
	}

	public static void release(final Reader reader) {
		if (null != reader) {
			try {
				reader.close();
			} catch (IOException ex) {

			}
		}
	}

	public static void release(final Writer writer) {
		if (null != writer) {
			try {
				writer.close();
			} catch (IOException ex) {

			}
		}
	}
}
