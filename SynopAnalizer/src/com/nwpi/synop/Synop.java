package com.nwpi.synop;

import java.util.ArrayList;

public class Synop {
	
	protected ArrayList<String> stringArray;
	
	protected int dayOfMonth;
	protected int hourOfObservaton;
	protected char windIndicator;
	
	public Synop(ArrayList<String> stringArray) {
		this.stringArray = stringArray;
	}
	
	protected void getDayHourAndWindIndicator() {
		String dayHourAndWindIndicator;
		
		if (this instanceof SynopLand)
			dayHourAndWindIndicator = stringArray.get(1);
		else
			dayHourAndWindIndicator = stringArray.get(2);
		
		dayOfMonth = Integer.parseInt(dayHourAndWindIndicator.substring(0, 2));
		hourOfObservaton = Integer.parseInt(dayHourAndWindIndicator.substring(2, 4));
		windIndicator = dayHourAndWindIndicator.charAt(4);
	}

}
