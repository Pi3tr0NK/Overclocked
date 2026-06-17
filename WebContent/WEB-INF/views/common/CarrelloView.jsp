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

<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

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

                        <div class="cart-item" id="cart-item-${item.prodotto.idProdotto}">

                            <a href="${pageContext.request.contextPath}/prodotto?id=${item.prodotto.idProdotto}">
                                <img class="cart-item-img"
                                     src="${pageContext.request.contextPath}/${item.prodotto.immagini[0].path}"
                                     alt="${item.prodotto.nome}">
                            </a>

                            <div class="cart-item-info">

                                <div class="cart-item-brand">
                                    ${item.prodotto.marca}
                                </div>

                                <a class="cart-item-link" href="${pageContext.request.contextPath}/prodotto?id=${item.prodotto.idProdotto}">
                                    <div class="cart-item-name">
                                        ${item.prodotto.nome} ${item.prodotto.modello}
                                    </div>
                                </a>

                                <div>
                                    <c:set var="scontato" value="${item.prodotto.prezzo - (item.prodotto.prezzo * item.prodotto.sconto / 100.0)}" />

                                    <c:choose>
                                        <c:when test="${item.prodotto.sconto > 0}">
                                            <div class="old-price">${item.prodotto.prezzo} €</div>
                                            <div class="product-price">${String.format('%.2f', scontato)} €</div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="product-price">${item.prodotto.prezzo} €</div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                            </div>

                            <%-- Controlli quantità e rimozione --%>
                            <div class="cart-item-controls">

                                <button class="qty-btn"
                                        data-action="decrementa"
                                        data-id="${item.prodotto.idProdotto}">
                                    −
                                </button>

                                <span class="qty-value" id="qty-${item.prodotto.idProdotto}">
                                    ${item.quantita}
                                </span>

                                <button class="qty-btn"
                                        data-action="incrementa"
                                        data-id="${item.prodotto.idProdotto}">
                                    +
                                </button>

                                <button class="remove-btn"
                                        data-action="rimuovi"
                                        data-id="${item.prodotto.idProdotto}"
                                        title="Rimuovi">
                                    ✕
                                </button>

                            </div>

                        </div>

                    </c:forEach>

                </div>

                <%-- Riepilogo ordine --%>
                <div class="cart-summary">

                    <h2>Riepilogo</h2>

                    <div class="summary-row">
                        <span>Prodotti</span>
                        <span id="summary-num-prodotti">${numProdotti}</span>
                    </div>

                    <div class="summary-row">
                        <span>Spedizione</span>
                        <span style="color:#28d14a">Gratuita</span>
                    </div>

                    <hr class="summary-divider">

                    <div class="summary-total">
                        <span>Totale</span>
                        <span id="summary-totale">€ <c:out value="${String.format('%.2f',totale)}" /></span>
                    </div>

                    <form action="${pageContext.request.contextPath}/common/pagamento"
                          method="get">
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

<script>
    const ctx = "${pageContext.request.contextPath}";

    document.querySelector(".cart-items")?.addEventListener("click", function (e) {
        const btn = e.target.closest("[data-action]");
        if (!btn) return;

        const action     = btn.dataset.action;
        const idProdotto = btn.dataset.id;
        const url        = ctx + "/Carrello?action=" + action + "&idProdotto=" + idProdotto;

        // Disabilita temporaneamente i bottoni dell'item per evitare click multipli
        const cartItem = document.getElementById("cart-item-" + idProdotto);
        if (cartItem) {
            cartItem.querySelectorAll("button").forEach(b => b.disabled = true);
        }

        fetch(url, {
            headers: { "X-Requested-With": "XMLHttpRequest" }
        })
        .then(function(r) {
            if (!r.ok) throw new Error("Risposta server non valida: " + r.status);
            return r.json();
        })
        .then(function(data) {

            if (data.quantita === 0) {
                // Rimuovi la riga dal DOM
                const itemEl = document.getElementById("cart-item-" + data.idProdotto);
                if (itemEl) itemEl.remove();

                // Se il carrello è ora completamente vuoto ricarica per
                // mostrare la schermata "carrello vuoto"
                if (data.numProdotti === 0) {
                    location.reload();
                    return;
                }
            } else {
                // Aggiorna solo il contatore della quantità
                const qtyEl = document.getElementById("qty-" + data.idProdotto);
                if (qtyEl) qtyEl.textContent = data.quantita;

                // Riabilita i bottoni
                if (cartItem) {
                    cartItem.querySelectorAll("button").forEach(b => b.disabled = false);
                }
            }

            // Aggiorna il riepilogo laterale
            const numEl    = document.getElementById("summary-num-prodotti");
            const totaleEl = document.getElementById("summary-totale");

            if (numEl)    numEl.textContent    = data.numProdotti;
            if (totaleEl) totaleEl.textContent = "€ " + data.totale.toFixed(2).replace(".", ",");
        })
        .catch(function(err) {
            console.error("Errore AJAX carrello:", err);
            // In caso di errore riabilita i bottoni e avvisa l'utente
            if (cartItem) {
                cartItem.querySelectorAll("button").forEach(b => b.disabled = false);
            }
            alert("Si è verificato un errore. Riprova.");
        });
    });
</script>

</body>
</html>
