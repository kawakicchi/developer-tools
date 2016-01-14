package com.github.kawakicchi.developer.component;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Types;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

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
}
