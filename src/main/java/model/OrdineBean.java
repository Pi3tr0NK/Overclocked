package model;

import java.io.Serializable;
import java.time.LocalDate;

public class OrdineBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
    public enum Stato { IN_PREPARAZIONE, SPEDITO, CONSEGNATO, RIMBORSATO }

    private int idOrdine;
    private LocalDate data;
    private Stato stato;
    private double totale;
    private String fatturaPath;
    private UtenteBean utente;
    private IndirizzoBean indirizzo;

    public OrdineBean() {
    	utente = new UtenteBean();
    	indirizzo = new IndirizzoBean();
    }

	public int getIdOrdine() {
		return idOrdine;
	}

	public void setIdOrdine(int idOrdine) {
		this.idOrdine = idOrdine;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Stato getStato() {
		return stato;
	}

	public void setStato(Stato stato) {
		this.stato = stato;
	}

	public double getTotale() {
		return totale;
	}

	public void setTotale(double totale) {
		this.totale = totale;
	}

	public String getFatturaPath() {
		return fatturaPath;
	}

	public void setFatturaPath(String fatturaPath) {
		this.fatturaPath = fatturaPath;
	}

	public UtenteBean getUtente() {
		return utente;
	}

	public void setUtente(UtenteBean utente) {
		this.utente = utente;
	}

	public IndirizzoBean getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(IndirizzoBean indirizzo) {
		this.indirizzo = indirizzo;
	}
    
    
}
