package com.nwpi.synop;

import java.util.ArrayList;

import com.nwpi.Constants;

public class SynopLand extends Synop {
	
	private String temp;
	
	private int rainfall;
	private String rainfallString;
	
	public SynopLand(ArrayList<String> stringArray, String fileName) {
		super(stringArray, fileName);
		
		setRainfall();
	}
	
	protected void setStationCode() {
		if (stringArray.size() < 3 || (temp = stringArray.get(2)).length() > 10 || temp.contains("/"))
			return;
		
		stationCode = temp;
	}
	
	protected void setHorizontalVisibilityInt() {
		if (stringArray.size() < 4 || !stringIsValid((temp = stringArray.get(3)))) {
			horizontalVisibilityInt = Constants.INITIAL_VALUE;
			return;
		}
		
		try {
			horizontalVisibilityInt = Integer.parseInt(temp.substring(3, 5));
		} catch (NumberFormatException e) {
			horizontalVisibilityInt = Constants.INITIAL_VALUE;
		}
	}
	
	protected void setTemperatureString() {
		if (stringArray.size() < 6 || !stringIsValid(stringArray.get(5)))
			return;
		
		if (stringArray.get(5).charAt(0) == '0') {
			if (stringArray.size() < 7 || !stringIsValid(stringArray.get(6)))
				return;
			
			temperatureString = stringArray.get(6).substring(1, 5);
		}
		else if (stringIsValid(stringArray.get(5)))
			temperatureString = stringArray.get(5).substring(1, 5);
	}
	
	protected void setWindString() {
		if (stringArray.size() < 5 || !stringIsValid((temp = stringArray.get(4))))
			return;
		
		windString = temp;
	}
	
	protected void setPressureString() {
		if (stringArray.size() < 8 || stringArray.get(7).charAt(0) != '3' || !stringIsValid((temp = stringArray.get(7))))
				return;
		
		pressureString = temp;
	}
	
	private void setRainfall() {
		setRainfallString();
		
		if (rainfallString == null) {
			rainfall = Constants.INITIAL_VALUE;
			return;
		}
		
		try {
			rainfall = Integer.parseInt(rainfallString.substring(1, 4));
			if (rainfall >= 990)
				rainfall = 0;
		} catch (NumberFormatException e) {
			rainfall = Constants.INITIAL_VALUE;
		}
	}
	
	protected void setRainfallString() {
		if (stringArray.size() < 11 || stringArray.get(10).charAt(0) != '6' || !stringIsValid((temp = stringArray.get(10))))
			return;
		
		rainfallString = temp;
	}
	
	public String getStationCode() {
		return stationCode;
	}	
	
	public int getRainfall() {
		return rainfall;
	}
}
