package com.nwpi.synop;

import java.util.ArrayList;

public class Synop {
	
	protected ArrayList<String> stringArray;
	
	protected int dayOfMonth;
	protected int hourOfObservation;
	protected int windIndicator;
	
	public Synop(ArrayList<String> stringArray) {		
		this.stringArray = stringArray;
		getDayHourAndWindIndicator();
	}
	
	protected void getDayHourAndWindIndicator() {
		String dayHourAndWindIndicator = "/";
		
		if (this instanceof SynopLand && stringArray.size() > 1)
			dayHourAndWindIndicator = stringArray.get(1);
		else if (stringArray.size() > 2)
			dayHourAndWindIndicator = stringArray.get(2);
		
		if (testString(dayHourAndWindIndicator))
			return;
		
		dayOfMonth = Integer.parseInt(dayHourAndWindIndicator.substring(0, 2));
		hourOfObservation = Integer.parseInt(dayHourAndWindIndicator.substring(2, 4));
		windIndicator = Character.getNumericValue(dayHourAndWindIndicator.charAt(4));
	}
	
	private boolean testString(String str) {
		for (char c : str.toCharArray())
			if (c == '/')
				return true;
		
		return false;		
	}
	
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	
	public int getHourOfObservation() {
		return hourOfObservation;
	}
	
	public int getWindIndicator() {
		return windIndicator;
	}
}
