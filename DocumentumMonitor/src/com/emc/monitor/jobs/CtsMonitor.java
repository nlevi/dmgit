package com.emc.monitor.jobs;

import java.util.Iterator;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.HttpServiceUtils;

public class CtsMonitor implements Job{

	private static final String CTS_MONITOR = "/cts/monitor";	
	private DocumentumService ds;

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
	//public void execute() {
		Set<DocumentumService> sds;
		String result = null;
		sds = DocumentumService.getServicesByType("cts");
		Iterator it = sds.iterator();
		int i = 0;
		String url;
		while (it.hasNext()) {
			ds = (DocumentumService) it.next();
			try {
				result = getStatus();				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (isRunning(result)) {
				result = getCTSVersion();
				ds.updateStatus(true, result);
			} else {
				ds.updateStatus(false, result);
			}
		}
	}

	private String getCTSVersion() {
		String version = null;
		IDfCollection col;
		IDfQuery query = new DfQuery();
		StringBuilder queryString = new StringBuilder();
		queryString.append("select r_object_id, hostname, status, websrv_url, inst_type, product, product_version");
        queryString.append(" where any product is not null and ");
        queryString.append(" hostname = '" + ds.getHost() + "' ");
        queryString.append(" order by r_object_id");
        
        query.setDQL(queryString.toString());
        
		IDfSessionManager sessionManager = null;
		IDfSession session = null;
		try {
			sessionManager = (new DfClientX()).getLocalClient().newSessionManager();
			sessionManager.setIdentity(ds.getDocbase(), new DfLoginInfo(ds.getUser(),ds.getPassword()));
			session = sessionManager.getSession("dctm72");
			
			col = query.execute(session, IDfQuery.DF_EXECREAD_QUERY);
			
			while(col.next()) {
				version = col.getString("cts_version");
			}
		} catch (DfException e) {
			e.printStackTrace();
		} finally {
			sessionManager.release(session);
		}
		
		return version;
	}

	private boolean isRunning(String result) {
		if (result == "Failed") {
			return false;
		} else {
			return true;
		}
	}

	private String getStatus() throws Exception {
		String response = HttpServiceUtils.sendRequest(ds.getHost(), ds.getPort(), "http", CTS_MONITOR, null, null);		
		return response;
	}
}