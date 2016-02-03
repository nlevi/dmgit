package nl.dctm.monitor.jobs;

import static nl.dctm.monitor.utils.HttpResponseParser.getVersionFromResponse;
import static nl.dctm.monitor.utils.HttpServiceUtils.*;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import nl.dctm.monitor.dao.DAOFactory;
import nl.dctm.monitor.dao.DocumentumServiceDAO;
import nl.dctm.monitor.service.DocumentumService;
import nl.dctm.monitor.utils.MailSender;

public class WebtopMonitor implements Job{

	private static final String WEBTOP_INFO = "/webtop/version.properties";	
	private DocumentumService ds;
	private MailSender ms = new MailSender();
	final static Logger logger = Logger.getLogger(WebtopMonitor.class);
	
	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		DAOFactory daofactory = DAOFactory.getInstance();
		
		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("webtop");
		String result = null;
		
		Iterator<DocumentumService> it = dslist.iterator();
		
		while (it.hasNext()) {
			ds = it.next();
			try {
				result = sendRequest(ds.getHost(), ds.getPort(), "http", WEBTOP_INFO, ds.getUser(), ds.getPassword());				
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

	private boolean isRunning(String result) {
		if (result.equals("Failed")) {
			return false;
		} else {
			return true;
		}
	}

//	private String getStatus() throws Exception {
//		String response = sendRequest(ds.getHost(), ds.getPort(), "http", WEBTOP_INFO, ds.getUser(), ds.getPassword());
//		if(logger.isDebugEnabled()) {
//			logger.debug("Webtop request response: " + response);
//		}
//		String version;
//		if (response != "Failed") {
//			version = response.replaceAll("[^0-9&&[^\\.]]", "");
//			if(logger.isDebugEnabled()) {
//				logger.debug("Webtop version: " + version + ". ServiceID: " + ds.getId());
//			}
//		} else {
//			logger.warn("Webtop service is not available. Service ID: " + ds.getId());
//			version = "Failed";
//		}
//		return version;
//	}
}
