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
import static com.emc.monitor.utils.HttpResponseParser.*;
import com.emc.monitor.utils.MailSender;

public class DaMonitor implements Job{

	private static final String DA_INFO = "/da/version.properties";	
	private DocumentumService ds;
	private MailSender ms = new MailSender();
	final static Logger logger = Logger.getLogger(DaMonitor.class);
	
	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		DAOFactory daofactory = DAOFactory.getInstance();
		
		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("da");
		String result = null;
		
		Iterator<DocumentumService> it = dslist.iterator();
		
		while (it.hasNext()) {
			ds = it.next();
			try {
				result = sendRequest(ds.getHost(), ds.getPort(), "http", DA_INFO, ds.getUser(), ds.getPassword());				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!result.equals("Failed")) {
				ds.setVersion(getVersionFromResponse(result));
				ds.setStatus("Running");
			} else {				
				ds.setStatus(result);;
				ms.sendMail(ds);
			}
			dsdao.update(ds);
		}
	}	
}
