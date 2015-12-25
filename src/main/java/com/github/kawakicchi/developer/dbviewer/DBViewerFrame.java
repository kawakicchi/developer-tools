package com.github.kawakicchi.developer.dbviewer;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.github.kawakicchi.developer.dbviewer.model.DatabaseModel;
import com.github.kawakicchi.developer.dbviewer.model.DatabaseModel.User;
import com.github.kawakicchi.developer.dbviewer.model.OracleDatabaseModel;

public class DBViewerFrame extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = -2512581293435452881L;

	private DBViewerPanel panel;

	public DBViewerFrame() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(null);

		initMenu();

		panel = new DBViewerPanel();
		panel.setLocation(0, 0);
		add(panel);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets inset = getInsets();
				int height = getHeight() - (inset.top + inset.bottom) - menuBar.getHeight();
				int width = getWidth() - (inset.left + inset.right);
				Dimension dimension = new Dimension(width, height);
				panel.setSize(dimension);
			}
		});

		setSize(800, 600);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				init();

				PreparedStatement stat = null;
				ResultSet rs = null;
				try {

					stat = connection.prepareStatement("select * from TEDI_JUCHU_JOKYO");

					rs = stat.executeQuery();
					rs.setFetchSize(1000);

					panel.setResultSet(rs);

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

		try {

			DatabaseModel model = new OracleDatabaseModel();
			List<User> users = model.getUserList(connection);
			for (User user : users) {
				System.out.println(user.getName());
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void destory() {
		DataSource.getInstance().returnConnection(connection);
	}
}
