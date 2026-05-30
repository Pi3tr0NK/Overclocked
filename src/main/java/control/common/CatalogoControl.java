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
@WebServlet("/CatalogoControl")
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
	}

	private void loadProducList(HttpServletRequest request) {
		String cat = request.getParameter("categoria");
		String prezzo = request.getParameter("prezzo");
		String marca = request.getParameter("marca");
		if(cat.equals("PSU"))
		{
			int potenza = Integer.parseInt(request.getParameter("potenza"));
			String certificazione= request.getParameter("certificazione");
			String modulare= request.getParameter("modulare");
			try 
			{
				request.setAttribute("products", psuDAO.doRetrieveAll(cat, prezzo, marca, potenza, certificazione, modulare));
			} catch (SQLException e) {
				System.err.println("Error:" + e.getMessage());
			}
		}
		else if(cat.equals("DISSIPATORE"))
		{
			String tipo= request.getParameter("tipo");
			try 
			{
				request.setAttribute("products", dissipatoreDAO.doRetrieveAll(cat, prezzo, marca, tipo));
			} catch (SQLException e) {
				System.err.println("Error:" + e.getMessage());
			}
		}
		else if(cat.equals("CPU"))
		{
			String core = request.getParameter("core");
			String frequenza= request.getParameter("frequenza");
			try 
			{
				request.setAttribute("products", cpuDAO.doRetrieveAll(cat, prezzo, marca, core, frequenza));  
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
				request.setAttribute("products", ramDAO.doRetrieveAll(cat, prezzo, marca, capacita, frequenza, tipo));
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
				request.setAttribute("products", chassisDAO.doRetrieveAll(cat, prezzo, marca, formato, colore));
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
				request.setAttribute("products", gpuDAO.doRetrieveAll(cat, prezzo, marca, vram, pcie));
			} catch (SQLException e) {
				System.err.println("Error:" + e.getMessage());
			}
		}
		else if(cat.equals("MOBO"))
		{
			String formato = request.getParameter("formato");
			String nvme = request.getParameter("nvme"); //forse qua bisogna passare un booleano
			int slotram = Integer.parseInt(request.getParameter("slotram"));
			try 
			{
				request.setAttribute("products", moboDAO.doRetrieveAll(cat, prezzo, marca, formato, nvme, slotram));
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
				request.setAttribute("products", memoriaDAO.doRetrieveAll(cat, prezzo, marca, capacita, tipo, tecnologia));
			} catch (SQLException e) {
				System.err.println("Error:" + e.getMessage());
			}
		}
		else
		{
			try 
			{
				request.setAttribute("products", prodottoDAO.doRetrieveAll(prezzo, marca));
			} catch (SQLException e) {
				System.err.println("Error:" + e.getMessage());
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
