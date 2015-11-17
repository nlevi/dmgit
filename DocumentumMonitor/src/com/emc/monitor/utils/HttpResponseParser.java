package com.emc.monitor.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public final class HttpResponseParser {
	
final static Logger logger = Logger.getLogger(HttpResponseParser.class);
	
	private HttpResponseParser() {
		
	}
	
	public static String getVersionFromResponse(String response) {
		return response.replaceAll("[^0-9&&[^\\.]]", "");
	}
	
	public static String getVersionFromResponse(String response, String[] tags) {
		String version = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
			Document xmlDoc = dBuilder.parse(is);
			xmlDoc.getDocumentElement().normalize();			
			version = xmlDoc.getElementsByTagName(tags[0]).item(0).getTextContent().concat(".")
					.concat(xmlDoc.getElementsByTagName(tags[1]).item(0).getTextContent()).concat(".")
					.concat(xmlDoc.getElementsByTagName(tags[2]).item(0).getTextContent());			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return version;
	}

}
