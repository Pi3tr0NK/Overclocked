<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Overclocked</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tema.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/navbar.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/footer.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/profilo.css">
	
<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />
</head>



<body>

	<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

	<div class="profilo-wrapper">

		<h1 class="profilo-titolo">Il mio account</h1>

		<div class="profilo-layout">

			<!-- NAVBAR SINISTRA -->
			<nav class="profilo-nav">
				<a
					href="${pageContext.request.contextPath}/common/profilo?view=ordini"
					class="profilo-nav-link active">Ordini e fatture</a> <a
					href="${pageContext.request.contextPath}/common/profilo?view=resi"
					class="profilo-nav-link">Resi</a> <a
					href="${pageContext.request.contextPath}/common/profilo?view=dati"
					class="profilo-nav-link">I miei dati</a> <a
					href="${pageContext.request.contextPath}/common/logout"
					class="profilo-nav-link logout">Logout &#x2192;</a>
			</nav>

			<!-- DATI A DESTRA -->
			<div class="profilo-content">

				<div class="profilo-card-title">Ordini e fatture</div>

				<c:if test="${not empty successo}">
					<div class="profilo-alert success">${successo}</div>
				</c:if>
				<c:if test="${not empty errore}">
					<div class="profilo-alert error">${errore}</div>
				</c:if>

				<c:choose>
					<c:when test="${empty ordini}">
						<p class="profilo-empty">Nessun ordine effettuato.</p>
					</c:when>
					<c:otherwise>
						<c:forEach var="ordine" items="${ordini}">
							<div class="ordine-card">

								<div class="ordine-header">
									<div>
										<span class="ordine-label">Ordine #${ordine.idOrdine}</span> <span
											class="ordine-data">${ordine.data}</span>
									</div>
									<span class="ordine-stato stato-${ordine.stato}">${ordine.stato}</span>
								</div>

								<div class="ordine-body">
									<div class="ordine-field">
										<span class="ordine-field-label">Totale</span> <span
											class="ordine-field-value">${String.format('%.2f', ordine.totale)}
											€</span>
									</div>
									<div class="ordine-field">
										<span class="ordine-field-label">Indirizzo</span> <span
											class="ordine-field-value">
											 <c:out value="${ordine.indirizzo.viaNumciv}, ${ordine.indirizzo.citta} (${ordine.indirizzo.provincia})"/>
										 </span>
									</div>
									<div class="ordine-field">
									    <span class="ordine-field-label">Carta</span>
									    <span class="ordine-field-value">•••• ${ordine.pagamento}</span>
									</div>
								</div>

								<div id="rimborso" class="ordine-footer">


									<a href="${pageContext.request.contextPath}/common/profilo?action=fattura&idOrdine=${ordine.idOrdine}"
										class="btn-fattura"> &#x1F4C4; Scarica fattura </a>

									<c:if test="${ordine.stato != 'RIMBORSATO'}">
										<form
											action="${pageContext.request.contextPath}/common/profilo"
											method="post">
											<input type="hidden" name="action" value="rimborsa" /> <input
												type="hidden" name="idOrdine" value="${ordine.idOrdine}" />
											<button type="submit" class="btn-rimborsa">
												Richiedi rimborso</button>
										</form>
									</c:if>

								</div>

							</div>
						</c:forEach>
					</c:otherwise>
				</c:choose>

			</div>

		</div>

	</div>

	<jsp:include page="/WEB-INF/views/components/footer.jsp" />
</body>
</html>
