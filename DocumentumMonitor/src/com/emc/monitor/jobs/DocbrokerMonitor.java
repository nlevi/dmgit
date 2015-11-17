package com.emc.monitor.jobs;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.emc.monitor.dao.DAOFactory;
import com.emc.monitor.dao.DocumentumServiceDAO;
import com.emc.monitor.service.DocumentumService;

public class DocbrokerMonitor implements Job {

	private DocumentumService ds;
	final static Logger logger = Logger.getLogger(DocbrokerMonitor.class);
	
	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		DAOFactory daofactory = DAOFactory.getInstance();

		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("dkbrkr");
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

	private String getStatus() {
		String response = "Running";
		Socket socket = null;
		try {
			socket = new Socket(ds.getHost(), ds.getPort());
		} catch (IOException e) {
			logger.warn("Docbroker service not reachable. Service ID: " + ds.getId());
			response = "Failed";
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
}