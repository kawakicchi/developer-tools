package com.github.kawakicchi.developer.dbviewer.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseModel {

	public static class User {
		private String name;
		public void setName(final String name) {
			this.name = name;
		}
		public String getName() {
			return this.name;
		}
	}
	
	public List<User> getUserList(final Connection connection) throws SQLException;
}
