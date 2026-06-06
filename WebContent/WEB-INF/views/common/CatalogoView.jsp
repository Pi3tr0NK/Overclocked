<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Catalogo</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/catalogo.css">
</head>

<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="catalog-layout">

    <!-- SIDEBAR -->
    <div class="sidebar">

        <form id="filterForm" action="Catalogo" method="get">
			<input type="hidden" name="cerca" value="${param.cerca}">
			
            <!-- ===== CATEGORIA ===== -->
            <label>Categoria</label>
            <select name="categoria" onchange="this.form.submit()">
                <option value=""            ${empty param.categoria            ? 'selected' : ''}>Tutte</option>
                <option value="CPU"         ${param.categoria == 'CPU'         ? 'selected' : ''}>CPU</option>
                <option value="GPU"         ${param.categoria == 'GPU'         ? 'selected' : ''}>GPU</option>
                <option value="RAM"         ${param.categoria == 'RAM'         ? 'selected' : ''}>RAM</option>
                <option value="MOBO"        ${param.categoria == 'MOBO'        ? 'selected' : ''}>MOBO</option>
                <option value="PSU"         ${param.categoria == 'PSU'         ? 'selected' : ''}>PSU</option>
                <option value="CASE"        ${param.categoria == 'CASE'        ? 'selected' : ''}>CASE</option>
                <option value="DISSIPATORE" ${param.categoria == 'DISSIPATORE' ? 'selected' : ''}>Dissipatore</option>
                <option value="STORAGE"     ${param.categoria == 'STORAGE'     ? 'selected' : ''}>Storage</option>
            </select>

            <!-- ===== FILTRI COMUNI ===== -->
            <label>Prezzo max (€)</label>
            <input type="number"
                   name="prezzo"
                   step="0.01"
                   min="0"
                   value="${param.prezzo}"
                   onchange="this.form.submit()">

            <label>Marca</label>
            <input type="text"
                   name="marca"
                   value="${param.marca}"
                   onchange="this.form.submit()">

            <!-- ===== FILTRI SPECIFICI PER CATEGORIA ===== -->
            <c:choose>

                <%-- CPU: core (int), frequenza (varchar) --%>
                <c:when test="${param.categoria == 'CPU'}">

                    <label>Core (min)</label>
                    <input type="number"
                           name="core"
                           min="1"
                           placeholder="Es. 6"
                           value="${param.core}"
                           onchange="this.form.submit()">

                    <label>Frequenza</label>
                    <input type="text"
                           name="frequenza"
                           placeholder="Es. 3.6GHz"
                           value="${param.frequenza}"
                           onchange="this.form.submit()">

                </c:when>

                <%-- GPU: vram (varchar), pcie (varchar) --%>
                <c:when test="${param.categoria == 'GPU'}">

                    <label>VRAM</label>
                    <input type="text"
                           name="vram"
                           placeholder="Es. 8GB"
                           value="${param.vram}"
                           onchange="this.form.submit()">

                    <label>PCIe</label>
                    <input type="text"
                           name="pcie"
                           placeholder="Es. 4.0"
                           value="${param.pcie}"
                           onchange="this.form.submit()">

                </c:when>

                <%-- RAM: capacita (varchar), frequenza (varchar), tipo (varchar) --%>
                <c:when test="${param.categoria == 'RAM'}">

                    <label>Capacità</label>
                    <input type="text"
                           name="capacita"
                           placeholder="Es. 16GB"
                           value="${param.capacita}"
                           onchange="this.form.submit()">

                    <label>Frequenza</label>
                    <input type="text"
                           name="frequenza"
                           placeholder="Es. 3200MHz"
                           value="${param.frequenza}"
                           onchange="this.form.submit()">

                    <label>Tipo</label>
                    <input type="text"
                           name="tipo"
                           placeholder="Es. DDR4"
                           value="${param.tipo}"
                           onchange="this.form.submit()">

                </c:when>

                <%-- MOBO: formato (varchar), nvme (boolean), slotram (int) --%>
                <c:when test="${param.categoria == 'MOBO'}">

                    <label>Formato</label>
                    <input type="text"
                           name="formato"
                           placeholder="Es. ATX"
                           value="${param.formato}"
                           onchange="this.form.submit()">

                    <label>Slot RAM (min)</label>
                    <input type="number"
                           name="slotram"
                           min="1"
                           placeholder="Es. 4"
                           value="${param.slotram}"
                           onchange="this.form.submit()">

                    <label>NVMe supportato</label>
                    <select name="nvme" onchange="this.form.submit()">
                        <option value=""     ${empty param.nvme          ? 'selected' : ''}>Si/No</option>
                        <option value="true" ${param.nvme == 'true'      ? 'selected' : ''}>Sì</option>
                        <option value="false"${param.nvme == 'false'     ? 'selected' : ''}>No</option>
                    </select>
                </c:when>

                <%-- PSU: potenza (int), certificazione (varchar), modulare (enum) --%>
                <c:when test="${param.categoria == 'PSU'}">

                    <label>Potenza min (W)</label>
                    <input type="number"
                           name="potenza"
                           min="0"
                           placeholder="Es. 650"
                           value="${param.potenza}"
                           onchange="this.form.submit()">

                    <label>Certificazione</label>
                    <input type="text"
                           name="certificazione"
                           placeholder="Es. 80+ Gold"
                           value="${param.certificazione}"
                           onchange="this.form.submit()">

                    <label>Modularità</label>
                    <select name="modulare" onchange="this.form.submit()">
                        <option value=""              ${empty param.modulare                   ? 'selected' : ''}>Tutte</option>
                        <option value="MODULARE"      ${param.modulare == 'MODULARE'           ? 'selected' : ''}>Modulare</option>
                        <option value="SEMIMODULARE"  ${param.modulare == 'SEMIMODULARE'       ? 'selected' : ''}>Semimodulare</option>
                        <option value="NON_MODULARE"  ${param.modulare == 'NON_MODULARE'       ? 'selected' : ''}>Non modulare</option>
                    </select>

                </c:when>

                <%-- CASE: formato (varchar), colore (varchar) --%>
                <c:when test="${param.categoria == 'CASE'}">

                    <label>Formato</label>
                    <input type="text"
                           name="formato"
                           placeholder="Es. Mid Tower"
                           value="${param.formato}"
                           onchange="this.form.submit()">

                    <label>Colore</label>
                    <input type="text"
                           name="colore"
                           placeholder="Es. Nero"
                           value="${param.colore}"
                           onchange="this.form.submit()">

                </c:when>

                <%-- DISSIPATORE: tipo (enum ARIA/LIQUIDO) --%>
                <c:when test="${param.categoria == 'DISSIPATORE'}">

                    <label>Tipo</label>
                    <select name="tipo" onchange="this.form.submit()">
                        <option value=""       ${empty param.tipo           ? 'selected' : ''}>Tutti</option>
                        <option value="ARIA"   ${param.tipo == 'ARIA'       ? 'selected' : ''}>Aria</option>
                        <option value="LIQUIDO"${param.tipo == 'LIQUIDO'    ? 'selected' : ''}>Liquido (AIO)</option>
                    </select>

                </c:when>

                <%-- STORAGE: capacita (varchar), tipo (enum SSD/HDD), tecnologia (enum SATA/NVME) --%>
                <c:when test="${param.categoria == 'STORAGE'}">

                    <label>Capacità</label>
                    <input type="text"
                           name="capacita"
                           placeholder="Es. 1TB"
                           value="${param.capacita}"
                           onchange="this.form.submit()">

                    <label>Tipo</label>
                    <select name="tipo" onchange="this.form.submit()">
                        <option value=""   ${empty param.tipo      ? 'selected' : ''}>Tutti</option>
                        <option value="SSD"${param.tipo == 'SSD'   ? 'selected' : ''}>SSD</option>
                        <option value="HDD"${param.tipo == 'HDD'   ? 'selected' : ''}>HDD</option>
                    </select>

                    <label>Tecnologia</label>
                    <select name="tecnologia" onchange="this.form.submit()">
                        <option value=""     ${empty param.tecnologia        ? 'selected' : ''}>Tutte</option>
                        <option value="NVME" ${param.tecnologia == 'NVME'    ? 'selected' : ''}>NVMe</option>
                        <option value="SATA" ${param.tecnologia == 'SATA'    ? 'selected' : ''}>SATA</option>
                    </select>

                </c:when>

            </c:choose>

        </form>

    </div>

    <!-- CONTENT -->
    <div class="content">

        <h2>Prodotti</h2>

        <div class="grid">

            <c:forEach var="p" items="${products}">

                <a class="product-link"
                   href="${pageContext.request.contextPath}/prodotto?id=${p.idProdotto}">

                    <div class="card">

                        <c:if test="${p.sconto > 0}">
                            <div class="discount-badge">-${p.sconto}%</div>
                        </c:if>

                        <img src="${pageContext.request.contextPath}/img/prodotti/default.png"
                             alt="${p.nome} ${p.modello}">

                        <div class="card-content">

                            <div class="product-brand">${p.marca}</div>

                            <div class="product-name">
                                ${p.nome} ${p.modello}
                            </div>

                            <c:set var="scontato"
                                   value="${p.prezzo - (p.prezzo * p.sconto / 100.0)}" />

                            <c:choose>

                                <c:when test="${p.sconto > 0}">
                                    <div class="old-price">${p.prezzo} €</div>
                                    <div class="product-price">${String.format('%.2f', scontato)} €</div>
                                </c:when>

                                <c:otherwise>
                                    <div class="product-price">${p.prezzo} €</div>
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

    </div>

</div>

<jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>
