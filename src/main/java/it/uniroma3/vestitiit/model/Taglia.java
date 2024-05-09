package it.uniroma3.vestitiit.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Taglia {

	@Column(nullable = false)
	private String taglia;
	
	private int quantita;

	public String getTaglia() {
		return taglia;
	}

	public void setTaglia(String taglia) {
		this.taglia = taglia;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	@Override
	public int hashCode() {
		return Objects.hash(taglia);
	}

	@Override
	public boolean equals(Object obj) {
		Taglia other = (Taglia) obj;
		return Objects.equals(taglia, other.taglia);
	}
	
	
	
}
