package com.emc.monitor.jobs;

import java.util.Iterator;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.HttpServiceUtils;

public class IndexMonitor implements Job{

	private static final String INDEX_INFO = "/IndexAgent";	
	private DocumentumService ds;

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
	//public void execute() {
		Set<DocumentumService> sds;
		String result = null;
		sds = DocumentumService.getServicesByType("indexagent");
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
				result = "Please check corresponding Dsearch version";
				ds.updateStatus(true, result);
			} else {
				ds.updateStatus(false, result);
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
		String response = HttpServiceUtils.sendRequest(ds.getHost(), ds.getPort(), "http", INDEX_INFO, null, null);		
		return response;
	}
}
