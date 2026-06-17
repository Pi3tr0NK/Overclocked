package control.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import dao.UtenteDAO;
import dao.UtenteDAOImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UtenteBean;

@WebServlet("/login")
public class LoginControl extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private UtenteDAO utenteDao;
	
	public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }

        utenteDao =new UtenteDAOImpl(ds);
    }

	protected void doPost(HttpServletRequest request,
						  HttpServletResponse response)
			throws ServletException, IOException {

		List<String> errors = new ArrayList<>();

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		email = validateField(email, "email", errors);
		password = validateField(password, "password", errors);

		RequestDispatcher dispatcher =request.getRequestDispatcher("/WEB-INF/views/common/LoginView.jsp");

		if(!errors.isEmpty()) {

			request.setAttribute("errors", errors);

			dispatcher.forward(request, response);

			return;
		}

		try {
			
			String digest = toDigest(password);
			UtenteBean utente = utenteDao.checkLogin(email,digest);

			if(utente != null) {

				request.getSession().setAttribute("utente", utente);
				request.getSession().setAttribute("role",utente.getRuolo().name());

				/*
				 * ADMIN
				 */
				if(utente.getRuolo()== UtenteBean.Ruolo.ADMIN) {
					
				response.sendRedirect(request.getContextPath()+ "/admin/dashboard");

				/*
				 * UTENTE NORMALE
				 */
				} else {
				// request.getSession().setAttribute("user", utente); 
				response.sendRedirect(request.getContextPath()+ "/home");
				}

			} else {

				errors.add("Email o password non validi");

				request.setAttribute("errors",errors);

				dispatcher.forward(request,response);
			}

		} catch(SQLException e) {

			throw new ServletException(e);
		}
	}

	private String validateField(
			String value,
			String fieldName,
			List<String> errors) {

		if(value == null ||
		   value.trim().isEmpty()) {

			errors.add(
				"Il campo " + fieldName + " non può essere vuoto"
			);

			return "";
		}

		return value.trim();
	}
	
	public static String toDigest(String password) {
        try {
        		// Definisco la funzione di hash SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            // Calcolo il digest della password
            byte[] digestBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            // Converto il digest in stringa esadecimale
            StringBuilder sb = new StringBuilder();
            for (byte b : digestBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo SHA-512 non disponibile", e);
        }
    }
}