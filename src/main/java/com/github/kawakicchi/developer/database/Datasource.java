package com.github.kawakicchi.developer.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;

public class Datasource {

	private DatabaseEntity entity;
	private DataSource datasource;

	public Datasource(final DatabaseEntity entity, DataSource datasource) {
		this.entity = entity;
		this.datasource = datasource;
	}

	public DatabaseEntity getDatabase() {
		return this.entity;
	}

	public Connection getConnection() throws SQLException {
		Connection connection = this.datasource.getConnection();
		connection.setAutoCommit(false);
		return connection;
	}

	public void returnConnection(final Connection connection) {
		if (null != connection) {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
