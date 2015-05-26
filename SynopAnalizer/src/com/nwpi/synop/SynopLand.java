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
	
	public int getStationCode() {
		return stationCode;
	}
	
}
