package nl.emonitor.service;

import java.util.List;

public interface EServiceService {
	
	EnvironmentService getServiceById(int id);
	
	List<EnvironmentService> getAllServices();
	
	List<EnvironmentService> getAllServicesByType(String type);
	
	void save(EnvironmentService eservice);
	
	void delete(int id);

}
