package com.emc.monitor.jobs;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.emc.monitor.dao.DAOFactory;
import com.emc.monitor.dao.DocumentumServiceDAO;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DocbaseSessionUtils;
import com.emc.monitor.utils.HttpServiceUtils;

public class CtsMonitor implements Job {

	private static final String CTS_MONITOR = "/cts/monitor";
	private DocumentumService ds;
	final static Logger logger = Logger.getLogger(CtsMonitor.class);
	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		DAOFactory daofactory = DAOFactory.getInstance();
		
		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getServicesByType("cts");
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
				result = getCTSVersion();
				ds.setVersion(result);
				ds.setStatus("Running");
			} else {
				ds.setStatus("Failed");
			}
			dsdao.update(ds);
		}
	}

	private String getCTSVersion() {
		String version = null;
		IDfCollection col = null;
		IDfQuery query = new DfQuery();
		StringBuilder queryString = new StringBuilder();
		queryString.append("select r_object_id, hostname, status, websrv_url, inst_type, product, product_version, cts_version from cts_instance_info");
		queryString.append(" where any product is not null and");
		queryString.append(" hostname = '" + ds.getHost() + "' ");
		queryString.append(" order by r_object_id");
		if(logger.isDebugEnabled()) {
			logger.debug("ServiceID: " + ds.getId() + "Retrieving CTS info.");
		}
		query.setDQL(queryString.toString());
		DocbaseSessionUtils dsu = DocbaseSessionUtils.getInstance();
		IDfSession session = null;

		try {
			session = dsu.getDocbaseSession(ds.getDocbase(), ds.getUser(), ds.getPassword());
			col = query.execute(session, IDfQuery.DF_EXECREAD_QUERY);
			while (col.next()) {
				version = col.getString("cts_version");
				if(logger.isDebugEnabled()){
					logger.debug("CTS version: " + version + ". ServiceID: " + ds.getId());
				}
			}
		} catch (DfException e) {			
			e.printStackTrace();
		} finally {
			try {
				col.close();
				dsu.releaseSession(session);
			} catch (DfException e) {
				e.printStackTrace();
			}
		}
		return version;
	}

	private boolean isRunning(String result) {
		if (result.equals("Failed")) {
			return false;
		} else {
			return true;
		}
	}

	private String getStatus() throws Exception {
		String response = HttpServiceUtils.sendRequest(ds.getHost(), ds.getPort(), "http", CTS_MONITOR, null, null);
		if (response.equals("Failed")) {
			logger.info("CTS service is not reachable. ServiceID: " + ds.getId());
		}
		return response;
	}
}
