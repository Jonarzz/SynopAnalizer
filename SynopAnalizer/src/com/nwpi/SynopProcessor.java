package com.nwpi;


import java.util.ArrayList;

import com.nwpi.synop.Synop;
import com.nwpi.synop.SynopLand;
import com.nwpi.synop.SynopMobile;
import com.nwpi.synop.SynopShip;

public class SynopProcessor {	
	
	private SQLQuerySender sqlqs;
	
	public SynopProcessor() {
		sqlqs = new SQLQuerySender();
	}
	
	public void sendSynopListToDatabase(ArrayList<Synop> synopList) {		
		for (Synop synop : synopList) 
			if (synop instanceof SynopLand)
				analizeSynop((SynopLand)synop);
			else
				analizeSynop((SynopMobile)synop);
	}
	
	public void closeSQLConnection() {
		sqlqs.closeConnection();
	}

	private void analizeSynop(SynopLand synop) {
		int stationID;
		
		sqlqs.addStatement(stationQuery((SynopLand) synop));
		
		if ((stationID = getStationIDFromDatabase(synop)) != -1)
			sqlqs.addStatement(dayHourAndWindIndicatorQuery(synop, stationID));
	}
	
	private void analizeSynop(SynopMobile synop) {
		int stationID;
		
		sqlqs.addStatement(stationQuery((SynopMobile) synop));
		
		if ((stationID = getStationIDFromDatabase(synop)) != -1)
			sqlqs.addStatement(dayHourAndWindIndicatorQuery(synop, stationID));
	}
	
	private String stationQuery(SynopLand synop) {
		if (getStationIDFromDatabase(synop) != -1)
			return null;		
		
		String command = "INSERT INTO stations (code, type) VALUES (" + synop.getStationCode() + ", \'AAXX\');";
		
		return command;
	}
	
	private String stationQuery(SynopMobile synop) {
		if (getStationIDFromDatabase(synop) != -1)
			return null;
		
		String command = "INSERT INTO stations (type, latitude, longitude, v_quadr, h_quadr) VALUES ("; 
		
		if (synop instanceof SynopShip)
			command += "\'BBXX\', ";
		else
			command += "\'OOXX\', ";
		
		command += synop.getLatitudeAsString() + ", " + synop.getLongitudeAsString() + ", " 
				+ synop.getVertQMultiplierAsString() + ", " + synop.getHorQMultiplierAsString() + ");";
		
		return command;
	}
	
	private String dayHourAndWindIndicatorQuery(Synop synop, int stationID) {	
		String command = "INSERT INTO date (day, hour, station_id) VALUES (" + synop.getDayAsString() + ", " +
				synop.getHourAsString() + ", " + Integer.toString(stationID) + "); ";
		
		command += "INSERT INTO weather (wind_unit, station_id) VALUES (";
		
		if (synop.getWindIndicator() == Constants.WS_ANEMOMETER_IN_MPS || 
				synop.getWindIndicator() == Constants.WS_WILDTYPE_IN_MPS)
			command += "\'mps\', " + Integer.toString(stationID) + ");";
		else if (synop.getWindIndicator() == Constants.WS_ANEMOMETER_IN_KNOT || 
				synop.getWindIndicator() == Constants.WS_WILDTYPE_IN_KNOT)
			command += "\'knot\', " + Integer.toString(stationID) + ");";
		else
			command += "NULL, " + Integer.toString(stationID) + ");";
		
		return command;
	}
	
	private int getStationIDFromDatabase(SynopLand synop) {
		return sqlqs.getStationID(synop.getStationCodeAsString());
	}
	
	private int getStationIDFromDatabase(SynopMobile synop) {
		return sqlqs.getStationID(synop.getLatitude(), synop.getLongitude(), 
				synop.getVerticalQuadrantMultiplier(), synop.getHorizontalQuadrantMultiplier());
	}
}