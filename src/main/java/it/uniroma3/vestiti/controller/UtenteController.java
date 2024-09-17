package it.uniroma3.vestiti.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.vestiti.costanti.Costanti;
import it.uniroma3.vestiti.model.Credentials;
import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.model.Utente;
import it.uniroma3.vestiti.service.CredentialsService;
import it.uniroma3.vestiti.service.NegozioService;
import it.uniroma3.vestiti.service.PrenotazioneService;
import it.uniroma3.vestiti.service.UtenteService;
import jakarta.validation.Valid;

@Controller
public class UtenteController {

	@Autowired
	UtenteService utenteService;

	@Autowired
	CredentialsService credentialsService;

	@Autowired
	PrenotazioneService prenotazioneService;

	@Autowired
	NegozioService negozioService;
	
	@Autowired
	ResourceLoader resourceLoader;


	@GetMapping("/user")
	public String getUser(@RequestParam(value = "citta_cercata", required = false)String citta_cercata, Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
		Utente utente = credentials.getUtente();

		model.addAttribute("utente", utente);
		model.addAttribute("nome", utente.getNome() + " " + utente.getCognome());
		model.addAttribute("prenotazioni", utente.getPrenotazioni());
		model.addAttribute("citta", utente.getCitta());
		model.addAttribute("utente", utente);

		if (citta_cercata == null || citta_cercata.isBlank()) {
			model.addAttribute("citta_cercata", null);
			model.addAttribute("negozi", this.negozioService.findByCitta(utente.getCitta()));
		} else {
			model.addAttribute("citta_cercata", citta_cercata);
			model.addAttribute("negozi", this.negozioService.findByCitta(citta_cercata));
		}


		return "userIndex.html";
	}

	@PostMapping("/user/citta")
	public String getUserCitta(@RequestParam("citta_cercata") String citta_cercata, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("citta_cercata", citta_cercata);
		return "redirect:/user";
	}


	@GetMapping("/negoziante")
	public String getHomeNegoziante(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
		Utente negoziante = credentials.getUtente();
		Negozio negozio = negoziante.getNegozio();
		List<Prenotazione> prenotazioni = negozio.getPrenotazioni();
		List<Prenotazione> inAttesa = this.prenotazioneService.findPrenotazioniByStatoPerNegozio(negozio.getId(), Costanti.stato_inAttesa);
		List<Prenotazione> ritirate = this.prenotazioneService.findPrenotazioniByStatoPerNegozio(negozio.getId(), Costanti.stato_ritirato);

		model.addAttribute("negozio", negozio);
		model.addAttribute("nome", negoziante.getNome() + " " + negoziante.getCognome());
		model.addAttribute("negozioNuovo", new Negozio());
		model.addAttribute("prenotazioni", prenotazioni);
		model.addAttribute("numeroPrenotazioni", inAttesa.size());
		model.addAttribute("totalePrenotazioni", prenotazioni.size());
		model.addAttribute("prenotazioniRitirate", ritirate.size());

		return "indexNegoziante.html";	
	}

	@GetMapping("/user/{id}/profilo")
	public String getUserInfo(@PathVariable Long id,Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utente utente = this.utenteService.getUtenteById(id);

		model.addAttribute("utente", utente);
		model.addAttribute("username", userDetails.getUsername());
		model.addAttribute("modificato", new Utente());
		model.addAttribute("nuovoUsername", new String());

		return "userProfile.html";
	}

	@GetMapping("user/{id}/immagine")
	public ResponseEntity<byte[]> getImmagine(@PathVariable Long id){
		Utente utente = this.utenteService.getUtenteById(id);

		return getImmagine(utente);
	}
	
	@PostMapping("/user/{id}/modifica")
	public String modificaInfoUtente(@PathVariable Long id,
									 @Valid @ModelAttribute("utente") Utente utente,
									 BindingResult bindingResult,
									 @RequestParam("immagineModificata")MultipartFile file,
									 Model model) {
		
		if(bindingResult.hasErrors()) {
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			model.addAttribute("username", userDetails.getUsername());
			model.addAttribute("modificato", new Utente());
			model.addAttribute("nuovoUsername", new String());

			return "userProfile.html";
		}
		
		modificaImmagineSePresente(file, utente);
		this.utenteService.saveUtente(utente);
		return "redirect:/user/" + id + "/profilo";
	}

	public ResponseEntity<byte[]> getImmagine(Utente utente){
		byte[] immagine = utente.getImmagine();
		String MIMEType = utente.getImageMIMEType();

		if(immagine != null && immagine.length > 0) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(MIMEType));
			return new ResponseEntity<>(immagine, headers, HttpStatus.OK);
		} else {

			return getImmagineNonDisponibile();

		}
	}

	public ResponseEntity<byte[]> getImmagineNonDisponibile(){
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
	
	public void modificaImmagineSePresente(MultipartFile file, Utente utente) {
		if (!file.isEmpty() && file!= null) {
            try {
                byte[] bytes = file.getBytes();
                String MIMEType = file.getContentType();
                utente.setImmagine(bytes);
                utente.setImageMIMEType(MIMEType);
            } catch (IOException e) {
                System.out.println("Immagine non caricata");
            }
        }
	}

}
