package com.emc.monitor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

public class HttpServiceUtils {

	public static String sendRequest(String host, int port, String protocol, String pinfo, String user, String password)
			throws IOException {
		String result = null;		
		URL url = null;
		try {
			url = new URL(protocol.concat("://").concat(host).concat(":").concat(Integer.toString(port)).concat(pinfo));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("Accept", "text/html,application/xml,*/*");

		if (user != null && password != null) {
			con.setRequestProperty("Authorization", "Basic " + getEncodedCredentials(user, password));
		}
		System.out.println("Executing request to " + url.toString());
		int responseStatusCode = 0;
		try {
			responseStatusCode = con.getResponseCode();
		} catch (IOException e) {
			System.out.println("Cannot connect to " + url.toString());
			responseStatusCode = 1;
		} finally {
			if (responseStatusCode == 1) {
				result = "Failed";
			} else {
				System.out.println("GET Response Status:: " + responseStatusCode);
				if (responseStatusCode == 259 || responseStatusCode == 200) {

					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();
					while ((inputLine = br.readLine()) != null) {
						response.append(inputLine);
					}
					result = response.toString();
					System.out.println("Response: " + result);
				} else {
					result = "Failed";
				}
			}
		}		
		return result;
	}

	private static String getEncodedCredentials(String user, String password) {
		String encodedPwd = new String(Base64.encodeBase64(user.concat(":").concat(password).getBytes()),
				StandardCharsets.UTF_8);
		return encodedPwd;
	}
}
