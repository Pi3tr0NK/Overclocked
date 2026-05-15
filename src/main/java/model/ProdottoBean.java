package model;

import java.io.Serializable;

public class ProdottoBean implements Serializable {

		private static final long serialVersionUID = 1L;

		private int idProdotto;
	    private String nome;
	    private String modello;
	    private String descrizione;
	    private String marca;
	    private double prezzo;
	    private int stock;
	    private String dimensioni;
	    private String peso;
	    private boolean attivo;

	    public ProdottoBean() {}

		public int getIdProdotto() {
			return idProdotto;
		}

		public void setIdProdotto(int idProdotto) {
			this.idProdotto = idProdotto;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getModello() {
			return modello;
		}

		public void setModello(String modello) {
			this.modello = modello;
		}

		public String getDescrizione() {
			return descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}

		public String getMarca() {
			return marca;
		}

		public void setMarca(String marca) {
			this.marca = marca;
		}

		public double getPrezzo() {
			return prezzo;
		}

		public void setPrezzo(double prezzo) {
			this.prezzo = prezzo;
		}

		public int getStock() {
			return stock;
		}

		public void setStock(int stock) {
			this.stock = stock;
		}

		public String getDimensioni() {
			return dimensioni;
		}

		public void setDimensioni(String dimensioni) {
			this.dimensioni = dimensioni;
		}

		public String getPeso() {
			return peso;
		}

		public void setPeso(String peso) {
			this.peso = peso;
		}

		public boolean isAttivo() {
			return attivo;
		}

		public void setAttivo(boolean attivo) {
			this.attivo = attivo;
		}
		
	    
		
}
