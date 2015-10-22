package com.github.kawakicchi.developer.dbviewer;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

		table.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				int keycode = e.getKeyCode();
				int mod = e.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					String presskey = e.getKeyText(keycode);
					System.out.println(String.format("%s(%d) + CTRL", presskey, keycode));

					System.out.println(String.format("%d, %d, %d, %d", table.getSelectedColumn(), table.getSelectedRow(), table.getSelectedColumnCount(),
							table.getSelectedRowCount()));

					// クリップボードへ
					String str = "AAA\tBBB\r\nCCC\tDDD"; // 保存するテキスト
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					StringSelection selection = new StringSelection(str);
					clipboard.setContents(selection, null);
				}
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});
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
}
