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
	private SQLQuerySender sqlqs;
	
	public SynopProcessorThread(File file, SQLQuerySender sqlqs, SynopAnalizerController sac) {
		this.sac = sac;
		this.sqlqs = sqlqs;

		try {
			sfh = new SingleFileHandler(file);
		} catch (FileNotFoundException e) {
			sac.increaseFilesNotFound();
		}

		synopList = sfh.getSynopObjectList();
		processor = new SynopProcessor(sqlqs);
	}
	
	public void run() {
		sqlqs.createStatement();
		for (Synop synop : synopList) 
			processor.sendSynopToDatabase(synop);	
		sqlqs.sendStatements();
		
		sac.increaseProcessedSynopLists();
	}
}
