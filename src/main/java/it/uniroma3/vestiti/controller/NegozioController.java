package it.uniroma3.vestiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
			return "nuovoNegozioForm.html";
		}
		
		//il path e' da cambiare in 'negoziante/nuovoNegozio' e aggiornare anche authconfig
		@PostMapping("/nuovoNegozio")
		public String InserisciNuovoNegozio(@ModelAttribute("negozio") Negozio negozio) {
			return "redirect:/";
		}
}
