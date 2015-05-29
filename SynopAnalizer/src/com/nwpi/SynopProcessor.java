package com.nwpi;


import java.util.ArrayList;

import com.nwpi.synop.Synop;
import com.nwpi.synop.SynopLand;
import com.nwpi.synop.SynopMobile;
import com.nwpi.synop.SynopShip;

public class SynopProcessor {
	
	public static int station_id;
	
	public SQLQuerySender sqlqs;
	
	public SynopProcessor() {
		sqlqs = new SQLQuerySender();
	}
	
	public void sendSynopListToDatabase(ArrayList<Synop> synopList) {		
		for (Synop synop : synopList) 
			analizeSynop(synop);
		
		sqlqs.closeConnection();
	}

	private void analizeSynop(Synop synop) {
		//sqlqs.addStatement(dayHourAndWindIndicatorQuery(synop));
		
		if (synop instanceof SynopLand)
			sqlqs.addStatement(stationQuery((SynopLand) synop));
		else
			sqlqs.addStatement(stationQuery((SynopMobile) synop));
	}
	// TODO 
	private String dayHourAndWindIndicatorQuery(Synop synop) {
		return "";
	}
	
	private String stationQuery(SynopLand synop) {
		String command = "INSERT INTO stations (code, type, station_id) VALUES (" + synop.getStationCode() + ", \'AAXX\', ";
		
		if (sqlqs.getStationID(synop.getStationCodeAsString()) != -1)
			return "";		
		else
			command += Integer.toString(++station_id) + ");";
			
		return command;
	}
	
	private String stationQuery(SynopMobile synop) {
		String command = "INSERT INTO stations (station_id, type, latitude, longitude, v_quadr, h_quadr) VALUES ("; 
		
		if (sqlqs.getStationID(synop.getLatitude(), synop.getLongitude(), 
				synop.getVerticalQuadrantMultiplier(), synop.getHorizontalQuadrantMultiplier()) != -1)
			return "";
		else
			command += Integer.toString(++station_id) + ", "; 
		
		if (synop instanceof SynopShip)
			command += "\'BBXX\', ";
		else
			command += "\'OOXX\', ";
		
		command += synop.getLatitudeAsString() + ", " + synop.getLongitudeAsString() + ", " 
				+ synop.getVertQMultiplierAsString() + ", " + synop.getHorQMultiplierAsString() + ");";
		
		return command;
	}	
}