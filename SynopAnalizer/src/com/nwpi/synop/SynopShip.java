package com.nwpi.synop;

import java.util.ArrayList;

import com.nwpi.Constants;

public class SynopShip extends SynopMobile {
	
	public SynopShip(ArrayList<String> stringArray) {
		super(stringArray);
	}
	
	protected void setTemperature() {
		String tempString;
		float temp = 0;
		
		if (stringArray.size() < 8 || stringArray.get(7).length() != 5) {
			temperature = Constants.INITIAL_VALUE;
			return;
		}

		tempString = stringArray.get(7).substring(2, 5);
		if (stringArray.get(7).charAt(1) == '0')
			temperature = 1;
		else
			temperature = -1;
		
		try {
			temp = Float.parseFloat(tempString) / 10;
			temperature *= temp;
		} catch (NumberFormatException e) {
			temperature = Constants.INITIAL_VALUE;
		}	
	}
}
