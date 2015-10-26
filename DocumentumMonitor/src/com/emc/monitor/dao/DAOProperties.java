package com.emc.monitor.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DAOProperties {

	private static final String PROPERTIES_FILE = "jdbc.properties";
	private static final Properties PROPERTIES = new Properties();

	static {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream pFile = classLoader.getResourceAsStream(PROPERTIES_FILE);

		if (pFile == null) {
			throw new DAOConfigurationException("Properties file '" + PROPERTIES_FILE + "' is missing in classpath.");
		}

		try {
			PROPERTIES.load(pFile);
		} catch (IOException e) {
			throw new DAOConfigurationException("Cannot load properties file '" + PROPERTIES_FILE + "'.", e);
		}
	}

	public String getProperty(String propertyName) throws DAOConfigurationException {
		String property = PROPERTIES.getProperty(propertyName);
		return property;
	}

}
