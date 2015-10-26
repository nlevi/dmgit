package com.emc.monitor.jobs;

import java.util.Iterator;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.emc.monitor.dao.DAOFactory;
import com.emc.monitor.dao.DocumentumServiceDAO;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.HttpServiceUtils;

public class DsearchMonitor implements Job{

	private static final String DSEARCH_INFO = "/dsearch";	
	private DocumentumService ds;

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		DAOFactory daofactory = DAOFactory.getInstance();
		
		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("dsearch");
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
		if (result == "Failed") {
			return false;
		} else {
			return true;
		}
	}

	private String getStatus() throws Exception {
		String response = HttpServiceUtils.sendRequest(ds.getHost(), ds.getPort(), "http", DSEARCH_INFO, null, null);
		String version;
		if (response != "Failed") {
			System.out.println(response);
			version = response.replaceAll("[^0-9&&[^\\.]]", "");			
			//System.out.println(version);
		} else {
			version = "Failed";
		}
		return version;
	}
}
