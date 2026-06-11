package control.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

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
import dao.RAMDAO;
import dao.RAMDAOImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CPUBean;
import model.ChassisBean;
import model.DissipatoreBean;
import model.GPUBean;
import model.MemoriaBean;
import model.MoboBean;
import model.PSUBean;
import model.RAMBean;

/**
 * Servlet implementation class BuildControl
 */
@WebServlet("/pcBuilder")
public class BuildControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
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

    		psuDAO =new PSUDAOImpl(ds);
    		chassisDAO =new ChassisDAOImpl(ds);
    		cpuDAO =new CPUDAOImpl(ds);
    		dissipatoreDAO =new DissipatoreDAOImpl(ds);
    		gpuDAO =new GPUDAOImpl(ds);
    		memoriaDAO =new MemoriaDAOImpl(ds);
    		moboDAO =new MoboDAOImpl(ds);
    		ramDAO =new RAMDAOImpl(ds);
    }
  
	
	 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        String action = request.getParameter("action");

	        // Nessuna action → mostra la pagina JSP
	        if (action == null || action.isEmpty()) {
	            request.getRequestDispatcher("/WEB-INF/views/common/BuildView.jsp")
	                   .forward(request, response);
	            return;
	        }

	        // Tutte le richieste AJAX rispondono in JSON
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out = response.getWriter();

	        JSONObject json = new JSONObject();

	        try {
	            switch (action) {

	                case "getCpus": {
	                    Collection<CPUBean> cpus = cpuDAO.doRetrieveAll(null, null, null, null, null, null, 0);
	                    json.put("functionName", "aggiornaCpus");
	                    json.put("result", new JSONArray(cpus));
	                    break;
	                }

	                case "getMobos": {
	                    String cpuIdStr = request.getParameter("cpuId");
	                    if (cpuIdStr == null || cpuIdStr.isEmpty()) {
	                        json.put("functionName", "errore");
	                        json.put("result", "cpuId mancante");
	                        break;
	                    }
	                    int cpuId = Integer.parseInt(cpuIdStr);
	                    Collection<MoboBean> mobos = moboDAO.moboCompatibili(cpuId);
	                    json.put("functionName", "aggiornaMobos");
	                    json.put("result", new JSONArray(mobos));
	                    break;
	                }

	                case "getRams": {
	                    String moboIdStr = request.getParameter("moboId");
	                    if (moboIdStr == null || moboIdStr.isEmpty()) {
	                        json.put("functionName", "errore");
	                        json.put("result", "moboId mancante");
	                        break;
	                    }
	                    int moboId = Integer.parseInt(moboIdStr);
	                    Collection<RAMBean> rams = ramDAO.ramCompatibili(moboId);
	                    json.put("functionName", "aggiornaRams");
	                    json.put("result", new JSONArray(rams));
	                    break;
	                }

	                case "getGpus": {
	                    Collection<GPUBean> gpus = gpuDAO.doRetrieveAll(null, null, null, null, null, null, 0);
	                    json.put("functionName", "aggiornaGpus");
	                    json.put("result", new JSONArray(gpus));
	                    break;
	                }

	                case "getStorages": {
	                    String moboIdStr = request.getParameter("moboId");
	                    if (moboIdStr == null || moboIdStr.isEmpty()) {
	                        json.put("functionName", "errore");
	                        json.put("result", "moboId mancante");
	                        break;
	                    }
	                    int moboId = Integer.parseInt(moboIdStr);
	                    Collection<MemoriaBean> storages = memoriaDAO.memoriaCompatibili(moboId);
	                    json.put("functionName", "aggiornaStorages");
	                    json.put("result", new JSONArray(storages));
	                    break;
	                }

	                case "getPsus": {
	                    String cpuIdStr = request.getParameter("cpuId");
	                    String gpuIdStr = request.getParameter("gpuId");
	                    if (cpuIdStr == null || cpuIdStr.isEmpty()) {
	                        json.put("functionName", "errore");
	                        json.put("result", "cpuId mancante");
	                        break;
	                    }
	                    if (gpuIdStr == null || gpuIdStr.isEmpty()) {
	                        json.put("functionName", "errore");
	                        json.put("result", "gpuId mancante");
	                        break;
	                    }
	                    int cpuId = Integer.parseInt(cpuIdStr);
	                    int gpuId = Integer.parseInt(gpuIdStr);
	                    Collection<PSUBean> psus = psuDAO.psuCompatibili(cpuId, gpuId);
	                    json.put("functionName", "aggiornaPsus");
	                    json.put("result", new JSONArray(psus));
	                    break;
	                }

	                case "getCases": {
	                    String moboIdStr = request.getParameter("moboId");
	                    if (moboIdStr == null || moboIdStr.isEmpty()) {
	                        json.put("functionName", "errore");
	                        json.put("result", "moboId mancante");
	                        break;
	                    }
	                    int moboId = Integer.parseInt(moboIdStr);
	                    Collection<ChassisBean> cases = chassisDAO.chassisCompatibili(moboId);
	                    json.put("functionName", "aggiornaCases");
	                    json.put("result", new JSONArray(cases));
	                    break;
	                }

	                case "getDissipatori": {
	                    String cpuIdStr = request.getParameter("cpuId");
	                    if (cpuIdStr == null || cpuIdStr.isEmpty()) {
	                        json.put("functionName", "errore");
	                        json.put("result", "cpuId mancante");
	                        break;
	                    }
	                    int cpuId = Integer.parseInt(cpuIdStr);
	                    Collection<DissipatoreBean> dissipatori = dissipatoreDAO.dissipatoriCompatibili(cpuId);
	                    json.put("functionName", "aggiornaDissipatori");
	                    json.put("result", new JSONArray(dissipatori));
	                    break;
	                }

	                default: {
	                    json.put("functionName", "errore");
	                    json.put("result", "Azione non riconosciuta");
	                    break;
	                }
	            }

	        } catch (NumberFormatException e) {
	            json.put("functionName", "errore");
	            json.put("result", "ID non valido: " + e.getMessage());
	        } catch (Exception e) {
	            json.put("functionName", "errore");
	            json.put("result", "Errore interno: " + e.getMessage());
	        }

	        out.print(json.toString());
	        out.flush();
	    }

	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        processRequest(request, response);
	    }

	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        processRequest(request, response);
	    }
}

