package com.nwpi.synop;

import java.util.ArrayList;

import com.nwpi.Constants;

public class SynopLand extends Synop {
	
	private int stationCode;

	public SynopLand(ArrayList<String> stringArray) {
		super(stringArray);
		
		setStationCode();
	}
	
	private void setStationCode() {
		if (stringArray.size() < 3)
			return;
		
		try {
			stationCode = Integer.parseInt(stringArray.get(2));
		} catch (NumberFormatException e) {
			stationCode = Constants.INITIAL_STATION_CODE;
		}
	}
	
	protected void setTemperature() {
		String tempString;
		float temp = 0;
		
		if (stringArray.size() < 6 || stringArray.get(5).length() != 5) {
			temperature = Constants.INITIAL_VALUE;
			return;
		}
		
		if (stringArray.get(5).charAt(0) == '0') {
			if (stringArray.size() < 7 || stringArray.get(6).length() != 5) {
				temperature = Constants.INITIAL_VALUE;
				return;
			}
			
			tempString = stringArray.get(6).substring(2, 5);
			if (stringArray.get(6).charAt(1) == '0')
				temperature = 1;
			else
				temperature = -1;
		}
		else {
			tempString = stringArray.get(5).substring(2, 5);
			if (stringArray.get(5).charAt(1) == '0')
				temperature = 1;
			else
				temperature = -1;
		}
		
		try {
			temp = Float.parseFloat(tempString) / 10;
			temperature *= temp;
		} catch (NumberFormatException e) {
			temperature = Constants.INITIAL_VALUE;
		}
	}
	
	public int getStationCode() {
		return stationCode;
	}	
	
	public float getTemperature() {
		return temperature;
	}
	
	public String getStationCodeAsString() {
		return Integer.toString(stationCode);
	}
	
	public String getTemperatureAsString() {
		if (temperature == Constants.INITIAL_VALUE)
			return Integer.toString((int)temperature);
		
		return Float.toString(temperature);
	}
}
