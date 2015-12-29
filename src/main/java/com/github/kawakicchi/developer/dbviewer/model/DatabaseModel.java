package com.github.kawakicchi.developer.dbviewer.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseModel {

	public List<String> getTypeList() throws SQLException;

	public List<UserEntity> getUserList() throws SQLException;
	
	public PreparedStatement prepareStatement(final String sql) throws SQLException;
}
