package it.uniroma3.vestiti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {
	
	@GetMapping("/")
	public String getIndex() {
		return "index.html";
	}
	
}
