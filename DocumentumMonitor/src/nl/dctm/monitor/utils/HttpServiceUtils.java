package nl.dctm.monitor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;

public final class HttpServiceUtils {
	
	final static Logger logger = Logger.getLogger(HttpServiceUtils.class);
	
	private HttpServiceUtils() {
		
	}
	
	public static String sendRequest(String host, int port, String protocol, String pinfo, String user, String password)
			throws IOException {
		String result = null;		
		URL url = null;
		try {
			url = new URL(protocol.concat("://").concat(host).concat(":").concat(Integer.toString(port)).concat(pinfo));
			if(logger.isDebugEnabled()) {
				logger.debug("URL: " + url.toString());
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("Accept", "text/html,application/xml,*/*");

		if (user != null && password != null) {
			con.setRequestProperty("Authorization", "Basic " + getEncodedCredentials(user, password));
		}
		
		int responseStatusCode = 0;
		try {
			responseStatusCode = con.getResponseCode();
			if(logger.isDebugEnabled()) {
				logger.debug("Response: " + responseStatusCode);
			}			
		} catch (IOException e) {
			logger.warn("Cannot connect to: " + url.toString());
			responseStatusCode = 1;
		} finally {
			if (responseStatusCode == 1) {				
				result = "Failed";
			} else {				
				if (responseStatusCode == 259 || responseStatusCode == 200) {

					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();
					while ((inputLine = br.readLine()) != null) {
						response.append(inputLine);
					}
					result = response.toString();
					if(logger.isDebugEnabled()) {
						logger.debug("Service response: " + result);
					}
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
