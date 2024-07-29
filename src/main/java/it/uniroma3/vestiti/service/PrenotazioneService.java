package it.uniroma3.vestiti.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.repository.NegozioRepository;
import it.uniroma3.vestiti.repository.PrenotazioneRepository;

@Service
public class PrenotazioneService {

	@Autowired
	PrenotazioneRepository prenotazioneRepository;
	
	@Autowired
	NegozioRepository negozioRepository;
	
	public List<Prenotazione> findAllPrenotazioneByNegozioId(Long negozioId){
		
		Optional<Negozio> optionalNegozio = negozioRepository.findById(negozioId);
		if(optionalNegozio.isPresent()) {
			Negozio negozio = optionalNegozio.get();
			return this.prenotazioneRepository.findByNegozio(negozio);
		} else {
			return new ArrayList<Prenotazione>();
		}
		
		
	}
	
}
