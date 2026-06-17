<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">
<title>Dettaglio Ordine</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">

</head>

<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp"/>

<div class="admin-layout">

```
<!-- SIDEBAR -->

<aside class="sidebar">

    <div class="menu-title">
        PANORAMICA
    </div>

    <a href="${pageContext.request.contextPath}/admin/dashboard"
       class="menu-item">
        Dashboard Prodotti
    </a>

    <div class="menu-title">
        VENDITE
    </div>

    <a href="${pageContext.request.contextPath}/admin/ordini"
       class="menu-item active">
        Ordini
    </a>

    <a href="${pageContext.request.contextPath}/admin/utenti"
       class="menu-item">
        Utenti
    </a>

</aside>

<!-- CONTENUTO -->

<main class="content">

    <!-- HEADER -->

    <div class="page-header">

        <div>

            <h1>
                Ordine #${ordine.idOrdine}
            </h1>

            <span class="stato-badge stato-${ordine.stato}">
                ${ordine.stato}
            </span>

            <p style="margin-top:10px;color:#888;">
                Effettuato il ${ordine.data}
            </p>

        </div>

        <a href="${pageContext.request.contextPath}/admin/ordini"
           class="new-product-btn">
            ← Torna agli ordini
        </a>

    </div>

    <!-- BLOCCO INFO -->

    <div class="stats-grid">

        <!-- CLIENTE -->

        <div class="stat-card">

            <h3>Cliente</h3>

            <p>
                <strong>
                    ${ordine.utente.nome}
                    ${ordine.utente.cognome}
                </strong>
            </p>

            <p>${ordine.utente.email}</p>

            <p>${ordine.utente.cellulare}</p>

        </div>

        <!-- RIEPILOGO -->

        <div class="stat-card">

            <h3>Riepilogo Ordine</h3>

            <p>ID Ordine: <strong>#${ordine.idOrdine}</strong></p>

            <p>Data: <strong>${ordine.data}</strong></p>

            <p>Stato: <strong>${ordine.stato}</strong></p>

        </div>

        <!-- INDIRIZZO -->

        <div class="stat-card">

            <h3>Indirizzo di spedizione</h3>

            <p>${ordine.indirizzo.viaNumciv}</p>

            <p>${ordine.indirizzo.citta}</p>

            <p>${ordine.indirizzo.codicePostale}</p>

            <p>${ordine.indirizzo.paese}</p>

        </div>

        <!-- PAGAMENTO -->

        <div class="stat-card">

            <h3>Pagamento</h3>

            <p>
                Totale:
                <strong style="color:#ff7300;">
                    € ${ordine.totale}
                </strong>
            </p>

        </div>

    </div>

    <!-- PRODOTTI -->

    <div class="table-container" style="margin-top:25px;">

        <h2 style="margin-bottom:20px;">
               Prodotti ordinati
        </h2>

        <table class="product-table">

            <thead>

                <tr>

                    <th></th>
                    <th>Prodotto</th>
                    <th>Categoria</th>
                    <th>Prezzo Unit.</th>
                    <th>Quantità</th>

                </tr>

            </thead>

            <tbody>

                <c:forEach var="d" items="${dettagli}">

                    <tr>

                        <td style="width:70px;">

                            <img
                                src="${pageContext.request.contextPath}/${d.prodotto.immagini[0].path}"
                                style="
                                    width:50px;
                                    height:50px;
                                    object-fit:contain;
                                    border-radius:6px;
                                    border:1px solid #222;
                                ">

                        </td>

                        <td>

                            <strong>
                                ${d.prodotto.nome}
                            </strong>

                            <br>

                            <span style="color:#777;">
                                ${d.prodotto.modello}
                            </span>

                        </td>

                        <td>
                            ${d.prodotto.categoria}
                        </td>

                        <td>
                            € ${d.prezzoUnitario}
                        </td>

                        <td>
                            ${d.quantita}
                        </td>

                    </tr>

                </c:forEach>

            </tbody>

        </table>

    </div>

</main>
```

</div>

</body>
</html>
