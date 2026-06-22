<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Overclocked</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/faq.css">
	
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tema.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/navbar.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/footer.css">
	
	<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

</head>
<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="container">

    <div class="page-title">Domande frequenti</div>
    <p class="page-sub">Tutto quello che devi sapere su ordini, spedizioni, resi e garanzia</p>

    <!-- ===== COME ORDINARE ===== -->
    <div class="faq-section" id="sezione-come-ordinare">
        <div class="faq-section-title">Come ordinare</div>

        <div class="faq-item" id="faq-come-ordinare-1">
            <button class="faq-question" onclick="toggleFaq(this)">
                Come faccio un ordine su Overclocked?
                <span class="faq-arrow">&#9660;</span>
            </button>
            <div class="faq-answer">
                <div class="faq-answer-inner">
                    Seleziona i prodotti che vuoi acquistare e aggiungili al carrello. Una volta pronto, clicca sull'icona del carrello e procedi al checkout inserendo l'indirizzo di spedizione e il metodo di pagamento.
                </div>
            </div>
        </div>

        <div class="faq-item" id="faq-come-ordinare-2">
            <button class="faq-question" onclick="toggleFaq(this)">
                Posso modificare un ordine dopo averlo inviato?
                <span class="faq-arrow">&#9660;</span>
            </button>
            <div class="faq-answer">
                <div class="faq-answer-inner">
                    Una volta confermato, l'ordine non pu&ograve; essere modificato direttamente. Contatta l'assistenza il prima possibile: se l'ordine non &egrave; ancora stato spedito, potremo intervenire manualmente.
                </div>
            </div>
        </div>
    </div>

    <!-- ===== SPEDIZIONI E CONSEGNE ===== -->
    <div class="faq-section" id="sezione-spedizioni">
        <div class="faq-section-title">Spedizioni e consegne</div>

        <div class="faq-item" id="faq-spedizioni-1">
            <button class="faq-question" onclick="toggleFaq(this)">
                Quanto tempo ci vuole per la consegna?
                <span class="faq-arrow">&#9660;</span>
            </button>
            <div class="faq-answer">
                <div class="faq-answer-inner">
                    Gli ordini effettuati entro le 14:00 vengono spediti lo stesso giorno e consegnati generalmente in 24 ore lavorative su tutto il territorio nazionale.
                </div>
            </div>
        </div>

        <div class="faq-item" id="faq-spedizioni-2">
            <button class="faq-question" onclick="toggleFaq(this)">
                La spedizione &egrave; davvero gratuita?
                <span class="faq-arrow">&#9660;</span>
            </button>
            <div class="faq-answer">
                <div class="faq-answer-inner">
                    S&igrave;, la spedizione &egrave; gratuita per tutti gli ordini.
                </div>
            </div>
        </div>

    </div>

    <!-- ===== RESI E RIMBORSI ===== -->
    <div class="faq-section" id="sezione-resi">
        <div class="faq-section-title">Resi e rimborsi</div>

        <div class="faq-item" id="faq-resi-1">
            <button class="faq-question" onclick="toggleFaq(this)">
                Quanto tempo ho per restituire un prodotto?
                <span class="faq-arrow">&#9660;</span>
            </button>
            <div class="faq-answer">
                <div class="faq-answer-inner">
                    Hai 30 giorni dalla data di acquisto per restituire un prodotto, purch&eacute; sia nelle condizioni originali e nella confezione integra.
                </div>
            </div>
        </div>

        <div class="faq-item" id="faq-resi-2">
            <button class="faq-question" onclick="toggleFaq(this)">
                Come richiedo un rimborso?
                <span class="faq-arrow">&#9660;</span>
            </button>
            <div class="faq-answer">
                <div class="faq-answer-inner">
                    Vai nella sezione "I miei ordini", seleziona l'ordine interessato e clicca su "Richiedi reso". Il rimborso viene elaborato entro 5-7 giorni lavorativi dal ricevimento del prodotto reso.
                </div>
            </div>
        </div>
    </div>

    <!-- ===== GARANZIA PRODOTTI ===== -->
    <div class="faq-section" id="sezione-garanzia">
        <div class="faq-section-title">Garanzia prodotti</div>

        <div class="faq-item" id="faq-garanzia-1">
            <button class="faq-question" onclick="toggleFaq(this)">
                Quanto dura la garanzia sui prodotti?
                <span class="faq-arrow">&#9660;</span>
            </button>
            <div class="faq-answer">
                <div class="faq-answer-inner">
                    La garanzia varia in base al produttore: generalmente va da 2 a 3 anni.
                </div>
            </div>
        </div>

        <div class="faq-item" id="faq-garanzia-2">
            <button class="faq-question" onclick="toggleFaq(this)">
                Come faccio valere la garanzia?
                <span class="faq-arrow">&#9660;</span>
            </button>
            <div class="faq-answer">
                <div class="faq-answer-inner">
                    Contatta l'assistenza indicando il numero d'ordine e una descrizione del problema. Ti guideremo nella procedura di assistenza o sostituzione, a seconda del produttore.
                </div>
            </div>
        </div>
    </div>

    <!-- ===== CONTATTI ===== -->
    <div class="faq-section" id="sezione-contatti">
        <div class="faq-section-title">Contattaci</div>

        <div class="faq-item" id="faq-contatti-1">
            <button class="faq-question" onclick="toggleFaq(this)">
                Come posso contattare l'assistenza clienti?
                <span class="faq-arrow">&#9660;</span>
            </button>
            <div class="faq-answer">
                <div class="faq-answer-inner">
                    Puoi scriverci contattandoci direttamente via email all'indirizzo <a href="mailto:supporto@overclocked.it">supporto@overclocked.it</a>. Rispondiamo entro 24 ore lavorative.
                </div>
            </div>
        </div>
    </div>

</div>

<jsp:include page="/WEB-INF/views/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/scripts/faq.js"></script>

</body>
</html>