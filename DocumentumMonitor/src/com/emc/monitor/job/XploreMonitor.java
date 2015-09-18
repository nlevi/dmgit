package com.emc.monitor.job;

import java.util.Iterator;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.HttpServiceUtils;

public class XploreMonitor implements Job {

	private final String USER_AGENT = "Mozilla/5.0";
	private DocumentumService ds;

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		Set<DocumentumService> sds;

		String result = null;

		sds = DocumentumService.getServicesByType("xplore");

		Iterator it = sds.iterator();
		int i = 0;
		String url;
		while (it.hasNext()) {
			ds = (DocumentumService) it.next();

			try {
				result = getStatus();
				
				if (result != "Failed") {
					getDssStatus();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (isRunning(result)) {
				ds.update(true, result);
			} else {
				ds.update(false, result);
			}
		}
	}

	private void getDssStatus() {
		// TODO Auto-generated method stub
		
	}

	private boolean isRunning(String result) {

		if (result == "Failed") {
			return false;
		} else {
			return true;
		}
	}

	private String getStatus() throws Exception {
		String response = HttpServiceUtils.sendRequest(ds);

		String version;

		if (response != "Failed") {
			System.out.println(response);
			version = response.replaceAll("[^0-9&&[^\\.]]", "");
			System.out.println(version);
		} else {
			version = "Failed";
		}
		return version;
	}
}
