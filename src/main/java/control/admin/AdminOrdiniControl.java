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
            settaPagineAndTotale(request);
            settaOrdiniPrep(request);
            settaOrdiniSped(request);
            settaOrdiniCons(request);
;            
            request.getRequestDispatcher("/WEB-INF/views/admin/OrdiniView.jsp").forward(request, response);
            

        } catch (Exception e) {
        	

            System.out.println("ho fatto le varie cose");
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
    
    private void ordineByUtenti(HttpServletRequest request,String idUtenteStr)
            throws Exception, IOException, ServletException {

        int idUtente = Integer.parseInt(idUtenteStr);
        request.setAttribute("ordini", ordineDAO.doRetrieveAllByUser(idUtente));
        request.setAttribute("idUtente", idUtenteStr);
    }
    
    
    private void settaPagineAndTotale(HttpServletRequest request)
    {
		int numOrdini = 0;
		
		try {
			numOrdini = ordineDAO.doCount(null);
			int totalePagine = (int)Math.ceil((double)numOrdini / 10);
			
			request.setAttribute("numOrdini", numOrdini);
			request.setAttribute("totalePagine", totalePagine);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
        String idUtenteStr = request.getParameter("idUtente");
    	String p = request.getParameter("pagina");
        String stato = request.getParameter("stato");
    	int pagina = 1;


    	if (p != null && !p.isBlank()) {
    	    pagina = Integer.parseInt(p);
    	}
    	
        request.setAttribute("paginaCorrente", pagina);	
        
        
        if(idUtenteStr != null && !idUtenteStr.isBlank())
        {
        	ordineByUtenti(request,idUtenteStr);
        }
        else if (stato != null && !stato.isBlank())
        	{
        		request.setAttribute("ordini",ordineDAO.doRetrieveAll(stato,pagina));
        	} 
        	else
        		request.setAttribute("ordini",ordineDAO.doRetrieveAll(null,pagina));
    	
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}