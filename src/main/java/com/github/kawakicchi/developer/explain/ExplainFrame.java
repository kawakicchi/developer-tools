package com.github.kawakicchi.developer.explain;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.github.kawakicchi.developer.component.BasicFrame;
import com.github.kawakicchi.developer.component.LabelStatusItem;
import com.github.kawakicchi.developer.component.StatusBar;
import com.github.kawakicchi.developer.component.editor.BasicTextPane;
import com.github.kawakicchi.developer.component.editor.TextLineNumberView;
import com.github.kawakicchi.developer.component.editor.SQLTextPane;

public class ExplainFrame extends BasicFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 37172323649760837L;

	private JMenu menuFile;
	private JMenuItem menuFileDBConnect;
	private JMenuItem menuFileDBLogout;
	private JMenuItem menuFileExit;

	private JMenu menuSQL;
	private JMenuItem menuSQLExecute;

	private JMenu menuTool;

	private JMenu menuHelp;

	private JSplitPane pnlSplitMain;
	private JSplitPane pnlSplitSub;
	private JScrollPane pnlScrollSQL;
	private JScrollPane pnlScrollExplain;
	private JScrollPane pnlScrollConsole;
	private SQLTextPane txtSQL;
	private BasicTextPane txtExplain;
	private BasicTextPane txtConsole;

	private LabelStatusItem lblStatus;
	private JProgressBar prgStatus;

	public ExplainFrame() {
		setTitle(LabelManager.get("Title"));
	}

	private void doExecute(final String sql) {
		
	}
	private void doExplain(final String sql) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				threadRun(sql);
			}
		});
		thread.start();
	}

	private void threadRun(final String sql) {
		lblStatus.setText(MessageManager.get("MSG0001"));
		long start = System.nanoTime();

		Connection connection = null;
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@bidbtest.elecom.co.jp:1521:RREBST";
			String user = "RAKFW2";
			String password = "RAKFW2T";
			connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(true);

			Explain explain = new Explain(connection);
			ExplainResult result = explain.explain(sql);

			txtConsole.setText(result.getLog());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (null != connection && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		long end = System.nanoTime();
		long time = end - start;
		if (time < 1000000000) {
			lblStatus.setText(String.format("%.0f ms", (double) (time) / 1000000.f));
		} else {
			lblStatus.setText(String.format("%.2fs", (double) (time) / 1000000000.f));
		}
	}

	private void doLoad() {
		String sql = read(new File("sample.sql"));
		txtSQL.setText(sql);
	}

	protected void doInit() {
		pnlSplitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		pnlSplitMain.setContinuousLayout(true);

		pnlSplitSub = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		pnlSplitSub.setContinuousLayout(true);

		txtSQL = new SQLTextPane();
		txtExplain = new BasicTextPane();
		txtConsole = new BasicTextPane();

		pnlScrollSQL = new JScrollPane(txtSQL);
		pnlScrollExplain = new JScrollPane(txtExplain);
		pnlScrollConsole = new JScrollPane(txtConsole);

		pnlScrollSQL.setRowHeaderView(new TextLineNumberView(txtSQL));

		pnlSplitSub.setTopComponent(pnlScrollExplain);
		pnlSplitSub.setBottomComponent(pnlScrollConsole);
		pnlSplitMain.setLeftComponent(pnlScrollSQL);
		pnlSplitMain.setRightComponent(pnlSplitSub);
		add(pnlSplitMain);

		txtSQL.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getModifiers() == KeyEvent.CTRL_MASK) {
					if (e.getKeyCode() == KeyEvent.VK_R) {
						doExecute(txtSQL.getText());
					}else if (e.getKeyCode() == KeyEvent.VK_P) {
						doExplain(txtSQL.getText());
					}
				}
			}
		});

		doLoad();
	}

	protected void doInitMenuBar(final JMenuBar menuBar) {
		// File Menu
		menuFile = new JMenu(LabelManager.get("Menu.File"));
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuFile);

		menuFileDBConnect = new JMenuItem(LabelManager.get("Menu.File.DB.Connect"));
		menuFile.add(menuFileDBConnect);

		menuFileDBLogout = new JMenuItem(LabelManager.get("Menu.File.DB.Logout"));
		menuFile.add(menuFileDBLogout);

		menuFile.addSeparator();

		menuFileExit = new JMenuItem(LabelManager.get("Menu.File.Exit"));
		menuFileExit.setMnemonic(KeyEvent.VK_X);
		menuFile.add(menuFileExit);

		// SQL Menu
		menuSQL = new JMenu(LabelManager.get("Menu.SQL"));
		menuSQL.setMnemonic(KeyEvent.VK_S);
		menuBar.add(menuSQL);

		menuSQLExecute = new JMenuItem(LabelManager.get("Menu.SQL.Execute"));
		menuSQL.add(menuSQLExecute);

		// Tool Menu
		menuTool = new JMenu(LabelManager.get("Menu.Tool"));
		menuTool.setMnemonic(KeyEvent.VK_T);
		menuBar.add(menuTool);

		// Help Menu
		menuHelp = new JMenu(LabelManager.get("Menu.Help"));
		menuHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menuHelp);
	}

	protected void doInitStatusBar(final StatusBar statusBar) {
		lblStatus = new LabelStatusItem();
		//lblStatus.setForeground(Color.ORANGE);
		//lblStatus.setBackground(Color.RED);
		//lblStatus.setOpaque(true);
		//lblStatus.setText("test");
		statusBar.add(lblStatus);

		prgStatus = new JProgressBar();
		prgStatus.setSize(160, 10);
		statusBar.add(prgStatus);
	}

	protected void doResized(final Dimension dimension) {
		pnlSplitMain.setSize(dimension);
		pnlSplitMain.setDividerLocation(getWidth() / 2);
		pnlSplitSub.setDividerLocation(getHeight() / 2);
	}

	private String read(final File file) {
		StringBuffer sb = new StringBuffer();
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file), "Windows-31J");
			char[] buffer = new char[1024];
			int size = -1;
			while (-1 != (size = reader.read(buffer, 0, 1024))) {
				sb.append(buffer, 0, size);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}
}
