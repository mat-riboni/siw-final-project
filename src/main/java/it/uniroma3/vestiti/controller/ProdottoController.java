package it.uniroma3.vestiti.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.vestiti.costanti.Costanti;
import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prodotto;
import it.uniroma3.vestiti.model.Taglia;
import it.uniroma3.vestiti.repository.TagliaRepository;
import it.uniroma3.vestiti.service.CredentialsService;
import it.uniroma3.vestiti.service.NegozioService;
import it.uniroma3.vestiti.service.ProdottoService;

@Controller
public class ProdottoController {

	@Autowired
	ProdottoService prodottoService;

	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	NegozioService negozioService;
	
	@Autowired
	TagliaRepository tagliaRepository;

	@Autowired
	ResourceLoader resourceLoader;

	@GetMapping("/negozio/{negozioId}/prodotti")
	public String getPrenotazioniNegozio(@PathVariable Long negozioId,
			Model model) {

		Optional<Negozio> optionalNegozio = this.negozioService.findById(negozioId);
		List<Prodotto> prodotti = this.prodottoService.findProdottibyNegozio(negozioId);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("isProprietario", false);
		} else{
			if(isPropietarioNegozio(optionalNegozio.get().getProprietario().getId())) {
				model.addAttribute("isProprietario", true);
			} else {
				model.addAttribute("isProprietario", false);
			}
		}
		
		model.addAttribute("prodotti", prodotti);
		model.addAttribute("nomeNegozio", optionalNegozio.get().getNome());

		return "prodotti.html";
	}

	@GetMapping("/negoziante/prodotto/{id}")
	public String getProdotto(Model model, @PathVariable Long id) {

		Prodotto prodotto = this.prodottoService.findById(id);
		Long negozioId = prodotto.getNegozio().getId();
		model.addAttribute("prodotto", prodotto);
		model.addAttribute("modificato", new Prodotto());
		model.addAttribute("quantitaXS", new String());
		model.addAttribute("quantitaS", new String());
		model.addAttribute("quantitaM", new String());
		model.addAttribute("quantitaL", new String());
		model.addAttribute("quantitaXL", new String());
		model.addAttribute("negozioId", negozioId);


		return "prodotto.html";
	}

	@PostMapping("/negoziante/prodotto/{id}/modifica")
	public String modificaProdotto(@PathVariable Long id,
			@ModelAttribute("modificato") Prodotto modificato,
			@RequestParam(value = "quantitaXS", required = false, defaultValue = "-1") int quantitaXS,
			@RequestParam(value = "quantitaS", required = false, defaultValue = "-1") int quantitaS,
			@RequestParam(value = "quantitaM", required = false, defaultValue = "-1") int quantitaM,
			@RequestParam(value = "quantitaL", required = false, defaultValue = "-1") int quantitaL,
			@RequestParam(value = "quantitaXL", required = false, defaultValue = "-1") int quantitaXL,
			@RequestParam("immagineModificata") MultipartFile file) {

		Prodotto prodotto = this.prodottoService.findById(id);

		aggiornaProdotto(prodotto, modificato, quantitaXS, quantitaS, quantitaM, quantitaL, quantitaXL, file);

		return "redirect:/negozio/" + prodotto.getNegozio().getId() + "/prodotti";
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


	public void aggiornaProdotto(Prodotto prodotto, Prodotto modificato, int quantitaXS, int quantitaS, int quantitaM, int quantitaL, int quantitaXL, MultipartFile file) {
		if (!modificato.getNome().isEmpty() && !modificato.getNome().isBlank()) {
			prodotto.setNome(modificato.getNome());
		}

		if(modificato.getPrezzo() > 0) {
			prodotto.setPrezzo(modificato.getPrezzo());
		}

		modificaQuantita(prodotto, quantitaXS, Costanti.XS);
		modificaQuantita(prodotto, quantitaS, Costanti.S);
		modificaQuantita(prodotto, quantitaM, Costanti.M);
		modificaQuantita(prodotto, quantitaL, Costanti.L);
		modificaQuantita(prodotto, quantitaXL, Costanti.XL);
		
		System.out.println("Quantità XS: " + quantitaXS);
		System.out.println("Quantità S: " + quantitaS);
		System.out.println("Quantità M: " + quantitaM);
		System.out.println("Quantità L: " + quantitaL);
		System.out.println("Quantità XL: " + quantitaXL);

		cambiaImmagineSeValida(prodotto, file); 
		prodottoService.save(prodotto);
	}

	public void modificaQuantita(Prodotto prodotto, int quantita, String nomeTaglia) {
		if(quantita > -1) {
			List<Taglia> taglie = prodotto.getTaglie();
			for(Taglia t : taglie) {
				if(t.getTaglia().equals(nomeTaglia)) {
					t.setQuantita(quantita);
					tagliaRepository.save(t);
				}
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
	
	public boolean isPropietarioNegozio(Long proprietarioId) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = this.credentialsService.getCredentialsByUsername(userDetails.getUsername());
		Long utenteId = credentials.getUtente().getId();
		return utenteId == proprietarioId;
	}


}
