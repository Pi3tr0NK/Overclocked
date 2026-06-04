<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.ProdottoBean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
ProdottoBean p =
(ProdottoBean) request.getAttribute("prodotto");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= p.getNome() %></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
 
<style>

body{
    background:#0b0b0b;
    color:white;
    font-family:Arial;
    margin:0;
}

.container{
    width:1200px;
    margin:auto;
    padding:30px;
}

.product{
    display:flex;
    gap:50px;
}

.left{
    width:45%;
}

.main-image{
    width:100%;
    height:450px;
    background:#111;
    border:1px solid #222;
    border-radius:10px;
    display:flex;
    justify-content:center;
    align-items:center;
}

.main-image img{
    max-width:90%;
    max-height:90%;
}

.thumbs{
    margin-top:15px;
    display:flex;
    gap:10px;
}

.thumb{
    width:80px;
    height:80px;
    background:#111;
    border:1px solid #333;
    border-radius:8px;
}

.right{
    width:55%;
}

.brand{
    color:#ff7300;
    font-weight:bold;
    margin-bottom:15px;
}

.title{
    font-size:36px;
    margin-bottom:10px;
}

.stock-available{
    color:#28d14a;
    font-weight:bold;
}

.stock-low{
    color:orange;
    font-weight:bold;
}

.stock-unavailable{
    color:red;
    font-weight:bold;
}

.price{
    font-size:48px;
    color:#ff7300;
    font-weight:bold;
}

.qty{
    margin-top:25px;
}

.qty input{
    width:70px;
    padding:10px;
    text-align:center;
}

.cart-btn{
    margin-top:20px;
    width:100%;
    background:#ff7300;
    color:black;
    border:none;
    padding:15px;
    font-size:18px;
    border-radius:10px;
    cursor:pointer;
    font-weight:bold;
}

.cart-btn:hover{
    opacity:.9;
}

.features{
    display:flex;
    gap:10px;
    margin-top:25px;
    flex-wrap:wrap;
}

.feature{
    border:1px solid #333;
    padding:10px;
    border-radius:8px;
}

.specs{
    margin-top:40px;
}

.specs table{
    width:100%;
    border-collapse:collapse;
}

.specs td{
    padding:12px;
    border-bottom:1px solid #222;
}

.specs td:first-child{
    color:#999;
    width:40%;
}

.related{
    margin-top:80px;
}

.cards{
    display:flex;
    gap:20px;
}

.card{
    width:220px;
    background:#111;
    border:1px solid #222;
    border-radius:10px;
    padding:15px;
}

.card img{
    width:100%;
    height:120px;
    object-fit:contain;
}

.card-title{
    margin-top:10px;
    font-size:14px;
}

.card-price{
    color:#ff7300;
    font-weight:bold;
    margin-top:10px;
}

.cart-btn:disabled{
    background:#555;
    color:#999;
    cursor:not-allowed;
    opacity:0.7;
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

.old-price{
    font-size:24px;
    color:#888;
    text-decoration:line-through;
    margin-bottom:5px;
}

.price{
    font-size:48px;
    color:#ff7300;
    font-weight:bold;
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

</style>


<style type="text/css">

</style>

</head>
<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="container">

<div class="product">

    <div class="left">

        <div class="main-image">

            <img src="<%= request.getContextPath() %>/img/prodotti/default.png">

        </div>

        <div class="thumbs">

            <div class="thumb"></div>
            <div class="thumb"></div>
            <div class="thumb"></div>
            <div class="thumb"></div>

        </div>

    </div>

    <div class="right">

        <div class="brand">

            <%= p.getMarca() %>

        </div>

        <div class="title">

            <%= p.getNome() %>

        </div>

		<div class="stock">
		
		    <c:choose>
		
		        <c:when test="${prodotto.stock == 0}">
		            <span class="stock-unavailable">
		                Non disponibile
		            </span>
		        </c:when>
		
		        <c:when test="${prodotto.stock <= 10}">
		            <span class="stock-low">
		                Solo ${prodotto.stock} pezzi rimasti
		            </span>
		        </c:when>
		
		        <c:otherwise>
		            <span class="stock-available">
		                Disponibile
		            </span>
		        </c:otherwise>
		
		    </c:choose>
		
		</div>

		<c:choose>
		
		    <c:when test="${prodotto.sconto > 0}">
		
		        <div class="old-price">
		            € <%= String.format("%.2f", p.getPrezzo()) %>
		        </div>
		
		        <div class="price">
		            € <%= String.format("%.2f",
		                    p.getPrezzo() - (p.getSconto() * p.getPrezzo() / 100.0)) %>
		        </div>
		
		    </c:when>
		
		    <c:otherwise>
		
		        <div class="price">
		            € <%= String.format("%.2f", p.getPrezzo()) %>
		        </div>
		
		    </c:otherwise>
		
		</c:choose>

        <form action="cart" method="post">

            <input type="hidden"
                   name="idProdotto"
                   value="<%= p.getIdProdotto() %>">

            <div class="qty">

                Quantità

                <input
                    type="number"
                    name="quantita"
                    value="1"
                    min="1">

            </div>

			<button
			    type="submit"
			    class="cart-btn"
			    <c:if test="${prodotto.stock == 0}">disabled</c:if>>
			
			    Aggiungi al carrello
			
			</button>

        </form>

        <div class="features">

            <div class="feature">
                🚚 Spedizione gratuita
            </div>

            <div class="feature">
                ⚡ Consegna 24h
            </div>

            <div class="feature">
                🛡 Garanzia 3 anni
            </div>

            <div class="feature">
                ↩ Reso 30 giorni
            </div>

        </div>

        <div class="specs">
			
	  

	    
            <h2>Specifiche tecniche</h2>

            <table>
            
                <tr>
                    <td>Nome</td>
                    <td><%= p.getNome() %></td>
                </tr>
                
                <tr>
                    <td>Marca</td>
                    <td><%= p.getMarca() %></td>
                </tr>

                <tr>
                    <td>Modello</td>
                    <td><%= p.getModello() %></td>
                </tr>
                
                <tr>
                    <td>Dimensioni</td>
                    <td><%= p.getDimensioni() %></td>
                </tr>
             
                <tr>
                    <td>Peso</td>
                    <td><%= p.getPeso() %></td>
                </tr>
                <c:forEach var="spec" items="${prodotto.specifiche}">

   				<tr>
        			<td>${spec.key}</td>
        			<td>${spec.value}</td>
    			</tr>

				</c:forEach>     
				
            </table>

			 <h2>Descrizione</h2>
			 	
			 <table>
                <tr>
                    <td><%= p.getDescrizione() %></td>
                </tr>			 
			 </table>
			 
			 
        </div>

    </div>

</div>

<!-- CORRELATI -->
<h2>Prodotti correlati</h2>

<div class="grid">

<c:forEach var="prod" items="${correlati}">

	<a class="product-link" href="${pageContext.request.contextPath}/prodotto?id=${prod.idProdotto}">
        <div class="card">

            <c:if test="${prod.sconto > 0}">
                <div class="discount-badge">-${prod.sconto}%</div>
            </c:if>

            <img src="${pageContext.request.contextPath}/${prod.immagini[0].path}">

            <div class="card-content">

                <div class="product-brand">${prod.marca}</div>

                <div class="product-name">
                    ${prod.nome} ${prod.modello}
                </div>

                <c:set var="scontato"
                       value="${prod.prezzo - (prod.prezzo * prod.sconto / 100)}" />

                <c:choose>

                    <c:when test="${prod.sconto > 0}">
                        <div class="old-price">${prod.prezzo} €</div>
                        <div class="product-price">${scontato} €</div>
                    </c:when>

                    <c:otherwise>
                        <div class="product-price">${prod.prezzo} €</div>
                    </c:otherwise>

                </c:choose>

            </div>

        </div>

	</a>
</c:forEach>

</div>

</div>


<jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>