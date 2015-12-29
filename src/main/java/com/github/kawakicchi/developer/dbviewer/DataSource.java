package com.github.kawakicchi.developer.dbviewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DataSource {

	private static final DataSource INSTANCE = new DataSource();

	private DataSource() {
	}

	public static DataSource getInstance() {
		return INSTANCE;
	}

	public Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "";
			String user = "";
			String password = "";
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return connection;
	}

	public void returnConnection(final Connection connection) {
		if (null != connection) {
			try {
				connection.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

}
