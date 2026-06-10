package control.admin;

import dao.OrdineDAOImpl;
import model.OrdineBean;
import model.OrdineBean.Stato;

import javax.sql.DataSource;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/ordini")
public class AdminOrdiniControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrdineDAOImpl ordineDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }

        ordineDAO = new OrdineDAOImpl(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String stato = request.getParameter("stato");
            String action = request.getParameter("action");
               
            if (action != null && !action.isBlank())
            {
	                switch (action) {
	
	                case "cambiaStato":
	                    cambiaStato(request);
	                    break;
	
	                case "dettaglio":
	                    dettaglio(request, response);
	                    return;
	
	                default:
	                    throw new Exception("Azione non riconosciuta: " + action);
	            }
            }
            
            if (stato != null && !stato.isBlank()) {
            	request.setAttribute("ordini",ordineDAO.doRetrieveAll(stato));
            } else {
            	request.setAttribute("ordini",ordineDAO.doRetrieveAll(null));
            }
            
            // contatori per le stat card
            request.setAttribute("numOrdiniTotali",      ordineDAO.doCount(null));
            request.setAttribute("numOrdiniInAttesa",    ordineDAO.doCount("IN_PREPARAZIONE"));
            request.setAttribute("numOrdiniSpediti",     ordineDAO.doCount("SPEDITO"));
            request.setAttribute("numOrdiniConsegnati",  ordineDAO.doCount("CONSEGNATO"));
            
            request.getRequestDispatcher("/WEB-INF/views/admin/OrdiniView.jsp").forward(request, response);
            

        } catch (Exception e) {
        	

            System.out.println("ho fatto le varie cose");
            e.printStackTrace();
            request.setAttribute("errore", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/OrdiniView.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
    

    // ── Azioni ───────────────────────────────────────────────────────────

    private void cambiaStato(HttpServletRequest request) throws Exception {

        String idOrdineStr  = request.getParameter("idOrdine");
        String nuovoStato   = request.getParameter("nuovoStato");

        if (idOrdineStr == null || idOrdineStr.isBlank()) {
            throw new Exception("ID ordine mancante.");
        }
        if (nuovoStato == null || nuovoStato.isBlank()) {
            throw new Exception("Nuovo stato mancante.");
        }

        int idOrdine = Integer.parseInt(idOrdineStr);

        OrdineBean ordine = ordineDAO.doRetrieveByKey(idOrdine);
        if (ordine == null) {
            throw new Exception("Ordine non trovato: " + idOrdine);
        }


        ordineDAO.setOrdineStatus(idOrdine, Stato.valueOf(nuovoStato));
    }

    private void dettaglio(HttpServletRequest request, HttpServletResponse response)
            throws Exception, IOException, ServletException {

        String idOrdineStr = request.getParameter("idOrdine");

        if (idOrdineStr == null || idOrdineStr.isBlank()) {
            throw new Exception("ID ordine mancante.");
        }

        int idOrdine = Integer.parseInt(idOrdineStr);

        OrdineBean ordine = ordineDAO.doRetrieveByKey(idOrdine);
        if (ordine == null) {
            throw new Exception("Ordine non trovato: " + idOrdine);
        }

        request.setAttribute("ordine", ordine);
        request.getRequestDispatcher("/WEB-INF/views/admin/AdminDettaglioOrdineView.jsp")
               .forward(request, response);
    }
    
}