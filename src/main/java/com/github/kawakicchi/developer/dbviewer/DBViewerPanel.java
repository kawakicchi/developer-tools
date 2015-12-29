package com.github.kawakicchi.developer.dbviewer;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.github.kawakicchi.developer.dbviewer.component.DBDataGrid;
import com.github.kawakicchi.developer.dbviewer.component.SQLEditer;
import com.github.kawakicchi.developer.dbviewer.model.DatabaseModel;

public class DBViewerPanel extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = 8575787753000760014L;

	private JSplitPane splitpane;

	private SQLEditer editer;
	private DBDataGrid dataGrid;

	private DatabaseModel model;

	public DBViewerPanel() {
		setLayout(null);

		splitpane = new JSplitPane();
		splitpane.setLocation(0, 0);
		splitpane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitpane.setContinuousLayout(true);

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

		editer.addSQLEditerListener(new SQLEditer.SQLEditerListener() {
			@Override
			public void onExecuteSQL(String text) {

				PreparedStatement stat = null;
				ResultSet rs = null;
				try {

					stat = model.prepareStatement(text);
					rs = stat.executeQuery();
					rs.setFetchSize(1000);

					dataGrid.set(rs);
				} catch (SQLException ex) {
					ex.printStackTrace();
				} finally {
					if (null != rs) {
						try {
							rs.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
					if (null != stat) {
						try {
							stat.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});
	}

	public void setDatabaseModel(final DatabaseModel model) {
		this.model = model;
	}

}
