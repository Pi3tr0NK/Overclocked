package control.common;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import dao.ProdottoDAOImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ProdottoBean;

@WebServlet("/prodotto")
public class ProdottoControl extends HttpServlet {

	
	
    private static final long serialVersionUID = 1L;
    


    private ProdottoDAOImpl productDao;
    
	public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }
        
        productDao = new ProdottoDAOImpl(ds);
    }
	
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
    
        try {
            int id =Integer.parseInt(request.getParameter("id"));
            

            ProdottoBean prodotto = productDao.doRetrieveByKey(id);
            
            if(prodotto == null) {
                response.sendRedirect(request.getContextPath()+ "/home");
                return;
            }
            
            correlati(request,prodotto);
            request.setAttribute("prodotto",prodotto);
            RequestDispatcher dispatcher =request.getRequestDispatcher("/WEB-INF/views/common/ProdottoView.jsp");
            dispatcher.forward(request,response);
            	
        }
        catch(Exception e) {

            throw new ServletException(e);
        }
    }
    
    private void correlati(HttpServletRequest request, ProdottoBean prodotto)
    {
		try {
			request.setAttribute("correlati", productDao.doRetrieveCorrelati(5,prodotto.getIdProdotto(),prodotto.getCategoria(), prodotto.getPrezzo()));
		} catch(SQLException e){
			System.err.println("Error:" + e.getMessage());
	}
    }
    
}