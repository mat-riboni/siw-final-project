package it.uniroma3.vestiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.vestiti.service.UtenteService;

@Controller
public class UtenteController {

	@Autowired
	UtenteService utenteService;
	
	@GetMapping("/user")
	public String getUser() {
		return "userIndex.html";
	}
}
