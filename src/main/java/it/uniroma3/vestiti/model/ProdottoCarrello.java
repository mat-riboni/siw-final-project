package it.uniroma3.vestiti.model;

import java.util.Objects;

public class ProdottoCarrello {

	private Long id;
	
	private Long negozioId;

	private String nome;
	
	private float prezzo;
	
	private String taglia;
	
	private int quantita;

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

	public String getTaglia() {
		return taglia;
	}

	public void setTaglia(String taglia) {
		this.taglia = taglia;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int qunatita) {
		this.quantita = qunatita;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nome, prezzo, quantita, taglia);
	}

	
	public Long getNegozioId() {
		return negozioId;
	}

	public void setNegozioId(Long negozioId) {
		this.negozioId = negozioId;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdottoCarrello other = (ProdottoCarrello) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& Float.floatToIntBits(prezzo) == Float.floatToIntBits(other.prezzo) && quantita == other.quantita
				&& Objects.equals(taglia, other.taglia);
	}

	
	
	
}
