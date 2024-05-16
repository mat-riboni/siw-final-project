package it.uniroma3.vestiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.service.CredentialsService;

@Controller
public class AuthController {
	
	@Autowired CredentialsService credentialsService;

	@GetMapping("/login")
	public String getLogin() {
		return "login.html";
	}
	
	@GetMapping("/register")
	public String getRegister(Model model) {
		model.addAttribute("utente", new Utente());
		model.addAttribute("credentials", new Credentials());
		return "register.html";
	}
	
	@PostMapping("/register")
	public String newUtente(@ModelAttribute("utente") Utente utente, @ModelAttribute("credentials") Credentials credentials) {
		credentials.setUtente(utente);
		this.credentialsService.saveCredentials(credentials);	
		return "registrationSuccessful";
	}
}
