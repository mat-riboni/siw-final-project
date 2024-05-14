package it.uniroma3.vestiti.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.vestiti.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, String>{
	
	public Optional<Credentials>findByEmail(String email);
	
}
