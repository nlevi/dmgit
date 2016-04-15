package nl.emonitor.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import nl.emonitor.dao.EnvironmentServiceDAO;
import nl.emonitor.dao.EnvironmentServiceDAOImpl;

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:jdbc.properties" })
@ComponentScan({ "nl.emonitor" })
public class PersistenceConfig {

	@Autowired
	private Environment env;
	
	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);

		sessionBuilder.scanPackages("nl.emonitor");
		sessionBuilder.setProperties(hibernateProperties());
		return sessionBuilder.buildSessionFactory();
	}

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
//		System.out.println(env.getProperty("jdbc.driver"));
//		System.out.println(env.getProperty("jdbc.url"));
//		System.out.println(env.getProperty("jdbc.username"));
//		System.out.println(env.getProperty("jdbc.password"));
//		dataSource.setDriverClassName(env.getProperty("jdbc.driver"));
//		dataSource.setUrl(env.getProperty("jdbc.url"));
//		dataSource.setUsername(env.getProperty("jdbc.username"));
//		dataSource.setPassword(env.getProperty("jdbc.password"));
		return dataSource;
	}

	Properties hibernateProperties() {
		return new Properties() {
			{				
				setProperty("hibernate.connection.driver_class", env.getProperty("hibernate.connection.driver_class"));
				setProperty("hibernate.connection.url", env.getProperty("hibernate.connection.url"));
				setProperty("hibernate.connection.username", env.getProperty("hibernate.connection.username"));
				setProperty("hibernate.connection.password", env.getProperty("hibernate.connection.password"));
				setProperty("hibernate.current_session_context_class", env.getProperty("hibernate.current_session_context_class"));
				setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
				setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
				setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
				setProperty("hibernate.type", env.getProperty("hibernate.type"));
				setProperty("hibernate.use_sql_comments", env.getProperty("hibernate.use_sql_comments"));
				setProperty("hibernate.c3p0.min_size", env.getProperty("hibernate.c3p0.min_size"));
				setProperty("hibernate.c3p0.max_size", env.getProperty("hibernate.c3p0.max_size"));
				setProperty("hibernate.c3p0.timeout", env.getProperty("hibernate.c3p0.timeout"));
				setProperty("hibernate.c3p0.max_statements", env.getProperty("hibernate.c3p0.max_statements"));
				setProperty("hibernate.c3p0.idle_test_period", env.getProperty("hibernate.c3p0.idle_test_period"));
				setProperty("hibernate.archive.autodetection", env.getProperty("hibernate.archive.autodetection"));
			}
		};
	}

	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
		return transactionManager;
	}

	@Autowired
	@Bean(name = "eServiceDAO")
	public EnvironmentServiceDAO getEnvironmentServiceDao(SessionFactory sessionFactory) {
		return new EnvironmentServiceDAOImpl(sessionFactory);
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslaction() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

}
