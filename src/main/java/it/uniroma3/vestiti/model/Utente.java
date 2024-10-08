package it.uniroma3.vestiti.model;

import java.sql.Types;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Utente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank(message = "{NotBlank}")
	private String nome;
	
	@NotBlank
	private String cognome;
	
	@NotBlank
	private String email;
	
	private String citta;
	
	
	private String indirizzo;
	
	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}


	@OneToOne(cascade = CascadeType.PERSIST)
	private Negozio negozio;
	
	@OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
	private List<Prenotazione> prenotazioni;
	
	@Lob
	@JdbcTypeCode(Types.VARBINARY)
	private byte[] immagine;
	
	private String imageMIMEType;
	
	public byte[] getImmagine() {
		return immagine;
	}

	public void setImmagine(byte[] immagine) {
		this.immagine = immagine;
	}

	public String getImageMIMEType() {
		return imageMIMEType;
	}

	public void setImageMIMEType(String imageMIMEType) {
		this.imageMIMEType = imageMIMEType;
	}

	public List<Prenotazione> getPrenotazioni() {
		return prenotazioni;
	}

	public void setPrenotazioni(List<Prenotazione> prenotazioni) {
		this.prenotazioni = prenotazioni;
	}

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
	

	public Negozio getNegozio() {
		return negozio;
	}

	public void setNegozio(Negozio negozio) {
		this.negozio = negozio;
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
	
	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}


	@Override
	public String toString() {
		return "Utente [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email + "]";
	}
	
	
}
