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
	
	public SingleFileHandler(File file) throws FileNotFoundException {
		this.file = file;

		createDividedFileString();
		createSynopObjectList();
	}
	
	private void createDividedFileString() throws FileNotFoundException {
		dividedFileString = new DividedFileString(FileInputHandler.divideFileIntoStrings(file));
	}
	
	private void createSynopObjectList() {
		synopObjectList = new ArrayList<Synop>();
		
		for (int i = 0; i < dividedFileString.length(); i++)
			synopObjectList.add(createSynopObject(i));
	}
	
	private Synop createSynopObject(int index) {
		getCurrentStringArray(index);
		trimStringArray();
		
		if (currentStringArray.get(0).equals(Constants.LAND_STATION_CODE))
			return new SynopLand(currentStringArray, file.getName());
		else if (currentStringArray.get(0).equals(Constants.SHIP_STATION_CODE))
			return new SynopShip(currentStringArray, file.getName());
		else 
			return new SynopMobileLand(currentStringArray, file.getName());
	}
	
	private void getCurrentStringArray(int index) {
		currentStringArray = dividedFileString.getSingleDividedString(index);
	}
	
	private void trimStringArray() {
		currentStringArray.remove("NIL");
	}
	
	public ArrayList<Synop> getSynopObjectList() {
		return synopObjectList;
	}
}