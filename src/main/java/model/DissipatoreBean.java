package model;

public class DissipatoreBean extends ProdottoBean{
	
	private static final long serialVersionUID = 1L;
	
	public enum Tipo {ARIA,LIQUIDO}

    private int idDissipatore;
    private Tipo tipo;
    private String socketSupportati;
    private String dimensioniVentola;
    private int rpmMax;
    private int rumore;
    private int tdpSupportato;
    
	
    public DissipatoreBean()
    {
    	super();
    }


	public int getIdDissipatore() {
		return idDissipatore;
	}


	public void setIdDissipatore(int idDissipatore) {
		this.idDissipatore = idDissipatore;
	}


	public Tipo getTipo() {
		return tipo;
	}


	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}


	public String getSocketSupportati() {
		return socketSupportati;
	}


	public void setSocketSupportati(String socketSupportati) {
		this.socketSupportati = socketSupportati;
	}


	public String getDimensioniVentola() {
		return dimensioniVentola;
	}


	public void setDimensioniVentola(String dimensioniVentola) {
		this.dimensioniVentola = dimensioniVentola;
	}


	public int getRpmMax() {
		return rpmMax;
	}


	public void setRpmMax(int rpmMax) {
		this.rpmMax = rpmMax;
	}


	public int getRumore() {
		return rumore;
	}


	public void setRumore(int rumore) {
		this.rumore = rumore;
	}


	public int getTdpSupportato() {
		return tdpSupportato;
	}


	public void setTdpSupportato(int tdpSupportato) {
		this.tdpSupportato = tdpSupportato;
	}
    
    
    
}
