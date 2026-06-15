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


import dao.OrdineDAOImpl;
import dao.ProdottoDAOImpl;
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
    	
    	int pagina = 1;

    	String p = request.getParameter("pagina");

    	if (p != null && !p.isBlank()) {
    	    pagina = Integer.parseInt(p);
    	}
    	
        contaOrdini(request);
        contaUtenti(request);
        contaProdottiEsauriti(request);
        contaPagine(request,categoria, attivoStr);
        ottieniProdotti(request,categoria,attivoStr,pagina);
        
        
        request.getRequestDispatcher("/WEB-INF/views/admin/DashboardView.jsp").forward(request, response);
        
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
			 request.setAttribute("numUtenti", utenteDAO.doCountUtenti(null));
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
    
    private void ottieniProdotti(HttpServletRequest request,String categoria, String stato, int pagina)
    {

    	
    	try {
    		 request.setAttribute("paginaCorrente", pagina);
			 request.setAttribute("prodotto", productDAO.doRetrieveAll(null,null,null, categoria, stato, pagina));
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
		}
    }
    
    private void contaPagine(HttpServletRequest request,String categoria, String stato)
    {
    	try {
			 int numFiltrato =  productDAO.doCountFilteredProducts(null,null,null, categoria, stato);
			 int totalePagine = (int)Math.ceil((double)numFiltrato / 10);

			 request.setAttribute("numProdotti", numFiltrato);
			 request.setAttribute("totalePagine", totalePagine);
			 
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
		}
    }
    
    
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
    
    

    
 
    
    
}

