package com.emc.monitor.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DatabaseUtil;

public class XploreMonitor implements Job {

	private final String USER_AGENT = "Mozilla/5.0";
	private DocumentumService ds;

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		Set<DocumentumService> sds;

		String result = null;

		sds = DocumentumService.getServicesByType("xplore");

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

		String version;

		if (response != "Failed") {
			System.out.println(response);
			version = response.replaceAll("[^0-9&&[^\\.]]", "");
			System.out.println(version);
		} else {
			version = "Failed";
		}
		return version;
	}

	private String sendRequest() {

		String result = null;

		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {

			HttpClientContext context = HttpClientContext.create();

			CookieStore cookieStore = new BasicCookieStore();

			context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

			HttpHost targetHost = new HttpHost(ds.getHost(), ds.getPort(), "http");
			HttpGet request = new HttpGet("/dsearch");
			String pwd = getEncodedCredentials();
			System.out.println(pwd);
			request.addHeader("Authorization", "Basic " + getEncodedCredentials());
			request.addHeader("Accept", "text/html,application/xml,*/*");

			System.out.println("Executing request " + request + " to " + targetHost);
			CloseableHttpResponse response = null;
			int responseStatusCode = 0;
			try {

				response = httpclient.execute(targetHost, request, context);
			} catch (IOException e) {
				System.out.println("Cannot connect to " + targetHost);
				responseStatusCode = 1;
			} finally {

				if (responseStatusCode == 1) {
					result = "Failed";
				} else {
					responseStatusCode = response.getStatusLine().getStatusCode();
					System.out.println("GET Response Status:: " + responseStatusCode);

					if (responseStatusCode == 259) {

						BufferedReader br = new BufferedReader(
								new InputStreamReader(response.getEntity().getContent()));
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
				}

			}
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
