package it.prova.gestioneimpiegatojdbc.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Compagnia {
	
	private Long id;
	private String ragioneSociale;
	private int fatturatoAnnuo;
	private Date dataFondazione;
	private List<Impiegato> impiegati = new ArrayList<>();
	
	public Compagnia() {
		
	}

	public Compagnia(String ragioneSociale, int fatturatoAnnuo, Date dataFondazione) {
		super();
		this.ragioneSociale = ragioneSociale;
		this.fatturatoAnnuo = fatturatoAnnuo;
		this.dataFondazione = dataFondazione;
	}

	public Compagnia(String ragioneSociale, int fatturatoAnnuo) {
		super();
		this.ragioneSociale = ragioneSociale;
		this.fatturatoAnnuo = fatturatoAnnuo;
	}

	public Compagnia(Long id, String ragioneSociale, int fatturatoAnnuo, Date dataFondazione) {
		super();
		this.id = id;
		this.ragioneSociale = ragioneSociale;
		this.fatturatoAnnuo = fatturatoAnnuo;
		this.dataFondazione = dataFondazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public int getFatturatoAnnuo() {
		return fatturatoAnnuo;
	}

	public void setFatturatoAnnuo(int fatturatoAnnuo) {
		this.fatturatoAnnuo = fatturatoAnnuo;
	}

	public Date getDataFondazione() {
		return dataFondazione;
	}

	public void setDataFondazione(Date dataFondazione) {
		this.dataFondazione = dataFondazione;
	}

	public List<Impiegato> getImpiegati() {
		return impiegati;
	}

	public void setImpiegati(List<Impiegato> impiegati) {
		this.impiegati = impiegati;
	}
	
	
	@Override
	public String toString() {
		String dataFondazioneString = dataFondazione != null ? new SimpleDateFormat("dd/MM/yyyy").format(dataFondazione)
				: " N.D.";
		
		return "Compagnia [id=" + id + ", ragioneSociale=" + ragioneSociale + ", fatturatoAnnuo=" + fatturatoAnnuo 
				+ ", dataFondazione=" + dataFondazioneString + "]";
	}

}
