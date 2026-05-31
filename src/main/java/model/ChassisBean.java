package model;

import java.util.LinkedHashMap;
import java.util.Map;

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
	
	@Override
	public Map<String, String> getSpecifiche() {

	    Map<String, String> specs = new LinkedHashMap<>();

	    specs.put("Formato", formato);
	    specs.put("Colore", colore);
	    specs.put("Materiale", materiale);
	    return specs;
	}         
}
