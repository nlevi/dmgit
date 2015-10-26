package com.emc.monitor.dao;

import java.util.List;
import com.emc.monitor.service.DocumentumService;

public interface DocumentumServiceDAO {

	public DocumentumService getServiceById(int id) throws DAOException;

	public List<DocumentumService> getServicesByType(String type) throws DAOException;

	public List<DocumentumService> getAllServices() throws DAOException;

	public void create(DocumentumService dctmService) throws IllegalArgumentException, DAOException;

	public void update(DocumentumService dctmService) throws IllegalArgumentException, DAOException;

}
