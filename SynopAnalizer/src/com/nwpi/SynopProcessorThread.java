package com.nwpi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.nwpi.synop.Synop;
import com.nwpi.view.SynopAnalizerController;

public class SynopProcessorThread  implements Runnable {
	
	private SynopAnalizerController sac;
	private SynopProcessor processor;
	private SingleFileHandler sfh;
	private ArrayList<Synop> synopList;
	
	public SynopProcessorThread(File file, SQLQuerySender sqlqs, SynopAnalizerController sac) {
		this.sac = sac;

		try {
			sfh = new SingleFileHandler(file);
		} catch (FileNotFoundException e) {
			sac.increaseFilesNotFound();
		}

		synopList = sfh.getSynopObjectList();
		processor = new SynopProcessor(sqlqs);
	}
	
	public void run() {
		for (Synop synop : synopList)
			processor.sendSynopToDatabase(synop);
		
		sac.increaseProcessedSynopLists();
	}
}