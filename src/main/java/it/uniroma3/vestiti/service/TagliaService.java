package it.uniroma3.vestiti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.vestiti.model.Taglia;
import it.uniroma3.vestiti.repository.TagliaRepository;

@Service
public class TagliaService {
	
	@Autowired
	TagliaRepository tagliaRepository;
	
	public Taglia save(Taglia taglia) {
		return this.tagliaRepository.save(taglia);
	}
	
}
