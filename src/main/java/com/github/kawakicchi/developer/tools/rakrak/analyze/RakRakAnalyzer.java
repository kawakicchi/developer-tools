package com.github.kawakicchi.developer.tools.rakrak.analyze;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class RakRakAnalyzer extends AbstractRakRakAnalyzer {

	public static void main(final String[] args) {
		String srcFilePath = args[0];
		String destFilePath = args[1];

		Calendar cln = Calendar.getInstance();
		cln.setTime(new Date());

		destFilePath = addSuffixName(destFilePath,
				String.format("-%04d%02d%02d", cln.get(Calendar.YEAR), cln.get(Calendar.MONTH) + 1, cln.get(Calendar.DAY_OF_MONTH)));

		File srcFile = new File(srcFilePath);
		File destFile = new File(destFilePath);

		RakRakAnalyzer ana = new RakRakAnalyzer();
		ana.analyze(srcFile, destFile);
	}

	protected boolean isIgnoreXPDFile(final String name) {
		if ("PmsXPD.xml".equals(name)) {
			return true;
		}
		return false;
	}

	private static String addSuffixName(final String name, final String add) {
		String buf = name;
		int index = buf.indexOf(".");
		if (-1 == index) {
			buf += add;
		} else {
			buf = buf.substring(0, index) + add + buf.substring(index);
		}
		return buf;
	}
}
