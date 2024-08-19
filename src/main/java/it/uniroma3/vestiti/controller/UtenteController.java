package it.uniroma3.vestiti.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.vestiti.costanti.Costanti;
import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.service.CredentialsService;
import it.uniroma3.vestiti.service.NegozioService;
import it.uniroma3.vestiti.service.PrenotazioneService;
import it.uniroma3.vestiti.service.UtenteService;

@Controller
public class UtenteController {

	@Autowired
	UtenteService utenteService;
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	PrenotazioneService prenotazioneService;
	
	@Autowired
	NegozioService negozioService;
	
	
	@GetMapping("/user")
	public String getUser(@RequestParam(value = "citta_cercata", required = false)String citta_cercata, Model model) {
		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
		Utente utente = credentials.getUtente();
		
		model.addAttribute("nome", utente.getNome() + " " + utente.getCognome());
		model.addAttribute("prenotazioni", utente.getPrenotazioni());
		model.addAttribute("citta", utente.getCitta());
		model.addAttribute("utente", utente);
		
		if (citta_cercata == null || citta_cercata.isBlank()) {
		    model.addAttribute("citta_cercata", null);
		    model.addAttribute("negozi", this.negozioService.findByCitta(utente.getCitta()));
		} else {
		    model.addAttribute("citta_cercata", citta_cercata);
		    model.addAttribute("negozi", this.negozioService.findByCitta(citta_cercata));
		}
		
		
		return "userIndex.html";
	}
	
	@PostMapping("/user/citta")
	public String getUserCitta(@RequestParam("citta_cercata") String citta_cercata, RedirectAttributes redirectAttributes) {
	    redirectAttributes.addAttribute("citta_cercata", citta_cercata);
	    return "redirect:/user";
	}
	
	
	@GetMapping("/negoziante")
	public String getHomeNegoziante(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
		Utente negoziante = credentials.getUtente();
		Negozio negozio = negoziante.getNegozio();
		List<Prenotazione> prenotazioni = negozio.getPrenotazioni();
		List<Prenotazione> inAttesa = this.prenotazioneService.findPrenotazioniByStatoPerNegozio(negozio.getId(), Costanti.stato_inAttesa);
		List<Prenotazione> ritirate = this.prenotazioneService.findPrenotazioniByStatoPerNegozio(negozio.getId(), Costanti.stato_ritirato);
		Collections.sort(prenotazioni, new Comparator<>() {

			@Override
			public int compare(Prenotazione o1, Prenotazione o2) {
				return o1.getDataOra().compareTo(o2.getDataOra());
			}
			
		});
		
		model.addAttribute("negozio", negozio);
		model.addAttribute("nome", negoziante.getNome() + " " + negoziante.getCognome());
		model.addAttribute("negozioNuovo", new Negozio());
		model.addAttribute("prenotazioni", prenotazioni);
		model.addAttribute("numeroPrenotazioni", inAttesa.size());
		model.addAttribute("totalePrenotazioni", prenotazioni.size());
		model.addAttribute("prenotazioniRitirate", ritirate.size());
		
		return "indexNegoziante.html";
		
	}
	
}
