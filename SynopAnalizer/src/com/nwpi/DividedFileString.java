package com.nwpi;

import java.util.ArrayList;
import java.util.Arrays;

public class DividedFileString {
	
	private ArrayList<String> stringsFromFile;
	private ArrayList<ArrayList<String>> dividedStringsArray;
	
	public DividedFileString(ArrayList<String> stringsFromFile) {
		this.stringsFromFile = stringsFromFile;
		
		createDividedStringsArray();
	}
	
	public void createDividedStringsArray() {
		dividedStringsArray = new ArrayList<ArrayList<String>>();
		
		for (String line : stringsFromFile) {
			String splitString[] = line.split(" ");
			dividedStringsArray.add(new ArrayList<String>(Arrays.asList(splitString)));
		}
	}
	
	public int length() {
		return stringsFromFile.size();
	}
	
	public ArrayList<ArrayList<String>> getDividedStringsArray() {
		return dividedStringsArray;	
	}
	
	public ArrayList<String> getSingleDividedString(int index) {
		return dividedStringsArray.get(index);
	}
}