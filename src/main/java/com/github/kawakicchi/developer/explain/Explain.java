package com.github.kawakicchi.developer.explain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Explain {

	public static void main(final String[] args) {
		LabelManager.getInstance().load();
		MessageManager.getInstance().load();

		ExplainFrame frm = new ExplainFrame();
		frm.setVisible(true);
	}

	private Connection connection;

	public Explain(final Connection connection) {
		this.connection = connection;
	}

	public ExplainResult explain(final String sql) {
		ExplainResult result = new ExplainResult();

		String executeSql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			executeSql = String.format("EXPLAIN PLAN FOR %s", sql);
			ps = connection.prepareStatement(executeSql);
			ps.execute();
			ps.close();
			ps = null;

			executeSql = "SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY('PLAN_TABLE',NULL,'SERIAL'))";
			ps = connection.prepareStatement(executeSql);
			rs = ps.executeQuery();
			parse(rs, result);
			rs.close();
			rs = null;
			ps.close();
			ps = null;

		} catch (SQLException ex) {
			System.out.println(executeSql);
			ex.printStackTrace();
		} finally {
			try {
				if (null != rs && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			try {
				if (null != ps && !ps.isClosed()) {
					ps.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return result;
	}

	private void parse(final ResultSet rs, final ExplainResult result) throws SQLException {
		int col = 1;
		while (rs.next()) {
			String s = rs.getString(col);
			result.println(s);

			if (s.startsWith("-----")) {
				break;
			}
		}
		List<String> headers = new ArrayList<String>();
		if (rs.next()) {
			String s = rs.getString(col);
			result.println(s);

			String[] ss = s.split("\\|");
			for (String header : ss) {
				headers.add(header.trim());
				//System.out.println(String.format("header : %s", header));
			}
		}
		while (rs.next()) {
			String s = rs.getString(col);
			result.println(s);

			if (s.startsWith("-----")) {
				break;
			}
		}
		List<List<String>> datas = new ArrayList<List<String>>();
		while (rs.next()) {
			String s = rs.getString(col);
			result.println(s);

			if (s.startsWith("-----")) {
				break;
			}
			System.out.println(s);
			String[] ss = s.split("\\|");
			List<String> data = new ArrayList<String>();
			for (String dt : ss) {
				data.add(dt);
				//System.out.println("data : " + dt);
			}
			datas.add(data);
		}

		while (rs.next()) {
			String s = rs.getString(col);
			result.println(s);

			System.out.println(s);
		}
	}
}
