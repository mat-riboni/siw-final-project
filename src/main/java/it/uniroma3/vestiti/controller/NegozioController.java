package it.uniroma3.vestiti.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.service.NegozioService;

@Controller
public class NegozioController {

	
	@Autowired
	NegozioService negozioService;
	
	//il path e' da cambiare in 'negoziante/nuovoNegozio' e aggiornare anche authconfig
		@GetMapping("/nuovoNegozio")
		public String getNuovoNegozioForm(Model model) {
			model.addAttribute("negozio", new Negozio());
			model.addAttribute("listaCategorie", new ArrayList<String>());
			return "nuovoNegozioForm.html";
		}
		
		//il path e' da cambiare in 'negoziante/nuovoNegozio' e aggiornare anche authconfig
		@PostMapping("/nuovoNegozio")
		public String InserisciNuovoNegozio(
				@ModelAttribute("negozio") Negozio negozio) {
			this.negozioService.save(negozio);
			return "redirect:/";
		}
		
		
		@PostMapping("/cercaNegozi")
		public String getNegoziCercati(Model model,
				@RequestParam("cercati") String cercati) {
			List<Negozio> negoziTrovati = this.negozioService.findByNome(cercati);
			model.addAttribute("trovati",negoziTrovati);
			model.addAttribute("isSearching", true);
			return "index.html";
		}
		
		@PostMapping("/negoziante/modificaNegozio")
		public String modificaNegozio(
				@ModelAttribute("negozioNuovo") Negozio nuovo,
				@ModelAttribute("negozio") Negozio negozio) {
			
			Optional<Negozio> optionalNegozio = this.negozioService.findById(negozio.getId());
		    
		    if (optionalNegozio.isPresent()) {
		        Negozio vecchio = optionalNegozio.get();
		        
		        if (nuovo.getCitta() != null && !nuovo.getCitta().trim().isEmpty()) {
		            vecchio.setCitta(nuovo.getCitta());
		        }
		        if (nuovo.getDescrizione() != null && !nuovo.getDescrizione().trim().isEmpty()) {
		            vecchio.setDescrizione(nuovo.getDescrizione());
		        }
		        if (nuovo.getNome() != null && !nuovo.getNome().trim().isEmpty()) {
		            vecchio.setNome(nuovo.getNome());
		        }
		        if (nuovo.getIndirizzo() != null && !nuovo.getIndirizzo().trim().isEmpty()) {
		            vecchio.setIndirizzo(nuovo.getIndirizzo());
		        }
		        if (nuovo.getImmagine() != null && nuovo.getImmagine().length > 0) {
		            vecchio.setImmagine(nuovo.getImmagine());
		        }
		        
		        this.negozioService.save(vecchio);
		        
		    } 
			
			
			return "redirect:/negoziante";
		}
		
		
}
