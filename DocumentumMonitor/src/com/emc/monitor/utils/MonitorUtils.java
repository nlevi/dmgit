package com.emc.monitor.utils;

import java.sql.ResultSet;
import com.emc.monitor.utils.DatabaseUtil;

public class MonitorUtils {
	private static DatabaseUtil dbutils = new DatabaseUtil();
	public static ResultSet getStatus() {

		ResultSet rs = dbutils
				.executeSelect("SELECT service_name, service_status, service_version, last_update FROM mntr_env_status");

		return rs;

	}
}
