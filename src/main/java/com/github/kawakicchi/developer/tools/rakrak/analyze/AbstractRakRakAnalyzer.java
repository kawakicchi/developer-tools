package com.github.kawakicchi.developer.tools.rakrak.analyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.digester.BeanPropertySetterRule;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ObjectCreateRule;
import org.apache.commons.digester.SetNextRule;
import org.apache.commons.digester.SetPropertiesRule;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.xml.sax.SAXException;

import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.DDEntity;
import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.DDTitleEntity;
import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.MSGEntity;
import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.ProgramEntity;
import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.ProgramOptionEntity;
import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.ProgramOptionParamEntity;
import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.ProgramPPEntity;
import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.ProgramPatternEntity;
import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.ProgramTitleEntity;
import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.XDDEntity;
import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.XMDEntity;
import com.github.kawakicchi.developer.tools.rakrak.analyze.entity.XPDEntity;

public abstract class AbstractRakRakAnalyzer {

	public final void analyze(final File file, final File destFile) {
		doAnalyze(file, destFile);
	}

	protected boolean isIgnoreXPDFile(final String name) {
		return false;
	}

	protected boolean isIgnoreXWDFile(final String name) {
		return false;
	}

	protected boolean isIgnoreXMDFile(final String name) {
		return false;
	}

	protected boolean isIgnoreXDDFile(final String name) {
		return false;
	}

	protected void doAnalyze(final File file, final File destFile) {
		SystemProperties sp = readSystemProperties(file, "MS932");

		File defDir = file.getParentFile();

		// XMD
		List<MSGEntity> msgList = new ArrayList<MSGEntity>();
		for (String xmdName : sp.getXMDList()) {
			File xmdFile = Paths.get(defDir.getAbsolutePath(), xmdName).toFile();
			if (xmdFile.isFile()) {
				XMDEntity xmd = readXMD(xmdFile);
				if (null != xmd) {
					for (MSGEntity msg : xmd.getMSGList()) {
						msgList.add(msg);
					}
				}
			} else {
				System.out.println(String.format("Not Found XMD file.[%s]", xmdFile.getAbsoluteFile()));
			}
		}
		sortXMD(msgList);

		// XDD
		List<DDEntity> ddList = new ArrayList<DDEntity>();
		for (String xddName : sp.getXDDList()) {
			File xddFile = Paths.get(defDir.getAbsolutePath(), xddName).toFile();
			if (xddFile.isFile()) {
				XDDEntity xdd = readXDD(xddFile);
				if (null != xdd) {
					for (DDEntity dd : xdd.getDDList()) {
						ddList.add(dd);
					}
				}
			} else {
				System.out.println(String.format("Not Found XDD file.[%s]", xddFile.getAbsoluteFile()));
			}
		}
		sortXDD(ddList);

		// XPD
		List<ProgramEntity> programPList = new ArrayList<ProgramEntity>();
		for (String xpdName : sp.getXPDList()) {
			File xpdFile = Paths.get(defDir.getAbsolutePath(), xpdName).toFile();
			if (xpdFile.isFile()) {
				XPDEntity xpd = readXPD(xpdFile);
				if (null != xpd) {
					for (ProgramEntity prg : xpd.getProgramList()) {
						programPList.add(prg);
					}
				}
			} else {
				System.out.println(String.format("Not Found XPD file.[%s]", xpdFile.getAbsoluteFile()));
			}
		}
		sortXPD(programPList);

		// XWD
		List<ProgramEntity> programWList = new ArrayList<ProgramEntity>();
		for (String xwdName : sp.getXWDList()) {
			File xwdFile = Paths.get(defDir.getAbsolutePath(), xwdName).toFile();
			if (xwdFile.isFile()) {
				XPDEntity xpd = readXPD(xwdFile);
				if (null != xpd) {
					for (ProgramEntity prg : xpd.getProgramList()) {
						programWList.add(prg);
					}
				}
			} else {
				System.out.println(String.format("Not Found XWD file.[%s]", xwdFile.getAbsoluteFile()));
			}
		}
		sortXWD(programWList);

		Pattern ptn = Pattern.compile("dd\\.([a-z0-9_]+)\\.window\\.([a-z0-9_]+)");
		for (ProgramEntity prg : programPList) {
			for (ProgramPPEntity pp : prg.getPPList()) {
				ProgramOptionEntity option = pp.getOption();
				if (null != option) {
					for (ProgramOptionParamEntity param : pp.getOption().getParamList()) {
						Matcher m = ptn.matcher(param.getName());
						if (m.matches()) {
							// System.out.println(String.format("%s : %s", m.group(1), m.group(2)));
						}
					}
				}
			}
		}

		// Workbook write
		Workbook workbook = new HSSFWorkbook();
		writeSheetMSGList(msgList, workbook.createSheet("メッセージ一覧"));
		writeSheetDDList(ddList, workbook.createSheet("DD一覧"));
		writeSheetProgramList(programPList, workbook.createSheet("画面一覧"));
		writeSheetProgramList(programWList, workbook.createSheet("ポップアップ一覧"));
		writeSheetDDProgramList(ddList, programPList, programWList, workbook.createSheet("DD - Program"), workbook);

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(destFile);
			workbook.write(out);
		} catch (IOException e) {
			System.out.println(e.toString());
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
	
	private void writeSheetDDProgramList(final List<DDEntity> listDD, final List<ProgramEntity> listPGP, final List<ProgramEntity> listPGW, final Sheet sheet,
			final Workbook workbook) {
		CellStyle cellStylePGName = workbook.createCellStyle();
		cellStylePGName.setRotation((short) -90);

		List<ProgramEntity> listPG = new ArrayList<ProgramEntity>();
		listPG.addAll(listPGP);
		listPG.addAll(listPGW);

		final int colSize = 4 + listPG.size();

		// header
		Row headRow = sheet.createRow(0);
		int colNum = 0;
		headRow.createCell(colNum++).setCellValue("No");
		headRow.createCell(colNum++).setCellValue("DD");
		headRow.createCell(colNum++).setCellValue("Name");
		headRow.createCell(colNum++).setCellValue("");
		for (ProgramEntity prg : listPG) {
			Cell cell = headRow.createCell(colNum++);
			cell.setCellStyle(cellStylePGName);
			cell.setCellValue(prg.getName());
		}

		// Data
		String startCol = CellReference.convertNumToColString(4);
		String endCol = CellReference.convertNumToColString(4 - 1 + listPG.size());
		int rowNum = 1;
		for (DDEntity dd : listDD) {
			Row dataRow = sheet.createRow(rowNum);
			dataRow.createCell(0).setCellValue(rowNum);
			dataRow.createCell(1).setCellValue(dd.getName());
			dataRow.createCell(2).setCellValue(dd.getTitle());
			dataRow.createCell(3).setCellFormula(String.format("COUNTA(%s%d:%s%d)", startCol, rowNum + 1, endCol, rowNum + 1));

			colNum = 4;
			for (ProgramEntity prg : listPG) {
				Cell cell = dataRow.createCell(colNum);
				boolean find = false;
				for (ProgramPPEntity pp : prg.getPPList()) {
					if (pp.isKeyField(dd.getName())) {
						find = true;
						break;
					}
				}
				if (find) {
					cell.setCellValue("●");
				}
				colNum++;
			}

			rowNum++;
		}

		// ウインドウ固定
		sheet.createFreezePane(4, 1);
		// 幅調整
		for (int i = 0; i < colSize; i++) {
			sheet.autoSizeColumn(i);
		}
	}

	private void writeSheetMSGList(final List<MSGEntity> list, final Sheet sheet) {
		// header
		Row headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("No");
		headRow.createCell(1).setCellValue("Id");
		headRow.createCell(2).setCellValue("Lang");
		headRow.createCell(3).setCellValue("Mode");
		headRow.createCell(4).setCellValue("Message");
		// data
		int row = 1;
		for (MSGEntity msg : list) {
			Row dataRow = sheet.createRow(row);
			dataRow.createCell(0).setCellValue(String.format("%d", row));
			dataRow.createCell(1).setCellValue(msg.getId());
			dataRow.createCell(2).setCellValue(msg.getLang());
			dataRow.createCell(3).setCellValue(msg.getMode());
			dataRow.createCell(4).setCellValue(msg.getValue());
			row++;
		}

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
	}

	private void writeSheetDDList(final List<DDEntity> list, final Sheet sheet) {
		// header
		Row headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("No");
		headRow.createCell(1).setCellValue("Name");
		headRow.createCell(2).setCellValue("Title(def)");
		headRow.createCell(3).setCellValue("DB Field");
		headRow.createCell(4).setCellValue("Type");
		headRow.createCell(5).setCellValue("Size");
		headRow.createCell(6).setCellValue("Align");
		headRow.createCell(7).setCellValue("Ref Button");
		headRow.createCell(8).setCellValue("Ref Window");
		headRow.createCell(9).setCellValue("Ref Table");
		// data
		int row = 1;
		for (DDEntity dd : list) {
			Row dataRow = sheet.createRow(row);
			dataRow.createCell(0).setCellValue(s(String.format("%d", row)));
			dataRow.createCell(1).setCellValue(s(dd.getName()));
			dataRow.createCell(2).setCellValue(s(dd.getTitle()));
			dataRow.createCell(3).setCellValue(s(dd.getDbfield()));
			dataRow.createCell(4).setCellValue(s(dd.getType()));
			dataRow.createCell(5).setCellValue(s(dd.getSize()));
			dataRow.createCell(6).setCellValue(s(dd.getAlign()));
			dataRow.createCell(7).setCellValue(s(dd.getRefButton()));
			dataRow.createCell(8).setCellValue(s(dd.getRefWindow()));
			dataRow.createCell(9).setCellValue(s(dd.getRefTable()));
			row++;
		}

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
	}

	private void writeSheetProgramList(final List<ProgramEntity> list, final Sheet sheet) {
		// header
		Row headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("No");
		headRow.createCell(1).setCellValue("Name");
		headRow.createCell(2).setCellValue("Title(def)");
		headRow.createCell(3).setCellValue("Plugins");
		// data
		int row = 1;
		for (ProgramEntity pg : list) {
			StringBuilder plugins = new StringBuilder();
			for (String plugin : pg.getPlugins()) {
				if (0 < plugins.length()) {
					plugins.append(", ");
				}
				plugins.append(plugin);
			}

			Row dataRow = sheet.createRow(row);
			dataRow.createCell(0).setCellValue(String.format("%d", row));
			dataRow.createCell(1).setCellValue(pg.getName());
			dataRow.createCell(2).setCellValue(pg.getDefaultTitle().getValue());
			dataRow.createCell(3).setCellValue(plugins.toString());

			row++;
		}

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
	}

	private XMDEntity readXMD(final File file) {
		Digester digester = new Digester();
		digester.addRule("XMD", new ObjectCreateRule(XMDEntity.class));
		digester.addRule("XMD/MSG", new ObjectCreateRule(MSGEntity.class));
		digester.addRule("XMD/MSG", new SetPropertiesRule("ID", "id"));
		digester.addRule("XMD/MSG", new SetPropertiesRule("Mode", "mode"));
		digester.addRule("XMD/MSG", new SetPropertiesRule("Lang", "lang"));
		digester.addRule("XMD/MSG", new BeanPropertySetterRule("value"));
		digester.addRule("XMD/MSG", new SetNextRule("add"));

		XMDEntity xmd = null;
		try {
			xmd = (XMDEntity) digester.parse(file);
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return xmd;
	}

	private XPDEntity readXPD(final File file) {
		Digester digester = new Digester();
		digester.addRule("XPD", new ObjectCreateRule(XPDEntity.class));
		digester.addRule("XPD/Program", new ObjectCreateRule(ProgramEntity.class));
		digester.addRule("XPD/Program", new SetPropertiesRule("Name", "name"));
		digester.addRule("XPD/Program", new SetNextRule("add"));

		digester.addRule("XPD/Program/Title", new ObjectCreateRule(ProgramTitleEntity.class));
		digester.addRule("XPD/Program/Title", new SetPropertiesRule("Lang", "lang"));
		digester.addRule("XPD/Program/Title", new BeanPropertySetterRule("value"));
		digester.addRule("XPD/Program/Title", new SetNextRule("addTitle"));

		digester.addRule("XPD/Program/Pattern", new ObjectCreateRule(ProgramPatternEntity.class));
		digester.addRule("XPD/Program/Pattern", new SetPropertiesRule("Offset", "offset"));
		digester.addRule("XPD/Program/Pattern", new BeanPropertySetterRule("value"));
		digester.addRule("XPD/Program/Pattern", new SetNextRule("addPattern"));

		digester.addRule("XPD/Program/PP", new ObjectCreateRule(ProgramPPEntity.class));
		digester.addRule("XPD/Program/PP", new SetPropertiesRule("page", "page"));
		digester.addRule("XPD/Program/PP/Plugin", new BeanPropertySetterRule("plugin"));
		digester.addRule("XPD/Program/PP/KeyFields", new BeanPropertySetterRule("keyFields"));
		digester.addRule("XPD/Program/PP/KeyFields2", new BeanPropertySetterRule("keyFields2"));
		digester.addRule("XPD/Program/PP/KeyFields3", new BeanPropertySetterRule("keyFields3"));
		digester.addRule("XPD/Program/PP/KeyFields4", new BeanPropertySetterRule("keyFields4"));
		digester.addRule("XPD/Program/PP/ListKeyFields", new BeanPropertySetterRule("listKeyFields"));
		digester.addRule("XPD/Program/PP/ListKeyFields2", new BeanPropertySetterRule("listKeyFields2"));
		digester.addRule("XPD/Program/PP/ListKeyFields3", new BeanPropertySetterRule("listKeyFields3"));
		digester.addRule("XPD/Program/PP/ListKeyFields4", new BeanPropertySetterRule("listKeyFields4"));
		digester.addRule("XPD/Program/PP/ListFields", new BeanPropertySetterRule("listFields"));
		digester.addRule("XPD/Program/PP/ListFields2", new BeanPropertySetterRule("listFields2"));
		digester.addRule("XPD/Program/PP/ListFields3", new BeanPropertySetterRule("listFields3"));
		digester.addRule("XPD/Program/PP/ListFields4", new BeanPropertySetterRule("listFields4"));
		digester.addRule("XPD/Program/PP/InqPrintFields", new BeanPropertySetterRule("inqPrintFields"));
		digester.addRule("XPD/Program/PP/InqPrintFields2", new BeanPropertySetterRule("inqPrintFields2"));
		digester.addRule("XPD/Program/PP/InqPrintFields3", new BeanPropertySetterRule("inqPrintFields3"));
		digester.addRule("XPD/Program/PP/InqPrintFields4", new BeanPropertySetterRule("inqPrintFields4"));
		digester.addRule("XPD/Program/PP/InqHiddenFields", new BeanPropertySetterRule("inqHiddenFields"));
		digester.addRule("XPD/Program/PP/InqHiddenFields2", new BeanPropertySetterRule("inqHiddenFields2"));
		digester.addRule("XPD/Program/PP/InqHiddenFields3", new BeanPropertySetterRule("inqHiddenFields3"));
		digester.addRule("XPD/Program/PP/InqHiddenFields4", new BeanPropertySetterRule("inqHiddenFields4"));
		digester.addRule("XPD/Program/PP/EntInputFields", new BeanPropertySetterRule("entInputFields"));
		digester.addRule("XPD/Program/PP/EntInputFields2", new BeanPropertySetterRule("entInputFields2"));
		digester.addRule("XPD/Program/PP/EntInputFields3", new BeanPropertySetterRule("entInputFields3"));
		digester.addRule("XPD/Program/PP/EntInputFields4", new BeanPropertySetterRule("entInputFields4"));
		digester.addRule("XPD/Program/PP/EntDBFields", new BeanPropertySetterRule("entDBFields"));
		digester.addRule("XPD/Program/PP/EntDBFields2", new BeanPropertySetterRule("entDBFields2"));
		digester.addRule("XPD/Program/PP/EntDBFields3", new BeanPropertySetterRule("entDBFields3"));
		digester.addRule("XPD/Program/PP/EntDBFields4", new BeanPropertySetterRule("entDBFields4"));
		digester.addRule("XPD/Program/PP/EntHiddenFields", new BeanPropertySetterRule("entHiddenFields"));
		digester.addRule("XPD/Program/PP/EntHiddenFields2", new BeanPropertySetterRule("entHiddenFields2"));
		digester.addRule("XPD/Program/PP/EntHiddenFields3", new BeanPropertySetterRule("entHiddenFields3"));
		digester.addRule("XPD/Program/PP/EntHiddenFields4", new BeanPropertySetterRule("entHiddenFields4"));
		digester.addRule("XPD/Program/PP/UpdPrintFields", new BeanPropertySetterRule("updPrintFields"));
		digester.addRule("XPD/Program/PP/UpdPrintFields2", new BeanPropertySetterRule("updPrintFields2"));
		digester.addRule("XPD/Program/PP/UpdPrintFields3", new BeanPropertySetterRule("updPrintFields3"));
		digester.addRule("XPD/Program/PP/UpdPrintFields4", new BeanPropertySetterRule("updPrintFields4"));
		digester.addRule("XPD/Program/PP/UpdInputFields", new BeanPropertySetterRule("updInputFields"));
		digester.addRule("XPD/Program/PP/UpdInputFields2", new BeanPropertySetterRule("updInputFields2"));
		digester.addRule("XPD/Program/PP/UpdInputFields3", new BeanPropertySetterRule("updInputFields3"));
		digester.addRule("XPD/Program/PP/UpdInputFields4", new BeanPropertySetterRule("updInputFields4"));
		digester.addRule("XPD/Program/PP/UpdDBFields", new BeanPropertySetterRule("updDBFields"));
		digester.addRule("XPD/Program/PP/UpdDBFields2", new BeanPropertySetterRule("updDBFields2"));
		digester.addRule("XPD/Program/PP/UpdDBFields3", new BeanPropertySetterRule("updDBFields3"));
		digester.addRule("XPD/Program/PP/UpdDBFields4", new BeanPropertySetterRule("updDBFields4"));
		digester.addRule("XPD/Program/PP/UpdHiddenFields", new BeanPropertySetterRule("updHiddenFields"));
		digester.addRule("XPD/Program/PP/UpdHiddenFields2", new BeanPropertySetterRule("updHiddenFields2"));
		digester.addRule("XPD/Program/PP/UpdHiddenFields3", new BeanPropertySetterRule("updHiddenFields3"));
		digester.addRule("XPD/Program/PP/UpdHiddenFields4", new BeanPropertySetterRule("updHiddenFields4"));
		digester.addRule("XPD/Program/PP/DelPrintFields", new BeanPropertySetterRule("delPrintFields"));
		digester.addRule("XPD/Program/PP/DelPrintFields2", new BeanPropertySetterRule("delPrintFields2"));
		digester.addRule("XPD/Program/PP/DelPrintFields3", new BeanPropertySetterRule("delPrintFields3"));
		digester.addRule("XPD/Program/PP/DelPrintFields4", new BeanPropertySetterRule("delPrintFields4"));
		digester.addRule("XPD/Program/PP/DelHiddenFields", new BeanPropertySetterRule("delHiddenFields"));
		digester.addRule("XPD/Program/PP/DelHiddenFields2", new BeanPropertySetterRule("delHiddenFields2"));
		digester.addRule("XPD/Program/PP/DelHiddenFields3", new BeanPropertySetterRule("delHiddenFields3"));
		digester.addRule("XPD/Program/PP/DelHiddenFields4", new BeanPropertySetterRule("delHiddenFields4"));
		digester.addRule("XPD/Program/PP/FileInputFields", new BeanPropertySetterRule("fileInputFields"));
		digester.addRule("XPD/Program/PP/FileInputFields2", new BeanPropertySetterRule("fileInputFields2"));
		digester.addRule("XPD/Program/PP/FileInputFields3", new BeanPropertySetterRule("fileInputFields3"));
		digester.addRule("XPD/Program/PP/FileInputFields4", new BeanPropertySetterRule("fileInputFields4"));
		digester.addRule("XPD/Program/PP/RptPrintFields", new BeanPropertySetterRule("rptPrintFields"));
		digester.addRule("XPD/Program/PP/RptPrintFields2", new BeanPropertySetterRule("rptPrintFields2"));
		digester.addRule("XPD/Program/PP/RptPrintFields3", new BeanPropertySetterRule("rptPrintFields3"));
		digester.addRule("XPD/Program/PP/RptPrintFields4", new BeanPropertySetterRule("rptPrintFields4"));
		digester.addRule("XPD/Program/PP/HiddenFields", new BeanPropertySetterRule("hiddenFields"));
		digester.addRule("XPD/Program/PP/HiddenFields2", new BeanPropertySetterRule("hiddenFields2"));
		digester.addRule("XPD/Program/PP/HiddenFields3", new BeanPropertySetterRule("hiddenFields3"));
		digester.addRule("XPD/Program/PP/HiddenFields4", new BeanPropertySetterRule("hiddenFields4"));
		digester.addRule("XPD/Program/PP", new SetNextRule("addPP"));

		digester.addRule("XPD/Program/PP/Option", new ObjectCreateRule(ProgramOptionEntity.class));
		digester.addRule("XPD/Program/PP/Option", new SetNextRule("setOption"));

		digester.addRule("XPD/Program/PP/Option/Param", new ObjectCreateRule(ProgramOptionParamEntity.class));
		digester.addRule("XPD/Program/PP/Option/Param", new SetPropertiesRule("Name", "name"));
		digester.addRule("XPD/Program/PP/Option/Param", new BeanPropertySetterRule("value"));
		digester.addRule("XPD/Program/PP/Option/Param", new SetNextRule("addParam"));

		XPDEntity xpd = null;
		try {
			xpd = (XPDEntity) digester.parse(file);
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return xpd;
	}

	private XDDEntity readXDD(final File file) {
		Digester digester = new Digester();
		digester.addRule("XDD", new ObjectCreateRule(XDDEntity.class));
		digester.addRule("XDD/DD", new ObjectCreateRule(DDEntity.class));
		digester.addRule("XDD/DD", new SetPropertiesRule("Name", "name"));
		digester.addRule("XDD/DD/DBFIELD", new BeanPropertySetterRule("dbfield"));
		digester.addRule("XDD/DD/TYPE", new BeanPropertySetterRule("type"));
		digester.addRule("XDD/DD/SIZE", new BeanPropertySetterRule("size"));
		digester.addRule("XDD/DD/INPUTTYPE", new BeanPropertySetterRule("inputType"));
		digester.addRule("XDD/DD/ALIGN", new BeanPropertySetterRule("align"));
		digester.addRule("XDD/DD/SQL", new BeanPropertySetterRule("sql"));
		digester.addRule("XDD/DD/REFWINDOW", new BeanPropertySetterRule("refWindow"));
		digester.addRule("XDD/DD/REFTABLE", new BeanPropertySetterRule("refTable"));
		digester.addRule("XDD/DD/REFBUTTON", new BeanPropertySetterRule("refButtonValue"));
		digester.addRule("XDD/DD/PLUGIN", new BeanPropertySetterRule("plugin"));
		digester.addRule("XDD/DD", new SetNextRule("add"));

		digester.addRule("XDD/DD/TITLE", new ObjectCreateRule(DDTitleEntity.class));
		digester.addRule("XDD/DD/TITLE", new SetPropertiesRule("Lang", "lang"));
		digester.addRule("XDD/DD/TITLE", new BeanPropertySetterRule("value"));
		digester.addRule("XDD/DD/TITLE", new SetNextRule("addTitle"));

		XDDEntity xdd = null;
		try {
			xdd = (XDDEntity) digester.parse(file);
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return xdd;
	}

	private SystemProperties readSystemProperties(final File file, final String charset) {
		SystemProperties result = new SystemProperties();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));

			String buffer = null;
			while (null != (buffer = reader.readLine())) {
				if (buffer.trim().startsWith("#")) {
					continue;
				}

				int index = buffer.indexOf("=");
				if (-1 == index) {
					continue;
				}

				String key = buffer.substring(0, index).trim().toUpperCase();
				String value = buffer.substring(index + 1).trim();

				// System.out.println(String.format("%s = %s", key, value));

				if ("XPD".equals(key)) {
					List<String> names = split(value);
					for (String name : names) {
						if (!isIgnoreXPDFile(name)) {
							result.addXPD(name);
						} else {

						}
					}
				} else if ("XWD".equals(key)) {
					List<String> names = split(value);
					for (String name : names) {
						if (!isIgnoreXWDFile(name)) {
							result.addXWD(name);
						} else {

						}
					}
					result.addXWDList(split(value));
				} else if ("XMD".equals(key)) {
					List<String> names = split(value);
					for (String name : names) {
						if (!isIgnoreXMDFile(name)) {
							result.addXMD(name);
						} else {

						}
					}
					result.addXMDList(split(value));
				} else if ("XDD".equals(key)) {
					List<String> names = split(value);
					for (String name : names) {
						if (!isIgnoreXDDFile(name)) {
							result.addXDD(name);
						} else {

						}
					}
					result.addXDDList(split(value));
				}
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
		return result;
	}

	protected void sortXPD(final List<ProgramEntity> list) {
		Collections.sort(list, new Comparator<ProgramEntity>() {
			@Override
			public int compare(final ProgramEntity o1, final ProgramEntity o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	protected void sortXWD(final List<ProgramEntity> list) {
		Collections.sort(list, new Comparator<ProgramEntity>() {
			@Override
			public int compare(final ProgramEntity o1, final ProgramEntity o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	protected void sortXMD(final List<MSGEntity> list) {
		Collections.sort(list, new Comparator<MSGEntity>() {
			@Override
			public int compare(final MSGEntity o1, final MSGEntity o2) {
				int i = o1.getId().compareTo(o2.getId());
				if (0 == i) {
					i = o1.getLang().compareTo(o2.getLang());
				}
				return i;
			}
		});
	}

	protected void sortXDD(final List<DDEntity> list) {
		Collections.sort(list, new Comparator<DDEntity>() {
			@Override
			public int compare(final DDEntity o1, final DDEntity o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	private List<String> split(final String string) {
		List<String> result = new ArrayList<String>();
		if (null != string) {
			String[] split = string.split(",");
			for (String s : split) {
				String buffer = s.trim();
				if (0 < buffer.length()) {
					result.add(buffer);
				}
			}
		}
		return result;
	}

	private static String s(final String string) {
		String s = "";
		if (null != string) {
			s = string;
		}
		return s;
	}
}
