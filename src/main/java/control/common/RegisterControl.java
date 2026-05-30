package control.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import dao.IndirizzoDAO;
import dao.IndirizzoDAOImpl;
import dao.UtenteDAO;
import dao.UtenteDAOImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.IndirizzoBean;
import model.UtenteBean;

@WebServlet("/register")
public class RegisterControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private UtenteDAO utenteDAO;
	private IndirizzoDAO indirizzoDAO;
	
	public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }

        utenteDAO =new UtenteDAOImpl(ds);
        indirizzoDAO =new IndirizzoDAOImpl(ds);
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RequestDispatcher dispatcher =request.getRequestDispatcher("/WEB-INF/views/common/RegisterView.jsp");
		dispatcher.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<String> errors = new ArrayList<>();

		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confermaPassword =request.getParameter("confermaPassword");
		String cellulare = request.getParameter("cellulare");

		String via = request.getParameter("via");
		String datiPlus =request.getParameter("datiPlus");
		String citta = request.getParameter("citta");
		String provincia = request.getParameter("provincia");
		String cap = request.getParameter("cap");
		String paese = request.getParameter("paese");

		nome = validateField(nome, "nome", errors);
		cognome = validateField(cognome, "cognome", errors);
		email = validateField(email, "email", errors);
		password = validateField(password, "password", errors);

		if(!password.equals(confermaPassword)) {

			errors.add("Le password non coincidono");
		}

		if(!errors.isEmpty()) {

			request.setAttribute("errors", errors);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/RegisterView.jsp");

			dispatcher.forward(request, response);

			return;
		}

		try {
			
			IndirizzoBean indirizzo = new IndirizzoBean();

			indirizzo.setViaNumciv(via);
			indirizzo.setDatiPlus(datiPlus);
			indirizzo.setCitta(citta);
			indirizzo.setProvincia(provincia);
			indirizzo.setCodicePostale(cap);
			indirizzo.setPaese(paese);

			indirizzoDAO.doSave(indirizzo);

			UtenteBean utente = new UtenteBean();
			
			utente.setNome(nome);
			utente.setCognome(cognome);
			utente.setEmail(email);
			String passwordDigest= toDigest(password);
			utente.setPassword(passwordDigest);
			utente.setCellulare(cellulare);

			utente.setRuolo(
				UtenteBean.Ruolo.USER
			);

			utente.setIndirizzo(indirizzo);

			utenteDAO.doSave(utente);

			request.getSession().setAttribute("utente", utente);

			request.getSession().setAttribute("role", "USER");

			response.sendRedirect(request.getContextPath()+ "/home");

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

			errors.add("Il campo "+ fieldName+ " non può essere vuoto");

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