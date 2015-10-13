package com.emc.monitor.jobs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.HttpServiceUtils;

public class XcpMonitor implements Job {

	private DocumentumService ds;
	private static final String XCP_INFO = "/products/xcp_product_info";

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		Set<DocumentumService> sds;
		String result = null;
		sds = DocumentumService.getInstance().getServicesByType("xcp");
		Iterator<DocumentumService> it = sds.iterator();
//		int i = 0;
//		String url;
		while (it.hasNext()) {
			ds = it.next();
			System.out.println(ds.getType());

			try {
				result = getStatus();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (isRunning(result)) {
				ds.updateStatus(true, result);
			} else {
				ds.updateStatus(false, result);
			}
		}
	}

	private boolean isRunning(String result) {
		if (result == "Failed") {
			return false;
		} else {
			return true;
		}
	}

	private String getStatus() throws Exception {
		String response = HttpServiceUtils.sendRequest(ds.getHost(), ds.getPort(), "http", "/".concat(ds.getName()).concat(XCP_INFO), ds.getUser(), ds.getPassword());
		if (response != "Failed") {
			response = readResponse(response);
		}
		System.out.println("Response: " + response);
		return response;
	}

	private String readResponse(String response) {
		String version = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
			Document xmlDoc = dBuilder.parse(is);
			xmlDoc.getDocumentElement().normalize();
			System.out.println("Root element: " + xmlDoc.getDocumentElement().getNodeName());
			version = xmlDoc.getElementsByTagName("dm:major").item(0).getTextContent().concat(".")
					.concat(xmlDoc.getElementsByTagName("dm:minor").item(0).getTextContent()).concat(".")
					.concat(xmlDoc.getElementsByTagName("dm:build_number").item(0).getTextContent());
			System.out.println(version);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return version.toString();
	}
}
