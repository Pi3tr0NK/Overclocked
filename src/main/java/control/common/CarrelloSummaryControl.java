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
import java.io.PrintWriter;
import java.util.List;

/**
 * Endpoint AJAX — restituisce un JSON con il riepilogo del carrello.
 * Usato dalla sidebar in prodotto.jsp (e in qualsiasi altra pagina).
 *
 * GET /carrello/summary
 *
 * Risposta esempio:
 * {
 *   "numProdotti": 3,
 *   "totale": "59.97",
 *   "items": [
 *     {
 *       "idProdotto": 42,
 *       "marca": "Sony",
 *       "nome": "WH-1000XM5",
 *       "quantita": 1,
 *       "prezzoScontato": "299.00",
 *       "immagine": "/ecommerce/img/prodotti/wh1000xm5_1.jpg"
 *     }
 *   ]
 * }
 *
 * Non accorpata con CarrelloAddControl né CarrelloControl:
 * le tre servlet hanno responsabilità distinte (aggiunta, pagina completa, summary AJAX).
 */
@WebServlet("/carrello/summary")
public class CarrelloSummaryControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CarrelloBean cart = (CarrelloBean) request.getSession().getAttribute("cart");
        if (cart == null) {
            cart = new CarrelloBean();
        }

        List<CarrelloItemBean> items = cart.getItems();

        double totale = 0;
        StringBuilder itemsJson = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            CarrelloItemBean item = items.get(i);
            ProdottoBean prod = item.getProdotto();

            double prezzoScontato = prod.getPrezzo()
                    - (prod.getPrezzo() * prod.getSconto() / 100.0);
            totale += prezzoScontato * item.getQuantita();

            // Prima immagine del prodotto (path relativo al context root)
            String immagine = "";
            if (prod.getImmagini() != null && !prod.getImmagini().isEmpty()) {
                immagine = request.getContextPath() + "/" + prod.getImmagini().get(0).getPath();
            }

            if (i > 0) itemsJson.append(",");
            itemsJson.append("{")
                    .append("\"idProdotto\":").append(prod.getIdProdotto()).append(",")
                    .append("\"marca\":\"").append(escapeJson(prod.getMarca())).append("\",")
                    .append("\"nome\":\"").append(escapeJson(prod.getNome())).append("\",")
                    .append("\"quantita\":").append(item.getQuantita()).append(",")
                    .append("\"prezzoScontato\":\"").append(String.format("%.2f", prezzoScontato)).append("\",")
                    .append("\"immagine\":\"").append(escapeJson(immagine)).append("\"")
                    .append("}");
        }

        String json = "{"
                + "\"numProdotti\":" + cart.getTotalQuantity() + ","
                + "\"totale\":\"" + String.format("%.2f", totale) + "\","
                + "\"items\":[" + itemsJson + "]"
                + "}";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Nessuna cache — il carrello cambia ad ogni aggiunta
        response.setHeader("Cache-Control", "no-store");

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /** Escape minimale per stringhe JSON (evita XSS e JSON injection). */
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
