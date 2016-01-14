package com.github.kawakicchi.developer.explain.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.kawakicchi.developer.database.Datasource;
import com.github.kawakicchi.developer.task.AbstractTask;

public final class QueryTask extends AbstractTask {

	private String sql;
	private Datasource datasource;
	private List<QueryTaskListener> listeners;

	public QueryTask(final String sql, final Datasource datasource) {
		this.sql = sql;
		this.datasource = datasource;
		listeners = new ArrayList<QueryTask.QueryTaskListener>();
	}

	public void addQueryTaskListener(final QueryTaskListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	protected void doExecute() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = datasource.getConnection();

			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();

			synchronized (listeners) {
				for (QueryTaskListener listener : listeners) {
					listener.queryTaskResultQuery(resultSet);
				}
			}

			while (resultSet.next()) {
				synchronized (listeners) {
					for (QueryTaskListener listener : listeners) {
						listener.queryTaskRecord(resultSet);
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			datasource.returnConnection(connection);
		}
	}
	
	public static interface QueryTaskListener {
		public void queryTaskResultQuery(final ResultSet resultSet) throws SQLException;
		public void queryTaskRecord(final ResultSet resultSet) throws SQLException;
	}
}
