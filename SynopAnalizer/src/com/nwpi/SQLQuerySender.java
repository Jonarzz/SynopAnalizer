package com.nwpi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLQuerySender {
	
	private final String SQL_URL = "jdbc:postgresql://localhost:5432/SynopBase";
	private final String SQL_USERNAME = "postgres";
	private final String SQL_PASSWORD = "vonsledz";
	
	private Connection connection;
	private Statement statement;

	public SQLQuerySender() {
		createConnection();
	}
	
	private void createConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(SQL_URL, SQL_USERNAME, SQL_PASSWORD);
			connection.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addStatement(String command) {
		if (command == null)
			return;
		
		createStatement();
		
		try {
			statement.executeUpdate(command);
		} catch (SQLException e) {
			System.out.println(command);
			e.printStackTrace();
		}
	}
	
	public int getStationID(int stationCode) {
		String command = "SELECT station_id FROM stations WHERE code = \'" + stationCode + "\';";
		
		int stationID = -1;
		
		createStatement();
		
		try {
			ResultSet rs = statement.executeQuery(command);
			while (rs.next())
				stationID = Integer.parseInt(rs.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (stationID == -1) {
			command = "SELECT CURRVAL(\'stations_station_id_seq\'::regclass) FROM stations;";
			try {
				ResultSet rs = statement.executeQuery(command);
				if (rs.next())
					stationID = -1 * (Integer.parseInt(rs.getString(1)) + 1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return stationID;
	}
	
	private void createStatement() {
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
