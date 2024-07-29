package it.uniroma3.vestiti;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.uniroma3.vestiti.costanti.Costanti;
import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.service.CredentialsService;

@SpringBootApplication
public class SiwProgettoFinaleApplication implements CommandLineRunner{

	@Autowired
	private CredentialsService credentialsService;
	
	
	public static void main(String[] args) {
		SpringApplication.run(SiwProgettoFinaleApplication.class, args);
	}
	
	
	
	@Override
	public void run(String... args) throws Exception {
		Utente utente = new Utente();
		utente.setNome("negoziante");
		utente.setCognome("prova");
		utente.setEmail("@mail");
		Negozio vuoto = new Negozio();
    	vuoto.setNome(Costanti.nomeDefault);
    	vuoto.setCitta(Costanti.cittaDefault);
    	vuoto.setDescrizione(Costanti.descrizioneDefault);
    	vuoto.setIndirizzo(Costanti.indirizzoDefault);
    	vuoto.setProprietario(utente);
    	vuoto.setPrenotazioni(new ArrayList<Prenotazione>());
    	utente.setNegozio(vuoto);
		Credentials c = new Credentials();
		c.setUsername("negozianteProva");
		c.setPassword("12");
		c.setRole(Credentials.NEGOZIANTE_ROLE);
		c.setUtente(utente);
		credentialsService.saveCredentials(c);
	}

}
