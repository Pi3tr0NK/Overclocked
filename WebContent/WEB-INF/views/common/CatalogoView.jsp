<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Catalogo</title>

<style>
body {
    margin: 0;
    font-family: Arial;
    display: flex;
    background: #111;
    color: white;
}

.sidebar {
    width: 280px;
    padding: 20px;
    background: #1a1a1a;
}

.content {
    flex: 1;
    padding: 20px;
}

input, select {
    width: 100%;
    margin-bottom: 10px;
    padding: 8px;
}

.product {
    background: #222;
    padding: 10px;
    margin-bottom: 10px;
    border-radius: 8px;
}
</style>

</head>

<body>

<!-- SIDEBAR FILTRI -->
<div class="sidebar">

<form action="CatalogoControl" method="get">

    <label>Categoria</label>
    <select name="categoria" onchange="this.form.submit()">
        <option value="tutte">Tutte</option>
        <option value="CPU" ${param.categoria == 'CPU' ? 'selected' : ''}>CPU</option>
        <option value="GPU" ${param.categoria == 'GPU' ? 'selected' : ''}>GPU</option>
        <option value="RAM" ${param.categoria == 'RAM' ? 'selected' : ''}>RAM</option>
        <option value="MOBO" ${param.categoria == 'MOBO' ? 'selected' : ''}>MOBO</option>
        <option value="PSU" ${param.categoria == 'PSU' ? 'selected' : ''}>PSU</option>
        <option value="CASE" ${param.categoria == 'CASE' ? 'selected' : ''}>CASE</option>
        <option value="DISSIPATORE" ${param.categoria == 'DISSIPATORE' ? 'selected' : ''}>Dissipatore</option>
        <option value="STORAGE" ${param.categoria == 'STORAGE' ? 'selected' : ''}>Storage</option>
    </select>

    <label>Prezzo max</label>
    <input type="number" name="prezzo" step="0.01">

    <label>Marca</label>
    <input type="text" name="marca">


    <!-- ===================== -->
    <!-- FILTRI DINAMICI CPU -->
    <!-- ===================== -->
    <c:if test="${param.categoria == 'CPU'}">
        <label>Core</label>
        <input type="number" name="core">

        <label>Frequenza</label>
        <input type="text" name="frequenza">
    </c:if>

    <!-- GPU -->
    <c:if test="${param.categoria == 'GPU'}">
        <label>VRAM</label>
        <input type="text" name="vram">

        <label>PCIe</label>
        <input type="text" name="pcie">
    </c:if>

    <!-- RAM -->
    <c:if test="${param.categoria == 'RAM'}">
        <label>Capacità</label>
        <input type="text" name="capacita">

        <label>Frequenza</label>
        <input type="text" name="frequenza">

        <label>Tipo</label>
        <input type="text" name="tipo">
    </c:if>

    <!-- PSU -->
    <c:if test="${param.categoria == 'PSU'}">
        <label>Potenza</label>
        <input type="number" name="potenza">

        <label>Certificazione</label>
        <input type="text" name="certificazione">

        <label>Modulare</label>
        <select name="modulare">
            <option value="">--</option>
            <option value="si">Si</option>
            <option value="no">No</option>
        </select>
    </c:if>

    <!-- CASE -->
    <c:if test="${param.categoria == 'CASE'}">
        <label>Formato</label>
        <input type="text" name="formato">

        <label>Colore</label>
        <input type="text" name="colore">
    </c:if>

    <!-- MOBO -->
    <c:if test="${param.categoria == 'MOBO'}">
        <label>Formato</label>
        <input type="text" name="formato">

        <label>NVMe</label>
        <select name="nvme">
            <option value="">--</option>
            <option value="si">Si</option>
            <option value="no">No</option>
        </select>

        <label>Slot RAM</label>
        <input type="number" name="slotram">
    </c:if>

    <!-- STORAGE -->
    <c:if test="${param.categoria == 'STORAGE'}">
        <label>Capacità</label>
        <input type="text" name="capacita">

        <label>Tipo</label>
        <input type="text" name="tipo">

        <label>Tecnologia</label>
        <input type="text" name="tecnologia">
    </c:if>

    <button type="submit">Filtra</button>

</form>

</div>


<!-- ===================== -->
<!-- RISULTATI -->
<!-- ===================== -->
<div class="content">

<h2>Prodotti</h2>

<c:forEach var="p" items="${products}">
    <div class="product">
        <strong>${p.nome}</strong><br>
        Prezzo: ${p.prezzo} €<br>
        Marca: ${p.marca}
    </div>
</c:forEach>

<c:if test="${empty products}">
    <p>Nessun prodotto trovato</p>
</c:if>

</div>

</body>
</html>