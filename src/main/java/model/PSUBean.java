package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class PSUBean extends ProdottoBean {
	
	private static final long serialVersionUID = 1L;
	
    public enum Modulare {MODULARE, SEMIMODULARE, NON_MODULARE }
    public enum Formato { ATX, SFX }

    private int idPsu;
    private int potenza;
    private String certificazione;
    private Modulare modulare;
    private Formato formato;

    public PSUBean() {
    	super();
    }

	public int getIdPsu() {
		return idPsu;
	}

	public void setIdPsu(int idPsu) {
		this.idPsu = idPsu;
	}

	public int getPotenza() {
		return potenza;
	}

	public void setPotenza(int potenza) {
		this.potenza = potenza;
	}

	public String getCertificazione() {
		return certificazione;
	}

	public void setCertificazione(String certificazione) {
		this.certificazione = certificazione;
	}

	public Modulare getModulare() {
		return modulare;
	}

	public void setModulare(Modulare modulare) {
		this.modulare = modulare;
	}

	public Formato getFormato() {
		return formato;
	}

	public void setFormato(Formato formato) {
		this.formato = formato;
	}
	
	@Override
	public Map<String, String> getSpecifiche() {

	    Map<String, String> specs = new LinkedHashMap<>();

	    specs.put("Potenza", String.valueOf(potenza));
	    specs.put("Certificazione", certificazione);
	    specs.put("Tipo", modulare.name());
	    specs.put("Formato", formato.name());
	    return specs;
	}    
    
    
}
