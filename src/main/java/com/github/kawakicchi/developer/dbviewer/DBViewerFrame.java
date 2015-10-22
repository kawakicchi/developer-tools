package com.github.kawakicchi.developer.dbviewer;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
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

	public static void main(final String[] args) {
		DBViewerFrame frm = new DBViewerFrame();
		frm.setVisible(true);
	}

	private DBDataGrid dataGrid;

	public DBViewerFrame() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(null);

		initMenu();

		dataGrid = new DBDataGrid();
		dataGrid.setLocation(0, 0);
		add(dataGrid);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets inset = getInsets();
				int height = getHeight() - (inset.top + inset.bottom);
				int width = getWidth() - (inset.left + inset.right);
				Dimension dimension = new Dimension(width, height);
				dataGrid.setSize(dimension);
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

					stat = connection.prepareStatement("SELECT * FROM DUAL");

					rs = stat.executeQuery();

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
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@";
			String user = "xxx";
			String password = "xxx";
			/* Connectionの作成 */
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		try {

			DatabaseModel model = new OracleDatabaseModel();
			System.out.println("User List");
			List<User> users = model.getUserList(connection);
			for (User user : users) {
				System.out.println(user.getName());
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void destory() {
		if (null != connection) {
			try {
				connection.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
