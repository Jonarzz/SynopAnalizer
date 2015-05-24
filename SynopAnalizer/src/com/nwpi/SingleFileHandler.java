package com.nwpi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.nwpi.synop.*;

public class SingleFileHandler {
	
	private File file;
	private DividedFileString dividedFileString;
	private ArrayList<String> currentStringArray;
	
	private ArrayList<Synop> synopObjectList;
	
	private int currentIndex;
	
	public SingleFileHandler(File file) {
		this.file = file;

		createDividedFileString();
		createSynopObjectList();
	}
		
	private void createDividedFileString() {
		try {
			dividedFileString = new DividedFileString(FileInputHandler.divideFileIntoStrings(file));
		} catch (FileNotFoundException e) {
			// TODO error dialog
			e.printStackTrace();
		}
	}
	
	private void createSynopObjectList() {
		synopObjectList = new ArrayList<Synop>();
		
		while (currentIndex < dividedFileString.length()) {
			synopObjectList.add(createSynopObject());
			currentIndex++;
		}
	}
	
	private Synop createSynopObject() {
		getCurrentStringArray();
		
		if (currentStringArray.get(0).equals(Constants.LAND_STATION_CODE))
			return new SynopLand(currentStringArray);
		else if (currentStringArray.get(0).equals(Constants.SHIP_STATION_CODE))
			return new SynopShip(currentStringArray);
		else 
			return new SynopMobileLand(currentStringArray);
	}
	
	private void getCurrentStringArray() {
		currentStringArray = dividedFileString.getSingleStringArray(currentIndex);
	}
	
	public ArrayList<Synop> getSynopObjectList() {
		return synopObjectList;
	}	
}