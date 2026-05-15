package model;

import java.io.Serializable;

public class ImmagineBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private int idImmagine;
    private String path;
    private ProdottoBean prodotto;

    public ImmagineBean() {
    	prodotto = new ProdottoBean();
    }

	public int getIdImmagine() {
		return idImmagine;
	}

	public void setIdImmagine(int idImmagine) {
		this.idImmagine = idImmagine;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ProdottoBean getProdotto() {
		return prodotto;
	}

	public void setProdotto(ProdottoBean prodotto) {
		this.prodotto = prodotto;
	}
    
    
}
