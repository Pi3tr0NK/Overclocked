package control.common;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import dao.OrdineDAOImpl;
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

@WebServlet("/common/pagamento")
public class PagamentoControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private DataSource ds;
    private OrdineDAOImpl ordineDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) throw new ServletException("DataSource non disponibile");
        ordineDAO = new OrdineDAOImpl(ds);
    }

    // ── GET: mostra la pagina di riepilogo oppure la conferma ─────────
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UtenteBean utente = (UtenteBean) request.getSession().getAttribute("user");
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/indexlogin");
            return;
        }

        // Pagina di conferma dopo il salvataggio
        if ("ok".equals(request.getParameter("conferma"))) {
            request.setAttribute("utente", utente);
            request.getRequestDispatcher("/WEB-INF/views/common/PagamentoView.jsp")
                   .forward(request, response);
            return;
        }

        // Mostra pagina di riepilogo checkout
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

    // ── POST: salva l'ordine ──────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UtenteBean utente = (UtenteBean) request.getSession().getAttribute("user");
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/indexlogin");
            return;
        }

        CarrelloBean cart = (CarrelloBean) request.getSession().getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Carrello");
            return;
        }

        // Verifica stock per ogni prodotto prima di procedere
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

        // Recupera id indirizzo — dal form se presente, altrimenti da quello dell'utente
        int idIndirizzo = utente.getIndirizzo().getIdIndirizzo();
        String idIndirizzoParam = request.getParameter("idIndirizzo");
        if (idIndirizzoParam != null && !idIndirizzoParam.isEmpty()) {
            idIndirizzo = Integer.parseInt(idIndirizzoParam);
        }

        try {
            // Salva ordine + dettagli + scala stock in un'unica transazione nel DAO
            ordineDAO.doSaveOrdineCompleto(cart, utente, idIndirizzo);

            // Svuota il carrello
            cart.clear();
            request.getSession().setAttribute("cart", cart);

            // Redirect alla pagina di conferma
            response.sendRedirect(request.getContextPath() + "/common/pagamento?conferma=ok");

        } catch (SQLException e) {
            throw new ServletException("Errore durante il checkout: " + e.getMessage(), e);
        }
    }

    // ─────────────────────────────────────────────
    //  Calcolo totale con sconto
    // ─────────────────────────────────────────────

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