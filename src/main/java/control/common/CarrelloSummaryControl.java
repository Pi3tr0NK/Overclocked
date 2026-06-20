package control.common;

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

@WebServlet("/carrello/summary")
public class CarrelloSummaryControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        String ajaxHeader = request.getHeader("X-Requested-With");
        if (!"XMLHttpRequest".equals(ajaxHeader)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso diretto non consentito.");
            return;
        }
        
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

            double prezzoScontato = prod.getPrezzo() - (prod.getPrezzo() * prod.getSconto() / 100.0);
            totale += prezzoScontato * item.getQuantita();

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

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
