package nl.dctm.monitor.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
			if (logger.isDebugEnabled()) {
				logger.debug("Response to parse: " + response);
				logger.debug("Parsed response: " + version);
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return version;
	}
	
	public static String getVersionFromResponse(String response, String tag, int position) {
		String version = null;
		List<String> versions = new ArrayList<>();
		
		org.jsoup.nodes.Document doc;
		doc = Jsoup.parse(response);
		
		Elements modules = doc.select(tag);
		for (Element module : modules) {
			String tmp = module.text().replaceAll("[^0-9&&[^\\.]]", "");
			if (tmp.length() == 13) {
				versions.add(tmp);
			}
		}
		
		version = versions.get(position);

		if (logger.isDebugEnabled()) {
			logger.debug("Response to parse: " + response);
			logger.debug("Parsed response: " + version);
		}
		return version;
	}

}
