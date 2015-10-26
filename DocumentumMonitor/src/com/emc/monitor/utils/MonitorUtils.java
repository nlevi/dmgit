package com.emc.monitor.utils;

import java.util.List;
import com.emc.monitor.dao.DAOFactory;
import com.emc.monitor.dao.DocumentumServiceDAO;
import com.emc.monitor.service.DocumentumService;

public class MonitorUtils {
//	private static DatabaseUtil dbutils = new DatabaseUtil();
	public static List<DocumentumService> getStatus() {

		DAOFactory daofactory = DAOFactory.getInstance();
		
		DocumentumServiceDAO dsdao = daofactory.getDocumentumServiceDAO();
		List<DocumentumService> dslist = dsdao.getAllServices();

		return dslist;

	}
}
