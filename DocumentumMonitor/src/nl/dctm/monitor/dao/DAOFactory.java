package nl.dctm.monitor.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public abstract class DAOFactory {

//	private static final String JDBC_URL = "jdbc.url";
//	private static final String JDBC_DRIVER = "jdbc.driver";
//	private static final String JDBC_USERNAME = "jdbc.username";
//	private static final String JDBC_PASSWORD = "jdbc.password";

	private Session currentSession;
	private Transaction currentTransaction;
	
	public static DAOFactory getInstance() throws DAOConfigurationException {

		DAOFactory instance;
		
		
		
//		DAOProperties properties = new DAOProperties();
//		String url = properties.getProperty(JDBC_URL);
//		String driverClass = properties.getProperty(JDBC_DRIVER);
//		String password = properties.getProperty(JDBC_PASSWORD);
//		String username = properties.getProperty(JDBC_USERNAME);
//		DAOFactory instance;
//
//		try {
//			Class.forName(driverClass).newInstance();
//		} catch (Exception e) {
//			throw new DAOConfigurationException("Driver class '" + driverClass + "' is missing in classpath.", e);
//		}
		instance = new ManagerDAOFactory();

		return instance;
	}
	
	abstract Session getCurrentSession();
	abstract Session openCurrentSession();
	abstract Session openCurrentSessionWithTransaction();
	abstract Session getCurrentSession();
	abstract Session getCurrentSession();
	abstract Session getCurrentSession();

	public DocumentumServiceDAO getDocumentumServiceDAO() {
		return new DocumentumServiceDAOJDBC(this);
	}

}

class ManagerDAOFactory extends DAOFactory {

	SessionFactory sessionFactory;
	
	ManagerDAOFactory() {
		Configuration configuration = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		this.sessionFactory = configuration.buildSessionFactory(builder.build());
	}

	@Override
	Session getCurrentSession() throws SQLException {		
		return DriverManager.getConnection(url, username, password);
	}
}
