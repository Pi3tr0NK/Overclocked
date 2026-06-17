<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout — Overclocked</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagamento.css">
</head>

<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="checkout-wrapper">

    <%-- ══════════════ CONFERMA ══════════════ --%>
    <c:if test="${param.conferma eq 'ok'}">
        <div class="conferma-box">
            <div class="icona">&#x2705;</div>
            <h2>Ordine confermato!</h2>
            <p>Grazie per il tuo acquisto. Il tuo ordine è in preparazione.</p>
            <a href="${pageContext.request.contextPath}/" class="btn-home">Torna alla home</a>
        </div>
    </c:if>

    <%-- ══════════════ CHECKOUT ══════════════ --%>
    <c:if test="${empty param.conferma}">

        <div class="checkout-header">
            <h1>&#x1F6D2; Checkout</h1>
            <p>Controlla il tuo ordine e conferma l'acquisto.</p>
        </div>

        <c:if test="${not empty errore}">
            <div class="alert-error">&#x26A0; ${errore}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/common/pagamento" method="post">

            <div class="checkout-layout">

                <%-- ── SINISTRA ──────────────────────────── --%>
                <div>

                    <%-- Dati utente --%>
                    <div class="co-card">
                        <div class="co-title">&#x1F464; Dati acquirente</div>
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

                    <%-- Indirizzo --%>
                    <div class="co-card">
                        <div class="co-title">&#x1F4CD; Indirizzo di spedizione</div>
                        <div class="co-field">
                            <label>Indirizzo</label>
                            <span><c:out value="${utente.indirizzo.viaNumciv}"/></span>
                        </div>
                        <div class="co-field">
                            <label>Città</label>
                            <span><c:out value="${utente.indirizzo.citta} (${utente.indirizzo.provincia})"/></span>
                        </div>
                        <div class="co-field">
                            <label>CAP</label>
                            <span><c:out value="${utente.indirizzo.codicePostale}"/></span>
                        </div>
                        <div class="co-field">
                            <label>Paese</label>
                            <span><c:out value="${utente.indirizzo.paese}"/></span>
                        </div>
                        <input type="hidden" name="idIndirizzo" value="${utente.indirizzo.idIndirizzo}"/>
                    </div>

                    <%-- Prodotti --%>
                    <div class="co-card">
                        <div class="co-title">&#x1F4E6; Prodotti</div>
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
                                    <c:set var="scontato" value="${p.prezzo - (p.prezzo * p.sconto / 100.0)}"/>
                                    <tr>
                                        <td>
                                            <span class="nome-prodotto">${p.nome}</span>
                                            <span class="marca-prodotto">
                                                <c:out value="${p.marca}"/>
                                                <c:if test="${p.sconto > 0}">
                                                    <span class="badge-sconto"><c:out value="-${p.sconto}%"/></span>
                                                </c:if>
                                            </span>
                                        </td>
                                        <td class="col-qta"><c:out value="${item.quantita}"/></td>
                                        <td class="col-prezzo">
                                            <c:choose>
                                                <c:when test="${p.sconto > 0}">
                                                    <div class="old-price"><c:out value="${p.prezzo} €"/></div>
                                                    <div class="product-price"><c:out value="${String.format('%.2f', scontato * item.quantita)} €"/></div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="product-price"><c:out value="${p.prezzo * item.quantita} €"/></div>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                </div><%-- fine col sinistra --%>

                <%-- ── DESTRA — riepilogo fisso ───────────── --%>
                <div class="co-riepilogo">
                    <div class="co-card">
                        <div class="co-title">&#x1F4CB; Riepilogo ordine</div>

                        <c:forEach var="item" items="${prodotti}">
                            <c:set var="p" value="${item.prodotto}"/>
                            <c:set var="scontato" value="${p.prezzo - (p.prezzo * p.sconto / 100.0)}"/>
                            <div class="riepilogo-riga">
                                <span class="riga-nome">
                                    <c:out value="${p.nome}"/>
                                    <c:if test="${item.quantita > 1}">&times;${item.quantita}</c:if>
                                </span>
                                <span class="riga-prezzo">
                                    <c:choose>
                                        <c:when test="${p.sconto > 0}">
                                            <div class="old-price"><c:out value="${p.prezzo} €"/></div>
                                            <div class="product-price"><c:out value="${String.format('%.2f', scontato * item.quantita)} €"/></div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="product-price"><c:out value="${p.prezzo * item.quantita} €"/></div>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </c:forEach>

                        <div class="riepilogo-totale">
                            <span>Totale</span>
                            <span><c:out value="${String.format('%.2f', totale)} €"/></span>
                        </div>

                        <button type="submit" class="btn-conferma">
                            &#x2714; Conferma ordine
                        </button>
                        <a href="${pageContext.request.contextPath}/Carrello"
                           class="btn-torna">&#8592; Torna al carrello</a>
                    </div>
                </div>

            </div><%-- fine checkout-layout --%>

        </form>

    </c:if>

</div><%-- fine checkout-wrapper --%>

<jsp:include page="/WEB-INF/views/components/footer.jsp" />
</body>
</html>
