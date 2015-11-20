package com.emc.monitor.jobs;

import static com.emc.monitor.utils.HttpResponseParser.getVersionFromResponse;
import static com.emc.monitor.utils.HttpServiceUtils.sendRequest;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.emc.monitor.dao.DAOFactory;
import com.emc.monitor.dao.DocumentumServiceDAO;
import com.emc.monitor.service.DocumentumService;

public class XcpMonitor implements Job {

	private DocumentumService ds;
	private static final String XCP_INFO = "/products/xcp_product_info";
	private static final String[] versionTags = {"dm:major","dm:minor","dm:build_number"};
	final static Logger logger = Logger.getLogger(XcpMonitor.class);

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		
		DAOFactory daofactory = DAOFactory.getInstance();
		
		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("xcp");
		String result = null;
		
		Iterator<DocumentumService> it = dslist.iterator();
		while (it.hasNext()) {
			ds = it.next();

			try {
				result = sendRequest(ds.getHost(), ds.getPort(), "http", "/".concat(ds.getName()).concat(XCP_INFO), ds.getUser(), ds.getPassword());				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!result.equals("Failed")) {
				ds.setVersion(getVersionFromResponse(result,versionTags));
				ds.setStatus("Running");
			} else {
				ds.setStatus("Failed");
			}
			
			dsdao.update(ds);
		}
	}	
}
