package nl.emonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getIndexPage() {
		return "index";
	}
	

	
//	@RequestMapping(value = "/service/{id}", method = RequestMethod.GET)
//	public String getServiceDetailsPage() {
//		return "ServiceDetails";
//	}
}
