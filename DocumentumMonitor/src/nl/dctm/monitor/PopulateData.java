package nl.dctm.monitor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import nl.dctm.monitor.dao.DAOFactory;
import nl.dctm.monitor.dao.DocumentumServiceDAO;
import nl.dctm.monitor.utils.UpdateDFCProperties;

/**
 * Servlet implementation class PopulateData
 */
@WebServlet("/PopulateData")
public class PopulateData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		try {
			UpdateDFCProperties.createDfcProperties();
//			createTable();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private void createTable() throws SQLException {
		DAOFactory daofactory = DAOFactory.getInstance();
		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		String stmt = "create table mntr_env_details (service_id integer not null generated always as identity (start with 1, increment by 1), service_name varchar(255) not null, docbase varchar(255), service_host varchar(255) not null, service_port integer not null, service_type varchar(128) not null, service_status varchar(32), service_version varchar(255), last_update timestamp, service_user varchar(255), password varchar(255), address varchar(255))";
		dsdao.execQuery(stmt);
	}
}
