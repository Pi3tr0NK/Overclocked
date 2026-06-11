package control.common;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import dao.CPUDAO;
import dao.CPUDAOImpl;
import dao.ChassisDAO;
import dao.ChassisDAOImpl;
import dao.DissipatoreDAO;
import dao.DissipatoreDAOImpl;
import dao.GPUDAO;
import dao.GPUDAOImpl;
import dao.MemoriaDAO;
import dao.MemoriaDAOImpl;
import dao.MoboDAO;
import dao.MoboDAOImpl;
import dao.PSUDAO;
import dao.PSUDAOImpl;
import dao.ProdottoDAO;
import dao.ProdottoDAOImpl;
import dao.RAMDAO;
import dao.RAMDAOImpl;

/**
 * Servlet implementation class CatalogoControl
 */
@WebServlet("/Catalogo")
public class CatalogoControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ProdottoDAO prodottoDAO;
	private PSUDAO psuDAO;
	private ChassisDAO chassisDAO;
	private CPUDAO cpuDAO;
	private DissipatoreDAO dissipatoreDAO;
	private GPUDAO gpuDAO;
	private MemoriaDAO memoriaDAO;
	private MoboDAO moboDAO;
	private RAMDAO ramDAO;
	
	public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }

        	prodottoDAO =new ProdottoDAOImpl(ds);
    		psuDAO =new PSUDAOImpl(ds);
    		chassisDAO =new ChassisDAOImpl(ds);
    		cpuDAO =new CPUDAOImpl(ds);
    		dissipatoreDAO =new DissipatoreDAOImpl(ds);
    		gpuDAO =new GPUDAOImpl(ds);
    		memoriaDAO =new MemoriaDAOImpl(ds);
    		moboDAO =new MoboDAOImpl(ds);
    		ramDAO =new RAMDAOImpl(ds);
    }
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		loadProducList(request);
		request.getRequestDispatcher("/WEB-INF/views/common/CatalogoView.jsp").forward(request, response);
	}
	
	
	private void loadProducList(HttpServletRequest request) {
		String cat = request.getParameter("categoria");
		cat = (cat == null || cat.trim().isEmpty()) ? null : cat;
		String prezzo= request.getParameter("prezzo"); //double
		String marca = request.getParameter("marca");
		String cerca = request.getParameter("cerca");

		
    	int pagina = 1;
    	String p = request.getParameter("pagina");

    	if (p != null && !p.isBlank()) {
    	    pagina = Integer.parseInt(p);
    	}
        
        
        
		cerca = (cerca == null || cerca.trim().isEmpty())
		        ? null
		        : "%" + cerca.trim() + "%";
		
		if(cat!=null)
		{
			if(cat.equals("PSU"))
			{
				String potenza = request.getParameter("potenza"); //integer
				String certificazione= request.getParameter("certificazione");
				String modulare= request.getParameter("modulare");
				try
				{
					int totalePagine = (int)Math.ceil((double)psuDAO.doCountFilteredProducts(cerca, cat, prezzo, marca, potenza, certificazione, modulare) / 10);
					request.setAttribute("totalePagine",totalePagine);
					request.setAttribute("products", psuDAO.doRetrieveAll(cerca, cat, prezzo, marca, potenza, certificazione, modulare, pagina));
				} catch (SQLException e) {
					System.err.println("Error:" + e.getMessage());
				}
			}
			else if(cat.equals("DISSIPATORE"))
			{
				String tipo= request.getParameter("tipo");
				try 
				{
					int totalePagine = (int)Math.ceil((double)dissipatoreDAO.doCountFilteredProducts(cerca, cat, prezzo, marca, tipo) / 10);
					request.setAttribute("totalePagine",totalePagine);
					request.setAttribute("products", dissipatoreDAO.doRetrieveAll(cerca, cat, prezzo, marca, tipo, pagina));
				} catch (SQLException e) {
					System.err.println("Error:" + e.getMessage());
				}
			}
			else if(cat.equals("CPU"))
			{
				String core = request.getParameter("core"); //integer
				String frequenza= request.getParameter("frequenza");
				try 
				{
					int totalePagine = (int)Math.ceil((double)cpuDAO.doCountFilteredProducts(cerca, cat, prezzo, marca, core, frequenza) / 10);
					request.setAttribute("totalePagine",totalePagine);
					request.setAttribute("products", cpuDAO.doRetrieveAll(cerca, cat, prezzo, marca, core, frequenza, pagina));  
				} catch (SQLException e) {
					System.err.println("Error:" + e.getMessage());
				}
			}
			else if(cat.equals("RAM"))
			{
				String capacita = request.getParameter("capacita");
				String frequenza= request.getParameter("frequenza");
				String tipo= request.getParameter("tipo");
				try 
				{
					int totalePagine = (int)Math.ceil((double)ramDAO.doCountFilteredProducts(cerca, cat, prezzo, marca, capacita, frequenza, tipo) / 10);
					request.setAttribute("totalePagine",totalePagine);
					request.setAttribute("products", ramDAO.doRetrieveAll(cerca, cat, prezzo, marca, capacita, frequenza, tipo, pagina));
				} catch (SQLException e) {
					System.err.println("Error:" + e.getMessage());
				}
			}
			else if(cat.equals("CASE"))
			{
				String formato = request.getParameter("formato");
				String colore = request.getParameter("colore");
				try 
				{
					int totalePagine = (int)Math.ceil((double)chassisDAO.doCountFilteredProducts(cerca, cat, prezzo, marca, formato, colore) / 10);
					request.setAttribute("totalePagine",totalePagine);
					request.setAttribute("products", chassisDAO.doRetrieveAll(cerca, cat, prezzo, marca, formato, colore, pagina));
				} catch (SQLException e) {
					System.err.println("Error:" + e.getMessage());
				}
			}
			else if(cat.equals("GPU"))
			{
				String vram = request.getParameter("vram");
				String pcie = request.getParameter("pcie");
				try 
				{
					int totalePagine = (int)Math.ceil((double)gpuDAO.doCountFilteredProducts(cerca, cat, prezzo, marca, vram, pcie) / 10);
					request.setAttribute("totalePagine",totalePagine);
					request.setAttribute("products", gpuDAO.doRetrieveAll(cerca, cat, prezzo, marca, vram, pcie, pagina));
				} catch (SQLException e) {
					System.err.println("Error:" + e.getMessage());
				}
			}
			else if(cat.equals("MOBO"))
			{
				String formato = request.getParameter("formato");
				String nvme = request.getParameter("nvme"); //forse qua bisogna passare un booleano
				String slotram = request.getParameter("slotram"); //integer
				try 
				{
					int totalePagine = (int)Math.ceil((double)moboDAO.doCountFilteredProducts(cerca, cat, prezzo, marca, formato, nvme, slotram) / 10);
					request.setAttribute("totalePagine",totalePagine);
					request.setAttribute("products", moboDAO.doRetrieveAll(cerca, cat, prezzo, marca, formato, nvme, slotram, pagina));
				} catch (SQLException e) {
					System.err.println("Error:" + e.getMessage());
				}
			}
			else if(cat.equals("STORAGE"))
			{
				String capacita = request.getParameter("capacita");
				String tipo = request.getParameter("tipo");
				String tecnologia = request.getParameter("tecnologia");
				try 
				{
					int totalePagine = (int)Math.ceil((double)memoriaDAO.doCountFilteredProducts(cerca, cat, prezzo, marca, capacita, tipo, tecnologia) / 10);
					request.setAttribute("totalePagine",totalePagine);
					request.setAttribute("products", memoriaDAO.doRetrieveAll(cerca, cat, prezzo, marca, capacita, tipo, tecnologia, pagina));
				} catch (SQLException e) {
					System.err.println("Error:" + e.getMessage());
				}
			}
		}
		else
		{
			try 
			{
				int totalePagine = (int)Math.ceil((double)prodottoDAO.doCountFilteredProducts(cerca, prezzo, marca, null, null) / 10);
				request.setAttribute("totalePagine",totalePagine);
				request.setAttribute("products", prodottoDAO.doRetrieveAll(cerca, prezzo, marca, null, null,pagina));
			} catch (SQLException e) {
				System.err.println("Error:" + e.getMessage());
			}
		}
		
        request.setAttribute("paginaCorrente", pagina);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
