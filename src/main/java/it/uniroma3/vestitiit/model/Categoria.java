package it.uniroma3.vestitiit.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Categoria {

	@Column(nullable = false)
	private String nome;
	
	@OneToMany
	@JoinColumn(name = "categoria_id")
	private List<Prodotto> prodotti;
	
}
