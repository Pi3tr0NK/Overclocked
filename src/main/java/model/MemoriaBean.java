package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class MemoriaBean extends ProdottoBean{
	private static final long serialVersionUID = 1L;
	
    public enum Tipo { SSD, HDD }
    public enum Tecnologia { SATA, NVME }
    
    private int idMemoria;
    private String capacita;
    private int velScrittura;
    private int velLettura;
    private Tipo tipo;
    private Tecnologia tecnologia;
    private String formato;
    
    public MemoriaBean() {
    	super();
    }

	public int getIdMemoria() {
		return idMemoria;
	}

	public void setIdMemoria(int idMemoria) {
		this.idMemoria = idMemoria;
	}

	public String getCapacita() {
		return capacita;
	}

	public void setCapacita(String capacita) {
		this.capacita = capacita;
	}

	public int getVelScrittura() {
		return velScrittura;
	}

	public void setVelScrittura(int velScrittura) {
		this.velScrittura = velScrittura;
	}

	public int getVelLettura() {
		return velLettura;
	}

	public void setVelLettura(int velLettura) {
		this.velLettura = velLettura;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Tecnologia getTecnologia() {
		return tecnologia;
	}

	public void setTecnologia(Tecnologia tecnologia) {
		this.tecnologia = tecnologia;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}
	
	@Override
	public Map<String, String> getSpecifiche() {

	    Map<String, String> specs = new LinkedHashMap<>();

	    specs.put("Capacita", capacita);
	    specs.put("Velocità scrittura", String.valueOf(velScrittura));
	    specs.put("Velocità lettura", String.valueOf(velLettura));
	    specs.put("Tipo", tipo.name());
	    specs.put("Tecnologia Ram", tecnologia.name());
	    specs.put("Formato", formato);
	    return specs;
	}
	
	
    
    
}
