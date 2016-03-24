package nl.emonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/service"})
public class EServiceController {
	
	@RequestMapping("/new")
	public String add() {
		return "service/add";
	}
	
	@RequestMapping(value = {"/list"})
	public String list() {
		return "service/list";
	}

}
