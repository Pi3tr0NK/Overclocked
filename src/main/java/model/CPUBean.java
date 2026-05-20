package model;

public class CPUBean extends ProdottoBean{
	private static final long serialVersionUID = 1L;
	
    private int idCpu;
    private int core;
    private int thread;
    private String frequenza;
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
}
