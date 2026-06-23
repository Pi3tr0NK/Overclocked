package control.common;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    if ("suggest".equals(request.getParameter("action"))) {
	        gestisciSuggerimenti(request, response);
	        return;
	    }

	    String cat = request.getParameter("categoria");
	    cat = (cat == null || cat.trim().isEmpty()) ? null : cat;

	    String errore = validaFiltri(request, cat);
	    if (errore != null) {
	        request.setAttribute("errore", errore);
	        request.getRequestDispatcher("/WEB-INF/views/common/CatalogoView.jsp")
	               .forward(request, response);
	        return;
	    }

	    loadProducList(request);
	    request.getRequestDispatcher("/WEB-INF/views/common/CatalogoView.jsp")
	           .forward(request, response);
	}
	
	private void gestisciSuggerimenti(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    String query = request.getParameter("cerca");
	    if (query == null || query.trim().isEmpty()) {
	        response.setContentType("application/json;charset=UTF-8");
	        response.getWriter().write("[]");
	        return;
	    }

	    try {
	        List<String> suggerimenti = prodottoDAO.getSuggerimenti(query.trim(), 5);
	        response.setContentType("application/json;charset=UTF-8");
	        StringBuilder json = new StringBuilder("[");
	        for (int i = 0; i < suggerimenti.size(); i++) {
	            json.append("\"")
	                .append(suggerimenti.get(i).replace("\\", "\\\\").replace("\"", "\\\""))
	                .append("\"");
	            if (i < suggerimenti.size() - 1) json.append(",");
	        }
	        json.append("]");
	        response.getWriter().write(json.toString());
	    } catch (SQLException e) {
	        response.setContentType("application/json;charset=UTF-8");
	        response.getWriter().write("[]");
	    }
	}
	
	
	private void loadProducList(HttpServletRequest request) {
		String cat = request.getParameter("categoria");
		cat = (cat == null || cat.trim().isEmpty()) ? null : cat;
		
		String ordine = request.getParameter("ordinamento");		
		
		String prezzo= request.getParameter("prezzo"); //double
		String marca = request.getParameter("marca");
		String cerca = request.getParameter("cerca");

		
    	int pagina = 1;
    	String p = request.getParameter("pagina");

     	if (p != null && !p.isBlank()) {
    	    try {
    	        pagina = Integer.parseInt(p);
    	    } catch (NumberFormatException e) {
    	        pagina = 1;
    	    }
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
			        if (pagina < 1 || pagina > totalePagine) {
			            pagina = 1;
			        }
					request.setAttribute("products", psuDAO.doRetrieveAll(cerca, cat, prezzo, marca, potenza, certificazione, modulare, ordine, pagina));
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
			        if (pagina < 1 || pagina > totalePagine) {
			            pagina = 1;
			        }
					request.setAttribute("products", dissipatoreDAO.doRetrieveAll(cerca, cat, prezzo, marca, tipo, ordine, pagina));
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
			        if (pagina < 1 || pagina > totalePagine) {
			            pagina = 1;
			        }
					request.setAttribute("products", cpuDAO.doRetrieveAll(cerca, cat, prezzo, marca, core, frequenza, ordine, pagina));  
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
			        if (pagina < 1 || pagina > totalePagine) {
			            pagina = 1;
			        }
					request.setAttribute("products", ramDAO.doRetrieveAll(cerca, cat, prezzo, marca, capacita, frequenza, tipo, ordine, pagina));
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
			        if (pagina < 1 || pagina > totalePagine) {
			            pagina = 1;
			        }
					request.setAttribute("products", chassisDAO.doRetrieveAll(cerca, cat, prezzo, marca, formato, colore, ordine, pagina));
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
			        if (pagina < 1 || pagina > totalePagine) {
			            pagina = 1;
			        }
					request.setAttribute("products", gpuDAO.doRetrieveAll(cerca, cat, prezzo, marca, vram, pcie, ordine, pagina));
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
			        if (pagina < 1 || pagina > totalePagine) {
			            pagina = 1;
			        }
					request.setAttribute("products", moboDAO.doRetrieveAll(cerca, cat, prezzo, marca, formato, nvme, slotram,ordine,pagina));
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
			        if (pagina < 1 || pagina > totalePagine) {
			            pagina = 1;
			        }
					request.setAttribute("products", memoriaDAO.doRetrieveAll(cerca, cat, prezzo, marca, capacita, tipo, tecnologia,ordine,pagina));
				} catch (SQLException e) {
					System.err.println("Error:" + e.getMessage());
				}
			}
		}
		else
		{
			try 
			{
				int totalePagine = (int)Math.ceil((double)prodottoDAO.doCountFilteredProducts(cerca, prezzo, marca, null, "true") / 10);
				request.setAttribute("totalePagine",totalePagine);
		        if (pagina < 1 || pagina > totalePagine) {
		            pagina = 1;
		        }
				request.setAttribute("products", prodottoDAO.doRetrieveAll(cerca, prezzo, marca, null, "true" ,ordine,pagina));
			} catch (SQLException e) {
				System.err.println("Error:" + e.getMessage());
			}
		}
		
        request.setAttribute("paginaCorrente", pagina);
	}
	
	private String validaFiltri(HttpServletRequest request, String cat) {
	    Map<String, String> regole = new LinkedHashMap<>();
	    Map<String, String> messaggi = new LinkedHashMap<>();
	    Map<String, String> valori = new LinkedHashMap<>();

	    // Campi comuni a tutte le categorie
	    regole.put("prezzo",  "^\\d+(\\.\\d{1,2})?$");
	    regole.put("marca",   "^[A-Za-z0-9\\s'\\-]{1,50}$");
	    messaggi.put("prezzo", "Inserisci un prezzo valido (es. 199.99).");
	    messaggi.put("marca",  "Inserisci una marca valida.");
	    valori.put("prezzo",  request.getParameter("prezzo"));
	    valori.put("marca",   request.getParameter("marca"));

	    if (cat != null) {
	        switch (cat) {
	            case "CPU":
	                regole.put("core",      "^[1-9][0-9]{0,2}$");
	                regole.put("frequenza", "^(?i)\\d+(\\.\\d+)?\\s?(GHz|MHz|KHz|Hz)$");
	                messaggi.put("core",      "Inserisci un numero di core valido (es. 6).");
	                messaggi.put("frequenza", "Inserisci la frequenza con l'unità di misura (es. 3.6GHz).");
	                valori.put("core",      request.getParameter("core"));
	                valori.put("frequenza", request.getParameter("frequenza"));
	                break;

	            case "GPU":
	                regole.put("vram", "^(?i)\\d+\\s?(GB|MB|TB)$");
	                regole.put("pcie", "^[1-5](\\.\\d)?$");
	                messaggi.put("vram", "Inserisci la VRAM con l'unità di misura (es. 8GB).");
	                messaggi.put("pcie", "Inserisci una versione PCIe valida (es. 4.0).");
	                valori.put("vram", request.getParameter("vram"));
	                valori.put("pcie", request.getParameter("pcie"));
	                break;

	            case "RAM":
	                regole.put("capacita",  "^(?i)\\d+\\s?(GB|MB|TB)$");
	                regole.put("frequenza", "^(?i)\\d+(\\.\\d+)?\\s?(GHz|MHz|KHz|Hz)$");
	                regole.put("tipo",      "^DDR[1-5]$");
	                messaggi.put("capacita",  "Inserisci la capacità con l'unità di misura (es. 16GB).");
	                messaggi.put("frequenza", "Inserisci la frequenza con l'unità di misura (es. 3.6GHz).");
	                messaggi.put("tipo",      "Il tipo deve essere DDR seguito da un numero da 1 a 5, es. DDR4.");
	                valori.put("capacita",  request.getParameter("capacita"));
	                valori.put("frequenza", request.getParameter("frequenza"));
	                valori.put("tipo",      request.getParameter("tipo"));
	                break;

	            case "STORAGE":
	                regole.put("capacita", "^(?i)\\d+\\s?(GB|MB|TB)$");
	                messaggi.put("capacita", "Inserisci la capacità con l'unità di misura (es. 512GB).");
	                valori.put("capacita", request.getParameter("capacita"));
	                break;

	            case "MOBO":
	                regole.put("formato",  "^[A-Za-z0-9\\s'\\-]{1,30}$");
	                regole.put("slotram",  "^[1-9][0-9]?$");
	                messaggi.put("formato",  "Inserisci un formato valido (es. ATX).");
	                messaggi.put("slotram",  "Inserisci un numero di slot RAM valido.");
	                valori.put("formato",  request.getParameter("formato"));
	                valori.put("slotram",  request.getParameter("slotram"));
	                break;

	            case "CASE":
	                regole.put("formato", "^[A-Za-z0-9\\s'\\-]{1,30}$");
	                regole.put("colore",  "^[A-Za-z\\s'\\-]{1,30}$");
	                messaggi.put("formato", "Inserisci un formato valido (es. Mid Tower).");
	                messaggi.put("colore",  "Inserisci un colore valido (es. Nero).");
	                valori.put("formato", request.getParameter("formato"));
	                valori.put("colore",  request.getParameter("colore"));
	                break;

	            case "PSU":
	                regole.put("potenza",        "^\\d{1,5}$");
	                regole.put("certificazione", "^[A-Za-z0-9\\s+]{1,30}$");
	                messaggi.put("potenza",        "Inserisci una potenza valida in Watt (es. 650).");
	                messaggi.put("certificazione", "Inserisci una certificazione valida (es. 80+ Gold).");
	                valori.put("potenza",        request.getParameter("potenza"));
	                valori.put("certificazione", request.getParameter("certificazione"));
	                break;
	        }
	    }

	    for (Map.Entry<String, String> campo : valori.entrySet()) {
	        String chiave = campo.getKey();
	        String valore = campo.getValue();

	        if (valore == null || valore.trim().isEmpty()) continue;

	        if (!Pattern.matches(regole.get(chiave), valore.trim())) {
	            return messaggi.get(chiave);
	        }
	    }

	    return null;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
