package control.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.LinkedHashMap;
import java.util.Map;

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
		
	    if (request.getSession(false) != null &&
	            request.getSession(false).getAttribute("utente") != null) {

	            response.sendRedirect(request.getContextPath() + "/home");
	            return;
	        }
	    
		RequestDispatcher dispatcher =request.getRequestDispatcher("/WEB-INF/views/common/RegisterView.jsp");
		dispatcher.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    if (request.getSession(false) != null &&
	            request.getSession(false).getAttribute("utente") != null) {

	            response.sendRedirect(request.getContextPath() + "/home");
	            return;
	        }

	    String nome = request.getParameter("nome");
	    String cognome = request.getParameter("cognome");
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");
	    String cellulare = request.getParameter("cellulare");

	    String via = request.getParameter("via");
	    String datiPlus = request.getParameter("datiPlus");
	    String citta = request.getParameter("citta");
	    String provincia = request.getParameter("provincia");
	    String cap = request.getParameter("cap");
	    String paese = request.getParameter("paese");

	    // ===== VALIDAZIONE LATO SERVER =====

	    Map<String, String> regole = new LinkedHashMap<>();
	    regole.put("nome",      "^[A-Za-zÀ-ÿ\\s']{2,50}$");
	    regole.put("cognome",   "^[A-Za-zÀ-ÿ\\s']{2,50}$");
	    regole.put("email",     "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{1,}$");
	    regole.put("password",  "^(?=.*[A-Z])(?=.*\\d).{8,}$");
	    regole.put("via",       "^[A-Za-zÀ-ÿ\\s']+\\s+\\d+$");
	    regole.put("citta",     "^[A-Za-zÀ-ÿ\\s']{1,80}$");
	    regole.put("provincia", "^[A-Za-zÀ-ÿ\\s']{1,80}$");
	    regole.put("cap",       "^[A-Za-z0-9\\s-]{2,12}$");
	    regole.put("paese",     "^[A-Za-zÀ-ÿ\\s']{1,80}$");
	    regole.put("cellulare", "^\\+?[0-9][0-9\\s-]{6,15}$");

	    Map<String, String> messaggi = new LinkedHashMap<>();
	    messaggi.put("nome", "Il nome deve contenere solo lettere.");
	    messaggi.put("cognome", "Il cognome deve contenere solo lettere.");
	    messaggi.put("email", "Inserisci un indirizzo email valido.");
	    messaggi.put("password", "La password deve contenere almeno 8 caratteri, una maiuscola e un numero.");
	    messaggi.put("via", "Inserisci la via seguita dal numero civico (es. Via Roma 12).");
	    messaggi.put("citta", "Inserisci una città valida.");
	    messaggi.put("provincia", "Inserisci una provincia/stato/regione valida.");
	    messaggi.put("cap", "Inserisci un codice postale valido.");
	    messaggi.put("paese", "Inserisci un paese valido.");
	    messaggi.put("cellulare", "Inserisci un numero di telefono valido (es. +39 333 1234567).");

	    Map<String, String> valori = new LinkedHashMap<>();
	    valori.put("nome", nome);
	    valori.put("cognome", cognome);
	    valori.put("email", email);
	    valori.put("password", password);
	    valori.put("via", via);
	    valori.put("citta", citta);
	    valori.put("provincia", provincia);
	    valori.put("cap", cap);
	    valori.put("paese", paese);
	    valori.put("cellulare", cellulare);

	    String errors = null;

	    for (Map.Entry<String, String> campo : valori.entrySet()) {

	        String chiave = campo.getKey();
	        String valore = campo.getValue();

	        if (valore == null || !Pattern.matches(regole.get(chiave), valore)) {
	            errors = messaggi.get(chiave);
	            break;
	        }
	    }

	    if (errors == null) {
	        try {
	            if (utenteDAO.checkEmail(email)) {
	                errors = "L'email esiste già";
	            }
	        } catch (SQLException e) {
	            throw new ServletException(e);
	        }
	    }

	    if (errors != null) {

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
	        String passwordDigest = toDigest(password);
	        utente.setPassword(passwordDigest);
	        utente.setCellulare(cellulare);

	        utente.setRuolo(
	            UtenteBean.Ruolo.USER
	        );

	        utente.setIndirizzo(indirizzo);

	        utenteDAO.doSave(utente);

	        request.getSession().setAttribute("utente", utente);

	        request.getSession().setAttribute("role", "USER");

	        response.sendRedirect(request.getContextPath() + "/home");

	    } catch (SQLException e) {

	        throw new ServletException(e);
	    }
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