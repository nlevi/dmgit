package com.emc.monitor.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpHost;
import org.apache.tomcat.util.codec.binary.Base64;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DatabaseUtil;

public class XcpMonitor {

	private final String USER_AGENT = "Mozilla/5.0";
	
	private DocumentumService ds;

	public void execute() {
		Set<DocumentumService> sds;
		
		String result = null;

		sds = DocumentumService.getServicesByType("xcp");

		Iterator it = sds.iterator();
		int i = 0;
		String url;
		while (it.hasNext()) {
			ds = (DocumentumService) it.next();
			url = "http://" + ds.getHost() + ":" + ds.getPort() + "/PlanTest";
			System.out.println(url);

			try {
				result = getStatus();
			} catch (Exception e) {
				e.printStackTrace();
			}

//			if (isRunning(result)) {
//				ds.update(true, result);
//			} else {
//				ds.update(false, result);
//			}
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

		if (response.length() > 3) {
			System.out.println(response);
			version = response.replaceAll("[^0-9&&[^\\.]]", "");
			System.out.println(version);
		} else {
			version = "Failed";
		}
		return version;
	}

	private String sendRequest() {

//		URI xcpuri = new URIBuilder()
//				.setScheme("http")
//				.setHost(ds.getHost())
//				.setPort(ds.getPort())
//				.setPath(ds.getName())
//				.build();
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
		
		HttpClientContext context = HttpClientContext.create();
		
		CookieStore cs = context.getCookieStore();
		List<Cookie> cookies;
		cookies = cs.getCookies();
		
		//Cookie cookie;
		for (Cookie cookie: cookies) {
			System.out.println("Name: " + cookie.getName() + ":" + "Value: " + cookie.getValue());
		}
		
		
		HttpHost targetXcpAppHost = new HttpHost(ds.getHost(), ds.getPort(), "http");
		HttpGet request = new HttpGet("/".concat(ds.getName()));
		request.addHeader("Authorization", "Basic " + getEncodedCredentials());
		
		System.out.println("Executing request " + request + " to " + targetXcpAppHost);
		CloseableHttpResponse response;
		
		response = httpclient.execute(targetXcpAppHost,request, context);
			
		       
        try {
        	System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine()); 
			EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
        
        cookies = cs.getCookies();
        for (Cookie cookie: cookies) {
			System.out.println("Name: " + cookie.getName() + ":" + "Value: " + cookie.getValue());
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
		
        
        return "OK";
//		try {
//			serviceurl = new URL(url);
//		} catch (MalformedURLException e1) {
//			e1.printStackTrace();
//		}
//
//		HttpURLConnection con = null;
//		int responseCode = 0;
//		StringBuffer response = new StringBuffer();
//
//		try {
//			con = (HttpURLConnection) serviceurl.openConnection();
//
//			con.setRequestMethod("GET");
//			
//			con.setRequestProperty("User-Agent", USER_AGENT);
//			con.setRequestProperty("Authorization", "Basic " + getEncodedCredentials());
//			System.out.println("\nSending 'GET' request to URL : " + url);
//			
//			Map<String, List<String>> map = con.getHeaderFields();
//			
//			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//				System.out.println("Key : " + entry.getKey() 
//		                           + " ,Value : " + entry.getValue());
//			}
//			
//			
//			
//			//responseCode = con.getResponseCode();
//			
//			//con.setRequestProperty("Set-Cookie", e);
//			
//		} catch (IOException e) {
//			System.out.println("Cannot connect to " + url);
//
//		} finally {
//
//			System.out.println("Response Code : " + responseCode);
//
//			if (responseCode == 259) {
//
//				BufferedReader br = null;
//				try {
//					br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//					String inputLine;
//
//					while ((inputLine = br.readLine()) != null) {
//						response.append(inputLine);
//					}
//
//					br.close();
//					System.out.println(response);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//			} else {
//				response.append(responseCode);
//				System.out.println(response);
//			}
//		}
//
//		return response.toString();
	}

	private String getEncodedCredentials() {
		String creds = ds.getUser().concat(":").concat(ds.getPassword());
		byte[] encodedCreds = Base64.encodeBase64(creds.getBytes());
				
		return encodedCreds.toString();
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
