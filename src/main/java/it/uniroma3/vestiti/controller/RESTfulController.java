package it.uniroma3.vestiti.controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.service.NegozioService;
import it.uniroma3.vestiti.service.PrenotazioneService;

@RestController
public class RESTfulController {

	@Autowired
	PrenotazioneService prenotazioneService;
	
	@Autowired
	NegozioService negozioService;

	@GetMapping("/negozio/{id}/prenotazioni")
	public Map<String, List<Prenotazione>> getMese2Prenotazioni(@PathVariable Long negozioId){
		
		Map<String, List<Prenotazione>> mese2prenotazioni = new HashMap<>();
		Optional<Negozio> optionalnegozio = this.negozioService.findById(negozioId);
		
		for(int i = 0; i <= LocalDate.now().getMonthValue(); i++) {
			String mese = LocalDate.of(2000, i, 1).getMonth().toString();
			mese2prenotazioni.put(mese, new ArrayList<>());
		}
		
		if(optionalnegozio.isPresent()) {
			Negozio negozio = optionalnegozio.get();
			List<Prenotazione> prenotazioni = negozio.getPrenotazioni();
			for(Prenotazione p : prenotazioni) {

				if(p.getDataOra().getYear() == LocalDate.now().getYear()) {
					String mese = p.getDataOra().getMonth().toString();
					mese2prenotazioni.get(mese).add(p);
				}
			}
		}

		return mese2prenotazioni;
	}

}
