package it.uniroma3.vestiti.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prodotto;
import it.uniroma3.vestiti.model.Taglia;
import it.uniroma3.vestiti.repository.NegozioRepository;
import it.uniroma3.vestiti.repository.ProdottoRepository;

@Service
public class ProdottoService {

	@Autowired
	private ProdottoRepository prodottoRepository;
	
	@Autowired 
	private NegozioRepository negozioRepository;
	
	public List<Prodotto> findProdottibyNegozio(Long negozioId){
		
		Optional<Negozio> optionalnegozio = this.negozioRepository.findById(negozioId);
		
		if(optionalnegozio.isPresent()) {
			Negozio negozio = optionalnegozio.get();
			
			return negozio.getProdotti();
		}
		
		return new ArrayList<>();
		
	}
	
	public String findTagliePerProdotto(Long prodottoId) {
		
		StringBuilder strb = new StringBuilder();
		Optional<Prodotto> optionalProdotto = this.prodottoRepository.findById(prodottoId);

		if(optionalProdotto.isPresent()) {
			Prodotto prodotto = optionalProdotto.get();
			for(Taglia t : prodotto.getTaglie()) {
				strb.append(t.getTaglia() + ", ");
			}
		}
		
		return strb.toString();
	}
	
	public Prodotto findById(Long Id) {
		Optional<Prodotto> optionalProdotto = this.prodottoRepository.findById(Id);
		Prodotto prodotto = null;
		if(optionalProdotto.isPresent()) {
			prodotto = optionalProdotto.get();
		}
		
		return prodotto;
	}
	
	public Prodotto save(Prodotto prodotto) {
		return this.prodottoRepository.save(prodotto);
	}
}
