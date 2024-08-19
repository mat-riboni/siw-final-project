package it.uniroma3.vestiti.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.model.Prodotto;
import it.uniroma3.vestiti.model.ProdottoCarrello;
import it.uniroma3.vestiti.model.Taglia;
import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.service.CredentialsService;
import it.uniroma3.vestiti.service.PrenotazioneService;
import it.uniroma3.vestiti.service.ProdottoService;


@Controller
public class PrenotazioneController {

	@Autowired
	PrenotazioneService prenotazioneService;

	@Autowired
	ProdottoService prodottoService;

	@Autowired
	CredentialsService credentialsService;

	@GetMapping("/prenotazione/{id}")
	public String getPrenotazione(Model model, @PathVariable Long id) {

		Optional<Prenotazione> optionalPrenotazione = this.prenotazioneService.findById(id);

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
		Utente utente = credentials.getUtente();

		if(optionalPrenotazione.isPresent()) {
			Prenotazione prenotazione = optionalPrenotazione.get();
			model.addAttribute("prenotazione", prenotazione);
			model.addAttribute("prodotti", prenotazione.getProdotti());
			String nome = prenotazione.getUtente().getNome() + " " + prenotazione.getUtente().getCognome();
			model.addAttribute("nomeUtente", nome);
			model.addAttribute("nome", utente.getNome() + ' ' + utente.getCognome());
			model.addAttribute("negozioId", prenotazione.getNegozio().getId());
			if(utente.equals(prenotazione.getNegozio().getProprietario())) {
				model.addAttribute("isProprietario", true);
			} else {
				model.addAttribute("isProprietario", false);
			}
			if(utente.equals(prenotazione.getUtente())) {
				model.addAttribute("isUtente", true);
			} else {
				model.addAttribute("isUtente", false);
			}
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

	@PostMapping("/negozio/checkout")
	public ResponseEntity<String> checkout(@RequestBody List<ProdottoCarrello> cart) {

		for (ProdottoCarrello p : cart) {
			Prodotto prodotto = this.prodottoService.findById(p.getId());

			boolean isQuantityAvailable = false;
			for (Taglia t : prodotto.getTaglie()) {
				if (p.getTaglia().equals(t.getTaglia())) {
					int quantitaPrima = t.getQuantita();
					if (quantitaPrima >= p.getQuantita()) {
						t.setQuantita(quantitaPrima - p.getQuantita());
						isQuantityAvailable = true;
					} else {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								.body("Non ci sono prodotti sufficienti per la taglia " + t.getTaglia() + " del prodotto " + prodotto.getNome());
					}
				}
			}

			if (!isQuantityAvailable) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Non ci sono prodotti sufficienti");
			}

			this.prodottoService.save(prodotto);
		}

		return ResponseEntity.ok("Ordine ricevuto!");
	}
	


}
