package it.uniroma3.vestiti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.vestiti.model.Prodotto;
import it.uniroma3.vestiti.service.ProdottoService;

@Controller
public class ProdottoController {

	@Autowired
	ProdottoService prodottoService;
	
	@GetMapping("/negozio/{negozioId}/prodotti")
	public String getPrenotazioniNegozio(@PathVariable Long negozioId,
										 Model model) {
		
		List<Prodotto> prodotti = this.prodottoService.findProdottibyNegozio(negozioId);
		model.addAttribute("prodotti", prodotti);
		
		return "prodotti.html";
	}
}
