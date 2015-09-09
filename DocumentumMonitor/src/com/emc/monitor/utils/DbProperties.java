package com.emc.monitor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbProperties {

	private String dbname;
	private String dbuser;
	private String dbpassword;
	private String dburl;
	private String driverclass;

	public DbProperties() {
		Properties prop = new Properties();

		String pfile = "monitor.properties";

		// is = Documentum
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream(pfile));
			this.dbname = prop.getProperty("dbname");
			this.dbuser = prop.getProperty("dbuser");
			this.dbpassword = prop.getProperty("dbpassword");
			this.dburl = prop.getProperty("dburl");
			this.driverclass = prop.getProperty("driverclass");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getJdbcUrl() {
		return dburl;
	}

	public String getDatabase() {
		return dbname;
	}

	public String getPwd() {
		return dbpassword;
	}

	public String getUser() {
		return dbuser;
	}
	
	public String getDriverClass() {
		return driverclass;
	}
}
