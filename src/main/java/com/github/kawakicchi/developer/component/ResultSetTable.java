package com.github.kawakicchi.developer.component;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Types;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ResultSetTable extends JPanel{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 3768818697051245638L;

	private JScrollPane scroll;
	private JTable table;
	private DatagridTableModel model;

	public ResultSetTable() {
		model = new DatagridTableModel();

		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(true); // 
		table.getTableHeader().setReorderingAllowed(false); // カラムの入れ替え禁止
		
		table.setDefaultRenderer(Object.class, new BaseTableCellRenderer());
		
		scroll = new JScrollPane(table);
		scroll.setLocation(0, 0);
		
		add(scroll);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				Insets insets = getInsets();
				int width = getWidth() - (insets.left + insets.right);
				int height = getHeight() - (insets.top + insets.bottom);
				Dimension dimension = new Dimension(width, height);
				scroll.setSize(dimension);
				scroll.setPreferredSize(dimension);
			}
		});
		
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getModifiers() == KeyEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_C){
					DBDataGridClipboard c = new DBDataGridClipboard();
					c.copy(model, table.getSelectedColumn(), table.getSelectedRow(), table.getSelectedColumnCount(), table.getSelectedRowCount());
					
					e.consume();
				}
				// repaint();
			}
		});
	}

	public void setColumns(final List<DataColumn> columns) {
		model.setRowCount(0);
		model.setColumnCount(0);
		for (DataColumn column : columns) {
			model.addColumn(column.getName());
		}
	}

	public void addRecord(final List<Object> datas) {
		Object[] objs = new Object[datas.size()];
		for (int i = 0 ; i < datas.size() ; i++) {
			Object obj = datas.get(i);
			ValueLabel label = new ValueLabel(obj);
			objs[i] = label;
		}
		model.addRow(objs);
	}
	
	public static class ValueLabel extends JLabel {

		/** serialVersionUID */
		private static final long serialVersionUID = -1367234894893138321L;

		private Object value;

		public ValueLabel(final Object value) {
			this.value = value;
		}

		public Object getValue() {
			return value;
		}

		public String toString() {
			if (null == value) {
				return "(null)";
			} else {
				return value.toString();
			}
		}

	}

	public static class DataColumn {

		private String name;

		private int type;

		public DataColumn(final String name, final int type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		/**
		 * {@link Types}
		 * @return
		 */
		public int getType() {
			return type;
		}
	}
	
	private static class DBDataGridClipboard {
		
		public DBDataGridClipboard() {

		}

		public void copy(final DefaultTableModel model, final int col, final int row, final int colSize, final int rowSize) {
			Toolkit kit = Toolkit.getDefaultToolkit();
			Clipboard clip = kit.getSystemClipboard();

			DBTransferable transferable = new DBTransferable(model, col, row, colSize, rowSize);
			clip.setContents(transferable, null);
		}
	}

	private static class DBTransferable implements Transferable {

		private final DefaultTableModel model;
		private final int col;
		private final int row;
		private final int colSize;
		private final int rowSize;

		public DBTransferable(final DefaultTableModel model, final int col, final int row, final int colSize, final int rowSize) {
			this.model = model;
			this.col = col;
			this.row = row;
			this.colSize = colSize;
			this.rowSize = rowSize;
		}

		@Override
		public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			StringBuilder s = new StringBuilder();
			if ("text/html".equals(flavor.getHumanPresentableName())) {
				s.append("<table style=\"border-style: solid; border-width: thin; table-layout: fixed;\">");
				// header
				s.append("<tr style=\"border-style: solid; border-width: thin; table-layout: fixed;\">");
				for (int bufCol = col; bufCol < col + colSize; bufCol++) {
					s.append("<td nowrap bgcolor=\"#CCFFCC\"><b>").append(model.getColumnName(bufCol)).append("</b></td>");
				}
				s.append("</tr>");
				// body
				for (int bufRow = row; bufRow < row + rowSize; bufRow++) {
					s.append("<tr style=\"border-style: dotted;\">");
					for (int bufCol = col; bufCol < col + colSize; bufCol++) {
						ValueLabel label = (ValueLabel) model.getValueAt(bufRow, bufCol);
						if (null == label.getValue()) {
							s.append("<td nowrap style='mso-number-format:\"\\@\"' ><font color='#777777'>").append(label).append("</font></td>");
						} else {
							s.append("<td nowrap style='mso-number-format:\"\\@\"' >").append(label).append("</td>");
						}
					}
					s.append("</tr>");
				}
				s.append("</table>");

			} else {
				// header
				for (int bufCol = col; bufCol < col + colSize; bufCol++) {
					if (bufCol != col) {
						s.append("\t");
					}
					s.append(model.getColumnName(bufCol));
				}
				s.append("\r\n");
				// body
				for (int bufRow = row; bufRow < row + rowSize; bufRow++) {
					for (int bufCol = col; bufCol < col + colSize; bufCol++) {
						if (bufCol != col) {
							s.append("\t");
						}
						s.append(model.getValueAt(bufRow, bufCol));
					}
					s.append("\r\n");
				}
			}
			return s.toString();
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			DataFlavor df1 = new DataFlavor("text/html; class=java.lang.String; charset=Unicode", "text/html");
			DataFlavor df2 = new DataFlavor("text/plain; class=java.lang.String; charset=Unicode", "text/plain");
			DataFlavor[] dfs = new DataFlavor[] { df1, df2 };
			return dfs;
		}

		@Override
		public boolean isDataFlavorSupported(final DataFlavor flavor) {
			System.out.println("isDataFlavorSupported("+flavor.getHumanPresentableName()+")");
			if ("text/html".equals(flavor.getHumanPresentableName())) {
				return true;
			} else if ("text/plain".equals(flavor.getHumanPresentableName())) {
				return true;
			}
			return false;
		}

	}
}
