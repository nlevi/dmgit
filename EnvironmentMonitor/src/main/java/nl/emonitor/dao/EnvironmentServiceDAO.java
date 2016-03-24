package nl.emonitor.dao;

import java.util.List;

import nl.emonitor.service.EnvironmentService;

public interface EnvironmentServiceDAO {
	
	public void save(EnvironmentService eService);
	
	public EnvironmentService getServiceById(int id);
	
	public List<EnvironmentService> getServicesByType(String type);
	
	public List<EnvironmentService> getAllServices();
	
	public void delete(int id);

}
