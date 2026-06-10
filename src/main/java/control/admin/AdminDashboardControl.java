package control.admin;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import dao.OrdineDAOImpl;
import dao.PSUDAOImpl;
import dao.ProdottoDAOImpl;
import dao.RAMDAOImpl;
import dao.UtenteDAOImpl;


@WebServlet("/admin/dashboard")
public class AdminDashboardControl extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private ProdottoDAOImpl productDAO;
	private OrdineDAOImpl ordineDAO;
	private UtenteDAOImpl utenteDAO;
	private DataSource ds;
	
    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        
        ds = (DataSource) getServletContext().getAttribute("DataSource");

        if(ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        
        productDAO = new ProdottoDAOImpl(ds);
        ordineDAO = new OrdineDAOImpl(ds);
        utenteDAO = new UtenteDAOImpl(ds);
    }
    
    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        
    	String categoria = request.getParameter("categoria");
    	String attivoStr = request.getParameter("attivo");
    	
    	
        contaProdotti(request);
        contaOrdini(request);
        contaUtenti(request);
        contaProdottiEsauriti(request);
        ottieniProdotti(request,categoria,attivoStr);
        
        
        request.getRequestDispatcher("/WEB-INF/views/admin/DashboardView.jsp").forward(request, response);
        
    }
    
    private void contaProdotti(HttpServletRequest request)
    {
    	try {
			 request.setAttribute("numProdotti", productDAO.doCountProducts());
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
		}
    }
    
    private void contaOrdini(HttpServletRequest request)
    {
    	try {
			 request.setAttribute("numOrdini", ordineDAO.doCountByLastMonth());
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
		}
    }
    
    private void contaUtenti(HttpServletRequest request)
    {
    	try {
			 request.setAttribute("numUtenti", utenteDAO.doCountUtenti());
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
		}
    }
    
    private void contaProdottiEsauriti(HttpServletRequest request)
    {
    	try {
			 request.setAttribute("numProdottiEsauriti", productDAO.doCountExpiredProducts());
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
		}
    }
    
    private void ottieniProdotti(HttpServletRequest request,String categoria, String stato)
    {
    	try {
			 request.setAttribute("prodotto", productDAO.doRetrieveAll(null,null,null, categoria, stato));
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
		}
    }
    
    
}

