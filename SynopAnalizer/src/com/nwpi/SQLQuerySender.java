package com.nwpi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLQuerySender {
	
	private Connection connection;
	private Statement statement;

	public SQLQuerySender(SQLConnectionPool sqlcp) {
		createConnection(sqlcp);
	}
	
	private void createConnection(SQLConnectionPool sqlcp) {
		try {
			connection = sqlcp.getConnection();
			connection.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createStatement() {
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addStatement(String command) {
		if (command == null)
			return;
		
		try {
			statement.addBatch(command);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void sendStatements() {
		try {
			statement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getStationID(String stationCode) {
		String command = "SELECT station_id FROM stations WHERE code = \'" + stationCode + "\';";
		
		int stationID = -1;
		
		try {
			Statement localStatement = connection.createStatement();
			ResultSet rs = localStatement.executeQuery(command);
			if (rs.next())
				stationID = Integer.parseInt(rs.getString(1));
			else {
				command = "SELECT last_value FROM stations_station_id_seq;";
				try {
					rs = localStatement.executeQuery(command);
					if (rs.next())
						stationID = -1 * (Integer.parseInt(rs.getString(1)) + 1);
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return stationID;
	}
	
	public void closeConnection() {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isDisconnected() {
		try {
			if (connection.isClosed())
				return true;
		} catch (SQLException e) {
			return true;
		}
		
		return false;
	}
}
