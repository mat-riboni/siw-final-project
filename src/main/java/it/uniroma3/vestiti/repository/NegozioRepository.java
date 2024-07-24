package it.uniroma3.vestiti.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.vestiti.model.Negozio;

public interface NegozioRepository extends CrudRepository<Negozio, Long>{

	public List<Negozio> findTop7ByOrderByIdAsc();
	
	public List<Negozio> findByNome(String nome);
	
}
