package com.emc.monitor.jobs;

import static com.emc.monitor.utils.HttpResponseParser.getVersionFromResponse;
import static com.emc.monitor.utils.HttpServiceUtils.sendRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.emc.monitor.dao.DAOFactory;
import com.emc.monitor.dao.DocumentumServiceDAO;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.HttpServiceUtils;
import com.emc.monitor.utils.MailSender;

public class XmsMonitor implements Job {

	private DocumentumService ds;
	private static final String XMS_INFO = "/xms-agent/server-status.jsp";
	private MailSender ms = new MailSender();
	final static Logger logger = Logger.getLogger(XmsMonitor.class);

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {

		DAOFactory daofactory = DAOFactory.getInstance();

		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("xms");
		String result = null;

		Iterator<DocumentumService> it = dslist.iterator();
		while (it.hasNext()) {
			ds = it.next();

			try {
				result = sendRequest(ds.getHost(), ds.getPort(), "http", "/".concat(XMS_INFO), ds.getUser(), ds.getPassword());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!result.equals("Failed")) {
				ds.setVersion(getVersionFromResponse(result,"div",0));
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
//		String response = HttpServiceUtils.sendRequest(ds.getHost(), ds.getPort(), "http",
//				XMS_INFO, ds.getUser(), ds.getPassword());
//		if (response.equals("Failed")) {
//			logger.warn("xMS Agent is not reachable. ServiceID: " + ds.getId());
//		} else {
//			response = readResponse(response);
//			if (logger.isDebugEnabled()) {
//				logger.debug("ServiceID: " + ds.getId() + "xMS Agent server-status.jsp page response" + response);
//			}
//		}
//		return response;
//	}
//
//	private String readResponse(String response) throws Exception {
//		String version = null;
//		List<String> versions = new ArrayList<>();
//		
//		Document doc;
//		doc = Jsoup.parse(response);
//		
//		Elements modules = doc.select("div");
//		for (Element module : modules) {
//			String tmp = module.text().replaceAll("[^0-9&&[^\\.]]", "");
//			if (tmp.length() == 13) {
//				versions.add(tmp);
//			}
//		}
//		
//		version = versions.get(0);
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("xMS Agent version: " + version + ". ServiceID: " + ds.getId());
//		}
//		return version;
//		
//	}
}
