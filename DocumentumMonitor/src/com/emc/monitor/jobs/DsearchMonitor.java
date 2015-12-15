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
import com.emc.monitor.utils.MailSender;

import static com.emc.monitor.utils.HttpResponseParser.getVersionFromResponse;
import static com.emc.monitor.utils.HttpServiceUtils.*;

public class DsearchMonitor implements Job{

	private static final String DSEARCH_INFO = "/dsearch";	
	private DocumentumService ds;
	private MailSender ms = new MailSender();
	final static Logger logger = Logger.getLogger(DsearchMonitor.class);
	
	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		DAOFactory daofactory = DAOFactory.getInstance();
		
		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("dsearch");
		String result = null;
		
		Iterator<DocumentumService> it = dslist.iterator();
		
		while (it.hasNext()) {
			ds = it.next();
			try {
				result = sendRequest(ds.getHost(), ds.getPort(), "http", DSEARCH_INFO, null, null);;				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!result.equals("Failed")) {
				ds.setVersion(getVersionFromResponse(result));
				ds.setStatus("Running");
			} else {				
				if(ds.getStatus() == null || !ds.getStatus().equals(result)) {
					ds.setStatus(result);
					ms.sendMail(ds);
				}					
			}
			dsdao.update(ds);
		}
	}

//	private boolean isRunning(String result) {
//		if (result.equals("Failed")) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	private String getStatus() throws Exception {
//		String response = sendRequest(ds.getHost(), ds.getPort(), "http", DSEARCH_INFO, null, null);
//		String version;
//		if (response != "Failed") {
//			version = response.replaceAll("[^0-9&&[^\\.]]", "");
//			if(logger.isDebugEnabled()) {
//				logger.debug("Dsearch version: " + version + ". ServiceID: " + ds.getId());
//			}
//		} else {
//			logger.warn("Dsearch service is not available. Service ID: " + ds.getId());
//			version = "Failed";
//		}
//		return version;
//	}
}
