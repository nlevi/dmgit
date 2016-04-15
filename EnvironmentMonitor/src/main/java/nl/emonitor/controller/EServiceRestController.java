package nl.emonitor.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import nl.emonitor.service.EServiceService;
import nl.emonitor.service.EnvironmentService;

@RestController
public class EServiceRestController {

	@Autowired
	EServiceService eServiceService;
	//EnvironmentServiceDAOImpl eServiceDAO;

	// Get all services
	@RequestMapping(value = "/service/", method = RequestMethod.GET)
	public ResponseEntity<List<EnvironmentService>> getAllServices() {
		List<EnvironmentService> eservices = eServiceService.getAllServices();
		if (eservices.isEmpty()) {
			return new ResponseEntity<List<EnvironmentService>>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<EnvironmentService>>(eservices, HttpStatus.OK);
	}

	// Get service by ID
	@RequestMapping(value = "/service/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EnvironmentService> getServiceById(@PathVariable("id") int id) {
		EnvironmentService eservice = eServiceService.getServiceById(id);
		if (eservice == null) {
			return new ResponseEntity<EnvironmentService>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<EnvironmentService>(eservice, HttpStatus.OK);
	}

	// Create service
	@RequestMapping(value = "/service/", method = RequestMethod.POST)
//	public void parseJson(HttpServletRequest request) throws IOException {
//		String jsonBody = IOUtils.toString(request.getInputStream());
//		System.out.println(jsonBody);
//	}
	public ResponseEntity<Void> createService(@ModelAttribute EnvironmentService eservice,
			UriComponentsBuilder ucBuilder) {
		System.out.println("Creating User " + eservice.toString());
		
		eServiceService.save(eservice);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/service/{id}").buildAndExpand(eservice.getId()).toUri());

		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	// Update service
	@RequestMapping(value = "/service/{id}", method = RequestMethod.PUT)
	public ResponseEntity<EnvironmentService> updateService(@PathVariable("id") int id,
			@RequestBody EnvironmentService eservice) {
		EnvironmentService es = eServiceService.getServiceById(id);
		es.setDocbase(eservice.getDocbase());
		es.setHost(eservice.getHost());
		es.setPort(eservice.getPort());
		es.setName(eservice.getName());
		es.setUser(eservice.getUser());
		es.setPassword(eservice.getPassword());
		es.setAddress(eservice.getAddress());
		es.setServiceType(eservice.getServiceType());
		es.setContext(eservice.getContext());

		eServiceService.save(es);

		return new ResponseEntity<EnvironmentService>(es, HttpStatus.OK);
	}
	
	//Delete service
	@RequestMapping(value = "/service/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<EnvironmentService> deleteService(@PathVariable("id") int id) {
		EnvironmentService es = eServiceService.getServiceById(id);
		if(es == null) {
			return new ResponseEntity<EnvironmentService>(HttpStatus.NOT_FOUND);
		}
		
		eServiceService.delete(id);
		return new ResponseEntity<EnvironmentService>(HttpStatus.NO_CONTENT);
	}

}
