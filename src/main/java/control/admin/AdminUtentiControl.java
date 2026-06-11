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

                case "modificaView":
                    //m(request, response);
                    return;
                case "modifica":
                	/*ordineByUtenti(request);
                	settato = 1;
                	*/
                	break;
                default:
                    throw new IOException("Azione non riconosciuta: " + action);
            }
        }
        
        settaPagineAndUtenti(request);
        contaAdmin(request);
        contaUser(request);
    	ottieniUtenti(request);
    	
		request.getRequestDispatcher("/WEB-INF/views/admin/UtentiView.jsp").forward(request, response);
	}
	
    private void ottieniUtenti(HttpServletRequest request)
    {
    	String p = request.getParameter("pagina");
    	int pagina = 1;
    	if (p != null && !p.isBlank()) {
    	    pagina = Integer.parseInt(p);
    	}
    	
    	try {
             request.setAttribute("paginaCorrente", pagina);	
			 request.setAttribute("utenti", utenteDAO.doRetrieveAll());
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
    
    private void settaPagineAndUtenti(HttpServletRequest request)
    {
		int numUtenti = 0;
		
		try {
			numUtenti = utenteDAO.doCountUtenti(null);
			int totalePagine = (int)Math.ceil((double)numUtenti / 10);
			
			request.setAttribute("numUtenti", numUtenti);
			request.setAttribute("totalePagine", totalePagine);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
