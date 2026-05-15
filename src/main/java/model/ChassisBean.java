package model;

public class ChassisBean extends ProdottoBean{
	private static final long serialVersionUID = 1L;
	
    private int idCase;
    private String formato;
    private String colore;
    private String materiale;

    public ChassisBean() {
    	super();
    }

	public int getIdCase() {
		return idCase;
	}

	public void setIdCase(int idCase) {
		this.idCase = idCase;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public String getMateriale() {
		return materiale;
	}

	public void setMateriale(String materiale) {
		this.materiale = materiale;
	}
    
    
}
