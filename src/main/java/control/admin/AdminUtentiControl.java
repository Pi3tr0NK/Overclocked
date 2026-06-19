package control.admin;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UtenteBean.Ruolo;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;


import dao.UtenteDAOImpl;



@WebServlet("/admin/utenti")
public class AdminUtentiControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    private DataSource ds;
    private UtenteDAOImpl utenteDAO;
    
	   @Override
	   public void init(ServletConfig config) throws ServletException {

		   super.init(config);
		        
		   ds = (DataSource) getServletContext().getAttribute("DataSource");
	
		   if(ds == null) {
			   throw new ServletException("DataSource non disponibile");
		   }
		   
		   utenteDAO = new UtenteDAOImpl(ds);
	   }


	 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
	
        String action = request.getParameter("action");
        
        if (action != null && !action.isBlank())
        {
                switch (action) {

                case "promuovi":
                    promuoviUtente(request);
                    break;

                default:
                    throw new IOException("Azione non riconosciuta: " + action);
            }
        }
        
        contaAdmin(request);
        contaUser(request);
    	ottieniUtenti(request);
    	
		request.getRequestDispatcher("/WEB-INF/views/admin/UtentiView.jsp").forward(request, response);
	}
	
    private void ottieniUtenti(HttpServletRequest request)
    {
    	String p = request.getParameter("pagina");
    	String ruolo = request.getParameter("filtroRuolo");

        int totalePagine = settaPagineAndUtenti(request,ruolo);
        
    	int pagina = 1;
    	
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

    	
    	try {
             request.setAttribute("paginaCorrente", pagina);	
			 request.setAttribute("utenti", utenteDAO.doRetrieveAll(ruolo,pagina));
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
		}
    }
    
    private void promuoviUtente(HttpServletRequest request)
    {
    	String idUtenteStr = request.getParameter("idUtente");
    	int idUtente = Integer.valueOf(idUtenteStr);
    	
    	try {
			 request.setAttribute("utenti", utenteDAO.setUtenteRuolo(idUtente, Ruolo.valueOf("ADMIN")));
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
		}
    }
    
    private int settaPagineAndUtenti(HttpServletRequest request,String ruolo)
    {
		int numUtenti = 0;
		
		try {
			numUtenti = utenteDAO.doCountFilteredUtenti(ruolo);
			int totalePagine = (int)Math.ceil((double)numUtenti / 10);
			
			request.setAttribute("numUtenti", numUtenti);
			request.setAttribute("totalePagine", totalePagine);
			return totalePagine;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
    }
    
    private void contaAdmin(HttpServletRequest request)
    {
		try {
			request.setAttribute("numAdmin", utenteDAO.doCountUtenti("ADMIN"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private void contaUser(HttpServletRequest request)
    {
		
		try {
			request.setAttribute("numUser", utenteDAO.doCountUtenti("USER"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
