package com.github.kawakicchi.developer.dbviewer;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class DBViewerPanel extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = 8575787753000760014L;

	
	private JSplitPane splitpane;

	private SQLEditer editer;
	private DBDataGrid dataGrid;

	public DBViewerPanel() {
		setLayout(null);

		splitpane = new JSplitPane();
		splitpane.setLocation(0, 0);
		splitpane.setOrientation(JSplitPane.VERTICAL_SPLIT);

		editer = new SQLEditer();
		dataGrid = new DBDataGrid();
		
		splitpane.setTopComponent(editer);
		splitpane.setBottomComponent(dataGrid);
		splitpane.setDividerLocation(200);
		add(splitpane);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets inset = getInsets();
				int height = getHeight() - (inset.top + inset.bottom);
				int width = getWidth() - (inset.left + inset.right);
				Dimension dimension = new Dimension(width, height);
				splitpane.setSize(dimension);
			}
		});
	}
	
	public void setResultSet(final ResultSet rs) throws SQLException {
		dataGrid.set(rs);
	}
}
