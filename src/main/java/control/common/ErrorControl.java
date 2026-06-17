package control.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/error")
public class ErrorControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ErrorControl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera il codice di stato dell'errore (es. 404, 500) se inviato dal server
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        
        // Se non c'è un codice di default, possiamo ipotizzare un errore generico o un accesso diretto
        if (statusCode == null) {
            statusCode = 500;
        }

        request.setAttribute("statusCode", statusCode);
        request.getRequestDispatcher("/WEB-INF/views/error/ErrorPage.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}