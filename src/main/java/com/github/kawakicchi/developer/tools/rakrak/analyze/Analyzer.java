package com.github.kawakicchi.developer.tools.rakrak.analyze;

import java.io.File;

/**
 * 
 * @author Kawakicchi
 */
public interface Analyzer {

	/**
	 * 解析を行う。
	 * @param file 対象ファイル
	 * @param destFile 解析結果ファイル
	 */
	public void analyze(final File file, final File destFile);

}
