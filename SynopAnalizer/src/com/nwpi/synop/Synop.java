package com.nwpi.synop;

import java.util.ArrayList;

import com.nwpi.Constants;

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
		String dayHourAndWindIndicator = "";
		
		if (this instanceof SynopLand && stringArray.size() > 1)
			dayHourAndWindIndicator = stringArray.get(1);
		else if (stringArray.size() > 2)
			dayHourAndWindIndicator = stringArray.get(2);
		
		if (dayHourAndWindIndicator.length() != 5)
			return;
		
		setDayOfMonth(dayHourAndWindIndicator);
		setHourOfObservation(dayHourAndWindIndicator);
		setWindIndicator(dayHourAndWindIndicator);
	}

	private void setDayOfMonth(String str) {
		try {
			dayOfMonth = Integer.parseInt(str.substring(0, 2));
		} catch (NumberFormatException e) {
			dayOfMonth = Constants.INITIAL_VALUE;
		}
	}
	
	private void setHourOfObservation(String str) {
		try {
			hourOfObservation = Integer.parseInt(str.substring(2, 4));
		} catch (NumberFormatException e) {
			hourOfObservation = Constants.INITIAL_VALUE;
		}
	}
	
	private void setWindIndicator(String str) {
		try {
			windIndicator = Character.getNumericValue(str.charAt(4));
		} catch (NumberFormatException e) {
			windIndicator = Constants.INITIAL_VALUE;
		}
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
