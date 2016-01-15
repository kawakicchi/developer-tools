package com.github.kawakicchi.developer.dbviewer.component;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class DBObjectListPanel extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = -2643208807003353857L;

	private JScrollPane scroll;
	private JTable table;
	private DefaultTableModel model;

	public DBObjectListPanel() {
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
		
	}
}
