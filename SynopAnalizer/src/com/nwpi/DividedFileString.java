package com.nwpi;

import java.util.ArrayList;
import java.util.Arrays;

public class DividedFileString {
	
	private ArrayList<String> stringList;
	private ArrayList<ArrayList<String>> stringArrayList;
	
	public DividedFileString(ArrayList<String> stringList) {
		this.stringList = stringList;
		
		createStringArrayList();
	}
	
	public void createStringArrayList() {
		stringArrayList = new ArrayList<ArrayList<String>>();
		
		for (String line : stringList) {
			String splitString[] = line.split(" ");
			stringArrayList.add(new ArrayList<String>(Arrays.asList(splitString)));
		}
	}
	
	public int length() {
		return stringList.size();
	}
	
	public ArrayList<ArrayList<String>> getStringArrayList() {
		return stringArrayList;	
	}
	
	public ArrayList<String> getSingleStringArray(int index) {
		return stringArrayList.get(index);
	}
}