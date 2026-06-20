<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Overclocked</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/catalogo.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/pagination.css">
</head>

<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

	<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

	<div class="catalog-layout">
	
		<input type="checkbox" id="filter-toggle" class="filter-toggle-checkbox">
		
		<!-- SIDEBAR -->
		<div class="sidebar">
			<label for="filter-toggle" class="filter-close">✕</label>
			
			<form id="filterForm" action="Catalogo" method="get">
				<input type="hidden" name="cerca" value="${param.cerca}">

				<!-- ===== CATEGORIA ===== -->
				<label>Categoria</label> 
				
				<select name="categoria" onchange="this.form.submit()">
					<option value=""
						${empty param.categoria            ? 'selected' : ''}>Tutte</option>
					<option value="CPU"
						${param.categoria == 'CPU'         ? 'selected' : ''}>CPU</option>
					<option value="GPU"
						${param.categoria == 'GPU'         ? 'selected' : ''}>GPU</option>
					<option value="RAM"
						${param.categoria == 'RAM'         ? 'selected' : ''}>RAM</option>
					<option value="MOBO"
						${param.categoria == 'MOBO'        ? 'selected' : ''}>MOBO</option>
					<option value="PSU"
						${param.categoria == 'PSU'         ? 'selected' : ''}>PSU</option>
					<option value="CASE"
						${param.categoria == 'CASE'        ? 'selected' : ''}>CASE</option>
					<option value="DISSIPATORE"
						${param.categoria == 'DISSIPATORE' ? 'selected' : ''}>Dissipatore</option>
					<option value="STORAGE"
						${param.categoria == 'STORAGE'     ? 'selected' : ''}>Storage</option>
				</select>

				<!-- ===== FILTRI COMUNI ===== -->
				<label>Prezzo max (€)</label> <input type="number" name="prezzo"
					step="0.01" min="0" value="${param.prezzo}"
					onchange="this.form.submit()"> <label>Marca</label> <input
					type="text" name="marca" value="${param.marca}"
					onchange="this.form.submit()">

				<!-- ===== FILTRI SPECIFICI PER CATEGORIA ===== -->
				<c:choose>

					<%-- CPU --%>
					<c:when test="${param.categoria == 'CPU'}">

						<label>Core (min)</label>
						<input type="number" name="core" min="1" placeholder="Es. 6"
							value="${param.core}" onchange="this.form.submit()">

						<label>Frequenza</label>
						<input type="text" name="frequenza" placeholder="Es. 3.6GHz"
							value="${param.frequenza}" onchange="this.form.submit()">

					</c:when>

					<%-- GPU --%>
					<c:when test="${param.categoria == 'GPU'}">

						<label>VRAM</label>
						<input type="text" name="vram" placeholder="Es. 8GB"
							value="${param.vram}" onchange="this.form.submit()">

						<label>PCIe</label>
						<input type="text" name="pcie" placeholder="Es. 4.0"
							value="${param.pcie}" onchange="this.form.submit()">

					</c:when>

					<%-- RAM --%>
					<c:when test="${param.categoria == 'RAM'}">

						<label>Capacità</label>
						<input type="text" name="capacita" placeholder="Es. 16GB"
							value="${param.capacita}" onchange="this.form.submit()">

						<label>Frequenza</label>
						<input type="text" name="frequenza" placeholder="Es. 3200MHz"
							value="${param.frequenza}" onchange="this.form.submit()">

						<label>Tipo</label>
						<input type="text" name="tipo" placeholder="Es. DDR4"
							value="${param.tipo}" onchange="this.form.submit()">

					</c:when>

					<%-- MOBO --%>
					<c:when test="${param.categoria == 'MOBO'}">

						<label>Formato</label>
						<input type="text" name="formato" placeholder="Es. ATX"
							value="${param.formato}" onchange="this.form.submit()">

						<label>Slot RAM (min)</label>
						<input type="number" name="slotram" min="1" placeholder="Es. 4"
							value="${param.slotram}" onchange="this.form.submit()">

						<label>NVMe supportato</label>
						<select name="nvme" onchange="this.form.submit()">
							<option value="" ${empty param.nvme          ? 'selected' : ''}>Si/No</option>
							<option value="true"
								${param.nvme == 'true'      ? 'selected' : ''}>Sì</option>
							<option value="false"
								${param.nvme == 'false'     ? 'selected' : ''}>No</option>
						</select>
					</c:when>

					<%-- PSU --%>
					<c:when test="${param.categoria == 'PSU'}">

						<label>Potenza min (W)</label>
						<input type="number" name="potenza" min="0" placeholder="Es. 650"
							value="${param.potenza}" onchange="this.form.submit()">

						<label>Certificazione</label>
						<input type="text" name="certificazione"
							placeholder="Es. 80+ Gold" value="${param.certificazione}"
							onchange="this.form.submit()">

						<label>Modularità</label>
						<select name="modulare" onchange="this.form.submit()">
							<option value=""
								${empty param.modulare                   ? 'selected' : ''}>Tutte</option>
							<option value="MODULARE"
								${param.modulare == 'MODULARE'           ? 'selected' : ''}>Modulare</option>
							<option value="SEMIMODULARE"
								${param.modulare == 'SEMIMODULARE'       ? 'selected' : ''}>Semimodulare</option>
							<option value="NON_MODULARE"
								${param.modulare == 'NON_MODULARE'       ? 'selected' : ''}>Non
								modulare</option>
						</select>

					</c:when>

					<%-- CASE --%>
					<c:when test="${param.categoria == 'CASE'}">

						<label>Formato</label>
						<input type="text" name="formato" placeholder="Es. Mid Tower"
							value="${param.formato}" onchange="this.form.submit()">

						<label>Colore</label>
						<input type="text" name="colore" placeholder="Es. Nero"
							value="${param.colore}" onchange="this.form.submit()">

					</c:when>

					<%-- DISSIPATORE --%>
					<c:when test="${param.categoria == 'DISSIPATORE'}">

						<label>Tipo</label>
						<select name="tipo" onchange="this.form.submit()">
							<option value="" ${empty param.tipo           ? 'selected' : ''}>Tutti</option>
							<option value="ARIA"
								${param.tipo == 'ARIA'       ? 'selected' : ''}>Aria</option>
							<option value="LIQUIDO"
								${param.tipo == 'LIQUIDO'    ? 'selected' : ''}>Liquido
								(AIO)</option>
						</select>

					</c:when>

					<%-- STORAGE --%>
					<c:when test="${param.categoria == 'STORAGE'}">

						<label>Capacità</label>
						<input type="text" name="capacita" placeholder="Es. 1TB"
							value="${param.capacita}" onchange="this.form.submit()">

						<label>Tipo</label>
						<select name="tipo" onchange="this.form.submit()">
							<option value="" ${empty param.tipo      ? 'selected' : ''}>Tutti</option>
							<option value="SSD" ${param.tipo == 'SSD'   ? 'selected' : ''}>SSD</option>
							<option value="HDD" ${param.tipo == 'HDD'   ? 'selected' : ''}>HDD</option>
						</select>

						<label>Tecnologia</label>
						<select name="tecnologia" onchange="this.form.submit()">
							<option value=""
								${empty param.tecnologia        ? 'selected' : ''}>Tutte</option>
							<option value="NVME"
								${param.tecnologia == 'NVME'    ? 'selected' : ''}>NVMe</option>
							<option value="SATA"
								${param.tecnologia == 'SATA'    ? 'selected' : ''}>SATA</option>
						</select>

					</c:when>

				</c:choose>
		</div>
		<label for="filter-toggle" class="filter-overlay"></label>

		<!-- CONTENT -->
		<div class="content">
			<div class="content-header">

			    <label for="filter-toggle" class="filter-btn">
			        <span>☰</span> Filtri
			    </label>
			
			    <div class="content-header-top">
			        <h2>Prodotti</h2>
			        <div class="content-order">
			            <label>Ordina per:</label>
			            <select name="ordinamento" onchange="this.form.submit()">
			                <option value=""            ${empty param.ordinamento            ? 'selected' : ''}>Rilevanza</option>
			                <option value="novita"    ${param.ordinamento == 'novita'   ? 'selected' : ''}>Novità</option>
			                <option value="prezzoASC"   ${param.ordinamento == 'prezzoASC'  ? 'selected' : ''}>Prezzo: dal più basso</option>
			                <option value="prezzoDESC"  ${param.ordinamento == 'prezzoDESC' ? 'selected' : ''}>Prezzo: dal più alto</option>
			                <option value="nomeASC"     ${param.ordinamento == 'nomeASC'    ? 'selected' : ''}>Nome: A-Z</option>
			                <option value="nomeDESC"    ${param.ordinamento == 'nomeDESC'   ? 'selected' : ''}>Nome: Z-A</option>
			            </select>
			        </div>
			    </div>
			
			</div>

			</form>

			<div class="grid">

				<c:forEach var="p" items="${products}">

					<a class="product-link"
						href="${pageContext.request.contextPath}/prodotto?id=${p.idProdotto}">

						<div class="card">

							<c:if test="${p.sconto > 0}">
								<div class="discount-badge">-${p.sconto}%</div>
							</c:if>

							<img
								src="${pageContext.request.contextPath}/${p.immagini[0].path}">

							<div class="card-content">


								<div class="product-brand">
									<c:out value="${p.marca}" />
								</div>


								<div class="product-name">
									<c:out value="${p.nome} ${p.modello}" />
								</div>

								<c:set var="scontato"
									value="${p.prezzo - (p.prezzo * p.sconto / 100.0)}" />

								<c:choose>

									<c:when test="${p.sconto > 0}">

										<div class="old-price">
											<c:out value="${p.prezzo} €" />
										</div>
										<div class="product-price">
											<c:out value="${String.format('%.2f', scontato)} €" />
										</div>
									</c:when>

									<c:otherwise>

										<div class="product-price">
											<c:out value="${p.prezzo} €" />
										</div>
									</c:otherwise>

								</c:choose>

							</div>

						</div>

					</a>

				</c:forEach>

			</div>

			<c:if test="${empty products}">
				<p>Nessun prodotto trovato</p>
			</c:if>

			<jsp:include page="/WEB-INF/views/components/pagination.jsp" />

		</div>

	</div>

	<jsp:include page="/WEB-INF/views/components/footer.jsp" />
<script src="${pageContext.request.contextPath}/scripts/catalogo.js"></script>
</body>
</html>
