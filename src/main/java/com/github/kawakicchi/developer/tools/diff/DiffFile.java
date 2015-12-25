package com.github.kawakicchi.developer.tools.diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DiffFile {

	public DiffFile() {

	}

	public void diff(final List<DiffTarget> targets, final DiffOption option, final File destFile) {
		Workbook wb = new XSSFWorkbook();

		for (DiffTarget target : targets) {
			Sheet sheet = wb.createSheet(target.getName());
			diff(sheet, target, option);
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(destFile);
			wb.write(out);
		} catch (IOException e) {
			System.out.println(e.toString());
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}

	public void diff(final Sheet sheet, final DiffTarget target, final DiffOption option) {
		List<File> srcFiles1 = new ArrayList<File>();
		List<File> srcFiles2 = new ArrayList<File>();
		file(target.getSource1(), srcFiles1);
		file(target.getSource2(), srcFiles2);
		Collections.sort(srcFiles1, new FileComparator());
		Collections.sort(srcFiles2, new FileComparator());

		DiffFileFilter fileFilter = target.getFileFilter();

		Charset charset = Charset.forName("SJIS");
		Row rowHeader = sheet.createRow(0);
		rowHeader.createCell(0).setCellValue("判定");
		rowHeader.createCell(1).setCellValue("ディレクトリパス");
		rowHeader.createCell(2).setCellValue("ファイル名[UT]");
		rowHeader.createCell(3).setCellValue("ファイル名[ST]");
		rowHeader.createCell(4).setCellValue("差異行番号");
		rowHeader.createCell(5).setCellValue("差異文字列[UT]");
		rowHeader.createCell(6).setCellValue("差異文字列[ST]");

		int rowNo = 0;
		int index1 = 0;
		int index2 = 0;
		while (index1 < srcFiles1.size() || index2 < srcFiles2.size()) {
			rowNo++;
			Row row = sheet.createRow(rowNo);

			File file1 = null;
			File file2 = null;
			if (index1 < srcFiles1.size()) {
				file1 = srcFiles1.get(index1);
			}
			if (index2 < srcFiles2.size()) {
				file2 = srcFiles2.get(index2);
			}

			File file = null;
			File dir = null;
			if (null != file1 && null != file2) {
				String path1 = file1.getAbsolutePath().substring((int) target.getSource1().getAbsolutePath().length());
				String path2 = file2.getAbsolutePath().substring((int) target.getSource2().getAbsolutePath().length());
				if (0 >= path1.compareTo(path2)) {
					file = file1;
					dir = target.getSource1();
				} else {
					file = file2;
					dir = target.getSource2();
				}
			} else {
				file = (null != file1) ? file1 : file2;
				dir = (null != file1) ? target.getSource1() : target.getSource2();
			}

			String baseDir = file.getAbsolutePath().substring(dir.getAbsolutePath().length(), file.getAbsolutePath().length() - file.getName().length());
			String fileName1 = "";
			String fileName2 = "";
			boolean match = false;

			DiffInfo diff = null;
			if (null != file1 && null != file2) {

				String path1 = file1.getAbsolutePath().substring((int) target.getSource1().getAbsolutePath().length());
				String path2 = file2.getAbsolutePath().substring((int) target.getSource2().getAbsolutePath().length());

				int cmp = path1.compareTo(path2);
				if (0 == cmp) {
					index1++;
					index2++;
					fileName1 = file1.getName();
					fileName2 = file2.getName();

					if (null == fileFilter || !fileFilter.excluded(target, file1)) {
						diff = diff(target, file1, file2, charset, option);
						match = (0 == diff.getDiffLineList().size());
					} else {
						match = true;
					}

				} else if (0 > cmp) {
					index1++;
					fileName1 = file1.getName();
				} else if (0 < cmp) {
					index2++;
					fileName2 = file2.getName();
				}
			} else if (null == file1) {
				index2++;
				fileName2 = file2.getName();
			} else if (null == file2) {
				index1++;
				fileName1 = file1.getName();
			}

			if (match) {
				row.createCell(0).setCellValue("○");
			} else {
				row.createCell(0).setCellValue("×");
			}

			row.createCell(1).setCellValue(baseDir);
			row.createCell(2).setCellValue(fileName1);
			row.createCell(3).setCellValue(fileName2);

			if (null != diff) {
				List<DiffLineInfo> lines = diff.getDiffLineList();
				if (1 <= lines.size()) {
					DiffLineInfo line = lines.get(0);
					row.createCell(4).setCellValue(line.getNo());
					row.createCell(5).setCellValue(line.getLine1());
					row.createCell(6).setCellValue(line.getLine2());
				}
				for (int i = 1; i < lines.size(); i++) {
					rowNo++;
					row = sheet.createRow(rowNo);
					DiffLineInfo line = lines.get(i);
					row.createCell(4).setCellValue(line.getNo());
					row.createCell(5).setCellValue(line.getLine1());
					row.createCell(6).setCellValue(line.getLine2());
				}
			}
		}

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
	}

	private DiffInfo diff(final DiffTarget target, final File aFile1, final File aFile2, final Charset aCharset, final DiffOption option) {
		DiffInfo diff = new DiffInfo();

		BufferedReader reader1 = null;
		BufferedReader reader2 = null;
		try {
			reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(aFile1), aCharset));
			reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(aFile2), aCharset));

			DiffLineFilter filter1 = target.getLineFilter1();
			DiffLineFilter filter2 = target.getLineFilter2();

			String orgLine1 = "";
			String orgLine2 = "";
			String fltLine1 = "";
			String fltLine2 = "";
			int no = 0;
			while (true) {
				if (null != orgLine1) {
					orgLine1 = reader1.readLine();
					fltLine1 = orgLine1;
					if (null != filter1) {
						fltLine1 = filter1.replace(target, aFile1, orgLine1);
					}
					if (option.isSkipEmptyLine()) {
						// TODO:
					}
				}
				if (null != orgLine2) {
					orgLine2 = reader2.readLine();
					fltLine2 = orgLine2;
					if (null != filter2) {
						fltLine2 = filter2.replace(target, aFile2, orgLine2);
					}
					if (option.isSkipEmptyLine()) {
						// TODO:
					}
				}
				if (null == fltLine1 && null == fltLine2) {
					break;
				}

				if (null != fltLine1 && null != fltLine2) {
					if (!fltLine1.equals(fltLine2)) {
						diff.add(no, orgLine1, orgLine2);
					}
				} else {
					diff.add(no, orgLine1, orgLine2);
				}
				no++;
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (null != reader1) {
				try {
					reader1.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (null != reader2) {
				try {
					reader2.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		return diff;
	}

	private String read(final File aFile, final Charset aCharset) {
		StringBuilder sb = new StringBuilder();

		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(aFile), aCharset);

			char[] buffer = new char[1024];
			int size = -1;
			while (-1 != (size = reader.read(buffer, 0, 1024))) {
				sb.append(buffer, 0, size);
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
		return sb.toString();
	}

	private void file(final File aFile, final List<File> aFiles) {
		if (aFile.isFile()) {
			aFiles.add(aFile);
		} else {
			dir(aFile, aFiles);
		}
	}

	private void dir(final File aDir, final List<File> aFiles) {
		// 無視するディレクトリ
		if (-1 != aDir.getAbsolutePath().indexOf(".svn")) {
			return;
		}
		if (-1 != aDir.getAbsolutePath().indexOf(".settings")) {
			return;
		}
		if (-1 != aDir.getAbsolutePath().indexOf("target")) {
			return;
		}

		File[] files = aDir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				aFiles.add(file);
			} else {
				dir(file, aFiles);
			}
		}
	}

	public static class DiffTarget {
		private String name;
		private File src1;
		private File src2;
		private DiffFileFilter fileFilter;
		private DiffLineFilter lineFilter1;
		private DiffLineFilter lineFilter2;

		public DiffTarget(final String name, final File src1, final File src2) {
			this.name = name;
			this.src1 = src1;
			this.src2 = src2;
		}

		public String getName() {
			return name;
		}

		public File getSource1() {
			return src1;
		}

		public File getSource2() {
			return src2;
		}

		public void setFileFilter(final DiffFileFilter filter) {
			fileFilter = filter;
		}

		public DiffFileFilter getFileFilter() {
			return fileFilter;
		}

		public void setLineFilter1(final DiffLineFilter filter) {
			lineFilter1 = filter;
		}

		public void setLineFilter2(final DiffLineFilter filter) {
			lineFilter2 = filter;
		}

		public DiffLineFilter getLineFilter1() {
			return lineFilter1;
		}

		public DiffLineFilter getLineFilter2() {
			return lineFilter2;
		}
	}

	public static interface DiffLineFilter {
		public String replace(final DiffTarget target, final File file, final String line);
	}

	public static interface DiffFileFilter {
		public boolean excluded(final DiffTarget target, final File file);
	}

	public static class DiffOption {

		private boolean skipEmptyLine;

		public DiffOption() {
			skipEmptyLine = false;
		}

		public void setSkipEmptyLine(final boolean aFlag) {
			skipEmptyLine = aFlag;
		}

		public boolean isSkipEmptyLine() {
			return skipEmptyLine;
		}

		// exclude
	}

	private class DiffInfo {
		private List<DiffLineInfo> diffLine;

		public DiffInfo() {
			diffLine = new ArrayList<DiffFile.DiffLineInfo>();
		}

		public void add(final int no, final String line1, final String line2) {
			diffLine.add(new DiffLineInfo(no, line1, line2));
		}

		public List<DiffLineInfo> getDiffLineList() {
			return diffLine;
		}
	}

	private class DiffLineInfo {
		private int no;
		private String line1;
		private String line2;

		public DiffLineInfo(final int no, final String line1, final String line2) {
			this.no = no;
			this.line1 = line1;
			this.line2 = line2;
		}

		public int getNo() {
			return no;
		}

		public String getLine1() {
			return line1;
		}

		public String getLine2() {
			return line2;
		}
	}

	private class FileComparator implements Comparator<File> {
		@Override
		public int compare(final File arg0, final File arg1) {
			String path1 = arg0.getAbsolutePath();
			String path2 = arg1.getAbsolutePath();
			return path1.compareTo(path2);
		}

	}

}
