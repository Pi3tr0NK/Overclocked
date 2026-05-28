<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Overclocked</title>
    
    <style type="text/css">
    /* =========================
   BASE
========================= */

body {
    margin: 0;
    font-family: Arial, Helvetica, sans-serif;
    background-color: #0d0d0d;
    color: #ffffff;
}

/* contenitore principale */
.container {
    width: 90%;
    margin: 0 auto;
    padding: 30px 0;
    text-align: center;
}

/* titolo */
h1 {
    color: #ff7a00;
    margin-bottom: 40px;
    font-size: 42px;
    letter-spacing: 2px;
}

/* =========================
   GRID CATEGORIE
========================= */

.category-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 25px;
}

/* =========================
   CARD
========================= */

.category-card {
    background: #1a1a1a;
    border: 2px solid #222;
    border-radius: 12px;
    padding: 20px;
    transition: all 0.3s ease;
    cursor: pointer;
}

/* hover effetto gaming */
.category-card:hover {
    transform: translateY(-8px);
    border-color: #ff7a00;
    box-shadow: 0 0 20px rgba(255, 122, 0, 0.4);
}

/* immagini */
.category-card img {
    width: 100%;
    height: 140px;
    object-fit: cover;
    border-radius: 10px;
    margin-bottom: 15px;
}

/* titolo categoria */
.category-card h3 {
    margin: 10px 0 5px 0;
    color: #ff7a00;
    font-size: 20px;
}

/* numero prodotti */
.category-card p {
    color: #cccccc;
    font-size: 16px;
}

/* =========================
   RESPONSIVE
========================= */

/* tablet */
@media (max-width: 1000px) {
    .category-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}

/* mobile */
@media (max-width: 600px) {
    .category-grid {
        grid-template-columns: 1fr;
    }

    h1 {
        font-size: 32px;
    }
}
    </style>
</head>

<body>

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
    
    <h2>Novità</h2>

	<div class="grid">
	    <c:forEach var="p" items="${novita}">
	        <div class="card">
	            <h3>${p.nome}</h3>
	            <p>${p.marca}</p>
	            <p>${p.modello}</p>
	            <p><b>${p.prezzo} €</b></p>
	        </div>
	    </c:forEach>
	</div>

</div>

</body>
</html>