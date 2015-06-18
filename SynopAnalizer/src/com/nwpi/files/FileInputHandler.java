package com.nwpi.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileInputHandler {
	
	public static ArrayList<String> divideFileIntoStrings(File file) throws FileNotFoundException {
		ArrayList<String> stringList = new ArrayList<String>();
		String line;
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    while ((line = br.readLine()) != null) 
		       stringList.add(line);  
		} catch (IOException e) {
			System.out.println("IOException was caught!");
			e.printStackTrace();
		}
		
		return stringList;
	}
}
