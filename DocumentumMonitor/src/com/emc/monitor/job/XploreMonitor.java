package com.emc.monitor.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.emc.monitor.service.DocumentumService;
import com.emc.monitor.utils.DatabaseUtil;
import com.emc.monitor.utils.HttpServiceUtils;

public class XploreMonitor implements Job {

	private final String USER_AGENT = "Mozilla/5.0";
	private DocumentumService ds;

	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		Set<DocumentumService> sds;

		String result = null;

		sds = DocumentumService.getServicesByType("xplore");

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
				ds.update(true, result);
			} else {
				ds.update(false, result);
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
		String response = HttpServiceUtils.sendRequest(ds);

		String version;

		if (response != "Failed") {
			System.out.println(response);
			version = response.replaceAll("[^0-9&&[^\\.]]", "");
			System.out.println(version);
		} else {
			version = "Failed";
		}
		return version;
	}

	private String getUrl(DocumentumService service) throws SQLException {

		String url = "http://" + service.getHost() + ":" + service.getPort() + "/dsearch";

		ResultSet rs = DatabaseUtil
				.executeSelect("SELECT service_host, service_port FROM mntr_env_details WHERE service_type = 'xplore'");
		try {
			while (rs.next()) {
				url = "http://" + rs.getString(1) + ":" + rs.getString(2) + "/dsearch";
				System.out.println(url);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return url;
	}

}
