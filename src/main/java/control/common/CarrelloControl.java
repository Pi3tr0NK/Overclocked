package control.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarrelloBean;
import model.CarrelloItemBean;

import java.io.IOException;



/**
 * Servlet implementation class CarrelloControl
 */
@WebServlet("/Carrello")
public class CarrelloControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CarrelloBean cart = (CarrelloBean) request.getSession().getAttribute("cart");
        if (cart == null) {
            cart = new CarrelloBean();
            request.getSession().setAttribute("cart", cart);
        }

        String action= request.getParameter("action");
        String idProdottoStr = request.getParameter("idProdotto");
        
        if(action != null)
        {
	        	if(action.equals("svuota"))
	    			svuotaCarrello(cart, request, response);
        }
        
        if (action != null && idProdottoStr != null) {
            int idProdotto = Integer.parseInt(idProdottoStr);
            CarrelloItemBean item = cart.findProduct(idProdotto);

            if (item != null) {
                switch (action) {
                    case "incrementa": item.aumentaQuantita(1);       
                    		  break;
                    		  
                    case "decrementa": item.diminuisciQuantita(1);
                    	      break;
                    	      
                    case "rimuovi": cart.removeProduct(idProdotto); 
                          break;
                }
            }

            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                CarrelloItemBean updated = cart.findProduct(idProdotto);
                int nuovaQuantita = (updated != null) ? updated.getQuantita() : 0;
                double totale     = calcolaTotale(cart);

                String json = String.format(java.util.Locale.US,
                    "{\"idProdotto\":%d,\"quantita\":%d,\"totale\":%.2f,\"numProdotti\":%d}",
                    idProdotto, nuovaQuantita, totale, cart.getTotalQuantity()
                );

                response.setContentType("application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
                response.getWriter().flush();
                return;
            }
        }

        totalCart(request, cart);
        prodotti(request, cart);
        numProdotti(request, cart);
        request.getRequestDispatcher("/WEB-INF/views/common/CarrelloView.jsp")
               .forward(request, response);
    }

    private double calcolaTotale(CarrelloBean cart) {
        double totale = 0;
        for (CarrelloItemBean i : cart.getItems())
            totale += (i.getProdotto().getPrezzo()
                      - (i.getProdotto().getPrezzo() * i.getProdotto().getSconto() / 100.0))
                      * i.getQuantita();
        return totale;
    }

    private void totalCart(HttpServletRequest request, CarrelloBean cart) {
        request.setAttribute("totale", calcolaTotale(cart));
    }

    private void prodotti(HttpServletRequest request, CarrelloBean cart) {
        request.setAttribute("prodotti", cart.getItems());
    }

    private void numProdotti(HttpServletRequest request, CarrelloBean cart) {
        request.setAttribute("numProdotti", cart.getTotalQuantity());
    }
    
    private void svuotaCarrello(CarrelloBean cart, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        cart.getItems().clear();
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/Carrello");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}