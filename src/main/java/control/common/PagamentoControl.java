package control.common;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import dao.OrdineDAOImpl;
import dao.IndirizzoDAOImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarrelloBean;
import model.CarrelloItemBean;
import model.ProdottoBean;
import model.UtenteBean;
import model.IndirizzoBean;

@WebServlet("/common/pagamento")
public class PagamentoControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private DataSource ds;
    private OrdineDAOImpl ordineDAO;
    private IndirizzoDAOImpl indirizzoDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) throw new ServletException("DataSource non disponibile");

        ordineDAO = new OrdineDAOImpl(ds);
        indirizzoDAO = new IndirizzoDAOImpl(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UtenteBean utente = (UtenteBean) request.getSession().getAttribute("utente");

        if ("ok".equals(request.getParameter("conferma"))) {
            request.setAttribute("utente", utente);
            request.getRequestDispatcher("/WEB-INF/views/common/PagamentoView.jsp")
                   .forward(request, response);
            return;
        }

        CarrelloBean cart = (CarrelloBean) request.getSession().getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Carrello");
            return;
        }

        request.setAttribute("totale", calcolaTotale(cart));
        request.setAttribute("utente", utente);
        request.setAttribute("prodotti", cart.getItems());

        request.getRequestDispatcher("/WEB-INF/views/common/PagamentoView.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UtenteBean utente = (UtenteBean) request.getSession().getAttribute("utente");
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/indexlogin");
            return;
        }

        CarrelloBean cart = (CarrelloBean) request.getSession().getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Carrello");
            return;
        }

        for (CarrelloItemBean item : cart.getItems()) {
            ProdottoBean p = item.getProdotto();
            if (p.getStock() < item.getQuantita()) {
                request.setAttribute("errore",
                        "Stock insufficiente per: " + p.getNome() +
                        " (disponibili: " + p.getStock() + ")");

                request.setAttribute("totale", calcolaTotale(cart));
                request.setAttribute("utente", utente);
                request.setAttribute("prodotti", cart.getItems());

                request.getRequestDispatcher("/WEB-INF/views/common/PagamentoView.jsp")
                       .forward(request, response);
                return;
            }
        }

        int idIndirizzo;

        String via = request.getParameter("via");
        String citta = request.getParameter("citta");
        String provincia = request.getParameter("provincia");
        String cap = request.getParameter("cap");

        if (via != null && !via.isEmpty()) {
        		try
        		{
        			IndirizzoBean indirizzo = new IndirizzoBean();
                    indirizzo.setViaNumciv(via);
                    indirizzo.setCitta(citta);
                    indirizzo.setProvincia(provincia);
                    indirizzo.setCodicePostale(cap);
                    
                    idIndirizzo = indirizzoDAO.doSave(indirizzo);
        		}catch (SQLException e) {
                    throw new ServletException("Errore durante il checkout: " + e.getMessage(), e);
                }

        } else {
            idIndirizzo = utente.getIndirizzo().getIdIndirizzo();
        }

        String numeroCarta = request.getParameter("numeroCarta");
        String pagamento = numeroCarta.substring(numeroCarta.length() - 4);

        try {
            ordineDAO.doSaveOrdineCompleto(cart, utente, idIndirizzo, pagamento);

            cart.clear();
            request.getSession().setAttribute("cart", cart);

            response.sendRedirect(request.getContextPath() + "/common/pagamento?conferma=ok");

        } catch (SQLException e) {
            throw new ServletException("Errore durante il checkout: " + e.getMessage(), e);
        }
    }

    private double calcolaTotale(CarrelloBean cart) {
        double totale = 0;
        for (CarrelloItemBean item : cart.getItems()) {
            ProdottoBean p = item.getProdotto();
            double prezzoScontato = p.getPrezzo() * (1.0 - p.getSconto() / 100.0);
            totale += prezzoScontato * item.getQuantita();
        }
        return totale;
    }
}