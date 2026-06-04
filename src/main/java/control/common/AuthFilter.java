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
	    	// Se l'URL non è protetto, lascia passare
	    if (!path.startsWith("/admin/") && !path.startsWith("/common/")) {
	    		chain.doFilter(request, response);
	        return; // Per evitare che il codice che segue venga eseguito
	    }
	    
	    // Controllo che il token sia in sessione
	    HttpSession session = request.getSession(false);
	    String role = (session != null) ? (  (UtenteBean) session.getAttribute("user")).getRuolo().name() : null;
	    
	    System.out.print(role);
	    
	    // Controllo autenticazione e autorizzazione
	    boolean autorizzato = false;
	    if (role != null) {
	    		if (path.startsWith("/admin/")) {
	            autorizzato = role.equals("admin");
	        } else if (path.startsWith("/common/")) {
	            autorizzato = role.equals("ADMIN") || role.equals("USER");
	        }
	    }
	    if (autorizzato) {
	        chain.doFilter(request, response);
	    } else {
	        response.sendRedirect(request.getContextPath() + "/indexlogin");
	    }
	}
}


