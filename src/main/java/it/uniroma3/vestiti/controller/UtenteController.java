package it.uniroma3.vestiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.service.CredentialsService;
import it.uniroma3.vestiti.service.UtenteService;

@Controller
public class UtenteController {

	@Autowired
	UtenteService utenteService;
	
	@Autowired
	CredentialsService credentialsService;
	
	@GetMapping("/user")
	public String getUser() {
		return "userIndex.html";
	}
	
	
	@GetMapping("/negoziante")
	public String getHomeNegoziante(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
		Utente negoziante = credentials.getUtente();
		Negozio negozio = negoziante.getNegozio();
		
		model.addAttribute("negozio", negozio);
		model.addAttribute("nome", negoziante.getNome() + " " + negoziante.getCognome());
		model.addAttribute("negozioNuovo", new Negozio());
		
		return "indexNegoziante.html";
		
	}
	
}
