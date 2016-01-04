package com.github.kawakicchi.developer.tools.rakrak.analyze;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Kawakicchi
 */
public abstract class AbstractAnalyzer implements Analyzer {

	@Override
	public final void analyze(final File file, final File destFile) {
		doAnalyze(file, destFile);
	}

	protected abstract void doAnalyze(final File file, final File destFile);

	protected static final List<String> split(final String string) {
		List<String> result = new ArrayList<String>();
		if (null != string) {
			String[] split = string.split("[,\\s\\t]+");
			for (String s : split) {
				String buffer = s.trim();
				if (0 < buffer.length()) {
					result.add(buffer);
				}
			}
		}
		return result;
	}

	protected static final String s(final String string) {
		String s = "";
		if (null != string) {
			s = string;
		}
		return s;
	}
}
