package com.github.kawakicchi.developer.tools.rakrak.analyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.digester.BeanPropertySetterRule;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ObjectCreateRule;
import org.apache.commons.digester.SetNextRule;
import org.apache.commons.digester.SetPropertiesRule;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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

public class RakRakAnalyzer {

	public static void main(final String[] args) {

		RakRakAnalyzer ana = new RakRakAnalyzer();

		ana.analyze(new File(args[0]), new File(args[1]));

	}

	public void analyze(final File file, final File destFile) {

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
		
		// XPD
		List<ProgramEntity> programList = new ArrayList<ProgramEntity>();
		for (String xpdName : sp.getXPDList()) {
			File xpdFile = Paths.get(defDir.getAbsolutePath(), xpdName).toFile();
			if (xpdFile.isFile()) {
				XPDEntity xpd = readXPD(xpdFile);
				if (null != xpd) {
					for (ProgramEntity prg : xpd.getProgramList()) {
						programList.add(prg);
					}
				}
			} else {
				System.out.println(String.format("Not Found XPD file.[%s]", xpdFile.getAbsoluteFile()));
			}
		}

		Workbook workbook = new HSSFWorkbook();
		
		writeMSG(msgList,  workbook.createSheet("メッセージ一覧"));
		writeDD(ddList,  workbook.createSheet("DD一覧"));
		writeProgram(programList,  workbook.createSheet("プログラム一覧"));
		
		
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
	
	private void writeMSG(final List<MSGEntity> list, final Sheet sheet) {
		// header
		Row headerRow = sheet.createRow(0);
		// data
		int row = 1;
		for (MSGEntity msg : list) {
			Row dataRow = sheet.createRow(row);
			dataRow.createCell(0).setCellValue(String.format("%d", row));
			dataRow.createCell(1).setCellValue(msg.getId());
			dataRow.createCell(2).setCellValue(msg.getLang());
			dataRow.createCell(3).setCellValue(msg.getMode());
			dataRow.createCell(4).setCellValue(msg.getValue());
			row ++;
		}
	}
	
	private void writeDD(final List<DDEntity> list, final Sheet sheet) {
		// header
		Row headerRow = sheet.createRow(0);
		// data
		int row = 1;
		for (DDEntity dd : list) {
			Row dataRow = sheet.createRow(row);
			dataRow.createCell(0).setCellValue(String.format("%d", row));
			dataRow.createCell(1).setCellValue(dd.getName());
			dataRow.createCell(2).setCellValue(dd.getTitle());
			row ++;
		}
	}
	
	private void writeProgram(final List<ProgramEntity> list, final Sheet sheet) {
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
		digester.addRule("XDD/DD/SIZE", new BeanPropertySetterRule("size"));
		digester.addRule("XDD/DD/INPUTTYPE", new BeanPropertySetterRule("inputType"));
		digester.addRule("XDD/DD/SQL", new BeanPropertySetterRule("sql"));
		digester.addRule("XDD/DD/REFWINDOW", new BeanPropertySetterRule("refWindow"));
		digester.addRule("XDD/DD/plugin", new BeanPropertySetterRule("plugin"));
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
					result.addXPDList(split(value));
				} else if ("XWD".equals(key)) {
					result.addXWDList(split(value));
				} else if ("XMD".equals(key)) {
					result.addXMDList(split(value));
				} else if ("XDD".equals(key)) {
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

}
