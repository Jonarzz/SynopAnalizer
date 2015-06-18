package com.nwpi.sql;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class SQLConnectionPool {
	
	private final String SQL_DRIVER = "org.postgresql.Driver";
	private final String SQL_URL = "jdbc:postgresql://localhost:5432/SynopBase";
	private final String SQL_USERNAME = "postgres";
	private final String SQL_PASSWORD = "vonsledz";
	
	private ComboPooledDataSource cpds;
	
	public SQLConnectionPool() {
		cpds = new ComboPooledDataSource();
		createConnection();
	}
	
	private void createConnection() {
		try {
			cpds.setDriverClass(SQL_DRIVER);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		cpds.setJdbcUrl(SQL_URL);
		cpds.setUser(SQL_USERNAME);
		cpds.setPassword(SQL_PASSWORD);
	}
	
	public Connection getConnection() throws SQLException {
		return cpds.getConnection(); 
	}
	
	public void closeConnection() {
		cpds.close();
	}
}
