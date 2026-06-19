<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">
<title>Overclocked - Admin</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/dashboard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/dettaglioOrdine.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">

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

					<span class="stato-badge stato-${ordine.stato}"> <c:out
							value="${ordine.stato}" />
					</span>

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
						Nome e cognome: <strong> <c:out
								value="${ordine.utente.nome}" /> <c:out
								value="${ordine.utente.cognome}" />
						</strong>
					</p>

					<p>
						Email: <strong><c:out value="${ordine.utente.email}" /></strong>
					</p>

					<p>
						Cellulare: <strong><c:out
								value="${ordine.utente.cellulare}" /></strong>
					</p>

				</div>

				<!-- RIEPILOGO -->

				<div class="stat-card">

					<h3>Riepilogo Ordine</h3>

					<p>
						ID Ordine: <strong>#<c:out value="${ordine.idOrdine}" /></strong>
					</p>

					<p>
						Data: <strong><c:out value="${ordine.data}" /></strong>
					</p>

					<p>
						Stato: <strong><c:out value="${ordine.stato}" /></strong>
					</p>

				</div>

				<!-- INDIRIZZO -->

				<div class="stat-card">

					<h3>Indirizzo di spedizione</h3>

					<p>
						Indirizzo: <strong><c:out
								value="${ordine.indirizzo.viaNumciv}" /> </strong>
					</p>

					<p>
						Città: <strong> <c:out value="${ordine.indirizzo.citta}" />
						</strong>
					</p>

					<p>
						Codice postale: <strong><c:out
								value="${ordine.indirizzo.codicePostale}" /></strong>
					</p>

					<p>
						Paese: <strong><c:out value="${ordine.indirizzo.paese}" /></strong>
					</p>

				</div>

				<!-- PAGAMENTO -->

				<div class="stat-card">

					<h3>Pagamento</h3>

					<p class="color-red">
						Totale: <strong> € <c:out value="${ordine.totale}" />
						</strong>
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

								<td class="td-immagini"><img class="product-thumb"
									src="${pageContext.request.contextPath}/${d.prodotto.immagini[0].path}">

								</td>

								<td>

									<div class="product-name">
										<c:out value="${d.prodotto.nome}" />
									</div> <br>

									<div class="product-model">
										<c:out value="${d.prodotto.modello}" />
									</div>

								</td>

								<td><c:out value="${d.prodotto.categoria}" /></td>

								<td class="price">€ <c:out value="${d.prezzoUnitario}" /></td>

								<td><c:out value="${d.quantita}" /></td>

							</tr>

						</c:forEach>

					</tbody>

				</table>

			</div>

		</main>

	</div>
	<jsp:include page="/WEB-INF/views/components/footer.jsp" />
</body>
</html>
