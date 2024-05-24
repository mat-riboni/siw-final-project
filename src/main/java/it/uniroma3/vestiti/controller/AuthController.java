package it.uniroma3.vestiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.service.CredentialsService;
import it.uniroma3.vestiti.service.NegozioService;

@Controller
public class AuthController {
	
	
	@Autowired 
	private CredentialsService credentialsService;
	
	@Autowired 
	private NegozioService negozioService;
	
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
	            return "redirect:/login";
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
	
	@GetMapping("/login")
	public String getLoginForm() {
		return "loginForm.html";
	}
	
	@GetMapping("/success")
	public String getHome() {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = this.credentialsService.getCredentialsByUsername(userDetails.getUsername());
		
		if(credentials.getRole().equals(Credentials.NEGOZIANTE_ROLE)) {
			return "indexNegoziante.html";
		} else {
			return "userIndex.html";
		}
	}
	
	@GetMapping("/") 
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("negozi", negozioService.getTop7Negozi());
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("isAuthenticated", false);
	        return "index.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
			model.addAttribute("isAuthenticated", true);
			model.addAttribute("username", credentials.getUsername());
			if (credentials.getRole().equals(Credentials.NEGOZIANTE_ROLE)) {
				return "admin/indexNegoziante.html";
			}
		}
        return "index.html";
	}
	

	
	
}
