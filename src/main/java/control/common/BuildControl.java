package control.common;

import java.io.IOException;
import java.util.Collection;

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
@WebServlet("/PcBuilder")
public class BuildControl extends HttpServlet {
	
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
  
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        String action = request.getParameter("action");
 
        if (action == null || action.isEmpty()) {
        		request.getRequestDispatcher("/WEB-INF/views/common/BuildView.jsp").forward(request, response);
            return;
        }
 
        // Richieste AJAX: rispondono sempre in JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
 
        switch (action) {
 
            // ── Carica tutte le CPU disponibili ──────────────
            case "getCpus":
                handleGetCpus(request, response);
                break;
 
            // ── Schede madri compatibili con la CPU scelta ───
            case "getMobos":
                handleGetMobos(request, response);
                break;
 
            // ── RAM compatibili con mobo selezionata ─────────
            case "getRams":
                handleGetRams(request, response);
                break;
 
            // ── GPU disponibili ───────────────────────────────
            case "getGpus":
                handleGetGpus(request, response);
                break;
 
            // ── Storage disponibile ───────────────────────────
            case "getStorages":
                handleGetStorages(request, response);
                break;
 
            // ── PSU ───────────────────────────────────────────
            case "getPsus":
                handleGetPsus(request, response);
                break;
 
            // ── Case / Chassis compatibili con il formato mobo
            case "getCases":
                handleGetCases(request, response);
                break;
 
            // ── Dissipatori compatibili con socket CPU ────────
            case "getDissipatori":
                handleGetDissipatori(request, response);
                break;
 
            default:
            	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeJson(response, "{\"error\":\"Azione non riconosciuta\"}");
        }
    }
 
    // ─────────────────────────────────────────────
    //  Handlers AJAX
    // ─────────────────────────────────────────────
 
    private void handleGetCpus(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            Collection<CPUBean> cpus = cpuDAO.doRetrieveAll(null, null, null, null, null, null);
            writeJson(resp, new Gson().toJson(cpus));
        } catch (Exception e) {
            writeError(resp, e);
        }
    }
 
    private void handleGetMobos(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String cpuIdStr = req.getParameter("cpuId");
        if (cpuIdStr == null) {
            writeJson(resp, "{\"error\":\"cpuId mancante\"}");
            return;
        }
        try {
            int cpuId = Integer.parseInt(cpuIdStr);
            
            Collection<MoboBean> mobos = moboDAO.moboCompatibili(cpuId);
            writeJson(resp, new Gson().toJson(mobos));
        } catch (Exception e) {
            writeError(resp, e);
        }
    }
 
    private void handleGetRams(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String moboIdStr = req.getParameter("moboId");
        if (moboIdStr == null) {
            writeJson(resp, "{\"error\":\"moboId mancante\"}");
            return;
        }
        try {
            int moboId = Integer.parseInt(moboIdStr);
            
            Collection<RAMBean> rams = ramDAO.ramCompatibili(moboId);
            writeJson(resp, new Gson().toJson(rams));
        } catch (Exception e) {
            writeError(resp, e);
        }
    }
 
    private void handleGetGpus(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
        		Collection<GPUBean> gpus = gpuDAO.doRetrieveAll(null, null, null, null, null, null);
            writeJson(resp, new Gson().toJson(gpus));
        } catch (Exception e) {
            writeError(resp, e);
        }
    }
 
    private void handleGetStorages(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    		String moboIdStr = req.getParameter("moboId");
        if (moboIdStr == null) {
            writeJson(resp, "{\"error\":\"moboId mancante\"}");
            return;
        }
        try {
        		int moboId = Integer.parseInt(moboIdStr);
        	
        		Collection<MemoriaBean> storages = memoriaDAO.memoriaCompatibili(moboId);
            writeJson(resp, new Gson().toJson(storages));
        } catch (Exception e) {
            writeError(resp, e);
        }
    }
 
    private void handleGetPsus(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    		String cpuIdStr = req.getParameter("cpuId");
        if (cpuIdStr == null) {
            writeJson(resp, "{\"error\":\"cpuId mancante\"}");
            return;
        }
        String gpuIdStr = req.getParameter("gpuId");
        if (cpuIdStr == null) {
            writeJson(resp, "{\"error\":\"gpuId mancante\"}");
            return;
        }
        try {
            int cpuId = Integer.parseInt(cpuIdStr);
            int gpuId = Integer.parseInt(gpuIdStr);
            Collection<PSUBean> psus = psuDAO.psuCompatibili(cpuId, gpuId);
            writeJson(resp, new Gson().toJson(psus));
        } catch (Exception e) {
            writeError(resp, e);
        }
    }
 
    private void handleGetCases(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
	    		String moboIdStr = req.getParameter("moboId");
	        if (moboIdStr == null) {
	            writeJson(resp, "{\"error\":\"moboId mancante\"}");
	            return;
	        }
	        try {
        			int moboId = Integer.parseInt(moboIdStr);
                Collection<ChassisBean> cases = chassisDAO.chassisCompatibili(moboId);
                writeJson(resp, new Gson().toJson(cases));
            }
            catch (Exception e) 
	        {
            		writeError(resp, e);
            }
    }
 
    private void handleGetDissipatori(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    		String cpuIdStr = req.getParameter("cpuId");
        if (cpuIdStr == null) {
            writeJson(resp, "{\"error\":\"cpuId mancante\"}");
            return;
        }
        try {
            int cpuId = Integer.parseInt(cpuIdStr);
            Collection<DissipatoreBean> dissipatori = dissipatoreDAO.dissipatoriCompatibili(cpuId);
            writeJson(resp, new Gson().toJson(dissipatori));
        } catch (Exception e) {
            writeError(resp, e);
        }
    }
 
    // ─────────────────────────────────────────────
    //  Utility
    // ─────────────────────────────────────────────
 
    private void writeJson(HttpServletResponse resp, String json) throws IOException {
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
 
    private void writeError(HttpServletResponse resp, Exception e) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeJson(resp, "{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
    }
}
