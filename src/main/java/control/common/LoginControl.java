package control.common;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import dao.UtenteDAOImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UtenteBean;

@WebServlet("/login")
public class LoginControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

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

			DataSource ds =(DataSource) getServletContext().getAttribute("DataSource");

			UtenteDAOImpl dao =new UtenteDAOImpl(ds);

			UtenteBean utente =dao.checkLogin(email,password);

			if(utente != null) {

				request.getSession().setAttribute("utente", utente);
				request.getSession().setAttribute("role",utente.getRuolo().name());

				/*
				 * ADMIN
				 */
				if(utente.getRuolo()== UtenteBean.Ruolo.ADMIN) {
					
				response.sendRedirect(request.getContextPath()+ "/admin/home");

				/*
				 * UTENTE NORMALE
				 */
				} else {
					
				response.sendRedirect(request.getContextPath()+ "/common/home");
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
				"Il campo "
				+ fieldName
				+ " non può essere vuoto"
			);

			return "";
		}

		return value.trim();
	}
}