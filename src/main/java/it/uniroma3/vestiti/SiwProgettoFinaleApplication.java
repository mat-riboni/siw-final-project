package it.uniroma3.vestiti;

import org.springframework.boot.CommandLineRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.service.CredentialsService;

@SpringBootApplication
public class SiwProgettoFinaleApplication {

	@Autowired
	private CredentialsService credentialsService;
	
	public static void main(String[] args) {
		SpringApplication.run(SiwProgettoFinaleApplication.class, args);
	}
	
	
	/*
	 * 
	 * @Override
	public void run(String... args) throws Exception {
		Utente utente = new Utente();
		utente.setNome("nome");
		utente.setCognome("congome");
		utente.setEmail("@mail");
		Credentials c = new Credentials();
		c.setUsername("user");
		c.setPassword("pass");
		c.setRole("ADMIN_ROLE");
		c.setUtente(utente);
		credentialsService.saveCredentials(c);
	}
	 */

}
