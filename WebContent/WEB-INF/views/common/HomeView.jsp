<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Overclocked</title>

<style>

/* =========================
   BASE
========================= */

body {
    margin: 0;
    font-family: Arial, Helvetica, sans-serif;
    background-color: #0d0d0d;
    color: #ffffff;
}

/* =========================
   NAVBAR
========================= */

.navbar {
    width: 100%;
    background-color: #111;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px 30px;
    box-sizing: border-box;
    border-bottom: 2px solid #ff7a00;
    position: sticky;
    top: 0;
    z-index: 1000;
}

.nav-left img {
    height: 55px;
}

.nav-center {
    flex: 1;
    display: flex;
    justify-content: center;
}

.nav-center form {
    display: flex;
    width: 100%;
    max-width: 600px;
}

.nav-center input {
    flex: 1;
    padding: 12px;
    border: none;
    background: #222;
    color: white;
    border-radius: 8px 0 0 8px;
}

.nav-center button {
    padding: 12px 18px;
    border: none;
    background: #ff7a00;
    color: white;
    font-weight: bold;
    border-radius: 0 8px 8px 0;
    cursor: pointer;
}

.nav-right {
    display: flex;
    gap: 10px;
}

.nav-btn {
    text-decoration: none;
    color: white;
    background: #ff7a00;
    padding: 10px 15px;
    border-radius: 8px;
}

/* =========================
   CONTAINER
========================= */

.container {
    width: 92%;
    margin: auto;
    padding: 30px 0;
}

h1, h2 {
    color: #ff7a00;
}

/* =========================
   GRID
========================= */

.category-grid,
.grid {
    display: grid;
    gap: 20px;
}

.category-grid {
    grid-template-columns: repeat(4, 1fr);
}

.grid {
    grid-template-columns: repeat(5, 1fr);
}

/* =========================
   CATEGORY CARD
========================= */

.category-card {
    background: #1a1a1a;
    padding: 15px;
    border-radius: 12px;
    text-align: center;
    transition: transform 0.2s ease, box-shadow 0.2s ease, border 0.2s ease;
    will-change: transform;
}

.category-card:hover {
    transform: translateY(-6px);
    border: 1px solid #ff7a00;
    box-shadow: 0 10px 20px rgba(0,0,0,0.4);
}

.category-card img {
    width: 100%;
    height: 150px;
    object-fit: cover;
}

/* =========================
   PRODUCT CARD
========================= */

.card {
    background: #1a1a1a;
    border-radius: 14px;
    overflow: hidden;
    position: relative;
    transition: 0.3s;
}

.card:hover {
    transform: translateY(-6px);
}

.card img {
    width: 100%;
    height: 220px;
    object-fit: cover;
}

.card-content {
    padding: 15px;
}

.product-brand {
    color: #999;
    font-size: 13px;
}

.product-name {
    font-size: 16px;
    margin: 10px 0;
    font-weight: bold;
}

.product-price {
    color: #ff7a00;
    font-size: 26px;
    font-weight: bold;
}

.old-price {
    color: #777;
    text-decoration: line-through;
    font-size: 15px;
}

/* =========================
   DISCOUNT BADGE
========================= */

.discount-badge {
    position: absolute;
    top: 10px;
    left: 10px;
    background: red;
    color: white;
    padding: 6px 10px;
    border-radius: 8px;
    font-weight: bold;
    font-size: 14px;
}

/* =========================
   RESPONSIVE
========================= */

@media (max-width: 1000px) {
    .grid { grid-template-columns: repeat(2, 1fr); }
    .category-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 700px) {
    .navbar {
        flex-direction: column;
        gap: 10px;
    }

    .grid { grid-template-columns: 1fr; }
    .category-grid { grid-template-columns: 1fr; }
}

</style>
</head>

<body>

<!-- NAVBAR -->
<nav class="navbar">

    <div class="nav-left">
        <img src="${pageContext.request.contextPath}/img/logo.png">
    </div>

    <div class="nav-center">
        <form action="${pageContext.request.contextPath}/search" method="get">
            <input type="text" name="q" placeholder="Cerca componenti...">
            <button>Cerca</button>
        </form>
    </div>

    <div class="nav-right">
        <a class="nav-btn" href="${pageContext.request.contextPath}/indexlogin">ACCEDI</a>
        <a class="nav-btn" href="${pageContext.request.contextPath}/cart">CARRELLO</a>
    </div>

</nav>

<div class="container">

<h1>Overclocked Shop</h1>

<!-- GRID CATEGORIE -->
    <div class="category-grid">

        <!-- CPU -->
        <div class="category-card">
            <img src="${pageContext.request.contextPath}/img/cpu.jpg" alt="CPU">
            <h3>CPU</h3>
            <p>${countByCategory["CPU"] != null ? countByCategory["CPU"] : 0} prodotti</p>
        </div>

        <!-- GPU -->
        <div class="category-card">
            <img src="${pageContext.request.contextPath}/img/gpu.jpg" alt="GPU">
            <h3>GPU</h3>
            <p>${countByCategory["GPU"] != null ? countByCategory["GPU"] : 0} prodotti</p>
        </div>

        <!-- RAM -->
        <div class="category-card">
            <img src="${pageContext.request.contextPath}/img/ram.jpg" alt="RAM">
            <h3>RAM</h3>
            <p>${countByCategory["RAM"] != null ? countByCategory["RAM"] : 0} prodotti</p>
        </div>

        <!-- PSU -->
        <div class="category-card">
            <img src="${pageContext.request.contextPath}/img/psu.jpg" alt="PSU">
            <h3>PSU</h3>
            <p>${countByCategory["PSU"] != null ? countByCategory["PSU"] : 0} prodotti</p>
        </div>

        <!-- CASE -->
        <div class="category-card">
            <img src="${pageContext.request.contextPath}/img/case.jpg" alt="CASE">
            <h3>CASE</h3>
            <p>${countByCategory["CASE"] != null ? countByCategory["CASE"] : 0} prodotti</p>
        </div>

        <!-- STORAGE -->
        <div class="category-card">
            <img src="${pageContext.request.contextPath}/img/ssd.jpg" alt="STORAGE">
            <h3>STORAGE</h3>
            <p>${countByCategory["STORAGE"] != null ? countByCategory["STORAGE"] : 0} prodotti</p>
        </div>

        <!-- MOBO -->
        <div class="category-card">
            <img src="${pageContext.request.contextPath}/img/mobo.jpg" alt="MOBO">
            <h3>MOBO</h3>
            <p>${countByCategory["MOBO"] != null ? countByCategory["MOBO"] : 0} prodotti</p>
        </div>

        <!-- DISSIPATORE -->
        <div class="category-card">
            <img src="${pageContext.request.contextPath}/img/cooler.jpg" alt="DISSIPATORE">
            <h3>DISSIPATORE</h3>
            <p>${countByCategory["DISSIPATORE"] != null ? countByCategory["DISSIPATORE"] : 0} prodotti</p>
        </div>

    </div>

<!-- NOVITA -->
<h2>Novità</h2>

<div class="grid">

<c:forEach var="p" items="${novita}">

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

</body>
</html>