package nl.dctm.monitor.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAOFactory {

	private static final String JDBC_URL = "jdbc.url";
	private static final String JDBC_DRIVER = "jdbc.driver";
	private static final String JDBC_USERNAME = "jdbc.username";
	private static final String JDBC_PASSWORD = "jdbc.password";

	public static DAOFactory getInstance() throws DAOConfigurationException {

		DAOProperties properties = new DAOProperties();
		String url = properties.getProperty(JDBC_URL);
		String driverClass = properties.getProperty(JDBC_DRIVER);
		String password = properties.getProperty(JDBC_PASSWORD);
		String username = properties.getProperty(JDBC_USERNAME);
		DAOFactory instance;

		try {
			Class.forName(driverClass).newInstance();
		} catch (Exception e) {
			throw new DAOConfigurationException("Driver class '" + driverClass + "' is missing in classpath.", e);
		}
		instance = new DriverManagerDAOFactory(url, username, password);

		return instance;
	}

	abstract Connection getConnection() throws SQLException;

	public DocumentumServiceDAO getDocumentumServiceDAO() {
		return new DocumentumServiceDAOJDBC(this);
	}

}

class DriverManagerDAOFactory extends DAOFactory {
	private String url;
	private String username;
	private String password;

	DriverManagerDAOFactory(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@Override
	Connection getConnection() throws SQLException {		
		return DriverManager.getConnection(url, username, password);
	}
}
