package model;

public class MoboBean extends ProdottoBean{
	private static final long serialVersionUID = 1L;
	
    private int idMobo;
    private String chipset;
    private String socket;
    private String tipoRam;
    private String maxFreq;
    private String formato;
    private String pcie;
    private int slotRam;
    private boolean nvme;
    private int porteSata;
    private int porteUsb;

    public MoboBean() {
    	super();
    }

	public int getIdMobo() {
		return idMobo;
	}

	public void setIdMobo(int idMobo) {
		this.idMobo = idMobo;
	}

	public String getChipset() {
		return chipset;
	}

	public void setChipset(String chipset) {
		this.chipset = chipset;
	}

	public String getSocket() {
		return socket;
	}

	public void setSocket(String socket) {
		this.socket = socket;
	}

	public String getTipoRam() {
		return tipoRam;
	}

	public void setTipoRam(String tipoRam) {
		this.tipoRam = tipoRam;
	}

	public String getMaxFreq() {
		return maxFreq;
	}

	public void setMaxFreq(String maxFreq) {
		this.maxFreq = maxFreq;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getPcie() {
		return pcie;
	}

	public void setPcie(String pcie) {
		this.pcie = pcie;
	}

	public int getSlotRam() {
		return slotRam;
	}

	public void setSlotRam(int slotRam) {
		this.slotRam = slotRam;
	}

	public boolean isNvme() {
		return nvme;
	}

	public void setNvme(boolean nvme) {
		this.nvme = nvme;
	}

	public int getPorteSata() {
		return porteSata;
	}

	public void setPorteSata(int porteSata) {
		this.porteSata = porteSata;
	}

	public int getPorteUsb() {
		return porteUsb;
	}

	public void setPorteUsb(int porteUsb) {
		this.porteUsb = porteUsb;
	}
    
    

}
