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
	private SQLConnectionPool sqlcp;
	
	public SynopProcessorThread(File file, SQLConnectionPool sqlcp, SynopAnalizerController sac) {
		this.sac = sac;
		this.sqlcp = sqlcp;

		try {
			sfh = new SingleFileHandler(file);
		} catch (FileNotFoundException e) {
			sac.increaseFilesNotFound();
		}

		synopList = sfh.getSynopObjectList();
	}
	
	public void run() {		
		SQLQuerySender sqlqs = new SQLQuerySender(sqlcp);
		processor = new SynopProcessor(sqlqs);
		sqlqs.createStatement();
		
		for (Synop synop : synopList) 
			processor.sendSynopToDatabase(synop);	
		
		sqlqs.sendStatements();
		sqlqs.closeConnection();
		
		sac.increaseProcessedSynopLists();
	}
}
