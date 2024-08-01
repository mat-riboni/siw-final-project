package it.uniroma3.vestiti;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.uniroma3.vestiti.costanti.Costanti;
import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.model.Prodotto;
import it.uniroma3.vestiti.model.Taglia;
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
    	for(int i = 0; i < 20; i++) {
    		Prenotazione p = new Prenotazione();
    		p.setDataOra(LocalDateTime.now());
    		p.setNegozio(vuoto);
    		if(i == 2 || i == 3) {
    			p.setStato(Costanti.stato_annullato);
    		} else if(i == 4 || i == 5) {
    			p.setStato(Costanti.stato_inRitiro);
    		} else if(i == 6 || i == 7) {
    			p.setStato(Costanti.stato_ritirato);
    		} else {
    			p.setStato(Costanti.stato_inAttesa);
    		}
    		p.setUtente(utente);
    		vuoto.getPrenotazioni().add(p);
    		List<Prodotto> prodotti = new ArrayList<>();
    		
    		for(int j = 0; j < 10; j++) {
    			Prodotto prod = new Prodotto();
    			prod.setNome("Green");
    			prod.setPrezzo(10);
    			List<Taglia> taglie = new ArrayList<>();
    			
    			Taglia xs = new Taglia();
    			xs.setTaglia("xs");
    			xs.setQuantita(2);
    			taglie.add(xs);
    			
    			Taglia s = new Taglia();
    			s.setTaglia("s");
    			s.setQuantita(2);
    			taglie.add(s);
    			
    			Taglia m = new Taglia();
    			m.setQuantita(3);
    			m.setTaglia("m");
    			taglie.add(m);
 
    			
    			Taglia l = new Taglia();
    			l.setTaglia("l");
    			l.setQuantita(5);
    			taglie.add(l);
    			
    			prod.setTaglie(taglie);
    			prodotti.add(prod);
    		}
    		vuoto.setProdotti(prodotti);
    		p.setProdotti(prodotti);
    		Utente ut = new Utente();
    		ut.setNome("Il grande Mario");
    		p.setUtente(ut);
    		
    	}
    	utente.setNegozio(vuoto);
		Credentials c = new Credentials();
		c.setUsername("negozianteProva");
		c.setPassword("12");
		c.setRole(Credentials.NEGOZIANTE_ROLE);
		c.setUtente(utente);
		credentialsService.saveCredentials(c);
	}

}
