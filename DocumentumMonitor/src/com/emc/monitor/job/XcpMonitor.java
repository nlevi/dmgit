package com.emc.monitor.job;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpHost;
import org.apache.tomcat.util.codec.binary.Base64;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DatabaseUtil;

public class XcpMonitor implements Job {

	private DocumentumService ds;

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		Set<DocumentumService> sds;

		String result = null;

		sds = DocumentumService.getServicesByType("xcp");

		Iterator it = sds.iterator();
		int i = 0;
		String url;
		while (it.hasNext()) {
			ds = (DocumentumService) it.next();

			try {
				result = getStatus();
			} catch (Exception e) {
				e.printStackTrace();
			}

			 if (isRunning(result)) {
			 ds.update(true, result);
			 } else {
			 ds.update(false, result);
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

		String response = sendRequest();
		
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
			System.out.println("Root element :" + xmlDoc.getDocumentElement().getNodeName());
			version = xmlDoc.getElementsByTagName("dm:major").item(0).getTextContent().concat(".")
					.concat(xmlDoc.getElementsByTagName("dm:minor").item(0).getTextContent()).concat(".")
					.concat(xmlDoc.getElementsByTagName("dm:build_number").item(0).getTextContent());
			System.out.println(version);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return version.toString();

	}

	private String sendRequest() {
		String result = null;

		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {

			HttpClientContext context = HttpClientContext.create();

			CookieStore cookieStore = new BasicCookieStore();

			context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

			HttpHost targetXcpAppHost = new HttpHost(ds.getHost(), ds.getPort(), "http");
			HttpGet request = new HttpGet("/".concat(ds.getName()).concat("/products/xcp_product_info"));
			String pwd = getEncodedCredentials();
			System.out.println(pwd);
			request.addHeader("Authorization", "Basic " + getEncodedCredentials());
			request.addHeader("Accept", "text/html,application/xml,*/*");

			System.out.println("Executing request " + request + " to " + targetXcpAppHost);
			CloseableHttpResponse response;

			response = httpclient.execute(targetXcpAppHost, request, context);

			int responseStatusCode = response.getStatusLine().getStatusCode();
			System.out.println("GET Response Status:: " + responseStatusCode);

			if (responseStatusCode == 200) {

				BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String inputLine;
				StringBuffer sb = new StringBuffer();
				while ((inputLine = br.readLine()) != null) {
					sb.append(inputLine);
				}
				result = sb.toString();
				System.out.println("Response: " + result);
			} else {
				result = "Failed";
			}

			EntityUtils.consume(response.getEntity());
			response.close();

		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Response: " + result);
		return result;
	}

	private String getEncodedCredentials() {
		String encodedPwd = new String(
				Base64.encodeBase64(ds.getUser().concat(":").concat(ds.getPassword()).getBytes()),
				StandardCharsets.UTF_8);
		return encodedPwd;
	}

	private String getUrl(DocumentumService service) throws SQLException {

		String url = "http://" + service.getHost() + ":" + service.getPort() + "/dsearch";

		ResultSet rs = DatabaseUtil
				.executeSelect("SELECT service_host, service_port FROM mntr_env_details WHERE service_type = 'xplore'");
		try {
			while (rs.next()) {
				url = "http://" + rs.getString(1) + ":" + rs.getString(2) + "/dsearch";
				System.out.println(url);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return url;
	}

}
