package control.common;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

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

        String errore = validaParametri(request);

        if (errore != null) {
            request.setAttribute("errore", errore);
            request.setAttribute("totale", calcolaTotale(cart));
            request.setAttribute("utente", utente);
            request.setAttribute("prodotti", cart.getItems());
            request.getRequestDispatcher("/WEB-INF/views/common/PagamentoView.jsp")
                   .forward(request, response);
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

        // Da qui in poi le variabili sono dichiarate una volta sola
        String via       = request.getParameter("via");
        String citta     = request.getParameter("citta");
        String provincia = request.getParameter("provincia");
        String cap       = request.getParameter("cap");
        String paese     = request.getParameter("paese");

        int idIndirizzo;

        if (via != null && !via.trim().isEmpty()) {
            try {
                IndirizzoBean indirizzo = new IndirizzoBean();
                indirizzo.setViaNumciv(via);
                indirizzo.setCitta(citta);
                indirizzo.setProvincia(provincia);
                indirizzo.setCodicePostale(cap);
                indirizzo.setPaese(paese);
                idIndirizzo = indirizzoDAO.doSave(indirizzo);
            } catch (SQLException e) {
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

    private String validaParametri(HttpServletRequest request) {

        String via          = request.getParameter("via");
        String citta        = request.getParameter("citta");
        String provincia    = request.getParameter("provincia");
        String cap          = request.getParameter("cap");
        String paese        = request.getParameter("paese");
        String numeroCarta  = request.getParameter("numeroCarta");
        String intestatario = request.getParameter("intestatario");
        String mese         = request.getParameter("mese");
        String anno         = request.getParameter("anno");
        String cvv          = request.getParameter("cvv");

        Map<String, String> regole   = new LinkedHashMap<>();
        Map<String, String> messaggi = new LinkedHashMap<>();
        Map<String, String> valori   = new LinkedHashMap<>();

        // Campi carta — sempre obbligatori
        regole.put("numeroCarta",  "^[0-9]{16}$");
        regole.put("intestatario", "^[A-Za-zÀ-ÿ\\s']{2,50}$");
        regole.put("mese",         "^(0?[1-9]|1[0-2])$");
        regole.put("anno",         "^[0-9]{4}$");
        regole.put("cvv",          "^[0-9]{3}$");

        messaggi.put("numeroCarta",  "Il numero carta deve contenere 16 cifre.");
        messaggi.put("intestatario", "Inserisci il nome come riportato sulla carta.");
        messaggi.put("mese",         "Inserisci un mese valido (1-12).");
        messaggi.put("anno",         "Inserisci un anno valido a 4 cifre (es. 2025).");
        messaggi.put("cvv",          "Il CVV deve contenere 3 cifre.");

        valori.put("numeroCarta",  numeroCarta);
        valori.put("intestatario", intestatario);
        valori.put("mese",         mese);
        valori.put("anno",         anno);
        valori.put("cvv",          cvv);

        // Campi indirizzo — obbligatori solo se via è valorizzata
        if (via != null && !via.trim().isEmpty()) {
            regole.put("via",       "^[A-Za-zÀ-ÿ\\s']+\\s+\\d+$");
            regole.put("citta",     "^[A-Za-zÀ-ÿ\\s']{1,80}$");
            regole.put("provincia", "^[A-Za-zÀ-ÿ\\s']{1,80}$");
            regole.put("cap",       "^[A-Za-z0-9\\s\\-]{2,12}$");
            regole.put("paese",     "^[A-Za-zÀ-ÿ\\s']{1,80}$");

            messaggi.put("via",       "Inserisci la via seguita dal numero civico (es. Via Roma 12).");
            messaggi.put("citta",     "Inserisci una città valida.");
            messaggi.put("provincia", "Inserisci una provincia/stato/regione valida.");
            messaggi.put("cap",       "Inserisci un codice postale valido.");
            messaggi.put("paese",     "Inserisci un paese valido.");

            valori.put("via",       via);
            valori.put("citta",     citta);
            valori.put("provincia", provincia);
            valori.put("cap",       cap);
            valori.put("paese",     paese);
        }

        for (Map.Entry<String, String> campo : valori.entrySet()) {
            String chiave = campo.getKey();
            String valore = campo.getValue();

            if (valore == null || valore.trim().isEmpty()) {
                return "Il campo " + chiave + " è obbligatorio.";
            }

            if (!Pattern.matches(regole.get(chiave), valore.trim())) {
                return messaggi.get(chiave);
            }
        }

        return null;
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