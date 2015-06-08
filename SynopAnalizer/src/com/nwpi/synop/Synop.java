package com.nwpi.synop;

import java.util.ArrayList;

import com.nwpi.Constants;

public abstract class Synop {
	
	protected ArrayList<String> stringArray;
	
	protected String stationType;
	
	protected int dayOfMonth;
	protected int hourOfObservation;
	protected int windIndicator;
	
	protected float temperature;
	
	public Synop(ArrayList<String> stringArray) {		
		this.stringArray = stringArray;

		setStationType();
		setDayHourAndWindIndicator();
		setTemperature();
	}
	
	protected void setStationType() {
		if (stringArray.get(0).length() == 4)
			stationType = stringArray.get(0);
		else
			stationType = "";
	}
	
	protected void setDayHourAndWindIndicator() {
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
	
	protected abstract void setTemperature();

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
	
	public abstract float getTemperature();
	
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	
	public int getHourOfObservation() {
		return hourOfObservation;
	}
	
	public int getWindIndicator() {
		return windIndicator;
	}
	
	public abstract String getTemperatureAsString();

	public String getDayAsString() {
		return Integer.toString(dayOfMonth);
	}
	
	public String getHourAsString() {
		return Integer.toString(hourOfObservation);
	}
	
	public String getWindIndicatorAsString() {
		return Integer.toString(windIndicator);
	}
}
