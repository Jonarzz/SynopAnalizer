package com.nwpi;

import com.nwpi.synop.Synop;
import com.nwpi.synop.SynopLand;
import com.nwpi.synop.SynopMobile;
//TODO add commands sending Synop data to database
public class SynopProcessor {	
	
	private SQLQuerySender sqlqs;
	
	private int stationID;

	public SynopProcessor(SQLQuerySender sqlqs) {
		this.sqlqs = sqlqs;
	}
	
	public void sendSynopListToDatabase(Synop synop) {	
		if (sqlqs.isDisconnected())
			return;
		
		if (synop instanceof SynopLand)
			analizeSynop((SynopLand)synop);
		else
			analizeSynop((SynopMobile)synop);
	}
	
	public void closeSQLConnection() {
		sqlqs.closeConnection();
	}

	private void analizeSynop(SynopLand synop) {
		sqlqs.addStatement(stationQuery(synop));

		//sqlqs.addStatement(dayHourAndWindIndicatorQuery(synop));
		//sqlqs.addStatement(temperatureQuery(synop, stationID));
	}
	
	private void analizeSynop(SynopMobile synop) {
		//sqlqs.addStatement(stationQuery(synop));
		
		//sqlqs.addStatement(dayHourAndWindIndicatorQuery(synop));
//		sqlqs.addStatement(temperatureQuery(synop, stationID));
	}
	
	private String stationQuery(Synop synop) {
		if ((stationID = sqlqs.getStationID(synop.getStationCode())) > 0)
			return null;
		
		stationID *= -1;
		
		String command = "INSERT INTO stations (type, code) VALUES (\'" + synop.getStationType() + "\', \'" + synop.getStationCode() + "\');";
		
		return command;
	}
	
	// test method
//	private String dayHourAndWindIndicatorQuery(Synop synop) {
//		String command = "INSERT INTO date (day) VALUES (" + stationID + ");";
//		
//		return command;
//	}
	
//	private String stationQuery(SynopMobile synop) {		
//		String command = "INSERT INTO stations (type, latitude, longitude, v_quadr, h_quadr) SELECT "; 
//		
//		if (synop instanceof SynopShip)
//			command += "\'BBXX\', " + synop.getLatitude() + ", " + synop.getLongitude() + ", " +
//					synop.getVerticalQuadrantMultiplier() + ", " + synop.getHorizontalQuadrantMultiplier() + 
//					"WHERE NOT EXISTS(SELECT * FROM stations WHERE type = \'BBXX\' AND latitude = " + synop.getLatitude() +
//					" AND longitude = " + synop.getLongitude() + " AND v_quadr = " + synop.getVerticalQuadrantMultiplier() + 
//					" AND h_quadr = " + synop.getHorizontalQuadrantMultiplier() + ");";
//		else
//			command += "\'OOXX\', " + synop.getLatitude() + ", " + synop.getLongitude() + ", " +
//					synop.getVerticalQuadrantMultiplier() + ", " + synop.getHorizontalQuadrantMultiplier() + 
//					"WHERE NOT EXISTS(SELECT * FROM stations WHERE type = \'OOXX\' AND latitude = " + synop.getLatitude() +
//					" AND longitude = " + synop.getLongitude() + " AND v_quadr = " + synop.getVerticalQuadrantMultiplier() + 
//					" AND h_quadr = " + synop.getHorizontalQuadrantMultiplier() + ");";
//		
//		return command;
//	}
	
//      private String dayHourAndWindIndicatorQuery(SynopLand synop) {	
//		String command = "INSERT INTO date (day, hour, station_id) VALUES (" + synop.getDayOfMonth() + ", " +
//				synop.getHourOfObservation() + ", " + stationID + ");";
//		
//		command += "INSERT INTO weather (wind_unit, station_id) VALUES (\'mps\', " + stationID + ");";
//		
//		return command;
		
//		String command = "INSERT INTO date (day, hour, station_id) SELECT " + synop.getDayOfMonth() + ", " +
//				synop.getHourOfObservation() + ", (SELECT station_id FROM stations WHERE code = \'" + synop.getStationCode() + "\');";
//		
//		command += "INSERT INTO weather (wind_unit, station_id) SELECT ";
//				
//		if (synop.getWindIndicator() == Constants.WS_ANEMOMETER_IN_MPS || 
//				synop.getWindIndicator() == Constants.WS_WILDTYPE_IN_MPS)
//			command += "\'mps\', (SELECT station_id FROM stations WHERE code = \'" + synop.getStationCode() + "\');";
//		else if (synop.getWindIndicator() == Constants.WS_ANEMOMETER_IN_KNOT || 
//				synop.getWindIndicator() == Constants.WS_WILDTYPE_IN_KNOT)
//			command += "\'knot\', (SELECT station_id FROM stations WHERE code = \'" + synop.getStationCode() + "\');";
//		else
//			command += "NULL, (SELECT station_id FROM stations WHERE code = \'" + synop.getStationCode() + "\');";
//		
//		return command;
//	}
	
//	private String dayHourAndWindIndicatorQuery(SynopMobile synop) {	
//		String command = "INSERT INTO date (day, hour, station_id) SELECT " + synop.getDayOfMonth() + ", " +
//				synop.getHourOfObservation() + ", (SELECT station_id FROM stations WHERE code = \'" + synop.getStationCode() + "\');";
//		
//		command += "INSERT INTO weather (wind_unit, station_id) SELECT ";
//				
//		if (synop.getWindIndicator() == Constants.WS_ANEMOMETER_IN_MPS || 
//				synop.getWindIndicator() == Constants.WS_WILDTYPE_IN_MPS)
//			command += "\'mps\', (SELECT station_id FROM stations WHERE code = \'" + synop.getStationCode() + "\');";
//		else if (synop.getWindIndicator() == Constants.WS_ANEMOMETER_IN_KNOT || 
//				synop.getWindIndicator() == Constants.WS_WILDTYPE_IN_KNOT)
//			command += "\'knot\', (SELECT station_id FROM stations WHERE code = \'" + synop.getStationCode() + "\');";
//		else
//			command += "NULL, (SELECT station_id FROM stations WHERE code = \'" + synop.getStationCode() + "\');";
//		
//		return command;
//	}
	
	private String temperatureQuery(Synop synop, int stationID) {
		float temperature = synop.getTemperature();

		if (temperature == Constants.INITIAL_VALUE)
			return "UPDATE weather SET temperature = NULL WHERE station_id = " + stationID; 

		String command = "UPDATE weather SET temperature = " + temperature + " WHERE station_id = " + stationID;
		
		return command;
	}
}