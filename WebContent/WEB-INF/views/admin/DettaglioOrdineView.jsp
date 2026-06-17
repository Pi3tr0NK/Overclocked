<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">
<title>Dettaglio Ordine</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/dashboard.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/navbar.css">
	
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/dettaglioOrdine.css">

</head>

<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

	<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

	<div class="admin-layout">

		<!-- SIDEBAR -->

		<aside class="sidebar">

			<div class="menu-title">PANORAMICA</div>

			<a href="${pageContext.request.contextPath}/admin/dashboard"
				class="menu-item"> Dashboard Prodotti </a>

			<div class="menu-title">VENDITE</div>

			<a href="${pageContext.request.contextPath}/admin/ordini"
				class="menu-item active"> Ordini </a> <a
				href="${pageContext.request.contextPath}/admin/utenti"
				class="menu-item"> Utenti </a>

		</aside>

		<!-- CONTENUTO -->

		<main class="content">

			<!-- HEADER -->

			<div class="page-header">

				<div>

					<h1>Ordine #${ordine.idOrdine}</h1>

					<span class="stato-badge stato-${ordine.stato}">
						${ordine.stato} </span>

					<p class="order-date">Effettuato il ${ordine.data}</p>

				</div>

				<a href="${pageContext.request.contextPath}/admin/ordini"
					class="new-product-btn"> ← Torna agli ordini </a>

			</div>

			<!-- BLOCCO INFO -->

			<div class="stats-grid">

				<!-- CLIENTE -->

				<div class="stat-card">

					<h3>Cliente</h3>

					<p>
						Nome e cognome: <strong> ${ordine.utente.nome} ${ordine.utente.cognome} </strong>
					</p>

					<p>Email: <strong>${ordine.utente.email}</strong></p>

					<p>Cellulare: <strong>${ordine.utente.cellulare}</strong></p>

				</div>

				<!-- RIEPILOGO -->

				<div class="stat-card">

					<h3>Riepilogo Ordine</h3>

					<p>
						ID Ordine: <strong>#${ordine.idOrdine}</strong>
					</p>

					<p>
						Data: <strong>${ordine.data}</strong>
					</p>

					<p>
						Stato: <strong>${ordine.stato}</strong>
					</p>

				</div>

				<!-- INDIRIZZO -->

				<div class="stat-card">

					<h3>Indirizzo di spedizione</h3>

					<p>Indirizzo: <strong>${ordine.indirizzo.viaNumciv} </strong></p>

					<p> Città: <strong> ${ordine.indirizzo.citta} </strong></p>

					<p>Codice postale: <strong>${ordine.indirizzo.codicePostale}</strong></p>

					<p>Paese: <strong>${ordine.indirizzo.paese}</strong></p>

				</div>

				<!-- PAGAMENTO -->

				<div class="stat-card">

					<h3>Pagamento</h3>

					<p>
						Totale: <strong style="color: #ff7300;"> €
							${ordine.totale} </strong>
					</p>

				</div>

			</div>

			<!-- PRODOTTI -->

			<div class="table-container order-products">

				<h2>Prodotti ordinati</h2>

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

								<td style="width: 70px;"><img class="product-thumb"
									src="${pageContext.request.contextPath}/${d.prodotto.immagini[0].path}">

								</td>

								<td>

									<div class="product-name">${d.prodotto.nome}</div> <br>

									<div class="product-model">${d.prodotto.modello}</div>

								</td>

								<td>${d.prodotto.categoria}</td>

								<td class="price">€ ${d.prezzoUnitario}</td>

								<td>${d.quantita}</td>

							</tr>

						</c:forEach>

					</tbody>

				</table>

			</div>

		</main>

	</div>

</body>
</html>
