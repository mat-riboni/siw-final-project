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

import it.uniroma3.vestiti.costanti.Costanti;
import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.service.CredentialsService;
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
