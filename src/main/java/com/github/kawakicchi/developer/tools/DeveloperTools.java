/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.kawakicchi.developer.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author Kawakicchi
 *
 */
public class DeveloperTools {

	public static void main(final String[] args) {

		DeveloperTools tools = new DeveloperTools();
		tools.execute();

	}

	public DeveloperTools() {

	}

	public void execute() {
		String shellTemplate = read(new File("./src/test/data/sample.sh"), Charset.forName("UTF-8"));
		String sqlTemplate = read(new File("./src/test/data/sample.sql"), Charset.forName("Windows-31J"));
	}

	private String read(final File file, final Charset charset) {
		StringBuilder s = new StringBuilder();
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file), charset);
			char buffer[] = new char[1024];
			int read = -1;
			while (-1 != (read = reader.read(buffer, 0, 1024))) {
				s.append(buffer, 0, read);
			}
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return s.toString();
	}

	private List<Map<String, Object>> readListFile(final File file) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		try {
			Workbook workbook = WorkbookFactory.create(new File("./src/test/data/list.xls"));
			// Workbook workbook = WorkbookFactory.create(new
			// File("./src/test/data/list.xlsx"));

			int sheetSize = workbook.getNumberOfSheets();
			System.out.println(String.format("SheetSize : %d", sheetSize));

			Sheet sheet = workbook.getSheetAt(0);
			System.out.println(String.format("SheetName : %s", sheet.getSheetName()));

			for (int rowNum = sheet.getFirstRowNum(); rowNum <= sheet.getLastRowNum(); rowNum++) {
				System.out.println(String.format("  Row : %d", rowNum));
				Row row = sheet.getRow(rowNum);
				if (null != row) {
					for (int colNum = row.getFirstCellNum(); colNum <= row.getLastCellNum(); colNum++) {
						System.out.println(String.format("    Col : %d", colNum));

						Cell cell = row.getCell(colNum);
						if (null != cell) {
							System.out.println(String.format("         %s", getStringValue(cell)));
						}
					}
				}
			}
		} catch (InvalidFormatException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return data;
	}

	private Cell getCell(Sheet sheet, int rowIndex, int columnIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row != null) {
			Cell cell = row.getCell(columnIndex);
			return cell;
		}
		return null;
	}

	private String getStringValue(final Cell cell) {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			return Double.toString(cell.getNumericCellValue());
		case Cell.CELL_TYPE_BOOLEAN:
			return Boolean.toString(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_FORMULA:
			// return cell.getCellFormula();
			return getStringFormulaValue(cell);
		case Cell.CELL_TYPE_BLANK:
			return getStringRangeValue(cell);
		default:
			System.out.println(cell.getCellType());
			return null;
		}
	}

	// セルの数式を計算し、Stringとして取得する例
	private String getStringFormulaValue(final Cell cell) {
		assert cell.getCellType() == Cell.CELL_TYPE_FORMULA;

		Workbook book = cell.getSheet().getWorkbook();
		CreationHelper helper = book.getCreationHelper();
		FormulaEvaluator evaluator = helper.createFormulaEvaluator();
		CellValue value = evaluator.evaluate(cell);
		switch (value.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return value.getStringValue();
		case Cell.CELL_TYPE_NUMERIC:
			return Double.toString(value.getNumberValue());
		case Cell.CELL_TYPE_BOOLEAN:
			return Boolean.toString(value.getBooleanValue());
		default:
			System.out.println(value.getCellType());
			return null;
		}
	}

	// 結合セルの値をStringとして取得する例
	private String getStringRangeValue(Cell cell) {
		int rowIndex = cell.getRowIndex();
		int columnIndex = cell.getColumnIndex();

		Sheet sheet = cell.getSheet();
		int size = sheet.getNumMergedRegions();
		for (int i = 0; i < size; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			if (range.isInRange(rowIndex, columnIndex)) {
				Cell firstCell = getCell(sheet, range.getFirstRow(), range.getFirstColumn()); // 左上のセルを取得
				return getStringValue(firstCell);
			}
		}
		return null;
	}

	private Date getDateValue(Cell cell) {
		// if (DateUtil.isCellDateFormatted(cell)) {
		double value = cell.getNumericCellValue();
		return DateUtil.getJavaDate(value);
		// }
		// return null;
	}
}
