package com.emc.monitor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import com.emc.monitor.service.DocumentumService;

public class HttpServiceUtils {

	private static DocumentumService service;

	public static String sendRequest(String host, int port, String protocol, String pinfo) {
		String result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			HttpClientContext context = HttpClientContext.create();
			CookieStore cookieStore = new BasicCookieStore();
			context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
			HttpHost targetHost = new HttpHost(host, port, protocol);
			HttpGet request = new HttpGet(protocol);
			request.addHeader("Authorization", "Basic " + getEncodedCredentials());
			request.addHeader("Accept", "text/html,application/xml,*/*");

			// System.out.println("Executing request " + request + " to " +
			// targetHost);

			CloseableHttpResponse response = null;
			int responseStatusCode = 0;
			try {
				response = httpclient.execute(targetHost, request, context);
			} catch (IOException e) {
				// System.out.println("Cannot connect to " + targetHost);
				responseStatusCode = 1;
			} finally {
				if (responseStatusCode == 1) {
					result = "Failed";
				} else {
					responseStatusCode = response.getStatusLine().getStatusCode();
					// System.out.println("GET Response Status:: " +
					// responseStatusCode);
					if (responseStatusCode == 259 || responseStatusCode == 200) {

						BufferedReader br = new BufferedReader(
								new InputStreamReader(response.getEntity().getContent()));
						String inputLine;
						StringBuffer sb = new StringBuffer();
						while ((inputLine = br.readLine()) != null) {
							sb.append(inputLine);
						}
						result = sb.toString();
						// System.out.println("Response: " + result);
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
		// System.out.println("Response: " + result);
		return result;
	}

	private static String getEncodedCredentials() {
		String encodedPwd = new String(
				Base64.encodeBase64(service.getUser().concat(":").concat(service.getPassword()).getBytes()),
				StandardCharsets.UTF_8);
		return encodedPwd;
	}
}
