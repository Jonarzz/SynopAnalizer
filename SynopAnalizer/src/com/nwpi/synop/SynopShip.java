package com.nwpi.synop;

import java.util.ArrayList;

public class SynopShip extends SynopMobile {
	
	public SynopShip(ArrayList<String> stringArray, String fileName) {
		super(stringArray, fileName);
	}
	
	protected void setPressureString() {
		String temp;
		
		if (stringArray.size() < 10 || !stringIsValid((temp = stringArray.get(9))) || 
				temp.charAt(0) != '4' || temp.charAt(1) != '0' ||
					temp.charAt(1) != '9')
			return;
		
		pressureString = temp;
	}
}
