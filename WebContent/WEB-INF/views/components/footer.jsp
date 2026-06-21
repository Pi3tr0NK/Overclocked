<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">

<footer class="footer">

    <!-- TOP: descrizione, supporto, newsletter -->
    <div class="footer-top">

        <div class="footer-brand">
            <p class="footer-desc">
                Il punto di riferimento italiano per i componenti PC.
                Spedizioni rapide, garanzia ufficiale e assistenza dedicata
                per costruire la build dei tuoi sogni.
            </p>
        </div>

        <div class="footer-col">
            <div class="footer-col-title">Supporto</div>
            <div class="footer-links">
                <a class="footer-link" href="${pageContext.request.contextPath}/faq#faq-come-ordinare-1">
                    <span class="footer-link-arrow">›</span> Come ordinare
                </a>
                <a class="footer-link" href="${pageContext.request.contextPath}/faq#faq-spedizioni-1">
                    <span class="footer-link-arrow">›</span> Spedizioni e consegne
                </a>
                <a class="footer-link" href="${pageContext.request.contextPath}/faq#faq-resi-1">
                    <span class="footer-link-arrow">›</span> Resi e rimborsi
                </a>
                <a class="footer-link" href="${pageContext.request.contextPath}/faq#faq-garanzia-1">
                    <span class="footer-link-arrow">›</span> Garanzia prodotti
                </a>
                <a class="footer-link" href="${pageContext.request.contextPath}/faq">
                    <span class="footer-link-arrow">›</span> FAQ
                </a>
                <a class="footer-link" href="${pageContext.request.contextPath}/faq#faq-contatti-1">
                    <span class="footer-link-arrow">›</span> Contattaci
                </a>
            </div>
        </div>
    </div>

    <!-- MID: garanzie -->
    <div class="footer-mid">

        <div class="footer-mid-item">
            <span class="footer-mid-icon">&#128666;</span>
            <div class="footer-mid-text">
                <strong>Spedizione gratuita</strong>
                <span>Su ordini superiori a € 99</span>
            </div>
        </div>

        <div class="footer-mid-item">
            <span class="footer-mid-icon">&#9889;</span>
            <div class="footer-mid-text">
                <strong>Consegna 24h</strong>
                <span>Ordini entro le 14:00</span>
            </div>
        </div>

        <div class="footer-mid-item">
            <span class="footer-mid-icon">&#128737;</span>
            <div class="footer-mid-text">
                <strong>Garanzia ufficiale</strong>
                <span>Fino a 3 anni sui prodotti</span>
            </div>
        </div>

        <div class="footer-mid-item">
            <span class="footer-mid-icon">&#8617;</span>
            <div class="footer-mid-text">
                <strong>Reso gratuito</strong>
                <span>Entro 30 giorni dall'acquisto</span>
            </div>
        </div>

        <div class="footer-mid-item">
            <span class="footer-mid-icon">&#128274;</span>
            <div class="footer-mid-text">
                <strong>Pagamento sicuro</strong>
                <span>Transazioni crittografate SSL</span>
            </div>
        </div>

    </div>

    <!-- BOTTOM: copyright, link legali, metodi di pagamento -->
    <div class="footer-bottom">

        <span class="footer-copy">
            © 2026 <span>Overclocked</span> S.r.l.
            &mdash; P.IVA 12345678901
            &mdash; Tutti i diritti riservati
        </span>

        <div class="footer-legal">
            <a href="#">Privacy Policy</a>
            <a href="#">Termini e condizioni</a>
            <a href="#">Cookie Policy</a>
        </div>

        <div class="footer-payments">
            <span class="footer-payments-label">Accettiamo</span>
            <span class="payment-badge">VISA</span>
            <span class="payment-badge">MC</span>
            <span class="payment-badge">AMEX</span>
            <span class="payment-badge">PayPal</span>
        </div>

    </div>

</footer>