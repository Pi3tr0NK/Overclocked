<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Carrello - Overclocked</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/carrello.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="container">

    <h1>Il tuo carrello</h1>

    <c:choose>

        <%-- ── CARRELLO VUOTO ── --%>
        <c:when test="${empty prodotti}">
            <div class="cart-empty">
                <h2>Il carrello è vuoto</h2>
                <p>Aggiungi qualche componente per iniziare la tua build.</p>
                <a href="${pageContext.request.contextPath}/Catalogo">
                    Vai al catalogo
                </a>
            </div>
        </c:when>

        <%-- ── CARRELLO CON PRODOTTI ── --%>
        <c:otherwise>
            <div class="cart-page">

                <%-- Lista prodotti --%>
                <div class="cart-items">

                    <c:forEach var="item" items="${prodotti}">

                        <c:set var="prezzo"
                               value="${item.prodotto.prezzo}" />
                        <c:set var="scontato"
                               value="${item.prodotto.prezzo - (item.prodotto.prezzo * item.prodotto.sconto / 100)}" />

                        <div class="cart-item">

                            <img class="cart-item-img"
                                 src="${pageContext.request.contextPath}/${item.prodotto.immagini[0].path}"
                                 alt="${item.prodotto.nome}">

                            <div class="cart-item-info">

                                <div class="cart-item-brand">
                                    ${item.prodotto.marca}
                                </div>

                                <div class="cart-item-name">
                                    ${item.prodotto.nome} ${item.prodotto.modello}
                                </div>

                                <div>
                                    <c:if test="${item.prodotto.sconto > 0}">
                                        <span class="cart-item-old-price">
                                            € <c:out value="${prezzo}" />
                                        </span>
                                    </c:if>
                                    <span class="cart-item-price">
                                        € <c:out value="${scontato}" />
                                    </span>
                                </div>

                            </div>

                            <%-- Controlli quantità e rimozione --%>
                            <div class="cart-item-controls">

                                <form action="${pageContext.request.contextPath}/cart"
                                      method="post">
                                    <input type="hidden" name="action"    value="decrementa">
                                    <input type="hidden" name="idProdotto" value="${item.prodotto.idProdotto}">
                                    <button class="qty-btn" type="submit">−</button>
                                </form>

                                <span class="qty-value">
                                    ${item.quantita}
                                </span>

                                <form action="${pageContext.request.contextPath}/cart"
                                      method="post">
                                    <input type="hidden" name="action"    value="incrementa">
                                    <input type="hidden" name="idProdotto" value="${item.prodotto.idProdotto}">
                                    <button class="qty-btn" type="submit">+</button>
                                </form>

                                <form action="${pageContext.request.contextPath}/cart"
                                      method="post">
                                    <input type="hidden" name="action"    value="rimuovi">
                                    <input type="hidden" name="idProdotto" value="${item.prodotto.idProdotto}">
                                    <button class="remove-btn" type="submit"
                                            title="Rimuovi">✕</button>
                                </form>

                            </div>

                        </div>

                    </c:forEach>

                </div>

                <%-- Riepilogo ordine --%>
                <div class="cart-summary">

                    <h2>Riepilogo</h2>

                    <div class="summary-row">
                        <span>Prodotti</span>
                        <span>${numProdotti}</span>
                    </div>

                    <div class="summary-row">
                        <span>Spedizione</span>
                        <span style="color:#28d14a">Gratuita</span>
                    </div>

                    <hr class="summary-divider">

                    <div class="summary-total">
                        <span>Totale</span>
                        <span>€ <c:out value="${totale}" /></span>
                    </div>

                    <form action="${pageContext.request.contextPath}/checkout"
                          method="post">
                        <button class="checkout-btn" type="submit">
                            Procedi all'acquisto
                        </button>
                    </form>

                    <a class="continue-link"
                       href="${pageContext.request.contextPath}/Catalogo">
                        ← Continua lo shopping
                    </a>

                </div>

            </div>

        </c:otherwise>

    </c:choose>

</div>

<jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>