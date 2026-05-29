package control.common;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import dao.IndirizzoDAOImpl;
import dao.UtenteDAOImpl;
import jakarta.servlet.RequestDispatcher;
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

	/*
	 * APRE LA PAGINA REGISTER
	 */
	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response)
			throws ServletException, IOException {

		RequestDispatcher dispatcher =
		request.getRequestDispatcher(
			"/WEB-INF/views/common/RegisterView.jsp"
		);

		dispatcher.forward(request, response);
	}

	/*
	 * REGISTRA L'UTENTE
	 */
	protected void doPost(HttpServletRequest request,
						  HttpServletResponse response)
			throws ServletException, IOException {

		List<String> errors = new ArrayList<>();

		/*
		 * DATI UTENTE
		 */
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confermaPassword =request.getParameter("confermaPassword");
		String cellulare = request.getParameter("cellulare");

		/*
		 * DATI INDIRIZZO
		 */
		String via = request.getParameter("via");
		String datiPlus =request.getParameter("datiPlus");
		String citta = request.getParameter("citta");
		String provincia = request.getParameter("provincia");
		String cap = request.getParameter("cap");
		String paese = request.getParameter("paese");

		/*
		 * VALIDAZIONE
		 */
		nome = validateField(nome, "nome", errors);
		cognome = validateField(cognome, "cognome", errors);
		email = validateField(email, "email", errors);
		password = validateField(password, "password", errors);

		if(!password.equals(confermaPassword)) {

			errors.add("Le password non coincidono");
		}

		if(!errors.isEmpty()) {

			request.setAttribute("errors", errors);

			RequestDispatcher dispatcher =
			request.getRequestDispatcher(
				"/WEB-INF/views/common/RegisterView.jsp"
			);

			dispatcher.forward(request, response);

			return;
		}

		try {

			DataSource ds =
			(DataSource) getServletContext()
			.getAttribute("DataSource");

			/*
			 * DAO
			 */
			IndirizzoDAOImpl indirizzoDAO =
					new IndirizzoDAOImpl(ds);

			UtenteDAOImpl utenteDAO =
					new UtenteDAOImpl(ds);

			/*
			 * CREAZIONE INDIRIZZO
			 */
			IndirizzoBean indirizzo =
					new IndirizzoBean();

			indirizzo.setViaNumciv(via);
			indirizzo.setDatiPlus(datiPlus);
			indirizzo.setCitta(citta);
			indirizzo.setProvincia(provincia);
			indirizzo.setCodicePostale(cap);
			indirizzo.setPaese(paese);

			/*
			 * SALVO INDIRIZZO
			 */
			indirizzoDAO.doSave(indirizzo);

			/*
			 * CREAZIONE UTENTE
			 */
			UtenteBean utente =
					new UtenteBean();

			utente.setNome(nome);
			utente.setCognome(cognome);
			utente.setEmail(email);
			utente.setPassword(password);
			utente.setCellulare(cellulare);

			utente.setRuolo(
				UtenteBean.Ruolo.USER
			);

			utente.setIndirizzo(indirizzo);

			/*
			 * SALVO UTENTE
			 */
			utenteDAO.doSave(utente);

			/*
			 * LOGIN AUTOMATICO
			 */
			request.getSession().setAttribute("utente", utente);

			request.getSession().setAttribute("role", "USER");

			/*
			 * REDIRECT HOME
			 */
			response.sendRedirect(
				request.getContextPath()
				+ "/home"
			);

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
				"Il campo "
				+ fieldName
				+ " non può essere vuoto"
			);

			return "";
		}

		return value.trim();
	}
}