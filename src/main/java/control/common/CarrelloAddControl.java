package control.common;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarrelloBean;
import model.CarrelloItemBean;
import model.ProdottoBean;
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
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		int idProdotto;
		int quantita;
		try {
			idProdotto = Integer.parseInt(request.getParameter("aggiungi"));
			quantita   = Integer.parseInt(request.getParameter("quantita"));
		} catch (NumberFormatException e) {
			response.getWriter().write("{\"success\":false,\"error\":\"Parametri non validi\"}");
			return;
		}

		ProdottoBean prodotto;
		try {
			prodotto = productDao.doRetrieveByKey(idProdotto);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		if (prodotto == null) {
			response.getWriter().write("{\"success\":false,\"error\":\"Prodotto non trovato\"}");
			return;
		}

		CarrelloBean cart = (CarrelloBean) request.getSession().getAttribute("cart");
		if (cart == null) {
			cart = new CarrelloBean();
			request.getSession().setAttribute("cart", cart);
		}

		int stock = prodotto.getStock();
		CarrelloItemBean itemEsistente = cart.findProduct(idProdotto);
		int quantitaGiaPresente = (itemEsistente != null) ? itemEsistente.getQuantita() : 0;

		if (quantita < 1 || quantita + quantitaGiaPresente > stock) {
			int disponibile = Math.max(stock - quantitaGiaPresente, 0);
			response.getWriter().write(String.format(
				"{\"success\":false,\"error\":\"Quantita non valida\",\"stock\":%d,\"disponibile\":%d}",
				stock, disponibile));
			return;
		}

		cart.addProduct(prodotto, quantita);

		response.getWriter().write(String.format(
			"{\"success\":true, \"id\":%d, \"numProdotti\":%d}", idProdotto, cart.getTotalQuantity()));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}