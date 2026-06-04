package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class RAMBean extends ProdottoBean {

	private static final long serialVersionUID = 1L;
	
    private int idRam;
    private String capacita;
    private String frequenza;
    private String tipo;

    public RAMBean() {
    	super();
    }

	public int getIdRam() {
		return idRam;
	}

	public void setIdRam(int idRam) {
		this.idRam = idRam;
	}

	public String getCapacita() {
		return capacita;
	}

	public void setCapacita(String capacita) {
		this.capacita = capacita;
	}

	public String getFrequenza() {
		return frequenza;
	}

	public void setFrequenza(String frequenza) {
		this.frequenza = frequenza;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
    
	@Override
	public Map<String, String> getSpecifiche() {

	    Map<String, String> specs = new LinkedHashMap<>();

	    specs.put("Capacita", capacita);
	    specs.put("Frequenza", frequenza);
	    specs.put("Tipo", tipo);
	    return specs;
	    
	} 

}
