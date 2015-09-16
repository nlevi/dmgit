package com.emc.monitor.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import com.emc.monitor.utils.DatabaseUtil;

public class DocumentumService {

	private String address;
	private String docbase;
	private String password;
	private String user;
	private String host;
	private int port;
	private String type;
	private String name;
	private String version;
	private String status;
	private int service_id;

	public DocumentumService() {
	}

	public DocumentumService(int service_id, String address, String docbase, String password, String user, String host,
			int port, String type, String name) {

		this.service_id = service_id;
		this.address = address;
		this.docbase = docbase;
		this.password = password;
		this.user = user;
		this.host = host;
		this.port = port;
		this.type = type;
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getAddress() {
		return address;
	}

	public String getDocbase() {
		return docbase;
	}

	public String getPassword() {
		return password;
	}

	public String getUser() {
		return user;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getStatus() {
		return status;
	}

	public int getId() {
		return service_id;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setDocbase(String docbase) {
		this.docbase = docbase;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static Set<DocumentumService> getServicesByType(String type) {

		ResultSet rs = DatabaseUtil.executeSelect("SELECT service_id, admin_address, docbase, user_passwd,"
				+ "service_user, service_host, service_port, service_type," + "service_name FROM mntr_env_details "
				+ "WHERE service_type = '" + type + "'");

		Set<DocumentumService> sds;
		sds = new HashSet<>();
		DocumentumService ds;

		try {
			while (rs.next()) {
				ds = new DocumentumService(rs.getInt("service_id"), rs.getString("admin_address"),
						rs.getString("docbase"), rs.getString("user_passwd"), rs.getString("service_user"),
						rs.getString("service_host"), rs.getInt("service_port"), rs.getString("service_type"),
						rs.getString("service_name"));
				sds.add(ds);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return sds;
	}

	public static Set<DocumentumService> getServices() {
		ResultSet rs = DatabaseUtil.executeSelect("SELECT service_id, admin_address, docbase, user_passwd,"
				+ "service_user, service_host, service_port, service_type, service_name FROM mntr_env_details ORDER by service_id ASC");

		Set<DocumentumService> sds;
		sds = new LinkedHashSet<>();
		DocumentumService ds;

		try {
			while (rs.next()) {
				ds = new DocumentumService(rs.getInt("service_id"), rs.getString("admin_address"),
						rs.getString("docbase"), rs.getString("user_passwd"), rs.getString("service_user"),
						rs.getString("service_host"), rs.getInt("service_port"), rs.getString("service_type"),
						rs.getString("service_name"));
				sds.add(ds);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return sds;
	}

	public void update(boolean b, String result) {
		int r;
		Locale currentLocale = Locale.getDefault();
		Date now = new Date();
		DateFormat d = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, currentLocale);
		//DateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateTime = d.format(now);
		System.out.println(currentDateTime);
		if (b) {

			r = DatabaseUtil.executeInsert("UPDATE mntr_env_status SET service_status = 'Running', service_version = '"
					+ result + "', " + "last_update = '" + d.format(now) + "' WHERE service_id = " + service_id);

			if (r < 1) {
				r = DatabaseUtil.executeInsert(
						"INSERT INTO mntr_env_status (service_id, service_name, service_status, service_version, last_update)"
								+ " VALUES (" + service_id + ", '" + name + "', 'Running', '" + result + "', '"
								+ d.format(now) + "')");
			}

		} else {
			r = DatabaseUtil.executeInsert(
					"UPDATE mntr_env_status SET service_status = '" + result + "', " + "last_update = '" + d.format(now)
							+ "', service_version = 'Not Available' WHERE service_id = " + service_id);

			if (r < 1) {
				r = DatabaseUtil.executeInsert(
						"INSERT INTO mntr_env_status (service_id, service_name, service_status,service_version , last_update)"
								+ " VALUES (" + service_id + ", '" + name + "', '" + result + "', 'Not Available', '"
								+ d.format(now) + "')");
			}
		}
	}

	public void save() {

		int n;		

		if (service_id != 0) {
			n = DatabaseUtil.executeInsert("UPDATE mntr_env_details SET name = '" + name + "', host = '" + host
					+ "', port = " + port + ", docbase = '" + docbase + "WHERE service_id = " + service_id);
		} else {		
			
			System.out.println("INSERT INTO mntr_env_details (service_id, admin_address, docbase, user_passwd, service_user, service_port, service_host, service_type, service_name)"
					+ " VALUES (NEXT VALUE FOR service_id, '" + address + "', '" + docbase + "', '" + password + "', '"
					+ user + "', " + port + ", '" + host + "', '" + type + "', '" + name + "')");
			
			n = DatabaseUtil.executeInsert(
					"INSERT INTO mntr_env_details (service_id, admin_address, docbase, user_passwd, service_user, service_port, service_host, service_type, service_name)"
							+ " VALUES (NEXT VALUE FOR service_id, '" + address + "', '" + docbase + "', '" + password + "', '"
							+ user + "', " + port + ", '" + host + "', '" + type + "', '" + name + "')");
		}

	}
}
