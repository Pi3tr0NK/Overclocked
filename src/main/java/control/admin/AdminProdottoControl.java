package control.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    
    private void aggiungiProdotto(String action, HttpServletRequest request,HttpServletResponse response) throws IOException
    {
        try {
        	
        	int idProdotto;
        	
            String categoria = request.getParameter("categoria");
            
            if(action.equals("aggiungi"))
            {
            	idProdotto = CreateAndSave(action, categoria, request);
            	salvaImmagini(action,request, idProdotto);
            }
            else
            {
            	idProdotto = Integer.valueOf(request.getParameter("idProdotto"));
            	modificaProdotto(action, categoria, request);
            	salvaImmagini(action,request, idProdotto);
            }
            
           
            
            response.sendRedirect( request.getContextPath() + "/admin/dashboard");
            
        }
        catch(Exception e) {
        	
            e.printStackTrace();
            request.setAttribute("errore", e.getMessage());
            
            response.sendRedirect(request.getContextPath() + "/home");
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

        String uploadDir = getServletContext().getRealPath("/img/prodotti");
        File folder = new File(uploadDir);
        if (!folder.exists()) folder.mkdirs();

        List<ImmagineBean> listaImmagini = new ArrayList<>();

        for (int i = 1; i <= MAX_IMMAGINI; i++) {

            Part part = request.getPart("immagine" + i);

            if (part != null && part.getSize() > 0
                    && part.getSubmittedFileName() != null
                    && !part.getSubmittedFileName().isBlank()) {

                // arrivato un file nuovo per questo slot
                String nomeOriginale = Paths.get(part.getSubmittedFileName())
                                            .getFileName().toString();
                String nomeFile = System.currentTimeMillis() + "_" + nomeOriginale;

                part.write(uploadDir + File.separator + nomeFile);

                ImmagineBean img = new ImmagineBean();
                img.setPath("img/prodotti/" + nomeFile);

                if (action.equals("aggiungi")) {

                    immaginiDAO.doSave(img, idProdotto);

                } else {

                    String idImmagineStr = request.getParameter("idImmagine" + i);

                    if (idImmagineStr != null && !idImmagineStr.isBlank()) {
                        // sostituisce immagine esistente tramite id
                        int idImmagine = Integer.parseInt(idImmagineStr);
                        img.setIdImmagine(idImmagine);
                        immaginiDAO.updateImage(idImmagine, img.getPath(), idProdotto);
                    } else {
                        // slot prima vuoto, ora ha un'immagine nuova
                        immaginiDAO.doSave(img, idProdotto);
                    }
                }

                listaImmagini.add(img);

            } else if (action.equals("modifica")) {

                // nessun file nuovo: mantieni immagine esistente
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
    
    
    
    
}