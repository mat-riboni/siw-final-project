package it.uniroma3.vestiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.vestiti.service.NegozioService;

@Controller
public class NegozioController {

	
	@Autowired
	NegozioService negozioService;
	
	//il pah e' da cambiare in 'negoziante/nuovoNegozio' e aggiornare anche authconfig
		@GetMapping("/nuovoNegozio")
		public String nuovoNegozio(Model model) {
			
			return "nuovoNegozioForm.html";
		}
}
