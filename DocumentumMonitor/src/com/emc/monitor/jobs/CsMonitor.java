package com.emc.monitor.jobs;

import static com.emc.monitor.utils.HttpResponseParser.getVersionFromResponse;

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
import com.emc.monitor.utils.MailSender;

public class CsMonitor implements Job{
	private DocumentumService ds;
	private MailSender ms = new MailSender();
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
			if (!result.equals("Failed")) {
				ds.setVersion(result);
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
