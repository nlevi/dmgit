package com.emc.monitor.jobs;

import java.util.Iterator;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfId;
import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DocbaseSessionUtils;
import com.emc.monitor.utils.HttpServiceUtils;

public class JmsMonitor implements Job{

	private static final String JMS_INFO = "/IndexAgent";	
	private DocumentumService ds;

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
	//public void execute() {
		Set<DocumentumService> sds;
		String result = null;
		sds = DocumentumService.getInstance().getServicesByType("cs");
		Iterator<DocumentumService> it = sds.iterator();
//		int i = 0;
//		String url;
		while (it.hasNext()) {
			ds = it.next();
			
			try {
				result = getStatus();				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (isRunning(result)) {
				result = "Please check corresponding Dsearch version";
				ds.updateStatus(true, result);
			} else {
				ds.updateStatus(false, result);
			}
		}
	}

	private boolean isRunning(String result) {
		if (result == "Failed") {
			return false;
		} else {
			return true;
		}
	}

	private String getStatus() throws Exception {
		String jmsURL;
		
		IDfCollection col;
		IDfQuery query = new DfQuery();
		StringBuilder queryString = new StringBuilder();
		queryString.append("select base_uri from dm_jms_config");
		queryString.append(" where config_type in (1,2)");
		System.out.println(queryString);
		query.setDQL(queryString.toString());
		DocbaseSessionUtils dsu = DocbaseSessionUtils.getInstance();
		IDfSession session = null;
		try {
			session = dsu.getDocbaseSession(ds.getDocbase(), ds.getUser(), ds.getPassword());
			col = query.execute(session, IDfQuery.DF_EXECREAD_QUERY);
			while (col.next()) {
				jmsURL = col.getString("base_uri");
				System.out.println(jmsURL);
			}
		} catch (DfException e) {
			e.printStackTrace();
		} finally {
			try {
				dsu.releaseSession(session);
			} catch (DfException e) {
				e.printStackTrace();
			}
		}
		
		String response = HttpServiceUtils.sendRequest(ds.getHost(), ds.getPort(), "http", JMS_INFO, null, null);		
		return response;
	}
}
