package model;

import java.io.Serializable;

public class DettaglioOrdineBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private OrdineBean ordine;
    private ProdottoBean prodotto;
    private int quantita;
    private double prezzoUnitario;

    public DettaglioOrdineBean() {
    	ordine = new OrdineBean();
    	prodotto = new ProdottoBean();
    }

	public OrdineBean getOrdine() {
		return ordine;
	}

	public void setOrdine(OrdineBean ordine) {
		this.ordine = ordine;
	}

	public ProdottoBean getProdotto() {
		return prodotto;
	}

	public void setProdotto(ProdottoBean prodotto) {
		this.prodotto = prodotto;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public double getPrezzoUnitario() {
		return prezzoUnitario;
	}

	public void setPrezzoUnitario(double prezzoUnitario) {
		this.prezzoUnitario = prezzoUnitario;
	}
    
    
    
    
}
