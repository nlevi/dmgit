package com.emc.monitor.jobs;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.emc.monitor.dao.DAOFactory;
import com.emc.monitor.dao.DocumentumServiceDAO;
import com.emc.monitor.service.DocumentumService;
import static com.emc.monitor.utils.HttpServiceUtils.*;

public class TspMonitor implements Job{

	private static final String TSP_INFO = "/taskspace/version.properties";	
	private DocumentumService ds;
	final static Logger logger = Logger.getLogger(TspMonitor.class);
	
	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		DAOFactory daofactory = DAOFactory.getInstance();
		
		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("tsp");
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
				ds.setStatus("Failed");;
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
		String response = sendRequest(ds.getHost(), ds.getPort(), "http", TSP_INFO, ds.getUser(), ds.getPassword());
		if(logger.isDebugEnabled()) {
			logger.debug("Taskspace request response: " + response);
		}
		String version;
		if (response != "Failed") {
			version = response.replaceAll("[^0-9&&[^\\.]]", "");
			if(logger.isDebugEnabled()) {
				logger.debug("Taskspace version: " + version + ". ServiceID: " + ds.getId());
			}
		} else {
			logger.warn("Taskspace service is not available. Service ID: " + ds.getId());
			version = "Failed";
		}
		return version;
	}
}
