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
    font-family: Arial, Helvetica, sans-serif;
    display: flex;
    background: #0f0f0f;
    color: white;
}

/* SIDEBAR */
.sidebar {
    width: 300px;
    padding: 20px;
    background: #1b1b1b;
    height: 100vh;
    box-sizing: border-box;
}

.sidebar h2 {
    margin-top: 0;
    color: #ff8c00;
}

label {
    font-size: 14px;
    display: block;
    margin-top: 10px;
    margin-bottom: 5px;
}

input, select {
    width: 100%;
    padding: 8px;
    border: none;
    border-radius: 5px;
    margin-bottom: 10px;
    background: #2a2a2a;
    color: white;
}

button {
    width: 100%;
    padding: 10px;
    background: #ff8c00;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-weight: bold;
}

button:hover {
    background: #ffa733;
}

/* CONTENT */
.content {
    flex: 1;
    padding: 20px;
}

.product {
    background: #1e1e1e;
    padding: 15px;
    margin-bottom: 10px;
    border-radius: 10px;
    border-left: 4px solid #ff8c00;
}
</style>

</head>

<body>

<!-- ===================== -->
<!-- SIDEBAR -->
<!-- ===================== -->
<div class="sidebar">

<h2>Catalogo PC</h2>

<form action="Catalogo" method="get">

    <label>Categoria</label>
    <select name="categoria" onchange="this.form.submit()">
        <option value="">Tutte</option>
        <option value="CPU">CPU</option>
        <option value="GPU">GPU</option>
        <option value="RAM">RAM</option>
        <option value="MOBO">MOBO</option>
        <option value="PSU">PSU</option>
        <option value="CASE">CASE</option>
        <option value="DISSIPATORE">Dissipatore</option>
        <option value="STORAGE">Storage</option>
    </select>

    <!-- FILTRI BASE (SEMPRE PRESENTI) -->
    <label>Prezzo max</label>
    <input type="number" name="prezzo" value="1500">

    <label>Marca</label>
    <input type="text" name="marca" value="">

    <!-- ===================== -->
    <!-- CPU -->
    <!-- ===================== -->
    <c:if test="${param.categoria == 'CPU'}">
        <label>Core</label>
        <input type="number" name="core" value="8">

        <label>Frequenza</label>
        <input type="text" name="frequenza" value="3.6">
    </c:if>

    <!-- GPU -->
    <c:if test="${param.categoria == 'GPU'}">
        <label>VRAM</label>
        <input type="text" name="vram" value="8GB">

        <label>PCIe</label>
        <input type="text" name="pcie" value="4.0">
    </c:if>

    <!-- RAM -->
    <c:if test="${param.categoria == 'RAM'}">
        <label>Capacità</label>
        <input type="text" name="capacita" value="16GB">

        <label>Frequenza</label>
        <input type="text" name="frequenza" value="3200">

        <label>Tipo</label>
        <input type="text" name="tipo" value="DDR4">
    </c:if>

    <!-- PSU -->
    <c:if test="${param.categoria == 'PSU'}">
        <label>Potenza</label>
        <input type="number" name="potenza" value="750">

        <label>Certificazione</label>
        <input type="text" name="certificazione" value="Gold">

        <label>Modulare</label>
        <select name="modulare">
            <option value="si" selected>Si</option>
            <option value="no">No</option>
        </select>
    </c:if>

    <!-- CASE -->
    <c:if test="${param.categoria == 'CASE'}">
        <label>Formato</label>
        <input type="text" name="formato" value="ATX">

        <label>Colore</label>
        <input type="text" name="colore" value="nero">
    </c:if>

    <!-- MOBO -->
    <c:if test="${param.categoria == 'MOBO'}">
        <label>Formato</label>
        <input type="text" name="formato" value="ATX">

        <label>NVMe</label>
        <select name="nvme">
            <option value="si" selected>Si</option>
            <option value="no">No</option>
        </select>

        <label>Slot RAM</label>
        <input type="number" name="slotram" value="4">
    </c:if>

    <!-- STORAGE -->
    <c:if test="${param.categoria == 'STORAGE'}">
        <label>Capacità</label>
        <input type="text" name="capacita" value="1TB">

        <label>Tipo</label>
        <input type="text" name="tipo" value="SSD">

        <label>Tecnologia</label>
        <input type="text" name="tecnologia" value="NVMe">
    </c:if>

    <button type="submit">Filtra</button>

</form>

</div>

<!-- ===================== -->
<!-- CONTENT -->
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