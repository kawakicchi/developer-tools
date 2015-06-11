package com.github.kawakicchi.developer.tools;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException, InvalidFormatException {

		App app = new App();
		app.test();
	}

	public App() {
	}

	public void test() throws IOException, InvalidFormatException {
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
