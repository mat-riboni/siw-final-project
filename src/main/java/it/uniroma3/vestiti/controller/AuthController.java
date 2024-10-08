package it.uniroma3.vestiti.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.vestiti.costanti.Costanti;
import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.service.CredentialsService;
import it.uniroma3.vestiti.service.NegozioService;
import jakarta.validation.Valid;

@Controller
public class AuthController {
	
	
	@Autowired 
	private CredentialsService credentialsService;
	
	@Autowired 
	private NegozioService negozioService;
	
	@Autowired
	PasswordEncoder encoder;
	
	@GetMapping("/register")
	public String getRegisterTemplate(Model model) {
		model.addAttribute("utente", new Utente());
		model.addAttribute("credentials", new Credentials());
		return "registrationForm.html";
	}
	
	@PostMapping("/register")
	public String registerUtente(
			@Valid @ModelAttribute("utente") Utente utente, 
			BindingResult bindingResultUtente,
			@Valid@ModelAttribute("credentials") Credentials credentials,
			BindingResult bindingResultCredentials,
			@RequestParam(value = "negoziante", required = false) String negoziante,
			Model model) {
				
				if(bindingResultUtente.hasErrors() || bindingResultCredentials.hasErrors()) {
					return "registrationForm.html";
				}
				
	            if(negoziante == null) {
	            	credentials.setRole(Credentials.DEFAULT_ROLE);
	            } else {
	            	credentials.setRole(Credentials.NEGOZIANTE_ROLE);
	            	Negozio vuoto = new Negozio();
	            	vuoto.setNome(Costanti.nomeDefault);
	            	vuoto.setCitta(Costanti.cittaDefault);
	            	vuoto.setDescrizione(Costanti.descrizioneDefault);
	            	vuoto.setIndirizzo(Costanti.indirizzoDefault);
	            	vuoto.setProprietario(utente);
	            	vuoto.setPrenotazioni(new ArrayList<Prenotazione>());
	            	utente.setNegozio(vuoto);
	            }
	            credentials.setUtente(utente);
	            credentialsService.saveCredentials(credentials);
	            return "redirect:/login";
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
			return "redirect:/negoziante";
		} else {
			return "redirect:/user";
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
				return "redirect:/negoziante";
			}
			if(credentials.getRole().equals(Credentials.DEFAULT_ROLE)) {
				return "redirect:/user";
			}
		}
        return "index.html";
	}
	

	
	
}
