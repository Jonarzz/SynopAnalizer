package com.nwpi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import com.nwpi.synop.Synop;

public class XMLMarshaller {
	
	public static void saveSynopsToXML(ArrayList<Synop> synopList, String file) throws IOException {
		String fileName = file + ".xml";
		FileWriter fw = new FileWriter(fileName);
		
		for (Synop synop : synopList)
			marshall(synop, fw);
		
		fw.close();
	}
	
	private static void marshall(Synop synop, FileWriter fw) throws IOException {
		StringWriter sw = new StringWriter();
		JAXBContext context;
		
		try {
			context = JAXBContext.newInstance(Synop.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(new JAXBElement<Synop>(new QName(Synop.class.getSimpleName()), Synop.class, synop), sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		fw.write(sw.toString());
	}
	
}
