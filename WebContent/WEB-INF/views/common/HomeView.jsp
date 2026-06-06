<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Overclocked</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
</head>

<body>

<!-- NAVBAR -->

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />


<div class="container">

<h1>Overclocked Shop</h1>

<!-- GRID CATEGORIE -->
    <div class="category-grid">

        <!-- CPU -->
		<a class="category-card"
		   href="${pageContext.request.contextPath}/Catalogo?categoria=CPU">
		    <img src="${pageContext.request.contextPath}/img/cpu.jpg" alt="CPU">
		    <h3>CPU</h3>
		    <p>${countByCategory["CPU"] != null ? countByCategory["CPU"] : 0} prodotti</p>
		</a>
		
		<!-- GPU -->
		<a class="category-card"
		   href="${pageContext.request.contextPath}/Catalogo?categoria=GPU">
		    <img src="${pageContext.request.contextPath}/img/gpu.jpg" alt="GPU">
		    <h3>GPU</h3>
		    <p>${countByCategory["GPU"] != null ? countByCategory["GPU"] : 0} prodotti</p>
		</a>
		
		<!-- RAM -->
		<a class="category-card"
		   href="${pageContext.request.contextPath}/Catalogo?categoria=RAM">
		    <img src="${pageContext.request.contextPath}/img/ram.jpg" alt="RAM">
		    <h3>RAM</h3>
		    <p>${countByCategory["RAM"] != null ? countByCategory["RAM"] : 0} prodotti</p>
		</a>
		
		<!-- PSU -->
		<a class="category-card"
		   href="${pageContext.request.contextPath}/Catalogo?categoria=PSU">
		    <img src="${pageContext.request.contextPath}/img/psu.jpg" alt="PSU">
		    <h3>PSU</h3>
		    <p>${countByCategory["PSU"] != null ? countByCategory["PSU"] : 0} prodotti</p>
		</a>
		
		<!-- CASE -->
		<a class="category-card"
		   href="${pageContext.request.contextPath}/Catalogo?categoria=CASE">
		    <img src="${pageContext.request.contextPath}/img/case.jpg" alt="CASE">
		    <h3>CASE</h3>
		    <p>${countByCategory["CASE"] != null ? countByCategory["CASE"] : 0} prodotti</p>
		</a>
		
		<!-- STORAGE -->
		<a class="category-card"
		   href="${pageContext.request.contextPath}/Catalogo?categoria=STORAGE">
		    <img src="${pageContext.request.contextPath}/img/ssd.jpg" alt="STORAGE">
		    <h3>STORAGE</h3>
		    <p>${countByCategory["STORAGE"] != null ? countByCategory["STORAGE"] : 0} prodotti</p>
		</a>
		
		<!-- MOBO -->
		<a class="category-card"
		   href="${pageContext.request.contextPath}/Catalogo?categoria=MOBO">
		    <img src="${pageContext.request.contextPath}/img/mobo.jpg" alt="MOBO">
		    <h3>MOBO</h3>
		    <p>${countByCategory["MOBO"] != null ? countByCategory["MOBO"] : 0} prodotti</p>
		</a>
		
		<!-- DISSIPATORE -->
		<a class="category-card"
		   href="${pageContext.request.contextPath}/Catalogo?categoria=DISSIPATORE">
		    <img src="${pageContext.request.contextPath}/img/cooler.jpg" alt="DISSIPATORE">
		    <h3>DISSIPATORE</h3>
		    <p>${countByCategory["DISSIPATORE"] != null ? countByCategory["DISSIPATORE"] : 0} prodotti</p>
		</a>

    </div>

<!-- NOVITA -->
<h2>Novità</h2>

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

            <c:set var="scontato"
                   value="${p.prezzo - (p.prezzo * p.sconto / 100)}" />

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