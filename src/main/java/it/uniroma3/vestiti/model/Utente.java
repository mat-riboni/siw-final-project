package it.uniroma3.vestiti.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Utente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nome;
	
	private String cognome;
	
	private String email;
	
	@OneToMany(mappedBy = "negoziante")
	List<Negozio> negoziPosseduti;
	
	@OneToMany
	@JoinColumn(name = "utente_id")
	private List<Prodotto> prodotti;
	
	
	@Override
	public int hashCode() {
		return Objects.hash(email, id);
	}

	@Override
	public boolean equals(Object obj) {
		Utente other = (Utente) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id);
	}

	public Long getId() {
		return id;
	}
	
	public void setNegoziPosseduti(List<Negozio> negoziPosseduti) {
		this.negoziPosseduti = negoziPosseduti;
	}
	
	public List<Negozio> getNegoiPosseduti(){
		return this.negoziPosseduti;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Prodotto> getProdotti() {
		return prodotti;
	}

	public void setProdotti(List<Prodotto> prodotti) {
		this.prodotti = prodotti;
	}

	@Override
	public String toString() {
		return "Utente [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email + "]";
	}
	
	
	
	
}
