<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<style>

.footer {
    background: #0f0f0f;
    border-top: 2px solid #ff7300;
    padding: 50px 60px 0;
}

/* ========================= TOP ========================= */

.footer-top {
    display: grid;
    grid-template-columns: 2fr 1fr 1fr;
    gap: 40px;
    padding-bottom: 40px;
}

.footer-desc {
    font-size: 13px;
    color: #555;
    line-height: 1.8;
}

/* ========================= COLONNE LINK ========================= */

.footer-col-title {
    font-size: 12px;
    color: #ff7300;
    letter-spacing: 2px;
    text-transform: uppercase;
    margin-bottom: 16px;
    font-weight: bold;
}

.footer-links {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.footer-link {
    font-size: 13px;
    color: #555;
    text-decoration: none;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 6px;
}

.footer-link:hover {
    color: #e0e0e0;
}

.footer-link-arrow {
    font-size: 12px;
    color: #ff7300;
}

/* ========================= NEWSLETTER ========================= */

.newsletter-desc {
    font-size: 13px;
    color: #555;
    margin-bottom: 12px;
    line-height: 1.6;
}

.newsletter-row {
    display: flex;
}

.newsletter-input {
    flex: 1;
    background: #080808;
    border: 1px solid #1f1f1f;
    border-radius: 8px 0 0 8px;
    height: 38px;
    padding: 0 12px;
    font-size: 12px;
    color: #e0e0e0;
    outline: none;
    font-family: Arial, sans-serif;
}

.newsletter-input:focus {
    border-color: #ff7300;
}

.newsletter-input::placeholder {
    color: #2a2a2a;
}

.newsletter-btn {
    height: 38px;
    padding: 0 16px;
    background: #ff7300;
    border: none;
    border-radius: 0 8px 8px 0;
    color: #080808;
    font-size: 12px;
    font-weight: bold;
    cursor: pointer;
}

.newsletter-btn:hover {
    opacity: 0.9;
}

/* ========================= MID (GARANZIE) ========================= */

.footer-mid {
    border-top: 1px solid #1a1a1a;
    padding: 20px 0;
    display: flex;
    gap: 30px;
    flex-wrap: wrap;
}

.footer-mid-item {
    display: flex;
    align-items: center;
    gap: 8px;
}

.footer-mid-icon {
    font-size: 18px;
}

.footer-mid-text strong {
    display: block;
    font-size: 13px;
    color: #e0e0e0;
    margin-bottom: 1px;
}

.footer-mid-text span {
    font-size: 11px;
    color: #444;
}

/* ========================= BOTTOM ========================= */

.footer-bottom {
    border-top: 1px solid #1a1a1a;
    padding: 18px 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 12px;
}

.footer-copy {
    font-size: 12px;
    color: #333;
}

.footer-copy span {
    color: #ff7300;
}

.footer-legal {
    display: flex;
    gap: 20px;
}

.footer-legal a {
    font-size: 12px;
    color: #333;
    text-decoration: none;
    cursor: pointer;
}

.footer-legal a:hover {
    color: #888;
}

.footer-payments {
    display: flex;
    gap: 8px;
    align-items: center;
}

.footer-payments-label {
    font-size: 11px;
    color: #333;
    margin-right: 4px;
}

.payment-badge {
    background: #111;
    border: 1px solid #1f1f1f;
    border-radius: 5px;
    padding: 4px 8px;
    font-size: 11px;
    color: #555;
    font-weight: bold;
}

</style>


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
                <a class="footer-link" href="#">
                    <span class="footer-link-arrow">›</span> Come ordinare
                </a>
                <a class="footer-link" href="#">
                    <span class="footer-link-arrow">›</span> Spedizioni e consegne
                </a>
                <a class="footer-link" href="#">
                    <span class="footer-link-arrow">›</span> Resi e rimborsi
                </a>
                <a class="footer-link" href="#">
                    <span class="footer-link-arrow">›</span> Garanzia prodotti
                </a>
                <a class="footer-link" href="#">
                    <span class="footer-link-arrow">›</span> FAQ
                </a>
                <a class="footer-link" href="${pageContext.request.contextPath}/contatti">
                    <span class="footer-link-arrow">›</span> Contattaci
                </a>
            </div>
        </div>

        <div class="footer-col">
            <div class="footer-col-title">Newsletter</div>
            <p class="newsletter-desc">
                Iscriviti e ricevi offerte esclusive, novità
                e guide per la tua build.
            </p>
            <div class="newsletter-row">
                <input
                    class="newsletter-input"
                    type="email"
                    placeholder="La tua email..."/>
                <button class="newsletter-btn">Iscriviti</button>
            </div>
        </div>

    </div>

    <!-- MID: garanzie -->
    <div class="footer-mid">

        <div class="footer-mid-item">
            <span class="footer-mid-icon">🚚</span>
            <div class="footer-mid-text">
                <strong>Spedizione gratuita</strong>
                <span>Su ordini superiori a € 99</span>
            </div>
        </div>

        <div class="footer-mid-item">
            <span class="footer-mid-icon">⚡</span>
            <div class="footer-mid-text">
                <strong>Consegna 24h</strong>
                <span>Ordini entro le 14:00</span>
            </div>
        </div>

        <div class="footer-mid-item">
            <span class="footer-mid-icon">🛡</span>
            <div class="footer-mid-text">
                <strong>Garanzia ufficiale</strong>
                <span>Fino a 3 anni sui prodotti</span>
            </div>
        </div>

        <div class="footer-mid-item">
            <span class="footer-mid-icon">↩</span>
            <div class="footer-mid-text">
                <strong>Reso gratuito</strong>
                <span>Entro 30 giorni dall'acquisto</span>
            </div>
        </div>

        <div class="footer-mid-item">
            <span class="footer-mid-icon">🔒</span>
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