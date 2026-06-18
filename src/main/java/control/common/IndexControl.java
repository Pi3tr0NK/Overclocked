package control.common;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UtenteBean;

@WebServlet("/indexlogin")
public class IndexControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    UtenteBean utente = (UtenteBean) request.getSession().getAttribute("utente");
	    if (utente != null) {
	        if (utente.getRuolo() == UtenteBean.Ruolo.ADMIN) {
	            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
	        } else {
	            response.sendRedirect(request.getContextPath() + "/home");
	        }
	        return;
	    }
	    request.getRequestDispatcher("/WEB-INF/views/common/LoginView.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
