package control.common;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarrelloBean;
import model.CarrelloItemBean;

import java.io.IOException;
import java.util.List;



/**
 * Servlet implementation class CarrelloControl
 */
@WebServlet("/Carrello")
public class CarrelloControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		 CarrelloBean cart = (CarrelloBean) request.getSession().getAttribute("cart");

		    if (cart == null) {
		        cart = new CarrelloBean();
		        request.getSession().setAttribute("cart", cart);
		    }

	        
	        totalCart(request, cart);
	        prodotti(request, cart);
	        numProdotti(request,cart);

	        request.getRequestDispatcher("/WEB-INF/views/common/CarrelloView.jsp").forward(request, response);
	}
	
	private void totalCart(HttpServletRequest request, CarrelloBean cart)
	{
		double totale=0;
		List<CarrelloItemBean> items= cart.getItems();
		
		for(CarrelloItemBean i : items)
		{
			totale+= (i.getProdotto().getPrezzo() - (i.getProdotto().getPrezzo()*i.getProdotto().getSconto()/100))*i.getQuantita();
		}
		
		request.setAttribute("totale", totale);
	}
	
	private void prodotti(HttpServletRequest request, CarrelloBean cart)
	{
		request.setAttribute("prodotti", cart.getItems());
	}
	
	private void numProdotti(HttpServletRequest request, CarrelloBean cart)
	{
		request.setAttribute("numProdotti", cart.getTotalQuantity());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
