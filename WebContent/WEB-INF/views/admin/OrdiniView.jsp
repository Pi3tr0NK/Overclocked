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
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/pagination.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">

<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

</head>



<body>

	<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

	<div class="admin-layout">

		<!-- SIDEBAR -->
		<aside class="sidebar">

			<div class="menu-title">PANORAMICA</div>

			<a href="${pageContext.request.contextPath}/admin/dashboard"
				class="menu-item"> Prodotti </a>

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
					<h1>Gestione Ordini</h1>
				</div>
			</div>

			<!-- STATS -->
			<div class="stats-grid">

				<div class="stat-card">
					<h2><c:out value="${numOrdini}"/></h2>
					<span>Ordini caricati</span>
				</div>

				<div class="stat-card">
					<h2><c:out value="${numOrdiniInAttesa}"/></h2>
					<span>In preparazione</span>
				</div>

				<div class="stat-card">
					<h2><c:out value="${numOrdiniSpediti}"/></h2>
					<span>Spediti</span>
				</div>

				<div class="stat-card">
					<h2><c:out value="${numOrdiniConsegnati}"/></h2>
					<span>Consegnati</span>
				</div>

			</div>

			<div class="filters">

				<form method="get"
					action="${pageContext.request.contextPath}/admin/ordini">

						<input type="text" name="cercaNome" placeholder="Nome..." value="${param.cercaNome}" /> 
						<input type="text" name="cercaCognome" placeholder="Cognome..." value="${param.cercaCognome}" /> 
						<input type="text" name="cercaEmail" placeholder="Email..." value="${param.cercaEmail}" /> 
						
						<input type="date" name="dataInizio" value="${param.dataInizio}"/> 
						<input type="date" name="dataFine" value="${param.dataFine}" /> 
						
						<select name="stato">

						<option value="">Tutti gli stati</option>

						<option value="IN_PREPARAZIONE"
							${param.stato == 'IN_PREPARAZIONE' ? 'selected' : ''}>
							In preparazione</option>

						<option value="SPEDITO"
							${param.stato == 'SPEDITO' ? 'selected' : ''}>Spedito</option>

						<option value="CONSEGNATO"
							${param.stato == 'CONSEGNATO' ? 'selected' : ''}>
							Consegnato</option>

						<option value="RIMBORSATO"
							${param.stato == 'RIMBORSATO' ? 'selected' : ''}>
							Rimborsa</option>

					</select>

					<button type="submit">Cerca</button>

				</form>

			</div>

			<!-- TABELLA -->
			<div class="table-container">

				<table class="product-table">

					<thead>
						<tr>
							<th>#</th>
							<th>Cliente</th>
							<th>Data</th>
							<th>Totale</th>
							<th>Stato</th>
							<th>Azioni</th>
						</tr>
					</thead>

					<tbody>

						<c:forEach var="o" items="${ordini}">
							<tr>

								<td><c:out value="${o.idOrdine}"/></td>

								<td><c:out value="${o.utente.nome}"/> <c:out value="${o.utente.cognome}"/></td>

								<td><c:out value="${o.data}"/></td>

								<td>&euro; <c:out value="${o.totale}"/></td>

								<td><span class="stato-badge stato-${o.stato}">
										<c:out value="${o.stato}"/> </span></td>

								<td>
									<form class="act-button"
										action="${pageContext.request.contextPath}/admin/dettaglioOrdini"
										method="get">
										<input type="hidden" name="action" value="dettaglio" /> <input
											type="hidden" name="idOrdine" value="${o.idOrdine}" />
										<button type="submit">Dettaglio</button>
									</form> <c:if test="${o.stato == 'IN_PREPARAZIONE'}">

										<form class="act-button"action="${pageContext.request.contextPath}/admin/ordini"
											method="get">
											<input type="hidden" name="action" value="cambiaStato" /> <input
												type="hidden" name="idOrdine" value="${o.idOrdine}" /> <input
												type="hidden" name="nuovoStato" value="RIMBORSATO" />
											<button type="submit" class="btn-danger">Rimborsato</button>
										</form>

										<form class="act-button" action="${pageContext.request.contextPath}/admin/ordini"
											method="get">
											<input type="hidden" name="action" value="cambiaStato" /> <input
												type="hidden" name="idOrdine" value="${o.idOrdine}" /> <input
												type="hidden" name="nuovoStato" value="SPEDITO" />
											<button type="submit" class="btn-danger">Spedito</button>
										</form>
									</c:if> <c:if test="${o.stato == 'SPEDITO'}">
										<form class="act-button"action="${pageContext.request.contextPath}/admin/ordini"
											method="get">
											<input type="hidden" name="action" value="cambiaStato" /> <input
												type="hidden" name="idOrdine" value="${o.idOrdine}" /> <input
												type="hidden" name="nuovoStato" value="CONSEGNATO" />
											<button type="submit">Segna consegnato</button>
										</form>
									</c:if>

								</td>

							</tr>
						</c:forEach>

					</tbody>

				</table>

			</div>

			<jsp:include page="/WEB-INF/views/components/pagination.jsp" />

		</main>

	</div>

<jsp:include page="/WEB-INF/views/components/footer.jsp" />
</body>
</html>