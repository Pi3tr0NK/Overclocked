package model;

import java.io.Serializable;
import java.util.Objects;

public class CarrelloItemBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProdottoBean prodotto;
    private int quantita;

    public CarrelloItemBean() {
    }

    public CarrelloItemBean(ProdottoBean prodotto, int quantita) {
        this.prodotto = prodotto;
        this.quantita = quantita;
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

    public void aumentaQuantita(int qta) {
        this.quantita += qta;
    }

    public void diminuisciQuantita(int qta) {
        this.quantita -= qta;

        if (this.quantita < 0) {
            this.quantita = 0;
        }
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarrelloItemBean other = (CarrelloItemBean) obj;
		return Objects.equals(prodotto, other.prodotto) && quantita == other.quantita;
	}	
    
    
}

