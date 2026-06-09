<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Overclocked</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">

</head>

<body>

<!-- NAVBAR -->
<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="container">
	
	<h2>Categorie</h2>
	
    <!-- GRID CATEGORIE -->
    <div class="category-grid">

        <a class="category-card" href="${pageContext.request.contextPath}/Catalogo?categoria=CPU">
            <img src="${pageContext.request.contextPath}/img/cpu.png" alt="CPU">
            <h3>CPU</h3>
            <p>${countByCategory["CPU"] != null ? countByCategory["CPU"] : 0} prodotti</p>
        </a>

        <a class="category-card" href="${pageContext.request.contextPath}/Catalogo?categoria=GPU">
            <img src="${pageContext.request.contextPath}/img/gpu.png" alt="GPU">
            <h3>GPU</h3>
            <p>${countByCategory["GPU"] != null ? countByCategory["GPU"] : 0} prodotti</p>
        </a>

        <a class="category-card" href="${pageContext.request.contextPath}/Catalogo?categoria=RAM">
            <img src="${pageContext.request.contextPath}/img/ram.png" alt="RAM">
            <h3>RAM</h3>
            <p>${countByCategory["RAM"] != null ? countByCategory["RAM"] : 0} prodotti</p>
        </a>

        <a class="category-card" href="${pageContext.request.contextPath}/Catalogo?categoria=PSU">
            <img src="${pageContext.request.contextPath}/img/psu.png" alt="PSU">
            <h3>PSU</h3>
            <p>${countByCategory["PSU"] != null ? countByCategory["PSU"] : 0} prodotti</p>
        </a>

        <a class="category-card" href="${pageContext.request.contextPath}/Catalogo?categoria=CASE">
            <img src="${pageContext.request.contextPath}/img/case.png" alt="CASE">
            <h3>CASE</h3>
            <p>${countByCategory["CASE"] != null ? countByCategory["CASE"] : 0} prodotti</p>
        </a>

        <a class="category-card" href="${pageContext.request.contextPath}/Catalogo?categoria=STORAGE">
            <img src="${pageContext.request.contextPath}/img/memoria.png" alt="STORAGE">
            <h3>STORAGE</h3>
            <p>${countByCategory["STORAGE"] != null ? countByCategory["STORAGE"] : 0} prodotti</p>
        </a>

        <a class="category-card" href="${pageContext.request.contextPath}/Catalogo?categoria=MOBO">
            <img src="${pageContext.request.contextPath}/img/mobo.png" alt="MOBO">
            <h3>MOBO</h3>
            <p>${countByCategory["MOBO"] != null ? countByCategory["MOBO"] : 0} prodotti</p>
        </a>

        <a class="category-card" href="${pageContext.request.contextPath}/Catalogo?categoria=DISSIPATORE">
            <img src="${pageContext.request.contextPath}/img/dissipatore.png" alt="DISSIPATORE">
            <h3>DISSIPATORE</h3>
            <p>${countByCategory["DISSIPATORE"] != null ? countByCategory["DISSIPATORE"] : 0} prodotti</p>
        </a>

    </div>

    <!-- CONFIGURATORE GUIDATO -->
    <h2>Pc Builder</h2>
    <div class="configuratore">

        <div class="configuratore-testo">

            <p class="configuratore-label">Configuratore Guidato</p>

            <p class="configuratore-titolo">Assembla il PC perfetto,<br>componente per componente</p>

            <p class="configuratore-desc">
                Scegli i pezzi compatibili tra loro con il nostro configuratore.
                Ti guidiamo noi &mdash; nessuna incompatibilità, nessun errore.
            </p>

            <a href="${pageContext.request.contextPath}/pcBuilder" class="configuratore-btn">
                Inizia la build
            </a>

        </div>

        <div class="configuratore-slot">
		    <div class="slot-item">CPU</div>
		    <div class="slot-item">Scheda madre</div>
		    <div class="slot-item">RAM</div>
		    <div class="slot-item">GPU</div>
		    <div class="slot-item">PSU</div>
		    <div class="slot-item">Case</div>
		    <div class="slot-item">Storage</div>
		    <div class="slot-item">Dissipatore</div>
		</div>

    </div>

    <!-- NOVITA -->
    <h2><a href="${pageContext.request.contextPath}/admin/aggiungiProdotto">Novità</a></h2>

    <div class="grid">

        <c:forEach var="p" items="${novita}">

            <a class="product-link" href="${pageContext.request.contextPath}/prodotto?id=${p.idProdotto}">

                <div class="card">

                    <c:if test="${p.sconto > 0}">
                        <div class="discount-badge">-${p.sconto}%</div>
                    </c:if>

                    <img src="${pageContext.request.contextPath}/${p.immagini[0].path}">

                    <div class="card-content">

                        <div class="product-brand">${p.marca}</div>
                        <div class="product-name">${p.nome} ${p.modello}</div>

                        <c:set var="scontato" value="${p.prezzo - (p.prezzo * p.sconto / 100.0)}" />

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

    <!-- BEST SELLER -->
    <h2>Best Seller</h2>

    <div class="grid">

        <c:forEach var="p" items="${bestSeller}">

            <div class="card">

                <c:if test="${p.sconto > 0}">
                    <div class="discount-badge">-${p.sconto}%</div>
                </c:if>

                <img src="${pageContext.request.contextPath}/${p.immagini[0].path}">

                <div class="card-content">

                    <div class="product-brand">${p.marca}</div>
                    <div class="product-name">${p.nome} ${p.modello}</div>

                    <c:set var="scontato" value="${p.prezzo - (p.prezzo * p.sconto / 100)}" />

                    <c:choose>
                        <c:when test="${p.sconto > 0}">
                            <div class="old-price">${p.prezzo} €</div>
                            <div class="product-price">${scontato} €</div>
                        </c:when>
                        <c:otherwise>
                            <div class="product-price">${p.prezzo} €</div>
                        </c:otherwise>
                    </c:choose>

                </div>

            </div>

        </c:forEach>

    </div>

</div>

<jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>