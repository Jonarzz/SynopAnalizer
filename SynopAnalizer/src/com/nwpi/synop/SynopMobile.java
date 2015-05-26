package com.nwpi.synop;

import java.util.ArrayList;

import com.nwpi.Constants;

public class SynopMobile extends Synop {
	
	private float latitude;
	private float longitude;
	
	private int verticalQuadrantMultiplier;
	private int horizontalQuadrantMultiplier;
	
	public SynopMobile(ArrayList<String> stringArray) {
		super(stringArray);
		
		setLatitude();
		setLongitudeAndQuadrant();
	}
	
	protected void setLatitude() {
		if (stringArray.size() < 4)
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
		if (stringArray.size() < 5)
			return;
		
		String longitudeString = stringArray.get(4);
		
		setQuadrantMultipliers(longitudeString.charAt(0));
		setLongitude(longitudeString.substring(1, 5));
	}
	
	protected void setQuadrantMultipliers(char q) {
		switch (q) {
			case 1:
				verticalQuadrantMultiplier = 1;
				horizontalQuadrantMultiplier = 1;
				break;
			case 3:
				verticalQuadrantMultiplier = -1;
				horizontalQuadrantMultiplier = 1;
				break;
			case 5:
				verticalQuadrantMultiplier = -1;
				horizontalQuadrantMultiplier = -1;
				break;
			case 7:
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
