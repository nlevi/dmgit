package com.emc.monitor.jobs;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.emc.monitor.dao.DAOFactory;
import com.emc.monitor.dao.DocumentumServiceDAO;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DocbaseSessionUtils;

public class CsMonitor implements Job{
	private DocumentumService ds;
	final static Logger logger = Logger.getLogger(CsMonitor.class);
	
	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		DAOFactory daofactory = DAOFactory.getInstance();

		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("cs");
		String result = null;

		Iterator<DocumentumService> it = dslist.iterator();

		while (it.hasNext()) {
			ds = it.next();
			try {
				result = getStatus();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (isRunning(result)) {
				ds.setVersion(result);
				ds.setStatus("Running");
			} else {
				ds.setStatus("Failed");				
			}
			dsdao.update(ds);
		}
	}

	private boolean isRunning(String result) {
		if (result.equals("Failed")) {
			return false;
		} else {
			return true;
		}
	}

	private String getStatus() throws Exception {
		String version = "";
		DocbaseSessionUtils dsu = DocbaseSessionUtils.getInstance();
		try {
			IDfSession session = dsu.getDocbaseSession(ds.getDocbase().concat(".").concat(ds.getName().toLowerCase()), ds.getUser(),
					ds.getPassword());
			version = session.getServerVersion().replaceAll("[^0-9&&[^\\.]]", "");
			if(logger.isDebugEnabled()) {
				logger.debug("Content Server version: " + version + ". Service ID: " + ds.getId());
			}
		} catch (DfException de) {
			de.printStackTrace();
			version = "Failed";
			logger.warn("Content Server is not available. Service ID: " + ds.getId());
		}

		return version;
	}
}
