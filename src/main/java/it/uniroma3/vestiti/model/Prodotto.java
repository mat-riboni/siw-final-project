package it.uniroma3.vestiti.model;

import java.sql.Types;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Prodotto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private float prezzo;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Taglia> taglie;
	
	@Lob
	@JdbcTypeCode(Types.VARBINARY)
	private byte[] immagine;
	
	private String imageMIMEType;
	
	@ManyToOne
	Negozio negozio;

	
	public Negozio getNegozio() {
		return negozio;
	}

	public void setNegozio(Negozio negozio) {
		this.negozio = negozio;
	}

	public List<Taglia> getTaglie() {
		return taglie;
	}

	public void setTaglie(List<Taglia> taglie) {
		this.taglie = taglie;
	}

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

	public Long getId() {
		return id;
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

	public float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		Prodotto other = (Prodotto) obj;
		return Objects.equals(id, other.id);
	}


	
	
	
	
	
}
