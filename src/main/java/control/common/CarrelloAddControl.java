package control.common;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarrelloBean;

import java.io.IOException;

import javax.sql.DataSource;

import dao.ProdottoDAOImpl;

/**
 * Servlet implementation class CarrelloAdd
 */
@WebServlet("/carrello/add")
public class CarrelloAddControl extends HttpServlet {
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 	int idProdotto = Integer.parseInt(request.getParameter("aggiungi"));
		    int quantita = Integer.parseInt(request.getParameter("quantita"));
		    
		    CarrelloBean cart = (CarrelloBean) request.getSession().getAttribute("cart");
		    
		    if (cart == null) {
		        cart = new CarrelloBean();
		        request.getSession().setAttribute("cart", cart);
		    }

		    try {
		        cart.addProduct(productDao.doRetrieveByKey(idProdotto),quantita);
		    } catch (Exception e) {
		        throw new ServletException(e);
		    }

		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");

		    response.getWriter().write(String.format("{\"success\":true, \"id\":%d, \"numProdotti\":%d}",idProdotto, cart.getTotalQuantity()));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
