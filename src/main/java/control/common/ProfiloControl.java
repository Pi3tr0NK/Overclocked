package control.common;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import dao.IndirizzoDAOImpl;
import dao.OrdineDAOImpl;
import dao.UtenteDAOImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DettaglioOrdineBean;
import model.IndirizzoBean;
import model.OrdineBean;
import model.UtenteBean;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.DettaglioOrdineDAOImpl;

@WebServlet("/common/profilo")
public class ProfiloControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrdineDAOImpl ordineDAO;
    private UtenteDAOImpl utenteDAO;
    private IndirizzoDAOImpl indirizzoDAO;
    private DettaglioOrdineDAOImpl dettaglioDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) throw new ServletException("DataSource non disponibile");
        ordineDAO   = new OrdineDAOImpl(ds);
        utenteDAO   = new UtenteDAOImpl(ds);
        indirizzoDAO = new IndirizzoDAOImpl(ds);
        dettaglioDAO = new DettaglioOrdineDAOImpl(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UtenteBean utente = (UtenteBean) request.getSession().getAttribute("utente");

        String view   = request.getParameter("view");
        String action = request.getParameter("action");

        // ── default: ordini ──
        if (view == null || view.isEmpty()) view = "ordini";

        // ── azioni POST ──────────────────────────────────────────────
        if (action != null) {
            switch (action) {

                case "rimborsa":
                    gestisciRimborso(request, response, utente);
                    return;

                case "aggiornaDati":
                    gestisciAggiornaDati(request, response, utente);
                    return;

                case "fattura":          
                    gestisciFattura(request, response, utente);
                    return;
            }
        }

        // ── carica dati in base alla view ────────────────────────────
        try {
            switch (view) {

                case "ordini":
                    request.setAttribute("ordini",
                        ordineDAO.doRetrieveAllByUser(utente.getIdUtente(),0));
                    request.setAttribute("view", "ordini");
                    request.getRequestDispatcher("/WEB-INF/views/common/OrdiniView.jsp")
                           .forward(request, response);
                    break;

                case "resi":
                    request.setAttribute("resi",
                        ordineDAO.doRetrieveResiByUser(utente.getIdUtente()));
                    request.setAttribute("view", "resi");
                    request.getRequestDispatcher("/WEB-INF/views/common/ResiView.jsp")
                           .forward(request, response);
                    break;

                case "dati":
                    request.setAttribute("utente", utente);
                    request.setAttribute("view", "dati");
                    request.getRequestDispatcher("/WEB-INF/views/common/DatiView.jsp")
                           .forward(request, response);
                    break;
                    
                case "fattura":
                    gestisciFattura(request, response, utente);
                    return;

                default:
                    response.sendRedirect(request.getContextPath() + "/common/profilo");
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // ─────────────────────────────────────────────
    //  Rimborso ordine
    // ─────────────────────────────────────────────

    private void gestisciRimborso(HttpServletRequest request, HttpServletResponse response,
                                   UtenteBean utente) throws ServletException, IOException {
        String idOrdineStr = request.getParameter("idOrdine");
        if (idOrdineStr == null || idOrdineStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/common/profilo?view=ordini");
            return;
        }

        try {
            int idOrdine = Integer.parseInt(idOrdineStr);

            // Il DAO verifica i 30 giorni, ripristina lo stock e mette RIMBORSATO
            boolean ok = ordineDAO.doRimborsa(idOrdine, utente.getIdUtente());

            if (ok) {
                request.setAttribute("successo", "Ordine rimborsato con successo.");
            } else {
                request.setAttribute("errore",
                    "Impossibile rimborsare: ordine non trovato, già rimborsato o oltre 30 giorni.");
            }

            request.setAttribute("ordini",
                ordineDAO.doRetrieveAllByUser(utente.getIdUtente(),0));
            request.setAttribute("view", "ordini");
            request.getRequestDispatcher("/WEB-INF/views/common/OrdiniView.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // ─────────────────────────────────────────────
    //  Aggiorna dati utente
    // ─────────────────────────────────────────────

    private void gestisciAggiornaDati(HttpServletRequest request, HttpServletResponse response,
                                       UtenteBean utente) throws ServletException, IOException {
        try {
            // Aggiorna campi utente (no email)
            utente.setNome(request.getParameter("nome"));
            utente.setCognome(request.getParameter("cognome"));
            utente.setCellulare(request.getParameter("cellulare"));

            // Aggiorna indirizzo
            IndirizzoBean indirizzo = utente.getIndirizzo();
            indirizzo.setViaNumciv(request.getParameter("viaNumciv"));
            indirizzo.setCitta(request.getParameter("citta"));
            indirizzo.setProvincia(request.getParameter("provincia"));
            indirizzo.setCodicePostale(request.getParameter("codicePostale"));
            indirizzo.setPaese(request.getParameter("paese"));
            indirizzo.setDatiPlus(request.getParameter("datiPlus"));

            indirizzoDAO.doUpdate(indirizzo);
            utenteDAO.doUpdate(utente);

            // Aggiorna l'utente in sessione
            request.getSession().setAttribute("utente", utente);

            request.setAttribute("successo", "Dati aggiornati con successo.");

        } catch (SQLException e) {
            request.setAttribute("errore", "Errore durante l'aggiornamento: " + e.getMessage());
        }

        request.setAttribute("utente", utente);
        request.setAttribute("view", "dati");
        request.getRequestDispatcher("/WEB-INF/views/common/DatiView.jsp")
               .forward(request, response);
    }
    
    private void gestisciFattura(HttpServletRequest request, HttpServletResponse response,
            UtenteBean utente) throws ServletException, IOException {
		String idOrdineStr = request.getParameter("idOrdine");
		if (idOrdineStr == null || idOrdineStr.isEmpty()) {
		response.sendRedirect(request.getContextPath() + "/common/profilo?view=ordini");
		return;
		}
		
		try {
		int idOrdine = Integer.parseInt(idOrdineStr);
		
		// Recupera ordine e dettagli
		OrdineBean ordine = ordineDAO.doRetrieveByKey(idOrdine);
		if (ordine == null) {
		response.sendRedirect(request.getContextPath() + "/common/profilo?view=ordini");
		return;
		}
		
		List<DettaglioOrdineBean> dettagli = dettaglioDAO.doRetrieveByOrdine(idOrdine);
		
		// Genera PDF
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition",
		"attachment; filename=\"fattura_ordine_" + idOrdine + ".pdf\"");
		
		Document document = new Document();
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		
		// Intestazione
		Font titoloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
		Font normalFont  = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
		Font boldFont    = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
		Font smallFont   = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL);
		
		Paragraph titolo = new Paragraph("FATTURA", titoloFont);
		titolo.setAlignment(Element.ALIGN_CENTER);
		document.add(titolo);
		
		document.add(Chunk.NEWLINE);
		
		// Dati ordine
		document.add(new Paragraph("Ordine #" + idOrdine, boldFont));
		document.add(new Paragraph("Data: " + ordine.getData(), normalFont));
		
		document.add(Chunk.NEWLINE);
		
		// Dati cliente
		document.add(new Paragraph("Dati cliente", boldFont));
		document.add(new Paragraph(
		utente.getNome() + " " + utente.getCognome(), normalFont));
		document.add(new Paragraph(utente.getEmail(), normalFont));
		document.add(new Paragraph(
		ordine.getIndirizzo().getViaNumciv() + ", " +
		ordine.getIndirizzo().getCitta() + " (" +
		ordine.getIndirizzo().getProvincia() + ") " +
		ordine.getIndirizzo().getCodicePostale(), normalFont));
		document.add(new Paragraph("Carta: •••• " + ordine.getPagamento(), normalFont));
		
		document.add(Chunk.NEWLINE);
		
		// Tabella prodotti
		PdfPTable tabella = new PdfPTable(4);
		tabella.setWidthPercentage(100);
		tabella.setWidths(new float[]{4f, 1f, 2f, 2f});
		
		// Intestazioni tabella
		for (String intestazione : new String[]{"Prodotto", "Qtà", "Prezzo unitario", "Subtotale"}) {
		PdfPCell cella = new PdfPCell(new Phrase(intestazione, boldFont));
		cella.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cella.setPadding(6);
		tabella.addCell(cella);
		}
		
		// Righe prodotti
		for (DettaglioOrdineBean d : dettagli) {
		tabella.addCell(new Phrase(d.getProdotto().getNome(), normalFont));
		tabella.addCell(new Phrase(String.valueOf(d.getQuantita()), normalFont));
		tabella.addCell(new Phrase(String.format("%.2f €", d.getPrezzoUnitario()), normalFont));
		tabella.addCell(new Phrase(
		String.format("%.2f €", d.getPrezzoUnitario() * d.getQuantita()), normalFont));
		}
		
		document.add(tabella);
		
		document.add(Chunk.NEWLINE);
		
		// Totale
		Paragraph totale = new Paragraph(
		"Totale: " + String.format("%.2f €", ordine.getTotale()), boldFont);
		totale.setAlignment(Element.ALIGN_RIGHT);
		document.add(totale);
		
		document.add(Chunk.NEWLINE);
		Paragraph piede = new Paragraph("Grazie per il tuo acquisto — Overclocked", smallFont);
		piede.setAlignment(Element.ALIGN_CENTER);
		document.add(piede);
		
		document.close();
		
		} catch (Exception e) {
		throw new ServletException("Errore generazione fattura: " + e.getMessage(), e);
		}
}
}