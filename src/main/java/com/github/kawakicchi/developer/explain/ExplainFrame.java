package com.github.kawakicchi.developer.explain;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import com.github.kawakicchi.developer.component.BasicFrame;
import com.github.kawakicchi.developer.component.LabelStatusItem;
import com.github.kawakicchi.developer.component.LoggerConsole;
import com.github.kawakicchi.developer.component.ResultSetTable;
import com.github.kawakicchi.developer.component.ResultSetTable.DataColumn;
import com.github.kawakicchi.developer.component.StatusBar;
import com.github.kawakicchi.developer.component.editor.TextLineNumberView;
import com.github.kawakicchi.developer.component.editor.SQLTextPane;
import com.github.kawakicchi.developer.database.DatabaseManager;
import com.github.kawakicchi.developer.dbviewer.component.DBObjectTypePanel;
import com.github.kawakicchi.developer.dbviewer.component.DBObjectListPanel;
import com.github.kawakicchi.developer.dbviewer.model.DatabaseModel;
import com.github.kawakicchi.developer.dbviewer.model.OracleDatabaseModel;
import com.github.kawakicchi.developer.explain.task.ExplainTask;
import com.github.kawakicchi.developer.explain.task.QueryTask;
import com.github.kawakicchi.developer.logger.Logger;
import com.github.kawakicchi.developer.task.Task;
import com.github.kawakicchi.developer.task.TaskAdapter;
import com.github.kawakicchi.developer.task.TaskManager;
import com.github.kawakicchi.developer.task.TaskManagerListener;
import com.github.kawakicchi.developer.util.FileUtility;
import com.github.kawakicchi.developer.util.FormatUtility;

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
	private JSplitPane pnlSplitSubLeft;
	private JScrollPane pnlScrollSQL;
	private JScrollPane pnlScrollConsole;
	private SQLTextPane txtSQL;
	private JTabbedPane tabSub;

	private JSplitPane pnlSplitSubRight;
	private DBObjectTypePanel pnlDBObjectType;
	private DBObjectListPanel tblDBObjectList;
	
	private ResultSetTable tblResult;
	private LoggerConsole txtConsole;

	private LabelStatusItem lblStatus;
	private JProgressBar prgStatus;

	private Thread endThread;

	private Logger logger;

	public ExplainFrame() {
		setTitle(LabelManager.get("Title"));
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				String sql = FileUtility.read(new File("tmp.sql"), "Windows-31J");
				txtSQL.setText(sql);
				
				TaskManager.getInstance().addTaskManagerListener(new TaskManagerListener() {
					@Override
					public void taskManagerTaskStart(final Task task) {
						prgStatus.setIndeterminate(true); // 不確定
					}
					@Override
					public void taskManagerTaskFinished(final Task task) {
						prgStatus.setIndeterminate(false); // 確定
					}
				});
				
				TaskManager.getInstance().start();
				
				logger.debug("DEBUG");
				logger.info("INFO");
				logger.warn("WARN");
				logger.error("ERROR");
				logger.fatal("FATAL");
			}
			@Override
			public void windowClosing(WindowEvent e) {
				if (TaskManager.getInstance().isStop()) {
					dispose();
				} else {
					if (null == endThread) {
						TaskManager.getInstance().stop();
						endThread = new Thread(new Runnable() {
							@Override
							public void run() {
								while (!TaskManager.getInstance().isStop()) {
									try {
									Thread.sleep(100);
									} catch (InterruptedException ex) {
										ex.printStackTrace();
									}
								}
								dispose();
							}
						});
						endThread.start();
					}
				}
			}
			@Override
			public void windowClosed(WindowEvent e) {
				FileUtility.write(new File("tmp.sql"), txtSQL.getText(), "Windows-31J");
			}
		});
	}

	private void doQuery(final String sql) {
		QueryTask task = new QueryTask(sql, DatabaseManager.getDatasource("DBNAME"));
		task.addTaskListener(new TaskAdapter() {
			@Override
			public void taskStart(final Task task) {
				lblStatus.setText(MessageManager.get("MSG0001"));
			}
			@Override
			public void taskSuccess(final Task task) {
				tabSub.setSelectedComponent(tblResult);
			}
			@Override
			public void taskError(final Task task) {
			}
			@Override
			public void taskFinished(final Task task) {
				lblStatus.setText( FormatUtility.nanoTimeToString( ((QueryTask)task).getExecuteTime() ) );
			}
		});
		task.addQueryTaskListener(new QueryTask.QueryTaskListener() {
			private List<DataColumn> columns;
			@Override
			public void queryTaskResultQuery(final ResultSet resultSet) throws SQLException {
				resultSet.setFetchSize(1000);
				
				columns = ExplainFrame.createDataColumn(resultSet);
				tblResult.setColumns(columns);
			}
			@Override
			public void queryTaskRecord(final ResultSet resultSet) throws SQLException {
				List<Object> record = ExplainFrame.createRecord(resultSet, columns);
				tblResult.addRecord(record);
			}
		});
		TaskManager.queueTask(task);
	}

	private void doExplain(final String sql) {
		ExplainTask task = new ExplainTask(sql, DatabaseManager.getDatasource("DBNAME"));
		task.addTaskListener(new TaskAdapter() {
			@Override
			public void taskStart(final Task task) {
				lblStatus.setText(MessageManager.get("MSG0001"));
			}
			@Override
			public void taskSuccess(final Task task) {
				tabSub.setSelectedComponent(pnlScrollConsole);
				logger.info( ((ExplainTask)task).getResult().getLog() );
			}
			@Override
			public void taskError(final Task task) {
			}
			@Override
			public void taskFinished(final Task task) {
				lblStatus.setText( FormatUtility.nanoTimeToString( ((ExplainTask)task).getExecuteTime() ) );
			}
		});
		TaskManager.queueTask(task);
	}

	private void doDatabaseList() {
		DatabaseListDialog dlg = new DatabaseListDialog(this, true);
		dlg.setVisible(true);
	}
	
	private static List<DataColumn> createDataColumn(final ResultSet rs) throws SQLException {
		List<DataColumn> columns = new ArrayList<ResultSetTable.DataColumn>();
		ResultSetMetaData meta = rs.getMetaData();
		for (int i = 1 ; i <= meta.getColumnCount(); i++) {
			DataColumn column = new DataColumn(meta.getColumnName(i), meta.getColumnType(i));
			columns.add(column);
		}
		return columns;
	}
	private static List<Object> createRecord(final ResultSet rs, final List<DataColumn> columns) throws SQLException {
		List<Object> data = new ArrayList<Object>();
		for (int i = 0 ; i < columns.size() ; i++) {
			data.add(rs.getObject(i+1));
		}
		return data;
	}

	protected void doInit() {
		pnlSplitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		pnlSplitMain.setContinuousLayout(true);

		pnlSplitSubLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		pnlSplitSubLeft.setContinuousLayout(true);

		pnlSplitSubRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		pnlSplitSubRight.setContinuousLayout(true);

		txtSQL = new SQLTextPane();
		txtConsole = new LoggerConsole();
		logger = txtConsole;
		
		tabSub = new JTabbedPane();

		pnlScrollSQL = new JScrollPane(txtSQL);
		pnlScrollConsole = new JScrollPane(txtConsole);
		
		pnlDBObjectType = new DBObjectTypePanel();
		pnlDBObjectType.setDatabaseModel(new OracleDatabaseModel(DatabaseManager.getDatasource("DBNAME")));
		pnlDBObjectType.setUser(DatabaseManager.getDatasource("DBNAME").getDatabase().getUser());

		tblDBObjectList = new DBObjectListPanel();
		
		tblResult = new ResultSetTable();

		pnlScrollSQL.setRowHeaderView(new TextLineNumberView(txtSQL));

		tabSub.add(LabelManager.get("Tab.Title.Console"), pnlScrollConsole);
		tabSub.add(LabelManager.get("Tab.Title.DataGrid"), tblResult);
		// pnlScrollExplain

		pnlSplitSubRight.setTopComponent(pnlDBObjectType);
		pnlSplitSubRight.setBottomComponent(tblDBObjectList);
		pnlSplitSubRight.setDividerLocation(120);
		
		pnlSplitSubLeft.setTopComponent(pnlScrollSQL);
		pnlSplitSubLeft.setBottomComponent(tabSub);
		pnlSplitMain.setLeftComponent(pnlSplitSubRight);
		pnlSplitMain.setRightComponent(pnlSplitSubLeft);
		add(pnlSplitMain);

		txtSQL.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getModifiers() == KeyEvent.CTRL_MASK) {
					if (e.getKeyCode() == KeyEvent.VK_R) {
						doQuery(txtSQL.getText());
					}else if (e.getKeyCode() == KeyEvent.VK_P) {
						doExplain(txtSQL.getText());
					}
				}
			}
		});
		pnlDBObjectType.addDBObjectTypeListener(new DBObjectTypePanel.DBObjectTypeListener() {
			@Override
			public void dbObjectTypeChanged(final DBObjectTypePanel panel) {
				String user = panel.getUser();
				String type = panel.getType();

				try {
				DatabaseModel model = new OracleDatabaseModel(DatabaseManager.getDatasource("DBNAME"));
				model.getObjectList(user, type);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
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
		
		
		menuFileDBConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doDatabaseList();
			}
		});
	}

	protected void doInitStatusBar(final StatusBar statusBar) {
		lblStatus = new LabelStatusItem();
		statusBar.add(lblStatus);

		prgStatus = new JProgressBar();
		prgStatus.setSize(160, 10);
		prgStatus.setIndeterminate(false); // 確定
		statusBar.add(prgStatus);
	}

	protected void doResized(final Dimension dimension) {
		pnlSplitMain.setSize(dimension);
		pnlSplitMain.setDividerLocation(200);
		pnlSplitSubLeft.setDividerLocation(getHeight() / 2);
	}
}
