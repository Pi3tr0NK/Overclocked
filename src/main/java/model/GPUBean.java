package model;

public class GPUBean extends ProdottoBean {
	private static final long serialVersionUID = 1L;
	
    private int idGpu;
    private String frequenza;
    private String vram;
    private String video;
    private String tipoVram;
    private String pcie;
    private String maxRes;
    private int tdp;
    
    public GPUBean() {
    }

	public int getIdGpu() {
		return idGpu;
	}

	public void setIdGpu(int idGpu) {
		this.idGpu = idGpu;
	}

	public String getFrequenza() {
		return frequenza;
	}

	public void setFrequenza(String frequenza) {
		this.frequenza = frequenza;
	}

	public String getVram() {
		return vram;
	}

	public void setVram(String vram) {
		this.vram = vram;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getTipoVram() {
		return tipoVram;
	}

	public void setTipoVram(String tipoVram) {
		this.tipoVram = tipoVram;
	}

	public String getPcie() {
		return pcie;
	}

	public void setPcie(String pcie) {
		this.pcie = pcie;
	}

	public String getMaxRes() {
		return maxRes;
	}

	public void setMaxRes(String maxRes) {
		this.maxRes = maxRes;
	}

	public int getTdp() {
		return tdp;
	}

	public void setTdp(int tdp) {
		this.tdp = tdp;
	}
    
    
    
}
