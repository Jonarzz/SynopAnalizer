package com.nwpi.synop;

import java.util.ArrayList;

public class SynopShip extends SynopMobile {
	
	public SynopShip(ArrayList<String> stringArray, String fileName) {
		super(stringArray, fileName);
	}
	
	protected void setPressureString() {
		if (stringArray.size() < 10 || stringArray.get(9).charAt(0) != '4' || 
				stringArray.get(9).charAt(1) != '0' || stringArray.get(9).charAt(1) != '9' ||
					!stringIsValid(stringArray.get(9)))
			return;
		
		pressureString = stringArray.get(9);
	}
}
