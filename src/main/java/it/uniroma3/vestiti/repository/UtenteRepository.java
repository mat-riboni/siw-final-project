package it.uniroma3.vestiti.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.vestiti.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long>{

	public Utente getUtenteById(Long id);
	
	public Utente getUtenteByEmail(String email);
	
}
