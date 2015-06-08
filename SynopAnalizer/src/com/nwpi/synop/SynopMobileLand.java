package com.nwpi.synop;

import java.util.ArrayList;

import com.nwpi.Constants;

public class SynopMobileLand extends SynopMobile {

	public SynopMobileLand(ArrayList<String> stringArray) {
		super(stringArray);
	}
	
	protected void setTemperature() {
		temperature = Constants.INITIAL_VALUE;
	}
}
