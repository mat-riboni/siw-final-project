package it.uniroma3.vestiti.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.repository.NegozioRepository;
import it.uniroma3.vestiti.repository.PrenotazioneRepository;

@Service
public class PrenotazioneService {

	@Autowired
	PrenotazioneRepository prenotazioneRepository;
	
	@Autowired
	NegozioRepository negozioRepository;
	

	
	public List<Prenotazione> findPrenotazioniByStatoPerNegozio(Long negozioId, String stato){
		return this.prenotazioneRepository.findByNegozioIdAndStato(negozioId, stato);
	}
	
	public Iterable<Prenotazione> findAllPrenotazioni(Long id){
		return this.prenotazioneRepository.findAll();
	}
	
	public Optional<Prenotazione> findById(Long id){
		return this.prenotazioneRepository.findById(id);
	}
	
	public Prenotazione save(Prenotazione prenotazione) {
		return this.prenotazioneRepository.save(prenotazione);
	}
}
