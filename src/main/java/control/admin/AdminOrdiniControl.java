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
import java.sql.SQLException;

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
            

            
        	getOrdiniAndPagina(request);
            settaOrdiniPrep(request);
            settaOrdiniSped(request);
            settaOrdiniCons(request);
;            
            request.getRequestDispatcher("/WEB-INF/views/admin/OrdiniView.jsp").forward(request, response);
            

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errore", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/OrdiniView.jsp").forward(request, response);
        }
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
    
    private int settaPagineAndTotale(HttpServletRequest request, String cercaNome, String cercaCognome, String cercaEmail, String stato, String dataStart, String dataEnd)
    {
		int numOrdini = 0;
		
		try {
			numOrdini = ordineDAO.doCountFilteredProducts(cercaNome,cercaCognome,cercaEmail,stato, dataStart, dataEnd);
			int totalePagine = (int)Math.ceil((double)numOrdini / 10);
			
			request.setAttribute("numOrdini", numOrdini);
			request.setAttribute("totalePagine", totalePagine);
			return totalePagine;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
    }

    private void settaOrdiniPrep(HttpServletRequest request)
    {
		
		try {
			request.setAttribute("numOrdiniInAttesa",    ordineDAO.doCount("IN_PREPARAZIONE"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private void settaOrdiniSped(HttpServletRequest request)
    {
		try {
			request.setAttribute("numOrdiniSpediti",     ordineDAO.doCount("SPEDITO"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private void settaOrdiniCons(HttpServletRequest request)
    {
		try {
			request.setAttribute("numOrdiniConsegnati",  ordineDAO.doCount("CONSEGNATO"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    
    private void getOrdiniAndPagina(HttpServletRequest request) throws IOException, ServletException, Exception
    {
    	String p = request.getParameter("pagina");
        String stato = request.getParameter("stato");
        String cercaNome = request.getParameter("cercaNome");
        String cercaCognome = request.getParameter("cercaCognome");
        String cercaEmail = request.getParameter("cercaEmail");
        
        String dataStart = request.getParameter("dataInizio");
        String dataEnd = request.getParameter("dataFine");
       
        
    	int pagina = 1;


        int totalePagine = settaPagineAndTotale(request,cercaNome,cercaCognome,cercaEmail,stato,dataStart,dataEnd);
        
        
     	if (p != null && !p.isBlank()) {
    	    try {
    	        pagina = Integer.parseInt(p);
    	    } catch (NumberFormatException e) {
    	        pagina = 1;
    	    }
    	}
    	
        if (pagina < 1 || pagina > totalePagine) {
            pagina = 1;
        }
    	
        request.setAttribute("paginaCorrente", pagina);	
        
        request.setAttribute("ordini",ordineDAO.doRetrieveAll(cercaNome,cercaCognome,cercaEmail,stato, dataStart, dataEnd, pagina));
        
    	
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}