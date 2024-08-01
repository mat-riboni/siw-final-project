package it.uniroma3.vestiti.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prenotazione;

public interface PrenotazioneRepository extends CrudRepository<Prenotazione, Long>{

	public List<Prenotazione> findByNegozio(Negozio negozio);
	
	public List<Prenotazione> findByNegozioIdAndStato(Long negozioId, String stato);

}
