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
		
		String tempString;
		String command = "INSERT INTO stations (type, code) VALUES (\'AAXX\', ";
		
		if ((tempString = synop.getStationCode()) != null)
			command += "\'" + tempString + "\');";
		else
			command += "NULL);";

		return command;
	}
	
	private String stationQuery(SynopMobile synop) {
		if ((stationID = sqlqs.getStationID(synop.getStationCode())) > 0)
			return null;
		
		stationID *= -1;
		
		int tempInt;
		float tempFloat;
		String tempString;
		String command = "INSERT INTO stations (type, code, latitude, longitude, v_quadr, h_quadr) VALUES (";
		
		if ((tempString = synop.getStationType()) == null)
			command += "NULL, ";
		else
			command += "\'" + tempString + "\', ";
		
		if ((tempString = synop.getStationCode()) != null)
			command += "\'" + tempString + "\', ";
		else
			command += "NULL, ";
		
		if ((tempFloat = synop.getLatitude()) != Constants.INITIAL_VALUE)
			command += tempFloat + ", ";
		else
			command += "NULL, ";
		
		if ((tempFloat = synop.getLongitude()) != Constants.INITIAL_VALUE)
			command += tempFloat + ", ";
		else
			command += "NULL, ";
		
		if ((tempInt = synop.getVerticalQuadrantMultiplier()) != Constants.INITIAL_VALUE)
			command += tempInt + ", ";
		else
			command += "NULL, ";
		
		if ((tempInt = synop.getHorizontalQuadrantMultiplier()) != Constants.INITIAL_VALUE)
			command += tempInt + ");";
		else
			command += "NULL);";

		return command;
	}
	
	private String dateAndWeatherQuery(Synop synop) {
		int tempInt;
		float tempFloat;
		String tempString;
		
		String command = "INSERT INTO date_and_weather (year, month, day, hour, station_id, " +
				"wind_unit, temperature, horizontal_visibility, overcast, wind_direction, wind_speed, pressure) VALUES (";
		
		if ((tempInt = synop.getYear()) != Constants.INITIAL_VALUE)
			command += tempInt + ", ";
		else
			command += "NULL, ";
		
		if ((tempInt = synop.getMonth()) != Constants.INITIAL_VALUE)
			command += tempInt + ", ";
		else
			command += "NULL, ";
		
		if ((tempInt = synop.getDay()) != Constants.INITIAL_VALUE)
			command += tempInt + ", ";
		else
			command += "NULL, ";
		
		if ((tempInt = synop.getHour()) != Constants.INITIAL_VALUE)
			command += tempInt + ", "; 
		else
			command += "NULL, ";
		
		command += stationID + ", ";
		
		if ((tempInt = synop.getWindIndicator()) == Constants.INITIAL_VALUE)
			command += "NULL, ";
		else
			if (tempInt == Constants.WS_ANEMOMETER_IN_KNOT || tempInt == Constants.WS_WILDTYPE_IN_KNOT)
				command += "\'knot\', ";
			else
				command += "\'mps\', ";
		
		if ((tempFloat = synop.getTemperature()) != Constants.INITIAL_VALUE)
			command += tempFloat + ", ";
		else
			command += "NULL, ";
		
		if ((tempString = synop.getHorizontalVisibility()) != null)
			command += "\'" + tempString + "\', ";
		else
			command += "NULL, ";
		
		if ((tempString = synop.getOvercast()) != null)
			command += "\'" + tempString + "\', "; 
		else
			command += "NULL, ";
		
		if ((tempInt = synop.getWindDirection()) != Constants.INITIAL_VALUE)
			command += tempInt + ", ";
		else
			command += "NULL, ";
		
		if ((tempInt = synop.getWindSpeed()) != Constants.INITIAL_VALUE)
			command += tempInt + ", ";
		else
			command += "NULL, ";
		
		if ((tempFloat = synop.getPressure()) != Constants.INITIAL_VALUE)
			command += tempFloat + ");";
		else
			command += "NULL);";

		return command;
	}
}