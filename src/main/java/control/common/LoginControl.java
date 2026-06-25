package control.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

	private static final Pattern EMAIL_PATTERN =
			Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{1,}$");

	private UtenteDAO utenteDao;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		if (ds == null) {
			throw new ServletException("DataSource non disponibile nel contesto");
		}
		utenteDao = new UtenteDAOImpl(ds);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<String> errors = new ArrayList<>();

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		email = validateEmail(email, errors);
		password = validateField(password, "password", errors);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/LoginView.jsp");

		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			request.setAttribute("emailInserita", email);
			dispatcher.forward(request, response);
			return;
		}

		try {
			String digest = toDigest(password);
			UtenteBean utente = utenteDao.checkLogin(email, digest);

			if (utente != null) {
				request.getSession().setAttribute("utente", utente);
				request.getSession().setAttribute("role", utente.getRuolo().name());

				if (utente.getRuolo() == UtenteBean.Ruolo.ADMIN) {
					response.sendRedirect(request.getContextPath() + "/admin/dashboard");
				} else {
					response.sendRedirect(request.getContextPath() + "/home");
				}
			} else {
				errors.add("Email o password non validi");
				request.setAttribute("errors", errors);
				request.setAttribute("emailInserita", email);
				dispatcher.forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			errors.add("Si è verificato un errore interno. Riprova più tardi.");
			request.setAttribute("errors", errors);
			request.setAttribute("emailInserita", email);
			dispatcher.forward(request, response);
		}
	}

	private String validateField(String value, String fieldName, List<String> errors) {
		if (value == null || value.trim().isEmpty()) {
			errors.add("Il campo " + fieldName + " non può essere vuoto");
			return "";
		}
		return value.trim();
	}


	private String validateEmail(String value, List<String> errors) {
		String trimmed = validateField(value, "email", errors);

		if (!trimmed.isEmpty() && !EMAIL_PATTERN.matcher(trimmed).matches()) {
			errors.add("Inserisci un indirizzo email valido.");
			return "";
		}

		return trimmed;
	}

	public static String toDigest(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] digestBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
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