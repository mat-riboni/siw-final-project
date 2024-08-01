package it.uniroma3.vestiti.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
import it.uniroma3.vestiti.service.NegozioService;

@Controller
public class NegozioController {

	
	@Autowired
	NegozioService negozioService;
		
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
		    @RequestParam("immagineNuova") MultipartFile file, 
		    @RequestParam("id") Long id) {

		    Optional<Negozio> optionalNegozio = this.negozioService.findById(id);

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
		        
		        if (!file.isEmpty() && file!= null) {
		            try {
		                byte[] bytes = file.getBytes();
		                String MIMEType = file.getContentType();
		                vecchio.setImmagine(bytes);
		                vecchio.setImageMIMEType(MIMEType);
		            } catch (IOException e) {
		                System.out.println("Immagine non caricata causa controller");
		            }
		        }

		        this.negozioService.save(vecchio);
		    }

		    return "redirect:/negoziante";
		}
		
		@GetMapping("/negozio/copertina/{id}")
		public ResponseEntity<byte[]> getCopertinaNegozio(@PathVariable Long id){
			
			Optional<Negozio> optionalNegozio = this.negozioService.findById(id);
			
			if(optionalNegozio.isPresent()) {
				
				Negozio negozio = optionalNegozio.get();
				byte[] immagine = negozio.getImmagine();
				String MIMEType = negozio.getImageMIMEType();
				
				if(immagine != null && immagine.length > 0) {
					
					HttpHeaders headers = new HttpHeaders();
		            headers.setContentType(MediaType.parseMediaType(MIMEType));
		            return new ResponseEntity<>(immagine, headers, HttpStatus.OK);
					
				}
				
			} 
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		}
		
		
}
