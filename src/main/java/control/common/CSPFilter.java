package control.common;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class CSPFilter extends HttpFilter {
	
	private static final long serialVersionUID = 1L;

	@Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws IOException, ServletException {
		
        response.setHeader(
            "Content-Security-Policy",
            "default-src 'self'; " +
            	"script-src 'self' 'unsafe-inline'; " +
            	"style-src 'self' 'unsafe-inline'; " +
            	  "img-src 'self' data:; " +
            "object-src 'none'; " +
            "frame-ancestors 'none';"
        );

        chain.doFilter(request, response);
    }
}