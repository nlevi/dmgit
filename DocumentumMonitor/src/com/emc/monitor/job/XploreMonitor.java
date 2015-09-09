package com.emc.monitor.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DatabaseUtil;

public class XploreMonitor {

	private final String USER_AGENT = "Mozilla/5.0";
	private DocumentumService[] ds;

	public void worker() throws Exception {
		Set<DocumentumService> sds;
		DocumentumService tempds;
		String result = null;

		sds = DocumentumService.getServicesByType("xplore");

		Iterator it = sds.iterator();
		int i = 0;
		String url;
		while (it.hasNext()) {
			tempds = (DocumentumService) it.next();
			url = "http://" + tempds.getHost() + ":" + tempds.getPort() + "/dsearch";

			result = getStatus(url);

			if (isRunning(result)) {
				tempds.update(true, result);
			} else {
				tempds.update(false, result);
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

	private String getStatus(String url) throws Exception {

		String response = sendRequest(url);

		String version;

		if (response.length() > 3) {
			System.out.println(response);
			version = response.replaceAll("[^0-9&&[^\\.]]", "");
			System.out.println(version);
		} else {
			version = "Failed";
		}
		return version;
	}

	private String sendRequest(String url) {

		URL serviceurl = null;
		try {
			serviceurl = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		HttpURLConnection con = null;
		int responseCode = 0;
		StringBuffer response = new StringBuffer();

		try {
			con = (HttpURLConnection) serviceurl.openConnection();

			con.setRequestMethod("GET");

			con.setRequestProperty("User-Agent", USER_AGENT);
			System.out.println("\nSending 'GET' request to URL : " + url);
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			System.out.println("Cannot connect to " + url);

		} finally {

			System.out.println("Response Code : " + responseCode);

			if (responseCode == 259) {

				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(con.getInputStream()));

					String inputLine;

					while ((inputLine = br.readLine()) != null) {
						response.append(inputLine);
					}

					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				response.append(responseCode);
			}
		}

		return response.toString();
	}

	private String getUrl(DocumentumService service) throws SQLException {

		String url = "http://" + service.getHost() + ":" + service.getPort() + "/dsearch";

		 ResultSet rs = DatabaseUtil
		 .executeSelect("SELECT service_host, service_port FROM mntr_env_details WHERE service_type = 'xplore'");
		 try {
		 while (rs.next()) {
		 url = "http://" + rs.getString(1) + ":" + rs.getString(2)+
		 "/dsearch";
		 System.out.println(url);
		 }
		 rs.close();
		 } catch (SQLException e) {
		 e.printStackTrace();
		 }

		return url;
	}

}
