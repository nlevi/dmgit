package com.emc.monitor.jobs;

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

public class BpmMonitor implements Job {

	private DocumentumService ds;
	private static final String BPM_INFO = "/bpm/modules.jsp";
	final static Logger logger = Logger.getLogger(BpmMonitor.class);

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {

		DAOFactory daofactory = DAOFactory.getInstance();

		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("bpm");
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

	private String getStatus() throws Exception {
		String response = HttpServiceUtils.sendRequest(ds.getHost(), ds.getPort(), "http",
				BPM_INFO, ds.getUser(), ds.getPassword());
		if (response.equals("Failed")) {
			logger.warn("BPM is not reachable. ServiceID: " + ds.getId());
		} else {
			response = readResponse(response);
			if (logger.isDebugEnabled()) {
				logger.debug("ServiceID: " + ds.getId() + "BPM modules.jsp page response" + response);
			}
		}
		return response;
	}

	private String readResponse(String response) throws Exception {
		String version = null;
		List<String> versions = new ArrayList<>();
		
		Document doc;
		doc = Jsoup.parse(response);
		
		Elements modules = doc.select("li");
		for (Element module : modules) {
			String tmp = module.text().replaceAll("[^0-9&&[^\\.]]", "");
			if (tmp.length() == 13) {
				versions.add(tmp);
			}
		}
		
		version = versions.get(1);

		if (logger.isDebugEnabled()) {
			logger.debug("BPM version: " + version + ". ServiceID: " + ds.getId());
		}
		return version;
		
	}
}
