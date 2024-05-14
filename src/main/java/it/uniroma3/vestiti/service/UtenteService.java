package it.uniroma3.vestiti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.repository.UtenteRepository;

@Service
public class UtenteService {

	@Autowired
	UtenteRepository utenteRepository;
	
	public Utente getUtenteById(Long id) {
		return this.utenteRepository.getUtenteById(id);
	}
	
	public Utente getUtenteByEmail(String email) {
		return this.utenteRepository.getUtenteByEmail(email);
	}
	
	public Utente saveUtente(Utente utente) {
		return this.utenteRepository.save(utente);
	}
		
}
