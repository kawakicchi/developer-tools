package com.github.kawakicchi.sample;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ExcelFormatCopy {

	public static class Test implements Transferable {

		public Test() {
			
		}
		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			return "<table><tr><td><font color=\"red\"><b>test</b></font></td><td><font color=\"red\"><b>test</b></font></td></tr><tr><td><font color=\"red\"><b>test</b></font></td><td></td></tr></table>";
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			DataFlavor[] dfs = new DataFlavor[] {new DataFlavor("text/html; class=java.lang.String; charset=Unicode","text/html")};
			return dfs;
		}

		@Override
		public boolean isDataFlavorSupported(final DataFlavor flavor) {
			// TODO 自動生成されたメソッド・スタブ
			return false;
		}
		
	}


	public static void main1(final String[] args) {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Clipboard clip = kit.getSystemClipboard();

		StringSelection ss = new StringSelection("AAA\tBBB\r\nCCC\tDDD\r\n<b>red</b>");
		Test test = new Test();
		clip.setContents(test, null);
	}

	public static void main(final String[] args) {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Clipboard clip = kit.getSystemClipboard();

		try {
			DataFlavor[] df = clip.getAvailableDataFlavors();
			for (DataFlavor f : df) {
				System.out.println(f);
				System.out.println(" -" + f.getMimeType());
				System.out.println(" -" + f.getHumanPresentableName());
			}
			
			//String s = (String) clip.getData(DataFlavor.stringFlavor);
			String s = (String) clip.getData(new DataFlavor("text/html; class=java.lang.String; charset=Unicode","text/html"));
			System.out.println(s);
//		} catch (ClassNotFoundException ex) {
//			ex.printStackTrace();
		} catch (UnsupportedFlavorException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
