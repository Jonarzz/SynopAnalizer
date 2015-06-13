package com.nwpi.synop;

import java.util.ArrayList;

public class SynopMobileLand extends SynopMobile {

	public SynopMobileLand(ArrayList<String> stringArray, String fileName) {
		super(stringArray, fileName);
	}
	
	protected void setPressureString() {
		return;
	}
}
