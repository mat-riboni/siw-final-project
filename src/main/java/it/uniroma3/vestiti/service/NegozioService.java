package it.uniroma3.vestiti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.repository.NegozioRepository;
import jakarta.transaction.Transactional;

@Service
public class NegozioService {

	@Autowired
	private NegozioRepository negozioRepository;
	
	
	@Transactional
	public Negozio save(Negozio negozio) {
		return this.negozioRepository.save(negozio);
	}
	
}
