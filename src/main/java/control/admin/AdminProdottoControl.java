package control.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
import model.MemoriaBean.Tipo;
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
    }

    
    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/admin/AdminView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            String categoria = request.getParameter("categoria");

            if(categoria == null || categoria.isBlank()) {
                throw new ServletException("Categoria non specificata");
            }

            ProdottoBean prodotto = creaProdotto(categoria, request);
            
            salvaProdotto(prodotto);
            
            salvaImmagini(request, prodotto.getIdProdotto());

           
            
            response.sendRedirect( request.getContextPath() + "/admin/aggiungiProdotto?success=1");

        }
        catch(Exception e) {

            e.printStackTrace();

            request.setAttribute("errore", e.getMessage());
            
            System.out.println(e.getMessage());
        }
    }


    private ProdottoBean creaProdotto(String categoria, HttpServletRequest request) throws Exception {

        switch(categoria.toUpperCase()) {

            case "CPU":
                return creaCPU(request);

            case "GPU":
                return creaGPU(request);

            case "RAM":
                return creaRAM(request);
 
            case "DISSIPATORE":
                return creaDissipatore(request);

            case "CASE":
                return creaCase(request);

            case "PSU":
                return creaPSU(request);
                
            case "MOBO":
                return creaMOBO(request);
                
            case "STORAGE":
                return creaStorage(request);

            default:
                throw new Exception(
                        "Categoria non supportata: "
                        + categoria);
        }
        

    }

    private void salvaProdotto(ProdottoBean prodotto)
            throws SQLException {

        switch(prodotto.getCategoria().toUpperCase()) {

            case "CPU":
                cpuDAO.doSave((CPUBean) prodotto);
                break;

            case "GPU":
                gpuDAO.doSave((GPUBean) prodotto);
                break;
                
            case "RAM":
            	ramDAO.doSave((RAMBean) prodotto);
                break;
 
            case "DISSIPATORE":
            	dissipatoreDAO.doSave((DissipatoreBean) prodotto);
                break;

            case "CASE":
            	chassisDAO.doSave((ChassisBean) prodotto);
                break;

            case "PSU":
            	psuDAO.doSave((PSUBean) prodotto);
                break;
                
            case "MOBO":
            	moboDAO.doSave((MoboBean) prodotto);
                break;
                
            case "STORAGE":
            	memDAO.doSave((MemoriaBean) prodotto);
                break;

            default:
                throw new SQLException("Categoria non supportata");
        }
    }



    private CPUBean creaCPU(HttpServletRequest request) {
    	
    	CPUBean cpu = new CPUBean();
    	
    	creaProdotto(cpu,request);
        
        cpu.setCore(Integer.parseInt(request.getParameter("core")));
        cpu.setThread(Integer.parseInt(request.getParameter("thread")));
        cpu.setFrequenza(request.getParameter("frequenza"));
        cpu.setFrequenza_ram(request.getParameter("frequenzaram"));
        cpu.setTiporam(request.getParameter("tiporam"));
        cpu.setSocket(request.getParameter("socket"));
        cpu.setTdp(Integer.parseInt(request.getParameter("tdp")));

        return cpu;
    }

    private GPUBean creaGPU(HttpServletRequest request) {

    	GPUBean gpu = new GPUBean();
    	
    	creaProdotto(gpu,request);
        
    	gpu.setFrequenza(request.getParameter("frequenza"));
	    gpu.setVram(request.getParameter("vram"));
	    gpu.setVideo(request.getParameter("video"));
	    gpu.setTipoVram(request.getParameter("tipovram"));
	    gpu.setMaxRes(request.getParameter("maxres"));
	    gpu.setPcie(request.getParameter("pcie"));
	    gpu.setTdp(Integer.parseInt(request.getParameter("tdp")));
	    
        return gpu;
    }

    private PSUBean creaPSU(HttpServletRequest request) {

    	PSUBean psu = new PSUBean();

    	creaProdotto(psu,request);
    	
    	psu.setPotenza(Integer.parseInt(request.getParameter("potenza")));
    	psu.setCertificazione(request.getParameter("certificazione"));
    	psu.setModulare(Modulare.valueOf(request.getParameter("modulare")));
    	psu.setFormato(Formato.valueOf(request.getParameter("formato")));
    	
        return psu;
    }
    
    private MoboBean creaMOBO(HttpServletRequest request) {

    	MoboBean mobo = new MoboBean();

    	creaProdotto(mobo,request);
    	
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
	    
        return mobo;
    }
    
    private MemoriaBean creaStorage(HttpServletRequest request) {

    	MemoriaBean mem = new MemoriaBean();

    	creaProdotto(mem,request);
    	
    	mem.setCapacita(request.getParameter("capacita"));
    	mem.setVelScrittura(Integer.parseInt(request.getParameter("scrittura")));
    	mem.setVelLettura(Integer.parseInt(request.getParameter("lettura")));
    	mem.setTipo(Tipo.valueOf(request.getParameter("tipo")));
    	mem.setTecnologia(Tecnologia.valueOf(request.getParameter("tecnologia")));
    	mem.setFormato(request.getParameter("formato"));
    	
        return mem;
    }
    
    private ChassisBean creaCase(HttpServletRequest request) {

    	ChassisBean c = new ChassisBean();

    	creaProdotto(c,request);
    	
    	c.setFormato(request.getParameter("formato"));
    	c.setColore(request.getParameter("colore"));
    	c.setMateriale(request.getParameter("materiale"));
    	
        return c;
    }
    
    private DissipatoreBean creaDissipatore(HttpServletRequest request) {

    	DissipatoreBean dissipatore = new DissipatoreBean();

    	creaProdotto(dissipatore,request);
    	
    	dissipatore.setTipo(model.DissipatoreBean.Tipo.valueOf(request.getParameter("tipo")));
    	dissipatore.setSocketSupportati(request.getParameter("socket"));
    	dissipatore.setDimensioniVentola("dimensione");
    	dissipatore.setRpmMax(Integer.parseInt(request.getParameter("rpm")));
    	dissipatore.setRumore(Integer.parseInt(request.getParameter("rumore")));
    	dissipatore.setTdpSupportato(Integer.parseInt(request.getParameter("tdp")));
        return dissipatore;
    }
    
    private RAMBean creaRAM(HttpServletRequest request) {
    	
    	RAMBean ram = new RAMBean();
    	
    	creaProdotto(ram,request);
        
    	ram.setCapacita(request.getParameter("capacita"));
    	ram.setFrequenza(request.getParameter("frequenza"));
    	ram.setTipo(request.getParameter("tipo"));
        return ram;
    }
    
    private void creaProdotto(ProdottoBean p,HttpServletRequest request) {

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
    
    private List<ImmagineBean> salvaImmagini(HttpServletRequest request, int idProdotto) throws Exception {

        List<Part> immagini = new ArrayList<>();

        for (Part part : request.getParts()) {
            if (part.getSubmittedFileName() != null
                    && !part.getSubmittedFileName().isBlank()
                    && part.getSize() > 0) {
                immagini.add(part);
            }
        }

        if (immagini.size() > MAX_IMMAGINI) {
            throw new Exception("Puoi caricare massimo " + MAX_IMMAGINI + " immagini");
        }

        String uploadDir = getServletContext().getRealPath("/img/prodotti");
        File folder = new File(uploadDir);
        if (!folder.exists()) folder.mkdirs();

        List<ImmagineBean> listaImmagini = new ArrayList<>();

        for (Part part : immagini) {

            String nomeOriginale = Paths.get(part.getSubmittedFileName())
                                        .getFileName()
                                        .toString();
            String nomeFile = System.currentTimeMillis() + "_" + nomeOriginale;

            part.write(uploadDir + File.separator + nomeFile);

            ImmagineBean img = new ImmagineBean();
            img.setPath("img/prodotti/" + nomeFile);

            immaginiDAO.doSave(img, idProdotto);

            listaImmagini.add(img);
        }

        return listaImmagini;
    }
    
    	
    
}