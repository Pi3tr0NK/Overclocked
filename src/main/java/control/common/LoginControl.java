package control.common;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import dao.UtenteDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UtenteBean;

@WebServlet("/login")
public class LoginControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {

            DataSource ds =
            (DataSource)getServletContext()
            .getAttribute("DataSource");

            UtenteDAOImpl dao = new UtenteDAOImpl(ds);

            UtenteBean utente =
            dao.checkLogin(email, password);

            if(utente != null) {

                HttpSession session = request.getSession();

                session.setAttribute("utente", utente);

                response.sendRedirect("index.jsp");

            } else {

                request.setAttribute(
                    "errore",
                    "Email o password errati"
                );

                request.getRequestDispatcher("/WEB-INF/views/common/LoginView.jsp").forward(request, response);
            }

        } catch(SQLException e) {

            throw new ServletException(e);
        }
        
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	
    	doGet(request,response);
    }   
}
