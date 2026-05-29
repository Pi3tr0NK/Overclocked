package controller;

import dao.UtenteDAO;
import dao.UtenteDAOImpl;
import model.UtenteBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // GET → mostra la pagina di login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Se l'utente è già loggato, rimanda alla home
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("utente") != null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    // POST → elabora il login (chiamato sia dal form normale che dall'AJAX)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");
        String ajax     = request.getHeader("X-Requested-With"); // identifica chiamata AJAX

        // --- Validazione base lato server ---
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {

            rispondi(request, response, ajax, "Compila tutti i campi.", false);
            return;
        }

        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            rispondi(request, response, ajax, "Formato email non valido.", false);
            return;
        }

        // --- Autenticazione tramite DAO ---
        try {
            UtenteDAO utenteDAO = new UtenteDAOImpl();
            UtenteBean utente   = utenteDAO.login(email.trim(), password);

            if (utente == null) {
                rispondi(request, response, ajax, "Credenziali errate. Riprova.", false);
                return;
            }

            // Login riuscito → salva utente in sessione
            HttpSession session = request.getSession(true);
            session.setAttribute("utente", utente);
            session.setMaxInactiveInterval(60 * 60); // sessione valida 1 ora

            rispondi(request, response, ajax, "ok", true);

        } catch (Exception e) {
            e.printStackTrace();
            rispondi(request, response, ajax, "Errore interno del server. Riprova più tardi.", false);
        }
    }

    /**
     * Risponde in JSON se la chiamata è AJAX,
     * altrimenti fa forward/redirect classico.
     */
    private void rispondi(HttpServletRequest request, HttpServletResponse response,
                          String ajax, String messaggio, boolean successo)
            throws ServletException, IOException {

        if ("XMLHttpRequest".equals(ajax)) {
            // Risposta JSON per AJAX
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            if (successo) {
                out.print("{\"successo\": true, \"redirect\": \""
                        + request.getContextPath() + "/index.jsp\"}");
            } else {
                // Escape del messaggio per sicurezza
                String msg = messaggio.replace("\"", "\\\"");
                out.print("{\"successo\": false, \"errore\": \"" + msg + "\"}");
            }
            out.flush();

        } else {
            // Risposta classica (form tradizionale senza JS)
            if (successo) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else {
                request.setAttribute("errore", messaggio);
                request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                       .forward(request, response);
            }
        }
    }
}