package com.emc.monitor.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfDocbrokerClient;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.client.impl.docbroker.DocbrokerClient;
import com.documentum.fc.client.impl.docbroker.DocbrokerMap;
import com.documentum.fc.common.DfException;
import com.emc.monitor.service.DocumentumService;

public class UpdateDFCProperties {

	private static String dfcpropertiespath = System.getProperty("java.io.tmpdir") + File.separator + "dfc.properties";

	public static void update(String hostname, int port) throws IOException, DfException {
		File dfcprops = new File(dfcpropertiespath);
		if (dfcprops.exists()) {
			Properties props = new Properties();
			props.load(new FileReader(dfcprops));

			DocbrokerMap docbrokers = (DocbrokerMap) new DfClient().getDocbrokerMap();
			HashMap<String, Integer> map = new HashMap();
			int limit = docbrokers.getDocbrokerCount();
			for (int i = 0; i < limit; i++) {
				map.put(docbrokers.getHostName(i), docbrokers.getPortNumber(i));
			}
			String docbrokerHostProperty = "dfc.docbroker.host[" + limit + "]";
			String docbrokerPortProperty = "dfc.docbroker.port[" + limit + "]";
			if (!map.containsKey(hostname) || map.get(hostname) != port) {
				props.setProperty(docbrokerHostProperty, hostname);
				props.setProperty(docbrokerPortProperty, Integer.toString(port));
			}
		} else {
			createDfcProperties();
		}
	}

	public static void createDfcProperties() throws IOException {
		File dfcprops = new File(dfcpropertiespath);
		if (!dfcprops.exists()) {
			DatabaseUtil du = new DatabaseUtil();
			Set<DocumentumService> sds = DocumentumService.getInstance().getServicesByType("cs");
			DocumentumService tempds;
			Iterator it = sds.iterator();
			int i = 0;
			BufferedWriter bw = new BufferedWriter(new FileWriter(dfcprops));
			while (it.hasNext()) {
				tempds = (DocumentumService) it.next();
				String fileContent = createDfcPropertiesFileContent(tempds.getHost(), tempds.getPort(), i);
				bw.write(fileContent);
				i++;
			}
			bw.close();
		} else {
			System.out.println("dfc.properties already exists at location: " + dfcpropertiespath);
		}
	}

	private static String createDfcPropertiesFileContent(String docbrokerHost, int docbrokerPort, int num) {
		String content = "dfc.docbroker.host[" + num + "]=" + docbrokerHost + "\r\n" + "dfc.docbroker.port[" + num
				+ "]=" + docbrokerPort + "\r\n";
		return content;
	}

}
