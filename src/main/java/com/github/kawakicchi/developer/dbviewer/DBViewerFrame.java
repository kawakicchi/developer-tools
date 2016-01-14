package com.github.kawakicchi.developer.dbviewer;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;

import com.github.kawakicchi.developer.dbviewer.model.DatabaseModel;

public class DBViewerFrame extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = -2512581293435452881L;

	private JSplitPane splitpane;

	private DBViewerPanel pnlDB;
	private DBObjectViewerPanel pnlObj;

	private DatabaseModel model;

	public DBViewerFrame() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(null);

		initMenu();

		splitpane = new JSplitPane();
		splitpane.setLocation(0, 0);
		splitpane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitpane.setContinuousLayout(true);
		splitpane.setDividerLocation(600);

		pnlDB = new DBViewerPanel();
		splitpane.setLeftComponent(pnlDB);

		pnlObj = new DBObjectViewerPanel();
		splitpane.setRightComponent(pnlObj);

		add(splitpane);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets inset = getInsets();
				int height = getHeight() - (inset.top + inset.bottom) - menuBar.getHeight();
				int width = getWidth() - (inset.left + inset.right);
				Dimension dimension = new Dimension(width, height);
				splitpane.setSize(dimension);
			}
		});

		setSize(800, 600);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				init();
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
				destory();
			}
		});

	}

	private JMenuBar menuBar;
	private JMenu menuFile;

	private void initMenu() {
		menuBar = new JMenuBar();
		menuFile = new JMenu("ファイル(F)");
		menuFile.setMnemonic(KeyEvent.VK_F); // Alt + F で呼び出し
		menuBar.add(menuFile);
		setJMenuBar(menuBar);
	}

	private Connection connection;

	private void init() {
		connection = DataSource.getInstance().getConnection();
		//model = new OracleDatabaseModel(connection);

		pnlDB.setDatabaseModel(model);
		pnlObj.setDatabaseModel(model);
	}

	private void destory() {
		DataSource.getInstance().returnConnection(connection);
	}
}
