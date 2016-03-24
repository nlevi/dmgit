package nl.emonitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nl.emonitor.dao.EnvironmentServiceDAO;

@Service("eServiceService")
@Transactional
public class EServiceServiceImpl implements EServiceService{
	
	@Autowired
	private EnvironmentServiceDAO eServiceDAO;
//	private EnvironmentService eService;
//	private List<EnvironmentService> eServices;
	
//	public EServiceServiceImpl() {
//		this.eServiceDAO = new EnvironmentServiceDAOImpl();		
//	}
	
	public EnvironmentService getServiceById(int id) {
		EnvironmentService eService = eServiceDAO.getServiceById(id);
		System.out.println("Service name: " + eService.getName());
		return eService;
	}

	public List<EnvironmentService> getAllServices() {
		List<EnvironmentService> eServices = eServiceDAO.getAllServices();
		return eServices;
	}

	public List<EnvironmentService> getAllServicesByType(String type) {
		List<EnvironmentService> eServices = eServiceDAO.getServicesByType(type);
		return eServices;
	}

	public void save(EnvironmentService eservice) {
		eServiceDAO.save(eservice);		
	}

	public void delete(int id) {
		eServiceDAO.delete(id);
		
	}

}
