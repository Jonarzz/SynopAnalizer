package com.nwpi.synop;

import java.util.ArrayList;

import com.nwpi.Constants;

public abstract class SynopMobile extends Synop {
	
	private float latitude;
	private float longitude;
	
	private int verticalQuadrantMultiplier;
	private int horizontalQuadrantMultiplier;
	
	public SynopMobile(ArrayList<String> stringArray, String fileName) {
		super(stringArray, fileName);
		
		setLatitude();
		setLongitudeAndQuadrant();
	}
	
	protected void setStationCode() {
		if (stringArray.size() < 2 || stringArray.get(1).length() > 10)
			return;
		
		stationCode = stringArray.get(1);
	}
	
	protected void setHorizontalVisibilityInt() {
		if (stringArray.size() < 6 || !stringIsValid(stringArray.get(5))) {
			horizontalVisibilityInt = Constants.INITIAL_VALUE;
			return;
		}
		
		try {
			horizontalVisibilityInt = Integer.parseInt(stringArray.get(5).substring(3, 5));
		} catch (NumberFormatException e) {
			horizontalVisibilityInt = Constants.INITIAL_VALUE;
		}
	}
	
	protected void setTemperatureString() {	
		if (stringArray.size() < 8 || !stringIsValid(stringArray.get(7)))
			return;

		temperatureString = stringArray.get(7).substring(1, 5);
	}
	
	protected void setWindString() {
		if (stringArray.size() < 7 || !stringIsValid(stringArray.get(6)))
			return;
		
		windString = stringArray.get(6);
	}
	
	protected abstract void setPressureString();
	
	protected void setLatitude() {
		if (stringArray.size() < 4 || !stringIsValid(stringArray.get(3)))
			return;
		
		String latitudeString = stringArray.get(3).substring(2, 5);
		int temp = 0;
		
		try {
			temp = Integer.parseInt(latitudeString);
		} catch (NumberFormatException e) {
			latitude = Constants.INITIAL_VALUE;
			return;
		}
		
		latitude = (float)temp / 10;
	}
	
	protected void setLongitudeAndQuadrant() {
		if (stringArray.size() < 5 || !stringIsValid(stringArray.get(4)))
			return;
		
		String longitudeString = stringArray.get(4);
		
		if (longitudeString.length() != 5)
			return;
		
		setQuadrantMultipliers(longitudeString.charAt(0));
		setLongitude(longitudeString.substring(1, 5));
	}
	
	protected void setQuadrantMultipliers(char q) {
		switch (q) {
			case '1':
				verticalQuadrantMultiplier = 1;
				horizontalQuadrantMultiplier = 1;
				break;
			case '3':
				verticalQuadrantMultiplier = -1;
				horizontalQuadrantMultiplier = 1;
				break;
			case '5':
				verticalQuadrantMultiplier = -1;
				horizontalQuadrantMultiplier = -1;
				break;
			case '7':
				verticalQuadrantMultiplier = 1;
				horizontalQuadrantMultiplier = -1;
				break;
			default:
				verticalQuadrantMultiplier = 0;
				horizontalQuadrantMultiplier = 0;
				break;
		}
	}
	
	protected void setLongitude(String str) {
		int temp = 0;
		
		try {
			temp = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			longitude = Constants.INITIAL_VALUE;
			return;
		}
		
		longitude = (float)temp / 10;
	}

	public float getLatitude() {
		return latitude;
	}
	
	public float getLongitude() {
		return longitude;
	}
	
	public int getVerticalQuadrantMultiplier() {
		return verticalQuadrantMultiplier;
	}
	
	public int getHorizontalQuadrantMultiplier() {
		return horizontalQuadrantMultiplier;
	}
}
