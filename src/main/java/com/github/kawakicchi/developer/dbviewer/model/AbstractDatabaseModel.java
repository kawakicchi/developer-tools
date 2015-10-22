package com.github.kawakicchi.developer.dbviewer.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDatabaseModel implements DatabaseModel {

	protected final void release(final PreparedStatement statment) {
		if (null != statment) {
			try {
				statment.close();
			} catch (SQLException ex) {
				fatal(ex);
			}
		}
	}
	
	protected final void release(final ResultSet resultSet) {
		if (null != resultSet) {
			try {
				resultSet.close();
			} catch (SQLException ex) {
				fatal(ex);
			}
		}
	}
	
	protected final void fatal(final Throwable t) {
		t.printStackTrace();
	}
}
