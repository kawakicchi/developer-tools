package com.github.kawakicchi.developer.dbviewer;

import java.awt.Color;
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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class DBDataGrid extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = -6740376415393911197L;

	private JScrollPane scroll;
	private JTable table;
	private DefaultTableModel model;

	public DBDataGrid() {
		setLayout(null);

		model = new DefaultTableModel();

		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(true); // 
		scroll = new JScrollPane(table);
		scroll.setLocation(0, 0);
		add(scroll);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets inset = getInsets();
				int height = getHeight() - (inset.top + inset.bottom);
				int width = getWidth() - (inset.left + inset.right);
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

		setBackground(Color.red);
		scroll.setBackground(Color.blue);
	}

	public void set(final ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();

		model.setRowCount(0);
		model.setColumnCount(0);
		for (int i = 0; i < meta.getColumnCount(); i++) {
			model.addColumn(meta.getColumnName(i + 1));
		}

		while (rs.next()) {
			String[] data = new String[meta.getColumnCount()];
			for (int i = 0; i < meta.getColumnCount(); i++) {
				Object obj = rs.getObject(i + 1);
				if (null == obj) {
					data[i] = "(null)";
				} else {
					data[i] = obj.toString();
				}
			}
			model.addRow(data);

			if (model.getRowCount() > 50) {
				break;
			}
		}
	}

	private static class DBDataGridClipboard {
		public DBDataGridClipboard() {

		}

		public void copy(final DefaultTableModel model, final int col, final int row, final int colSize, final int rowSize) {
			Toolkit kit = Toolkit.getDefaultToolkit();
			Clipboard clip = kit.getSystemClipboard();

			Test test = new Test(model, col, row, colSize, rowSize);
			clip.setContents(test, null);
		}
	}

	private static class Test implements Transferable {

		private final DefaultTableModel model;
		private final int col;
		private final int row;
		private final int colSize;
		private final int rowSize;

		public Test(final DefaultTableModel model, final int col, final int row, final int colSize, final int rowSize) {
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
				s.append("<tr>");
				for (int bufCol = col; bufCol < col + colSize; bufCol++) {
					s.append("<td nowrap bgcolor=\"#CCFFCC\"><b>").append(model.getColumnName(bufCol)).append("</b></td>");
				}
				s.append("</tr>");
				for (int bufRow = row; bufRow < row + rowSize; bufRow++) {
					s.append("<tr style=\"border-style: dotted;\">");
					for (int bufCol = col; bufCol < col + colSize; bufCol++) {
						s.append("<td nowrap>").append(model.getValueAt(bufRow, bufCol)).append("</td>");
					}
					s.append("</tr>");
				}
				s.append("</table>");

			} else {
				for (int bufCol = col; bufCol < col + colSize; bufCol++) {
					if (bufCol != col) {
						s.append("\t");
					}
					s.append(model.getColumnName(bufCol));
				}
				s.append("\r\n");

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
