package it.uniroma3.vestiti.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Negozio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String nome;
	
	@Column(length = 2000)
	private String descrizione;
	
	private String indirizzo;
	
	private String citta;
	
	@Lob
	private byte[] immagine;
	
	
	@OneToMany
	@JoinColumn(name = "negozio_id")
	private List<Recensione> recensioni;
	
	@ManyToOne
	private Utente negoziante;
	
	public void setCitta(String citta) {
		this.citta = citta;
	}
	
	public String getCitta() {
		return this.citta;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(id, nome);
	}

	@Override
	public boolean equals(Object obj) {
		Negozio other = (Negozio) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome);
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	
	
	
	
}
