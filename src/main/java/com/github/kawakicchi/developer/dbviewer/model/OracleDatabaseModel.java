package com.github.kawakicchi.developer.dbviewer.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class OracleDatabaseModel extends AbstractDatabaseModel {

	@Override
	public List<User> getUserList(final Connection connection) throws SQLException {
		List<User> users = new ArrayList<User>();

		String sql = "SELECT username AS name FROM dba_users order by name";

		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			stat = connection.prepareStatement(sql);
			rs = stat.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setName(rs.getString("name"));
				users.add(user);
			}
		} finally {
			release(rs);
			release(stat);
		}

		return users;
	}

}
