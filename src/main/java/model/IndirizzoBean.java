package model;

import java.io.Serializable;

public class IndirizzoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private int idIndirizzo;
    private String viaNumciv;
    private String paese;
    private String citta;
    private String provincia;
    private String datiPlus;
    private String codicePostale;

    public IndirizzoBean() {}

	public int getIdIndirizzo() {
		return idIndirizzo;
	}

	public void setIdIndirizzo(int idIndirizzo) {
		this.idIndirizzo = idIndirizzo;
	}

	public String getViaNumciv() {
		return viaNumciv;
	}

	public void setViaNumciv(String viaNumciv) {
		this.viaNumciv = viaNumciv;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getDatiPlus() {
		return datiPlus;
	}

	public void setDatiPlus(String datiPlus) {
		this.datiPlus = datiPlus;
	}

	public String getCodicePostale() {
		return codicePostale;
	}

	public void setCodicePostale(String codicePostale) {
		this.codicePostale = codicePostale;
	}
    
    
}
