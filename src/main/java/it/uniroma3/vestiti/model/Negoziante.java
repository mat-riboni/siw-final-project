package it.uniroma3.vestiti.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Negoziante {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String nome;
	
	
	@Column(nullable = false)
	private String cognome;
	
	@OneToMany(mappedBy = "negoziante")
	List<Negozio> negoziPosseduti;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public List<Negozio> getNegoziPosseduti() {
		return negoziPosseduti;
	}

	public void setNegoziPosseduti(List<Negozio> negoziPosseduti) {
		this.negoziPosseduti = negoziPosseduti;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id);
	}

	@Override
	public boolean equals(Object obj) {
		Negoziante other = (Negoziante) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id);
	}

}
