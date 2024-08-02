package it.uniroma3.vestiti.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prodotto;
import it.uniroma3.vestiti.model.Taglia;
import it.uniroma3.vestiti.repository.TagliaRepository;
import it.uniroma3.vestiti.service.ProdottoService;

@Controller
public class ProdottoController {

	@Autowired
	ProdottoService prodottoService;

	@Autowired
	TagliaRepository tagliaRepository;
	
	@Autowired
	ResourceLoader resourceLoader;

	@GetMapping("/negozio/{negozioId}/prodotti")
	public String getPrenotazioniNegozio(@PathVariable Long negozioId,
			Model model) {

		List<Prodotto> prodotti = this.prodottoService.findProdottibyNegozio(negozioId);
		model.addAttribute("prodotti", prodotti);
		model.addAttribute("modificato", new Prodotto());
		model.addAttribute("nuovoProdotto", new Prodotto());
		model.addAttribute("tagliaXS", new String());
		model.addAttribute("quantitaXS", new String());
		model.addAttribute("tagliaS", new String());
		model.addAttribute("quantitaS", new String());
		model.addAttribute("tagliaM", new String());
		model.addAttribute("quantitaM", new String());
		model.addAttribute("tagliaL", new String());
		model.addAttribute("quantitaL", new String());
		model.addAttribute("tagliaXL", new String());
		model.addAttribute("quantitaXL", new String());

		return "prodotti.html";
	}

	@GetMapping("/negoziante/prodotto/{id}")
	public String getProdotto(@PathVariable Long id) {
		return "prodotto.html";
	}
	

	@GetMapping("/prodotto/{prodottoId}/immagine")
	public ResponseEntity<byte[]> getImmagineProdotto(@PathVariable Long prodottoId){

		Prodotto prodotto = this.prodottoService.findById(prodottoId);

		byte[] immagine = prodotto.getImmagine();
		String MIMEType = prodotto.getImageMIMEType();

		if(immagine != null && immagine.length > 0) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(MIMEType));
			return new ResponseEntity<>(immagine, headers, HttpStatus.OK);
		} else {
			 try {
			        Resource resource = resourceLoader.getResource("classpath:static/images/no-photo-available.jpg");
			        byte[] bytes = resource.getInputStream().readAllBytes();
			        HttpHeaders headers = new HttpHeaders();
			        headers.setContentType(MediaType.IMAGE_JPEG);
			        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
			    } catch (IOException e) {
			        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			    }
		}
	}

	public void cambiaImmagineSeValida(Prodotto prodotto, MultipartFile file) {
		if (!file.isEmpty() && file!= null) {
			try {
				byte[] bytes = file.getBytes();
				String MIMEType = file.getContentType();
				prodotto.setImmagine(bytes);
				prodotto.setImageMIMEType(MIMEType);
			} catch (IOException e) {
				System.out.println("Immagine non caricata causa controller");
			}
		}
	}


}
