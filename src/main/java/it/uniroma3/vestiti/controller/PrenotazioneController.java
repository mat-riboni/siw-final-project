package it.uniroma3.vestiti.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
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

import it.uniroma3.vestiti.costanti.Costanti;
import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Negozio;
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
			
			float totale = 0;
			
			for(Prodotto p : prenotazione.getProdotti()) {
				float prezzo = p.getPrezzo();
				int quantita = 0;
				for(Taglia t : p.getTaglie()) {
					quantita += t.getQuantita();
				}
				totale += (quantita*prezzo);
			}
			
			model.addAttribute("totale", totale);
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

		boolean operazioneRiuscita = trasformaRispostaInPrenotazione(cart);
		
		if(operazioneRiuscita) {
			return ResponseEntity.ok("Ordine ricevuto!");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il checkout.");
		}
	}

	
	
	
	private boolean trasformaRispostaInPrenotazione(List<ProdottoCarrello> cart) {
		List<Prodotto> prodotti = new ArrayList<>();

		Negozio negozio = null;
		
		for (ProdottoCarrello p : cart) {
			Prodotto prodotto = this.prodottoService.findById(p.getId());
			negozio = prodotto.getNegozio();

			boolean isQuantityAvailable = false;
			for (Taglia t : prodotto.getTaglie()) {
				if (p.getTaglia().equals(t.getTaglia())) {
					int quantitaPrima = t.getQuantita();
					if (quantitaPrima >= p.getQuantita()) {
						t.setQuantita(quantitaPrima - p.getQuantita());
						isQuantityAvailable = true;

						prodotti.add(creaProdottoDaCarrello(p, negozio));
						
					} else {
						return false;
					}
				}
			}

			if (!isQuantityAvailable) {
				return false;
			} 
		}
		
		salvaProdotti(prodotti);
		creaPrenotazione(prodotti, negozio);
		
		return true;
	}

		private void salvaProdotti(List<Prodotto> prodotti) {
			for(Prodotto p : prodotti) {
				this.prodottoService.save(p);
			}
	}

		private Prodotto creaProdottoDaCarrello(ProdottoCarrello prodCart, Negozio negozio) {
			Prodotto prodotto  = new Prodotto();
			prodotto.setNome(prodCart.getNome());
			prodotto.setPrezzo(prodCart.getPrezzo());
			prodotto.setNegozio(negozio);

			List<Taglia> taglie = new ArrayList<>();
			Taglia taglia = new Taglia();

			taglia.setTaglia(prodCart.getTaglia());
			taglia.setQuantita(prodCart.getQuantita());
			taglie.add(taglia);

			prodotto.setTaglie(taglie);


			return prodotto;
		}

		private void creaPrenotazione(List<Prodotto> prodotti, Negozio negozio) {
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
			Utente utente = credentials.getUtente();
			Prenotazione prenotazione = new Prenotazione();

			prenotazione.setNegozio(negozio);
			prenotazione.setProdotti(prodotti);
			prenotazione.setUtente(utente);
			prenotazione.setStato(Costanti.stato_inAttesa);
			prenotazione.setDataOra(LocalDateTime.now());

			this.prenotazioneService.save(prenotazione);

		}


	}
