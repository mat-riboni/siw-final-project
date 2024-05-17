package it.uniroma3.vestiti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.repository.CredentialsRepository;

@Service
public class CredentialsService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CredentialsRepository credentialsRepository;
	
	public Credentials getCredentialsById(String id) {
		return this.credentialsRepository.findById(id).get();	
	}
		
	public Credentials getCredentialsByUsername(String email) {
		return this.credentialsRepository.findByUsername(email).get();
	}
		
	
	public Credentials saveCredentials(Credentials credentials) {
		credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);
	}
	
	
}
