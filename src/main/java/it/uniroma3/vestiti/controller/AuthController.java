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
	
	
	@Autowired 
	private CredentialsService credentialsService;

	@GetMapping("/login")
	public String getLogin() {
		return "login.html";
	}
	
	@GetMapping("/register")
	public String getRegisterTemplate(Model model) {
		model.addAttribute("utente", new Utente());
		model.addAttribute("credentials", new Credentials());
		return "registrationForm.html";
	}
	
	@PostMapping("/register")
	public String registerUtente(
			@ModelAttribute("utente") Utente utente, 
			@ModelAttribute("credentials") Credentials credentials) {
				utente.setNegoziPosseduti(null);
	        	credentials.setRole(Credentials.DEFAULT_ROLE);
	            credentials.setUtente(utente);
	            credentialsService.saveCredentials(credentials);
	            return "redirect:/";
	}
	
	@GetMapping("/registerNegoziante")
	public String getRegisterNegozioTemplate(Model model) {
		model.addAttribute("negoziante", new Utente());
		model.addAttribute("credentials", new Credentials());
		return "registerNegozianteForm.html";
	}
	
	@PostMapping("/registerNegoziante")
	public String registerNegoziante(
			@ModelAttribute("negoziante") Utente negoziante,
			@ModelAttribute("credentials") Credentials credentials) {
			credentials.setRole(Credentials.NEGOZIANTE_ROLE);
			negoziante.setProdotti(null);
			credentials.setUtente(negoziante);
			credentialsService.saveCredentials(credentials);
			return "redirect:/";
	}
}
