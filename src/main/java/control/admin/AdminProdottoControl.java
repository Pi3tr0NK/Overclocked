package control.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import dao.CPUDAOImpl;
import dao.ChassisDAOImpl;
import dao.DissipatoreDAOImpl;
import dao.GPUDAOImpl;
import dao.ImmaginiDAOImpl;
import dao.MemoriaDAOImpl;
import dao.MoboDAOImpl;
import dao.PSUDAOImpl;
import dao.ProdottoDAOImpl;
import dao.RAMDAOImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.CPUBean;
import model.ChassisBean;
import model.DissipatoreBean;
import model.GPUBean;
import model.ImmagineBean;
import model.MemoriaBean;
import model.MemoriaBean.Tecnologia;
import model.MoboBean;
import model.PSUBean;
import model.ProdottoBean;
import model.RAMBean;
import model.PSUBean.Formato;
import model.PSUBean.Modulare;

@WebServlet("/admin/aggiungiProdotto")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 25 * 1024 * 1024
)
public class AdminProdottoControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int MAX_IMMAGINI = 5;

    private DataSource ds;
    private ImmaginiDAOImpl immaginiDAO;
    private CPUDAOImpl cpuDAO;
    private GPUDAOImpl gpuDAO;
    private PSUDAOImpl psuDAO;
    private RAMDAOImpl ramDAO;
    private MemoriaDAOImpl memDAO;
    private ChassisDAOImpl chassisDAO;
    private DissipatoreDAOImpl dissipatoreDAO;
    private MoboDAOImpl moboDAO;
    private ProdottoDAOImpl productDAO;
    
    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        
        ds = (DataSource) getServletContext().getAttribute("DataSource");

        if(ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        
        immaginiDAO = new ImmaginiDAOImpl(ds);
        cpuDAO = new CPUDAOImpl(ds);
		psuDAO =new PSUDAOImpl(ds);
		chassisDAO =new ChassisDAOImpl(ds);
		dissipatoreDAO =new DissipatoreDAOImpl(ds);
		gpuDAO =new GPUDAOImpl(ds);
		memDAO =new MemoriaDAOImpl(ds);
		moboDAO =new MoboDAOImpl(ds);
		ramDAO =new RAMDAOImpl(ds);
		productDAO = new ProdottoDAOImpl(ds);
    }

    
    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
    	
        request.setCharacterEncoding("UTF-8");
        
        try {
			processAction(request,response);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
          
    }

    
    private void processAction(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException, ServletException
    {
    	String action = request.getParameter("action");
    	
    	
    	if(action.equals("attiva"))
    		attivaProdotto(request,response);
    	else if(action.equals("disattiva"))
    		disattivaProdotto(request,response);
    	else if(action.equals("aggiungi"))
    		aggiungiProdotto(action,request,response);
    	else if(action.equals("aggiungiView"))
    		aggiungiViewProdotto(request,response);
    	else if(action.equals("modifica"))
    		aggiungiProdotto(action,request,response);
    	else if(action.equals("modificaView"))
    		modificaViewProdotto(request,response);
    	
    }
    
    
    private void aggiungiViewProdotto(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
    {
    	request.getRequestDispatcher("/WEB-INF/views/admin/AggiungiProdotto.jsp").forward(request, response);
    	
    }
    
    private void aggiungiProdotto(String action,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException
	{
		try {
		
			String errore = validaParametri(action, request);
			
			if (errore != null) {
			
				String erroreEncoded = java.net.URLEncoder.encode(errore, "UTF-8");
				
				if (action.equals("aggiungi")) {
					response.sendRedirect(
					  request.getContextPath()
					  + "/admin/aggiungiProdotto?action=aggiungiView&errore="
					  + erroreEncoded);
				} else {
			
					String idProdotto = request.getParameter("idProdotto");
					String categoria  = request.getParameter("categoria");
					
					response.sendRedirect(
					  request.getContextPath()
					  + "/admin/aggiungiProdotto?action=modificaView"
					  + "&id=" + idProdotto
					  + "&categoria=" + categoria
					  + "&errore=" + erroreEncoded);
				  }
			
				return;
			}
			
			int idProdotto;
			String categoria = request.getParameter("categoria");
			
			if (action.equals("aggiungi")) {
				
				idProdotto = CreateAndSave(action, categoria, request);
				salvaImmagini(action, request, idProdotto);
				
				response.sendRedirect(
				request.getContextPath()
				+ "/admin/aggiungiProdotto?action=aggiungiView&success=true");
			} else {
			
				idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
				
				modificaProdotto(action, categoria, request);
				salvaImmagini(action, request, idProdotto);
				
				response.sendRedirect(
				request.getContextPath()
				+ "/admin/aggiungiProdotto?action=modificaView"
				+ "&id=" + idProdotto
				+ "&categoria=" + categoria
				+ "&success=true");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			
			response.sendRedirect(
			request.getContextPath()
			+ "/home");
		}
	}
    
    private int CreateAndSave(String action, String categoria, HttpServletRequest request) throws Exception {

        switch(categoria.toUpperCase()) {

            case "CPU":
                CPUBean cpu = creaCPU(action, request);
                cpuDAO.doSave(cpu);
                return cpu.getIdProdotto();
                
            case "GPU":
                GPUBean gpu = creaGPU(action, request);
                gpuDAO.doSave(gpu);
                return gpu.getIdProdotto();
                
            case "RAM":
                RAMBean ram = creaRAM(action, request);
                ramDAO.doSave(ram);
                return ram.getIdProdotto();
                	
            case "DISSIPATORE":
                DissipatoreBean dissipatore = creaDissipatore(action, request);
                dissipatoreDAO.doSave(dissipatore);
                return dissipatore.getIdProdotto();
                
            case "CASE":
                ChassisBean chassis = creaCase(action, request);
                chassisDAO.doSave(chassis);
                return chassis.getIdProdotto();
                
            case "PSU":
                PSUBean psu = creaPSU(action, request);
                psuDAO.doSave(psu);
                return psu.getIdProdotto();
                
            case "MOBO":
                MoboBean mobo = creaMOBO(action, request);
                moboDAO.doSave(mobo);
                return mobo.getIdProdotto();
                
            case "STORAGE":
                MemoriaBean mem = creaStorage(action, request);
                memDAO.doSave(mem);
                return mem.getIdProdotto();
                
            default:
                throw new Exception( "Categoria non supportata: "+ categoria);
        	}
    }

  
    private void creaProdotto(String action, ProdottoBean p,HttpServletRequest request) {

	    if(action.equals("modifica"))
	    	p.setIdProdotto(Integer.valueOf(request.getParameter("idProdotto")));
	    
        p.setNome(request.getParameter("nome"));
        p.setModello(request.getParameter("modello"));
        p.setDescrizione(request.getParameter("descrizione"));
        p.setMarca(request.getParameter("marca"));
        p.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
        p.setStock(Integer.parseInt(request.getParameter("stock")));
        p.setDimensioni(request.getParameter("dimensioni"));
        p.setPeso(request.getParameter("peso"));
        p.setAttivo(request.getParameter("attivo") != null);
        p.setSconto(Integer.parseInt(request.getParameter("sconto")));
        p.setCategoria(request.getParameter("categoria"));
    }
    

    private CPUBean creaCPU(String action, HttpServletRequest request) {
    	
    	CPUBean cpu = new CPUBean();
    	
    	creaProdotto(action,cpu,request);
        
        cpu.setCore(Integer.parseInt(request.getParameter("core")));
        cpu.setThread(Integer.parseInt(request.getParameter("thread")));
        cpu.setFrequenza(request.getParameter("frequenza"));
        cpu.setFrequenza_ram(request.getParameter("frequenzaram"));
        cpu.setTiporam(request.getParameter("tiporam"));
        cpu.setSocket(request.getParameter("socket"));
        cpu.setTdp(Integer.parseInt(request.getParameter("tdp")));
        
	    if(action.equals("modifica"))
	    	cpu.setIdCpu(Integer.valueOf(request.getParameter("idCpu")));
	    
        return cpu;
    }

    private GPUBean creaGPU(String action, HttpServletRequest request) {

    	GPUBean gpu = new GPUBean();
    	
    	creaProdotto(action,gpu,request);
        
    	gpu.setFrequenza(request.getParameter("frequenza"));
	    gpu.setVram(request.getParameter("vram"));
	    gpu.setVideo(request.getParameter("video"));
	    gpu.setTipoVram(request.getParameter("tipovram"));
	    gpu.setMaxRes(request.getParameter("maxres"));
	    gpu.setPcie(request.getParameter("pcie"));
	    gpu.setTdp(Integer.parseInt(request.getParameter("tdp")));
	    
	    if(action.equals("modifica"))
	    	gpu.setIdGpu(Integer.valueOf(request.getParameter("idGpu")));
	    
        return gpu;
    }

    private PSUBean creaPSU(String action, HttpServletRequest request) {

    	PSUBean psu = new PSUBean();

    	creaProdotto(action,psu,request);
    	
    	psu.setPotenza(Integer.parseInt(request.getParameter("potenza")));
    	psu.setCertificazione(request.getParameter("certificazione"));
    	psu.setModulare(Modulare.valueOf(request.getParameter("modulare")));
    	psu.setFormato(Formato.valueOf(request.getParameter("formato")));
    	
	    if(action.equals("modifica"))
	    	psu.setIdPsu(Integer.valueOf(request.getParameter("idPsu")));
	    
        return psu;
    }
    
    private MoboBean creaMOBO(String action,HttpServletRequest request) {

    	MoboBean mobo = new MoboBean();

    	creaProdotto(action,mobo,request);
    	
	    mobo.setChipset(request.getParameter("chipset"));
	    mobo.setSocket(request.getParameter("socket"));
	    mobo.setTipoRam(request.getParameter("tipoRam"));
	    mobo.setMaxFreq(request.getParameter("maxFreq"));
	    mobo.setFormato(request.getParameter("formato"));
	    mobo.setPcie(request.getParameter("pcie"));
	    mobo.setSlotRam(Integer.parseInt(request.getParameter("slotRam")));
	    mobo.setNvme(Boolean.parseBoolean(request.getParameter("nvme")));
	    mobo.setPorteSata(Integer.parseInt(request.getParameter("porteSata")));
	    mobo.setPorteUsb(Integer.parseInt(request.getParameter("porteUsb")));
	    
	    if(action.equals("modifica"))
	    	mobo.setIdMobo(Integer.valueOf(request.getParameter("idMobo")));
	    
        return mobo;
    }
    
    private MemoriaBean creaStorage(String action,HttpServletRequest request) {

    	MemoriaBean mem = new MemoriaBean();

    	creaProdotto(action,mem,request);
    	
    	mem.setCapacita(request.getParameter("capacita"));
    	mem.setVelScrittura(Integer.parseInt(request.getParameter("scrittura")));
    	mem.setVelLettura(Integer.parseInt(request.getParameter("lettura")));
    	mem.setTipo(model.MemoriaBean.Tipo.valueOf(request.getParameter("tipo")));
    	mem.setTecnologia(Tecnologia.valueOf(request.getParameter("tecnologia")));
    	mem.setFormato(request.getParameter("formato"));
    	
	    if(action.equals("modifica"))
	    	mem.setIdMemoria(Integer.valueOf(request.getParameter("idMemoria")));
        return mem;
    }
    
    private ChassisBean creaCase(String action,HttpServletRequest request) {

    	ChassisBean c = new ChassisBean();

    	creaProdotto(action,c,request);
    	
    	c.setFormato(request.getParameter("formato"));
    	c.setColore(request.getParameter("colore"));
    	c.setMateriale(request.getParameter("materiale"));
    	
	    if(action.equals("modifica"))
	    	c.setIdCase(Integer.valueOf(request.getParameter("idCase")));
	    
        return c;
    }
    
    private DissipatoreBean creaDissipatore(String action, HttpServletRequest request) {

    	DissipatoreBean dissipatore = new DissipatoreBean();

    	creaProdotto(action, dissipatore,request);
    	
    	dissipatore.setTipo(model.DissipatoreBean.Tipo.valueOf(request.getParameter("tipo")));
    	dissipatore.setSocketSupportati(request.getParameter("socket"));
    	dissipatore.setDimensioniVentola("dimensione");
    	dissipatore.setRpmMax(Integer.parseInt(request.getParameter("rpm")));
    	dissipatore.setRumore(Integer.parseInt(request.getParameter("rumore")));
    	dissipatore.setTdpSupportato(Integer.parseInt(request.getParameter("tdp")));
    	
	    if(action.equals("modifica"))
	    	dissipatore.setIdDissipatore(Integer.valueOf(request.getParameter("idDissipatore")));
	    
        return dissipatore;
        

    }
    
    private RAMBean creaRAM(String action, HttpServletRequest request) {
    	
    	RAMBean ram = new RAMBean();
    	
    	creaProdotto(action, ram,request);
        
    	ram.setCapacita(request.getParameter("capacita"));
    	ram.setFrequenza(request.getParameter("frequenza"));
    	ram.setTipo(request.getParameter("tipo"));
    	
	    if(action.equals("modifica"))
	    	ram.setIdRam(Integer.valueOf(request.getParameter("idRam")));
	    
        return ram;
    }  
    
    private List<ImmagineBean> salvaImmagini(String action, HttpServletRequest request, int idProdotto) throws Exception {

        String uploadDir = getServletContext().getRealPath(File.separator + "img" + File.separator + "prodotti");
        File folder = new File(uploadDir);
        if (!folder.exists()) folder.mkdirs();

        List<ImmagineBean> listaImmagini = new ArrayList<>();

        for (int i = 1; i <= MAX_IMMAGINI; i++) {

            Part part = request.getPart("immagine" + i);
            String rimuoviStr = request.getParameter("rimuoviImmagine" + i);
            boolean daRimuovere = "true".equals(rimuoviStr);

            if (part != null && part.getSize() > 0
                    && part.getSubmittedFileName() != null
                    && !part.getSubmittedFileName().isBlank()) {

                String nomeOriginale = Paths.get(part.getSubmittedFileName())
                                            .getFileName().toString();

                String estensione = "";
                int dotIndex = nomeOriginale.lastIndexOf('.');
                if (dotIndex > 0) {
                    estensione = nomeOriginale.substring(dotIndex);
                }

                String nomeFile = "immagine" + System.currentTimeMillis() + "_" + idProdotto + estensione;

                part.write(uploadDir + File.separator + nomeFile);

                ImmagineBean img = new ImmagineBean();
                img.setPath("img/prodotti/" + nomeFile);

                if (action.equals("aggiungi")) {

                    immaginiDAO.doSave(img, idProdotto);

                } else {

                    String idImmagineStr = request.getParameter("idImmagine" + i);

                    if (idImmagineStr != null && !idImmagineStr.isBlank()) {
                        int idImmagine = Integer.parseInt(idImmagineStr);
                        img.setIdImmagine(idImmagine);
                        immaginiDAO.updateImage(idImmagine, img.getPath(), idProdotto);
                    } else {
                        immaginiDAO.doSave(img, idProdotto);
                    }
                }

                listaImmagini.add(img);

            } else if (action.equals("modifica") && daRimuovere) {

                String idImmagineStr = request.getParameter("idImmagine" + i);
                String pathEsistente = request.getParameter("pathEsistente" + i);

                if (idImmagineStr != null && !idImmagineStr.isBlank()) {
                    int idImmagine = Integer.parseInt(idImmagineStr);

                    immaginiDAO.doDelete(idImmagine);

                    if (pathEsistente != null && !pathEsistente.isBlank()) {
                        String nomeFileEsistente = Paths.get(pathEsistente).getFileName().toString();
                        File vecchioFile = new File(uploadDir, nomeFileEsistente);
                        if (vecchioFile.exists()) vecchioFile.delete();
                    }
                }

            } else if (action.equals("modifica")) {

                String idImmagineStr  = request.getParameter("idImmagine" + i);
                String pathEsistente  = request.getParameter("pathEsistente" + i);

                if (idImmagineStr != null && !idImmagineStr.isBlank()
                        && pathEsistente != null && !pathEsistente.isBlank()) {

                    ImmagineBean img = new ImmagineBean();
                    img.setIdImmagine(Integer.parseInt(idImmagineStr));
                    img.setPath(pathEsistente);
                    listaImmagini.add(img);
                }
            }
        }

        return listaImmagini;
    }
    
    
    // DISABILITA - ATTIVA PRODOTTO
    
    private void disattivaProdotto (HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
    	
    	int id = Integer.valueOf(request.getParameter("id"));
    	productDAO.setProductStatus(id, false);
    	response.sendRedirect( request.getContextPath() + "/admin/dashboard");
    }  
    
    private void attivaProdotto (HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
    	
    	int id = Integer.valueOf(request.getParameter("id"));
    	productDAO.setProductStatus(id, true);
    	response.sendRedirect( request.getContextPath() + "/admin/dashboard");
    	
    }  
    
    
    private void modificaProdotto (String action, String categoria, HttpServletRequest request) throws SQLException {
     	

        switch(categoria.toUpperCase()) {

        case "CPU":
            CPUBean cpu = creaCPU(action, request);
            cpuDAO.doUpdate(cpu);
            return;
            
        case "GPU":
            GPUBean gpu = creaGPU(action, request);
            gpuDAO.doUpdate(gpu);
            return;
            
        case "RAM":
            RAMBean ram = creaRAM(action, request);
            ramDAO.doUpdate(ram);
            return;
            	
        case "DISSIPATORE":
            DissipatoreBean dissipatore = creaDissipatore(action, request);
            dissipatoreDAO.doUpdate(dissipatore);
            return;
            
        case "CASE":
            ChassisBean chassis = creaCase(action, request);
            chassisDAO.doUpdate(chassis);
            return;
            
        case "PSU":
            PSUBean psu = creaPSU(action, request);
            psuDAO.doUpdate(psu);
            return;
            
        case "MOBO":
            MoboBean mobo = creaMOBO(action, request);
            moboDAO.doUpdate(mobo);
            return;
            
        case "STORAGE":
            MemoriaBean mem = creaStorage(action, request);
            memDAO.doUpdate(mem);
            return;
            
        }
    }  
    
    private void modificaViewProdotto (HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
    	
       	int id = Integer.valueOf(request.getParameter("id"));
        String categoria = request.getParameter("categoria");
    	
        switch(categoria.toUpperCase()) {

        case "CPU":
        	request.setAttribute("prodotto",cpuDAO.doRetrieveByKey(id));
        	break;
        case "GPU":
        	request.setAttribute("prodotto",gpuDAO.doRetrieveByKey(id));
            break;
        case "RAM":
        	request.setAttribute("prodotto",ramDAO.doRetrieveByKey(id));
        	break;
        case "DISSIPATORE":
        	request.setAttribute("prodotto",dissipatoreDAO.doRetrieveByKey(id));
            break;
        case "CASE":
        	request.setAttribute("prodotto",chassisDAO.doRetrieveByKey(id));
            break;
        case "PSU":
        	request.setAttribute("prodotto",psuDAO.doRetrieveByKey(id));
            break;
        case "MOBO":
        	request.setAttribute("prodotto",moboDAO.doRetrieveByKey(id));
            break;
        case "STORAGE":
        	request.setAttribute("prodotto",memDAO.doRetrieveByKey(id));
        	break;
        }
        
        request.getRequestDispatcher("/WEB-INF/views/admin/ModificaProdotto.jsp").forward(request, response);
    }
    
    private String validaParametri(String action, HttpServletRequest request) {

        Map<String, String> regole   = new LinkedHashMap<>();
        Map<String, String> messaggi = new LinkedHashMap<>();
        Map<String, String> valori   = new LinkedHashMap<>();

        // campi comuni
        regole.put("nome",        "^.{2,255}$");
        regole.put("modello",     "^.{1,255}$");
        regole.put("marca",       "^[A-Za-z0-9À-ÿ\\s'.&\\-]{1,255}$");
        regole.put("descrizione", "^[\\s\\S]{0,255}$");
        regole.put("prezzo",      "^\\d{1,8}(\\.\\d{1,2})?$");
        regole.put("sconto",      "^(0|[1-9][0-9]?|100)$");
        regole.put("stock",       "^(0|[1-9]\\d*)$");
        regole.put("dimensioni",  "^.{0,50}$");
        regole.put("peso",        "^(?i)(?=.{1,50}$)\\d+(\\.\\d+)?\\s?(Kg|g)$");

        messaggi.put("nome",        "Il nome deve essere compreso tra 2 e 255 caratteri.");
        messaggi.put("modello",     "Il modello deve essere compreso tra 1 e 255 caratteri.");
        messaggi.put("marca",       "La marca non può superare i 255 caratteri.");
        messaggi.put("descrizione", "La descrizione non può superare i 255 caratteri.");
        messaggi.put("prezzo",      "Inserisci un prezzo valido (es. 199.99).");
        messaggi.put("sconto",      "Lo sconto deve essere un numero intero compreso tra 0 e 100.");
        messaggi.put("stock",       "Lo stock deve essere un numero intero maggiore o uguale a 0.");
        messaggi.put("dimensioni",  "Le dimensioni non possono superare i 50 caratteri.");
        messaggi.put("peso",        "Inserisci il peso seguito dall'unità di misura (es. 1.4 Kg o 500g).");

        valori.put("nome",        request.getParameter("nome"));
        valori.put("modello",     request.getParameter("modello"));
        valori.put("marca",       request.getParameter("marca"));
        valori.put("descrizione", request.getParameter("descrizione"));
        valori.put("prezzo",      request.getParameter("prezzo"));
        valori.put("sconto",      request.getParameter("sconto"));
        valori.put("stock",       request.getParameter("stock"));
        valori.put("dimensioni",  request.getParameter("dimensioni"));
        valori.put("peso",        request.getParameter("peso"));

        // campi per categoria
        String categoria = request.getParameter("categoria");
        if (categoria == null) return "Seleziona una categoria valida.";

        switch (categoria.toUpperCase()) {

            case "CPU":
                regole.put("core",         "^[1-9]\\d*$");
                regole.put("thread",       "^[1-9]\\d*$");
                regole.put("frequenza",    "^(?i)\\d+(\\.\\d+)?\\s?(GHz|MHz|KHz|Hz)$");
                regole.put("frequenzaram", "^(?i)\\d+(\\.\\d+)?\\s?(GHz|MHz|KHz|Hz)$");
                regole.put("tiporam",      "^[A-Za-z0-9\\s\\-]{2,10}$");
                regole.put("socket",       "^[A-Za-z0-9\\s\\-+,._]{2,20}$");
                regole.put("tdp",          "^(0|[1-9]\\d*)$");

                messaggi.put("core",         "Il numero di core deve essere maggiore di 0.");
                messaggi.put("thread",       "Il numero di thread deve essere maggiore di 0.");
                messaggi.put("frequenza",    "Inserisci la frequenza con l'unità di misura (es. 3.6GHz).");
                messaggi.put("frequenzaram", "Inserisci una frequenza RAM valida (es. 5600 MHz).");
                messaggi.put("tiporam",      "Specificare un tipo di RAM valido (es. DDR5).");
                messaggi.put("socket",       "Inserisci un socket valido (es. LGA1700).");
                messaggi.put("tdp",          "Il TDP deve essere un numero intero valido.");

                valori.put("core",         request.getParameter("core"));
                valori.put("thread",       request.getParameter("thread"));
                valori.put("frequenza",    request.getParameter("frequenza"));
                valori.put("frequenzaram", request.getParameter("frequenzaram"));
                valori.put("tiporam",      request.getParameter("tiporam"));
                valori.put("socket",       request.getParameter("socket"));
                valori.put("tdp",          request.getParameter("tdp"));
                break;

            case "GPU":
                regole.put("frequenza", "^(?i)\\d+(\\.\\d+)?\\s?(GHz|MHz|KHz|Hz)$");
                regole.put("vram",      "^(?i)(?=.{2,10}$)\\d+(\\.\\d+)?\\s?(GB|TB|MB|KB)$");
                regole.put("tipovram",  "^[A-Za-z0-9\\s\\-]{2,20}$");
                regole.put("pcie",      "^.{2,10}$");
                regole.put("video",     "^.{2,50}$");
                regole.put("maxres",    "^\\d+x\\d+$");
                regole.put("tdp",       "^(0|[1-9]\\d*)$");

                messaggi.put("frequenza", "Inserisci la frequenza con l'unità di misura (es. 2.5GHz).");
                messaggi.put("vram",      "Inserisci la VRAM con l'unità di misura (es. 8 GB).");
                messaggi.put("tipovram",  "Specificare un tipo di VRAM valido (es. GDDR6X).");
                messaggi.put("pcie",      "Specificare l'interfaccia PCIe (es. PCIe 4.0).");
                messaggi.put("video",     "Inserisci le uscite video (es. 3x DP, 1x HDMI).");
                messaggi.put("maxres",    "Inserisci la risoluzione nel formato corretto (es. 7680x4320).");
                messaggi.put("tdp",       "Il TDP deve essere un numero intero valido.");

                valori.put("frequenza", request.getParameter("frequenza"));
                valori.put("vram",      request.getParameter("vram"));
                valori.put("tipovram",  request.getParameter("tipovram"));
                valori.put("pcie",      request.getParameter("pcie"));
                valori.put("video",     request.getParameter("video"));
                valori.put("maxres",    request.getParameter("maxres"));
                valori.put("tdp",       request.getParameter("tdp"));
                break;

            case "RAM":
                regole.put("capacita",  "^(?i)(?=.{2,10}$)\\d+(\\.\\d+)?\\s?(GB|TB|MB|KB)$");
                regole.put("frequenza", "^\\d+(\\.\\d+)?\\s?(GHz|MHz|KHz|Hz)$");
                regole.put("tipo",      "^.{2,10}$");

                messaggi.put("capacita",  "Inserisci la capacità con l'unità di misura (es. 16 GB).");
                messaggi.put("frequenza", "Inserisci la frequenza con l'unità di misura (es. 5600 MHz).");
                messaggi.put("tipo",      "Seleziona un tipo valido (es. DDR5).");

                valori.put("capacita",  request.getParameter("capacita"));
                valori.put("frequenza", request.getParameter("frequenza"));
                valori.put("tipo",      request.getParameter("tipo"));
                break;

            case "STORAGE":
                regole.put("capacita",   "^(?i)(?=.{2,10}$)\\d+(\\.\\d+)?\\s?(GB|TB|MB|KB)$");
                regole.put("lettura",    "^(0|[1-9]\\d*)$");
                regole.put("scrittura",  "^(0|[1-9]\\d*)$");
                regole.put("tipo",       "^.{2,10}$");
                regole.put("tecnologia", "^(NVME|SATA)$");
                regole.put("formato",    "^.{2,20}$");

                messaggi.put("capacita",   "Inserisci la capacità con l'unità di misura (es. 1 TB).");
                messaggi.put("lettura",    "La velocità di lettura deve essere un numero intero.");
                messaggi.put("scrittura",  "La velocità di scrittura deve essere un numero intero.");
                messaggi.put("tipo",       "Seleziona un tipo valido.");
                messaggi.put("tecnologia", "Seleziona una tecnologia valida (NVME o SATA).");
                messaggi.put("formato",    "Specificare un formato valido (es. M.2 2280).");

                valori.put("capacita",   request.getParameter("capacita"));
                valori.put("lettura",    request.getParameter("lettura"));
                valori.put("scrittura",  request.getParameter("scrittura"));
                valori.put("tipo",       request.getParameter("tipo"));
                valori.put("tecnologia", request.getParameter("tecnologia"));
                valori.put("formato",    request.getParameter("formato"));
                break;

            case "MOBO":
                regole.put("chipset",    "^[A-Za-z0-9\\s\\-]{2,20}$");
                regole.put("socket",     "^[A-Za-z0-9\\s\\-+,._]{2,20}$");
                regole.put("tipoRam",    "^[A-Za-z0-9\\s\\-]{2,10}$");
                regole.put("maxFreq",    "^.{2,20}$");
                regole.put("formato",    "^.{2,20}$");
                regole.put("pcie",       "^.{2,10}$");
                regole.put("slotRam",    "^[1-9]\\d*$");
                regole.put("porteSata",  "^(0|[1-9]\\d*)$");
                regole.put("porteUsb",   "^(0|[1-9]\\d*)$");
                regole.put("nvme",       "^(true|false)$");

                messaggi.put("chipset",    "Inserisci un chipset valido (es. Z790).");
                messaggi.put("socket",     "Inserisci un socket valido (es. LGA1700).");
                messaggi.put("tipoRam",    "Specificare un tipo di RAM valido (es. DDR5).");
                messaggi.put("maxFreq",    "La frequenza massima non può superare i 20 caratteri.");
                messaggi.put("formato",    "Specificare un formato valido (es. ATX).");
                messaggi.put("pcie",       "Specificare l'interfaccia PCIe (es. PCIe 4.0).");
                messaggi.put("slotRam",    "Gli slot RAM devono essere un numero intero valido.");
                messaggi.put("porteSata",  "Inserisci un numero di porte SATA valido.");
                messaggi.put("porteUsb",   "Inserisci un numero di porte USB valido.");
                messaggi.put("nvme",       "Seleziona un'opzione valida per NVMe.");

                valori.put("chipset",    request.getParameter("chipset"));
                valori.put("socket",     request.getParameter("socket"));
                valori.put("tipoRam",    request.getParameter("tipoRam"));
                valori.put("maxFreq",    request.getParameter("maxFreq"));
                valori.put("formato",    request.getParameter("formato"));
                valori.put("pcie",       request.getParameter("pcie"));
                valori.put("slotRam",    request.getParameter("slotRam"));
                valori.put("porteSata",  request.getParameter("porteSata"));
                valori.put("porteUsb",   request.getParameter("porteUsb"));
                valori.put("nvme",       request.getParameter("nvme"));
                break;

            case "PSU":
                regole.put("potenza",        "^[1-9]\\d*$");
                regole.put("certificazione", "^[A-Za-z0-9\\s+]{1,255}$");
                regole.put("modulare",       "^(MODULARE|SEMIMODULARE|NON_MODULARE)$");
                regole.put("formato",        "^.{2,20}$");

                messaggi.put("potenza",        "La potenza deve essere un numero intero maggiore di 0.");
                messaggi.put("certificazione", "Inserisci una certificazione valida (es. 80+ Gold).");
                messaggi.put("modulare",       "Seleziona un'opzione di modularità valida.");
                messaggi.put("formato",        "Specificare un formato valido (es. ATX).");

                valori.put("potenza",        request.getParameter("potenza"));
                valori.put("certificazione", request.getParameter("certificazione"));
                valori.put("modulare",       request.getParameter("modulare"));
                valori.put("formato",        request.getParameter("formato"));
                break;

            case "CASE":
                regole.put("formato",   "^.{2,20}$");
                regole.put("colore",    "^[A-Za-zÀ-ÿ\\s']{2,20}$");
                regole.put("materiale", "^.{2,255}$");

                messaggi.put("formato",   "Specificare un formato valido (es. Mid Tower).");
                messaggi.put("colore",    "Il colore non può superare i 20 caratteri.");
                messaggi.put("materiale", "Il materiale non può superare i 255 caratteri.");

                valori.put("formato",   request.getParameter("formato"));
                valori.put("colore",    request.getParameter("colore"));
                valori.put("materiale", request.getParameter("materiale"));
                break;

            case "DISSIPATORE":
                regole.put("tipo",   "^(ARIA|LIQUIDO)$");
                regole.put("socket", "^[A-Za-z0-9\\s\\-+,._]{2,20}$");
                regole.put("rpm",    "^(0|[1-9]\\d*)$");
                regole.put("rumore", "^(0|[1-9]\\d*)$");
                regole.put("tdp",    "^(0|[1-9]\\d*)$");

                messaggi.put("tipo",   "Seleziona un tipo di dissipatore valido.");
                messaggi.put("socket", "Inserisci i socket supportati (es. LGA1700).");
                messaggi.put("rpm",    "I giri al minuto (RPM) devono essere un numero intero.");
                messaggi.put("rumore", "Il livello di rumore (dBA) deve essere un numero intero.");
                messaggi.put("tdp",    "Il TDP deve essere un numero intero valido.");

                valori.put("tipo",   request.getParameter("tipo"));
                valori.put("socket", request.getParameter("socket"));
                valori.put("rpm",    request.getParameter("rpm"));
                valori.put("rumore", request.getParameter("rumore"));
                valori.put("tdp",    request.getParameter("tdp"));
                break;

            default:
                return "Categoria non supportata: " + categoria;
        }

        for (Map.Entry<String, String> campo : valori.entrySet()) {
            String chiave = campo.getKey();
            String valore = campo.getValue();

            boolean opzionale = chiave.equals("descrizione") || chiave.equals("dimensioni");

            if (valore == null || valore.trim().isEmpty()) {
                if (opzionale) continue;
                return "Il campo " + chiave + " è obbligatorio.";
            }

            if (!Pattern.matches(regole.get(chiave), valore.trim())) {
                return messaggi.get(chiave);
            }
        }

        return null;
    }
    
    
    
    
}