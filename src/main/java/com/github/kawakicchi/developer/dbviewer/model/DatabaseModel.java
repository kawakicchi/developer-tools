package com.github.kawakicchi.developer.dbviewer.model;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseModel {

	public List<String> getObjectTypeList() throws SQLException;

	public List<UserEntity> getUserList() throws SQLException;
	
}
