package control.common;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UtenteBean;

@WebFilter("/*")
public class AuthFilter extends HttpFilter {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
	    String path = request.getServletPath();
	    
	    if (!path.startsWith("/admin/") && !path.startsWith("/common/")) {
	    		chain.doFilter(request, response);
	        return; 
	    }
	    
	   
	    HttpSession session = request.getSession(false);
	    UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utente") : null;
	    String role = (utente != null) ? utente.getRuolo().name() : null;
	    
	    
	    boolean autorizzato = false;
	    if (role != null) {
	    		if (path.startsWith("/admin/")) {
	            autorizzato = role.equals("ADMIN");
	        } else if (path.startsWith("/common/")) {
	            autorizzato = role.equals("ADMIN") || role.equals("USER");
	        }
	    }
	    if (autorizzato) {
	        chain.doFilter(request, response);
	    } else {
	        if (path.startsWith("/admin/")) {
	            response.sendError(403);
	        } else {
	            response.sendRedirect(request.getContextPath() + "/indexlogin");
	        }
	    }
	}
}


