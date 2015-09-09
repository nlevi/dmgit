package com.emc.monitor.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.emc.monitor.utils.DatabaseUtil;

public class DocumentumService {

	public String address;
	public String docbase;
	public String password;
	public String user;
	public String host;
	public int port;
	public String type;
	public String name;
	public String version;
	public String status;

	public DocumentumService() {
	}

	public DocumentumService(String address, String docbase, String password, String user, String host, int port,
			String type, String name) {

		this.address = address;
		this.docbase = docbase;
		this.password = password;
		this.user = user;
		this.host = host;
		this.port = port;
		this.type = type;
		this.name = name;
	}

	// public DocumentumService() throws SQLException {
	//
	// ResultSet rs = DatabaseUtil
	// .executeSelect("SELECT admin_address, docbase, user_passwd,"
	// + "service_user, service_host, service_port, service_type,"
	// + "service_name FROM mntr_env_details"
	// + "WHERE service_type = '" + type + " AND service_name = '" + name +
	// "'");
	// rs.first();
	//
	// this.address = rs.getString(1);
	// this.docbase = rs.getString(2);
	// this.password = rs.getString(3);
	// this.user = rs.getString(4);
	// this.host = rs.getString(5);
	// this.port = rs.getInt(6);
	// this.type = rs.getString(7);
	// this.name = rs.getString(8);
	//
	// }

	public static Set<DocumentumService> getServicesByType(String type) throws Exception {

		ResultSet rs = DatabaseUtil.executeSelect("SELECT admin_address, docbase, user_passwd,"
				+ "service_user, service_host, service_port, service_type," + "service_name FROM mntr_env_details "
				+ "WHERE service_type = '" + type + "'");

		Set<DocumentumService> sds;
		sds = new HashSet<>();
		DocumentumService ds;

		while (rs.next()) {
			ds = new DocumentumService(rs.getString("admin_address"), rs.getString("docbase"),
					rs.getString("user_passwd"), rs.getString("service_user"), rs.getString("service_host"),
					rs.getInt("service_port"), rs.getString("service_type"), rs.getString("service_name"));
			sds.add(ds);
		}

		if (rs != null) {
			rs.close();
		}
		return sds;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;	
	}

	public void update(boolean b, String result) {
		int r;
		Date now = new Date();
		DateFormat d = new SimpleDateFormat("yyyy-MM-dd");

		System.out.println(d.format(now));
		if (b) {

			r = DatabaseUtil.executeInsert("UPDATE mntr_env_status SET service_status = 'Running', service_version = '"
					+ result + "', " + "last_update = '" + d.format(now) + "' WHERE service_name = '" + name + "'");

			if (r < 1) {
				r = DatabaseUtil.executeInsert(
						"INSERT INTO mntr_env_status (service_name, service_status, service_version, last_update)"
								+ " VALUES ('" + name + "', 'Running', '" + result + "', '" + d.format(now) + "')");
			}

		} else {
			r = DatabaseUtil.executeInsert(
					"UPDATE mntr_env_status SET service_status = '" + result + "', " + "last_update = '" + d.format(now)
							+ "', service_version = 'Not Available' WHERE service_name = '" + name + "'");

			if (r < 1) {
				r = DatabaseUtil.executeInsert(
						"INSERT INTO mntr_env_status (service_name, service_status,service_version , last_update)"
								+ " VALUES ('" + name + "', '" + result + "', 'Not Available', '" + d.format(now)
								+ "')");
			}
		}
	}
}
