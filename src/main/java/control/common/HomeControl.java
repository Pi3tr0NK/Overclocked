package control.common;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ProdottoDAO;
import dao.ProdottoDAOImpl;

@WebServlet("/home")
public class HomeControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProdottoDAO productDao;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }

        productDao = new ProdottoDAOImpl(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    		
    		countByCategory(request);
    		novita(request);
    		bestseller(request);
        
        request.getRequestDispatcher("/WEB-INF/views/common/HomeView.jsp").forward(request, response);
    }
    
    private void countByCategory(HttpServletRequest request)
    {
    		try {
    			 request.setAttribute("countByCategory", productDao.doCountProductsByCategory());
    		} catch(SQLException e){
    			System.err.println("Error:" + e.getMessage());
    		}
    }
    
    private void novita(HttpServletRequest request)
    {
    		try {
			 request.setAttribute("novita", productDao.doRetrieveNovita(10));
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
		}
    }
    
    private void bestseller(HttpServletRequest request)
    {
    		try {
			 request.setAttribute("bestSeller", productDao.doRetrieveBestseller(10));
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
		}
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
    
    
    
    
    
    
    
    
    
    
    
    