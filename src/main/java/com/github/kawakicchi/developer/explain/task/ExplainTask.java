package com.github.kawakicchi.developer.explain.task;

import java.sql.Connection;

import com.github.kawakicchi.developer.database.Datasource;
import com.github.kawakicchi.developer.explain.Explain;
import com.github.kawakicchi.developer.explain.ExplainResult;
import com.github.kawakicchi.developer.task.AbstractTask;

public final class ExplainTask extends AbstractTask {

	private String sql;
	private Datasource datasource;
	private ExplainResult result;

	public ExplainTask(final String sql, final Datasource datasource) {
		this.sql = sql;
		this.datasource = datasource;
	}

	@Override
	protected void doExecute() {
		Connection connection = null;
		try {
			connection = datasource.getConnection();
			Explain explain = new Explain(connection);
			result = explain.explain(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			datasource.returnConnection(connection);
		}
	}

	public ExplainResult getResult() {
		return result;
	}
}
