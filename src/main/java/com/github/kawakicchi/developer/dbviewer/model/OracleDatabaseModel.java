package com.github.kawakicchi.developer.dbviewer.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class OracleDatabaseModel extends AbstractDatabaseModel {

	private Connection connection;

	public OracleDatabaseModel(final Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<String> getTypeList() throws SQLException {
		List<String> types = new ArrayList<String>();
		types.add("TABLE");
		types.add("VIEW");
		types.add("SYNONYM");
		types.add("INDEX");
		types.add("CLUSTER");
		types.add("SEQUENCE");
		types.add("DATABASE LINK");
		types.add("SNAPSHOT");
		types.add("SNAPSHOT LOG");
		types.add("PACKAGE");
		types.add("PACKAGE BODY");
		types.add("FUNCTION");
		types.add("PROCEDURE");
		types.add("TRIGGER");
		types.add("TYPE");
		types.add("TYPE BODY");
		types.add("LIBRARY");
		return types;
	}

	@Override
	public List<UserEntity> getUserList() throws SQLException {
		List<UserEntity> users = new ArrayList<UserEntity>();

		String sql = "SELECT username AS name FROM all_users order by name";

		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			stat = connection.prepareStatement(sql);
			rs = stat.executeQuery();
			while (rs.next()) {
				UserEntity user = new UserEntity();
				user.setName(rs.getString("name"));
				users.add(user);
			}
		} finally {
			release(rs);
			release(stat);
		}

		return users;
	}

	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}

}
