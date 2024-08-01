package it.uniroma3.vestiti.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.service.PrenotazioneService;


@Controller
public class PrenotazioneController {
	
	@Autowired
	PrenotazioneService prenotazioneService;

	@GetMapping("/prenotazione/{id}")
	public String getPrenotazione(Model model, @PathVariable Long id) {
		
		Optional<Prenotazione> optionalPrenotazione = this.prenotazioneService.findById(id);
		
		if(optionalPrenotazione.isPresent()) {
			Prenotazione prenotazione = optionalPrenotazione.get();
			model.addAttribute("prenotazione", prenotazione);
			model.addAttribute("prodotti", prenotazione.getProdotti());
			String nome = prenotazione.getUtente().getNome() + " " + prenotazione.getUtente().getCognome();
			model.addAttribute("nomeUtente", nome);
			Utente negoziante = prenotazione.getNegozio().getProprietario();
			model.addAttribute("nomeNegoziante", negoziante.getNome() + ' ' + negoziante.getCognome());
			model.addAttribute("negozioId", prenotazione.getNegozio().getId());
		}
		
		
		return "prenotazione.html";
	}
	
	@PostMapping("/prenotazione/{id}")
	public String changeStatoPrenotazione(@PathVariable Long id,
										  @RequestParam("stato") String stato) {
		
		Optional<Prenotazione> optionalPenotazione = this.prenotazioneService.findById(id);
		
		optionalPenotazione.ifPresent(prenotazione -> {
			prenotazione.setStato(stato);
			this.prenotazioneService.save(prenotazione);
		});
		
		return "redirect:/prenotazione/" + id;
		
	}
	
	
}
