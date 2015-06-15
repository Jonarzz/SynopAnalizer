package com.nwpi;

import com.nwpi.synop.Synop;
import com.nwpi.synop.SynopLand;
import com.nwpi.synop.SynopMobile;

public class SynopProcessor {	
	
	private SQLQuerySender sqlqs;
	
	private int stationID;

	public SynopProcessor(SQLQuerySender sqlqs) {
		this.sqlqs = sqlqs;
	}
	
	public void sendSynopToDatabase(Synop synop) {	
		if (sqlqs.isDisconnected())
			return;
		
		analizeSynop(synop);
	}
	
	public void closeSQLConnection() {
		sqlqs.closeConnection();
	}

	private void analizeSynop(Synop synop) {
		if (synop instanceof SynopLand)
			sqlqs.addStatement(stationQuery((SynopLand)synop));
		else
			sqlqs.addStatement(stationQuery((SynopMobile)synop));
			
		sqlqs.addStatement(dateAndWeatherQuery(synop));
	}
	
	private String stationQuery(SynopLand synop) {
		if ((stationID = sqlqs.getStationID(synop.getStationCode())) > 0)
			return null;
		
		stationID *= -1;
		
		String command = "INSERT INTO stations (type, code) VALUES (\'AAXX\', ";
		
		if (synop.getStationCode() != null)
			command += "\'" + synop.getStationCode() + "\');";
		else
			command += "NULL);";

		return command;
	}
	
	private String stationQuery(SynopMobile synop) {
		if ((stationID = sqlqs.getStationID(synop.getStationCode())) > 0)
			return null;
		
		stationID *= -1;
		
		String command = "INSERT INTO stations (type, code, latitude, longitude, v_quadr, h_quadr) VALUES (";
		
		if (synop.getStationType() == null)
			command += "NULL, ";
		else
			command += "\'" + synop.getStationType() + "\', ";
		
		if (synop.getStationCode() != null)
			command += "\'" + synop.getStationCode() + "\', ";
		else
			command += "NULL, ";
		
		if (synop.getLatitude() != Constants.INITIAL_VALUE)
			command += synop.getLatitude() + ", ";
		else
			command += "NULL, ";
		
		if (synop.getLongitude() != Constants.INITIAL_VALUE)
			command += synop.getLongitude() + ", ";
		else
			command += "NULL, ";
		
		if (synop.getVerticalQuadrantMultiplier() != Constants.INITIAL_VALUE)
			command += synop.getVerticalQuadrantMultiplier() + ", ";
		else
			command += "NULL, ";
		
		if (synop.getHorizontalQuadrantMultiplier() != Constants.INITIAL_VALUE)
			command += synop.getHorizontalQuadrantMultiplier() + ");";
		else
			command += "NULL);";

		return command;
	}
	
	private String dateAndWeatherQuery(Synop synop) {
		int windIndicator;
		
		String command = "INSERT INTO date_and_weather (year, month, day, hour, station_id, " +
				"wind_unit, temperature, horizontal_visibility, overcast, wind_direction, wind_speed, pressure) VALUES (";
		
		if (synop.getYear() != Constants.INITIAL_VALUE)
			command += synop.getYear() + ", ";
		else
			command += "NULL, ";
		
		if (synop.getMonth() != Constants.INITIAL_VALUE)
			command += synop.getMonth() + ", ";
		else
			command += "NULL, ";
		
		if (synop.getDay() != Constants.INITIAL_VALUE)
			command += synop.getDay() + ", ";
		else
			command += "NULL, ";
		
		if (synop.getHour() != Constants.INITIAL_VALUE)
			command += synop.getHour() + ", "; 
		else
			command += "NULL, ";
		
		command += stationID + ", ";
		
		if ((windIndicator = synop.getWindIndicator()) == Constants.INITIAL_VALUE)
			command += "NULL, ";
		else
			if (windIndicator == Constants.WS_ANEMOMETER_IN_KNOT || windIndicator == Constants.WS_WILDTYPE_IN_KNOT)
				command += "\'knot\', ";
			else
				command += "\'mps\', ";
		
		if (synop.getTemperature() != Constants.INITIAL_VALUE)
			command += synop.getTemperature() + ", ";
		else
			command += "NULL, ";
		
		if (synop.getHorizontalVisibility() != null)
			command += "\'" + synop.getHorizontalVisibility() + "\', ";
		else
			command += "NULL, ";
		
		if (synop.getOvercast() != null)
			command += "\'" + synop.getOvercast() + "\', "; 
		else
			command += "NULL, ";
		
		if (synop.getWindDirection() != Constants.INITIAL_VALUE)
			command += synop.getWindDirection() + ", ";
		else
			command += "NULL, ";
		
		if (synop.getWindSpeed() != Constants.INITIAL_VALUE)
			command += synop.getWindSpeed() + ", ";
		else
			command += "NULL, ";
		
		if (synop.getPressure() != Constants.INITIAL_VALUE)
			command += synop.getPressure() + ");";
		else
			command += "NULL);";

		return command;
	}
}