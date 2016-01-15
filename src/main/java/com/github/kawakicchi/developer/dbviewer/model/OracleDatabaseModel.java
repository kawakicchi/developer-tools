package com.github.kawakicchi.developer.dbviewer.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.kawakicchi.developer.database.Datasource;

public final class OracleDatabaseModel extends AbstractDatabaseModel {

	private Datasource datasource;

	public OracleDatabaseModel(final Datasource datasource) {
		this.datasource = datasource;
	}

	@Override
	public List<String> getObjectTypeList() throws SQLException {
		List<String> types = new ArrayList<String>();
		types.add("TABLE");
		types.add("VIEW");
		types.add("SYNONYM");
		types.add("TABLE|VIEW|SYNONYM");
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

		String sql = "SELECT username AS name FROM all_users ORDER BY name";

		Connection connection = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			connection = datasource.getConnection();
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
			datasource.returnConnection(connection);
		}
		return users;
	}

	public List<ObjectEntity> getObjectList(final String user, final String type) throws SQLException {
		List<ObjectEntity> objects = new ArrayList<ObjectEntity>();

		Connection connection = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			connection = datasource.getConnection();
			switch (type) {
			case "TABLE":
				stat = createTableObjectPrepareStatement(user, connection);
				break;
			case "VIEW":
				stat = createViewObjectPrepareStatement(user, connection);
				break;
			default:
				System.out.println(String.format("Undefined type.[%s]", type));
				break;
			}
			if (null != stat) {
				rs = stat.executeQuery();
				while (rs.next()) {
					System.out.println(rs.getString("COMMENTS"));
				}
			}
		} finally {
			release(rs);
			release(stat);
			datasource.returnConnection(connection);
		}

		return objects;
	}

	private PreparedStatement createTableObjectPrepareStatement(final String user, final Connection c) throws SQLException {
		StringBuffer s = new StringBuffer();
		s.append("SELECT ");
		s.append("    TBL.TABLE_NAME       AS TABLE_NAME ");
		s.append("  , CMT.COMMENTS         AS COMMENTS ");
		s.append("  , TBL.TABLESPACE_NAME  AS TABLESPACE_NAME ");
		s.append("  , TBL.STATUS           AS STATUS ");
		s.append("FROM ");
		s.append("    DBA_OBJECTS      OBJ ");
		s.append("  , DBA_TABLES       TBL ");
		s.append("  , DBA_TAB_COMMENTS CMT ");
		s.append("WHERE ");
		s.append("    OBJ.OWNER       = ? ");
		s.append("AND OBJ.OBJECT_TYPE = 'TABLE' ");
		s.append("AND OBJ.OWNER       = TBL.OWNER ");
		s.append("AND OBJ.OBJECT_NAME = TBL.TABLE_NAME ");
		s.append("AND TBL.OWNER       = CMT.OWNER ");
		s.append("AND OBJ.OBJECT_TYPE = CMT.TABLE_TYPE ");
		s.append("AND TBL.TABLE_NAME  = CMT.TABLE_NAME ");
		s.append("ORDER BY ");
		s.append("    TBL.TABLE_NAME ");

		PreparedStatement ps = c.prepareStatement(s.toString());
		ps.setString(1, user);
		return ps;
	}

	private PreparedStatement createViewObjectPrepareStatement(final String user, final Connection c) throws SQLException {
		StringBuffer s = new StringBuffer();
		s.append("SELECT ");
		s.append("    VIW.VIEW_NAME  AS VIEW_NAME ");
		s.append("  , CMT.COMMENTS   AS COMMENTS ");
		s.append("FROM ");
		s.append("    DBA_OBJECTS      OBJ ");
		s.append("  , DBA_VIEWS        VIW ");
		s.append("  , DBA_TAB_COMMENTS CMT ");
		s.append("WHERE ");
		s.append("    OBJ.OWNER       = ? ");
		s.append("AND OBJ.OBJECT_TYPE = 'VIEW' ");
		s.append("AND OBJ.OWNER       = VIW.OWNER ");
		s.append("AND OBJ.OBJECT_NAME = VIW.VIEW_NAME ");
		s.append("AND VIW.OWNER       = CMT.OWNER ");
		s.append("AND OBJ.OBJECT_TYPE = CMT.TABLE_TYPE ");
		s.append("AND VIW.VIEW_NAME   = CMT.TABLE_NAME ");
		s.append("ORDER BY ");
		s.append("    VIW.VIEW_NAME  ");

		PreparedStatement ps = c.prepareStatement(s.toString());
		ps.setString(1, user);
		return ps;
	}
}
