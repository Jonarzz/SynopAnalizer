package com.nwpi.synop;

import java.util.ArrayList;

public class SynopMobile extends Synop {
	
	protected String stationCode;

	public SynopMobile(ArrayList<String> stringArray) {
		super(stringArray);
	}
	
	protected void getStationCode() {
		stationCode = stringArray.get(1);
	}
	
}
