package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class CPUBean extends ProdottoBean{
	private static final long serialVersionUID = 1L;
	
    private int idCpu;
    private int core;
    private int thread;
    private String frequenza;
    private String frequenza_ram;
	private String tiporam;
    private String socket;
    private int tdp;
    
    public CPUBean() {
    	super();
    }

	public int getIdCpu() {
		return idCpu;
	}

	public void setIdCpu(int idCpu) {
		this.idCpu = idCpu;
	}

	public int getCore() {
		return core;
	}

	public void setCore(int core) {
		this.core = core;
	}

	public int getThread() {
		return thread;
	}

	public void setThread(int thread) {
		this.thread = thread;
	}

	public String getFrequenza() {
		return frequenza;
	}

	public void setFrequenza(String frequenza) {
		this.frequenza = frequenza;
	}

	public String getSocket() {
		return socket;
	}

	public void setSocket(String socket) {
		this.socket = socket;
	}

	public int getTdp() {
		return tdp;
	}

	public void setTdp(int tdp) {
		this.tdp = tdp;
	}
    
    public String getFrequenza_ram() {
		return frequenza_ram;
	}

	public void setFrequenza_ram(String frequenza_ram) {
		this.frequenza_ram = frequenza_ram;
	}

	public String getTiporam() {
		return tiporam;
	}

	public void setTiporam(String tiporam) {
		this.tiporam = tiporam;
	}
	
	@Override
	public Map<String, String> getSpecifiche() {

	    Map<String, String> specs = new LinkedHashMap<>();

	    specs.put("Socket", socket);
	    specs.put("Core", String.valueOf(core));
	    specs.put("Thread", String.valueOf(thread));
	    specs.put("Frequenza", frequenza);
	    specs.put("Frequenza Ram", frequenza_ram);
	    specs.put("Tipo Ram", tiporam);
	    specs.put("Tdp", String.valueOf(tdp));
	    return specs;
	}
	
}
