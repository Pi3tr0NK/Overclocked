<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Overclocked</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/pagamento.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/tema.css">
</head>

<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="checkout-wrapper">

    <!-- ========================= -->
    <!-- CONFERMA ORDINE -->
    <!-- ========================= -->
    <c:if test="${param.conferma eq 'ok'}">
        <div class="conferma-box">
            <div class="icona">✔</div>
            <h2>Ordine confermato!</h2>
            <p>Grazie per il tuo acquisto. Il tuo ordine è in preparazione.</p>
            <a href="${pageContext.request.contextPath}/" class="btn-home">
                Torna alla home
            </a>
        </div>
    </c:if>

    <!-- ========================= -->
    <!-- CHECKOUT -->
    <!-- ========================= -->
    <c:if test="${empty param.conferma}">

        <div class="checkout-header">
            <h1>🛒 Checkout</h1>
            <p>Controlla il tuo ordine e conferma l'acquisto.</p>
        </div>

        <c:if test="${not empty errore}">
            <div class="alert-error">⚠ ${errore}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/common/pagamento" method="post" novalidate>

            <div class="checkout-layout">

                <!-- ========================= -->
                <!-- SINISTRA -->
                <!-- ========================= -->
                <div>

                    <!-- ===== UTENTE ===== -->
                    <div class="co-card">
                        <div class="co-title">👤 Dati acquirente</div>

                        <div class="co-field">
                            <label>Nome e cognome</label>
                            <span><c:out value="${utente.nome} ${utente.cognome}"/></span>
                        </div>

                        <div class="co-field">
                            <label>Email</label>
                            <span><c:out value="${utente.email}"/></span>
                        </div>

                        <div class="co-field">
                            <label>Cellulare</label>
                            <span><c:out value="${utente.cellulare}"/></span>
                        </div>
                    </div>

                    <!-- ===== INDIRIZZO EDITABILE ===== -->
                    <div class="co-card">
                        <div class="co-title">📍 Indirizzo di spedizione</div>

                        <div class="co-field">
                            <label>Via e numero civico</label>
                            <input type="text"
                                   name="via"
                                   value="${utente.indirizzo.viaNumciv}"
                                   placeholder="Es. Via Roma, 12"
                                   required>
                        </div>

                        <div class="co-field-row">
                            <div class="co-field">
                                <label>Città</label>
                                <input type="text"
                                       name="citta"
                                       value="${utente.indirizzo.citta}"
                                       placeholder="Es. Campobasso"
                                       required>
                            </div>

                            <div class="co-field">
                                <label>Provincia</label>
                                <input type="text"
                                       name="provincia"
                                       value="${utente.indirizzo.provincia}"
                                       maxlength="2"
                                       placeholder="Es. CB"
                                       required>
                            </div>

                            <div class="co-field">
                                <label>CAP</label>
                                <input type="text"
                                       name="cap"
                                       value="${utente.indirizzo.codicePostale}"
                                       maxlength="5"
                                       placeholder="86100"
                                       required>
                            </div>
                            
                            <div class="co-field">
                                <label>Paese</label>
                                <input type="text"
                                       name="paese"
                                       value="${utente.indirizzo.paese}"
                                       maxlength="5"
                                       placeholder="Italia"
                                       required>
                            </div>
                        </div>
                    </div>

                    <!-- ===== PAGAMENTO ===== -->
                    <div class="co-card co-card-pagamento">
                        <div class="co-title">
                            <span>💳 Pagamento</span>
                            <span class="icone-carte">VISA · MASTERCARD</span>
                        </div>

                        <div class="co-field">
                            <label>Numero carta</label>
                            <input type="text"
                                   name="numeroCarta"
                                   maxlength="16"
                                   pattern="[0-9]{16}"
                                   placeholder="4023456712246699"
                                   required>
                        </div>

                        <div class="co-field">
                            <label>Intestatario</label>
                            <input type="text"
                                   name="intestatario"
                                   placeholder="Nome come sulla carta"
                                   required>
                        </div>

                        <div class="co-field-row">
                            <div class="co-field">
                                <label>Mese</label>
                                <input type="number"
                                       name="mese"
                                       min="1"
                                       max="12"
                                       placeholder="MM"
                                       required>
                            </div>

                            <div class="co-field">
                                <label>Anno</label>
                                <input type="number"
                                       name="anno"
                                       min="2025"
                                       placeholder="AAAA"
                                       required>
                            </div>

                            <div class="co-field">
                                <label>CVV</label>
                                <input type="password"
                                       name="cvv"
                                       maxlength="3"
                                       pattern="[0-9]{3}"
                                       placeholder="•••"
                                       required>
                            </div>
                        </div>

                        <div class="co-field-hint">
                            🔒 I dati di pagamento sono trasmessi in modo sicuro.
                        </div>
                    </div>

                    <!-- ===== PRODOTTI ===== -->
                    <div class="co-card">
                        <div class="co-title">📦 Prodotti</div>

                        <table class="co-table">
                            <thead>
                                <tr>
                                    <th>Prodotto</th>
                                    <th class="col-qta">Qtà</th>
                                    <th class="col-prezzo">Prezzo</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="item" items="${prodotti}">
                                    <c:set var="p" value="${item.prodotto}"/>
                                    <c:set var="scontato"
                                           value="${p.prezzo - (p.prezzo * p.sconto / 100.0)}"/>

                                    <tr>
                                        <td>
                                            <span class="nome-prodotto">
                                                <c:out value="${p.nome}"/>
                                            </span>
                                            <span class="marca-prodotto">
                                                <c:out value="${p.marca}"/>
                                                <c:if test="${p.sconto > 0}">
                                                    <span class="badge-sconto">
                                                        -${p.sconto}%
                                                    </span>
                                                </c:if>
                                            </span>
                                        </td>

                                        <td class="col-qta">${item.quantita}</td>

                                        <td class="col-prezzo">
                                            <c:choose>
                                                <c:when test="${p.sconto > 0}">
                                                    <div class="old-price">
                                                        ${p.prezzo} €
                                                    </div>
                                                    <div class="product-price">
                                                        ${String.format('%.2f', scontato * item.quantita)} €
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="product-price">
                                                        ${String.format('%.2f', p.prezzo * item.quantita)} €
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                </div>

                <!-- ========================= -->
                <!-- DESTRA RIEPILOGO -->
                <!-- ========================= -->
                <div class="co-riepilogo">

                    <div class="co-card">
                        <div class="co-title">📋 Riepilogo ordine</div>

                        <c:forEach var="item" items="${prodotti}">
                            <c:set var="p" value="${item.prodotto}"/>
                            <c:set var="scontato"
                                   value="${p.prezzo - (p.prezzo * p.sconto / 100.0)}"/>

                            <div class="riepilogo-riga">
                                <span class="riga-nome">
                                    <c:out value="${p.nome}"/>
                                    <c:if test="${item.quantita > 1}">
                                        ×${item.quantita}
                                    </c:if>
                                </span>

                                <span class="riga-prezzo">
                                    <c:choose>
                                        <c:when test="${p.sconto > 0}">
                                            ${String.format('%.2f', scontato * item.quantita)} €
                                        </c:when>
                                        <c:otherwise>
                                            ${String.format('%.2f', p.prezzo * item.quantita)} €
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </c:forEach>

                        <div class="riepilogo-totale">
                            <span>Totale</span>
                            <span>${String.format('%.2f', totale)} €</span>
                        </div>

                        <button type="submit" class="btn-conferma">
                            ✔ Conferma ordine
                        </button>

                        <a href="${pageContext.request.contextPath}/Carrello"
                           class="btn-torna">
                            ← Torna al carrello
                        </a>

                    </div>

                </div>

            </div>

        </form>

    </c:if>

</div>


<jsp:include page="/WEB-INF/views/components/footer.jsp" />


<script src="${pageContext.request.contextPath}/scripts/pagamentoCheck.js"></script>

</body>
</html>
