package com.emc.monitor.jobs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.emc.monitor.service.DocumentumService;

public class DocbrokerMonitor implements Job{

	private DocumentumService ds;
	
	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		Set<DocumentumService> sds;
		String result = null;
		sds = DocumentumService.getInstance().getServicesByType("cs");
		Iterator<DocumentumService> it = sds.iterator();
		int i = 0;
		String url;
		while (it.hasNext()) {
			ds = it.next();
			try {
				result = getStatus();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (isRunning(result)) {
				result = "Check version of corresponsind Content Server instance";
				ds.updateStatus(true, "Check version of corresponsind Content Server instance");
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
	
	private String getStatus() {
		String response = "Running";
		Socket socket = null;
		try {
			socket = new Socket(ds.getHost(), ds.getPort());			
		} catch (IOException e) {
			response = "Failed";
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		return response;
	}
}
