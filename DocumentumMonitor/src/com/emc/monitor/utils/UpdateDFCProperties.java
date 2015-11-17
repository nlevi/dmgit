package com.emc.monitor.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.impl.docbroker.DocbrokerMap;
import com.documentum.fc.common.DfException;
import com.emc.monitor.dao.DAOFactory;
import com.emc.monitor.dao.DocumentumServiceDAO;
import com.emc.monitor.jobs.DsearchMonitor;
import com.emc.monitor.service.DocumentumService;

public final class UpdateDFCProperties {

	private static String dfcPropertiesPath = System.getProperty("java.io.tmpdir") + File.separator + "dfc.properties";
	final static Logger logger = Logger.getLogger(UpdateDFCProperties.class);

	public static void update(String hostname, int port) throws IOException, DfException {
		File dfcprops = new File(dfcPropertiesPath);
		if (dfcprops.exists()) {
			Properties props = new Properties();
			props.load(new FileReader(dfcprops));

			DocbrokerMap docbrokers = (DocbrokerMap) new DfClient().getDocbrokerMap();
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			int limit = docbrokers.getDocbrokerCount();
			for (int i = 0; i < limit; i++) {
				map.put(docbrokers.getHostName(i), docbrokers.getPortNumber(i));
			}
			String docbrokerHostProperty = "dfc.docbroker.host[" + limit + "]";
			String docbrokerPortProperty = "dfc.docbroker.port[" + limit + "]";
			if (!map.containsKey(hostname) || map.get(hostname) != port) {
				props.setProperty(docbrokerHostProperty, hostname);
				props.setProperty(docbrokerPortProperty, Integer.toString(port));
				if(logger.isDebugEnabled()) {
					logger.debug("Updating dfc.properties with: " + docbrokerHostProperty + " = " + hostname + "\n" + docbrokerPortProperty + " = " + port);
				}
				FileOutputStream out = new FileOutputStream(dfcPropertiesPath);
				if(logger.isDebugEnabled()) {
					logger.debug("Updating dfc.properties: " + out.toString());
				}
				props.store(out, null);
				out.close();
			}
		} else {
			createDfcProperties();
		}		
	}

	public static void createDfcProperties() throws IOException {
		File dfcprops = new File(dfcPropertiesPath);
		if (!dfcprops.exists()) {
			DAOFactory daofactory = DAOFactory.getInstance();
			
			DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
			List<DocumentumService> dslist = dsdao.getServicesByType("cs");
			Iterator<DocumentumService> it = dslist.iterator();
			DocumentumService tempds = new DocumentumService();
			int i = 0;
			BufferedWriter bw = new BufferedWriter(new FileWriter(dfcprops));
			while (it.hasNext()) {
				tempds = it.next();
				String fileContent = createDfcPropertiesFileContent(tempds.getHost(), tempds.getPort(), i);
				bw.write(fileContent);
				i++;
			}
			bw.close();
		} else {
			logger.warn("dfc.properties is already exists at location: " + dfcPropertiesPath);
		}
	}

	private static String createDfcPropertiesFileContent(String docbrokerHost, int docbrokerPort, int num) {
		String content = "dfc.docbroker.host[" + num + "]=" + docbrokerHost + "\r\n" + "dfc.docbroker.port[" + num
				+ "]=" + docbrokerPort + "\r\n";
		if(logger.isDebugEnabled()) {
			logger.debug("Adding to dfc.properties" + content);
		}
		return content;
	}

}
