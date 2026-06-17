<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.ProdottoBean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> ${prodotto.nome}</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/prodotto.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart-sidebar.css">

<!-- Definisci contextPath per il JS -->
<script>const contextPath = "${pageContext.request.contextPath}";</script>
<jsp:include page="/WEB-INF/views/components/cart-sidebar.jsp" />

</head>

<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="container">

<div class="product">

    <div class="left">

        <div class="main-image">
		
            <img id ="mainImg" src="${pageContext.request.contextPath}/${prodotto.immagini[0].path}">

        </div>

        <div class="thumbs">

		<c:forEach var="img" items="${prodotto.immagini}" varStatus="status">
		
		    <div class="thumb ${status.first ? 'active' : ''}">
		        <img src="${pageContext.request.contextPath}/${img.path}">
		    </div>
		
		</c:forEach>

        </div>

    </div>

    <div class="right">

        <div class="brand">
		
            ${prodotto.marca}

        </div>

        <div class="title">
             <c:out value="${prodotto.nome}"/>
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
		                <c:out value="Solo ${prodotto.stock} pezzi rimasti"/>
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
		    
		    	<c:set var="scontato" value="${prodotto.prezzo - (prodotto.prezzo * prodotto.sconto / 100.0)}" />
		
		        <div class="old-price">
		            <c:out value="€ ${prodotto.prezzo}"/>
		        </div>
				
				
				
		        <div class="price">
					<c:out value="€ ${String.format('%.2f', scontato)}"/>
		        </div>
		
		    </c:when>
		
		    <c:otherwise>
		
		        <div class="price">
		            <c:out value="€ ${prodotto.prezzo}"/> 
		        </div>
		
		    </c:otherwise>
		
		</c:choose>
		
		<div class="qty">

		    Quantità
		
		    <input type="number"
		           id="quantita"
		           value="1"
		           min="1">
		</div>
		
		<button type="button" class="cart-btn" onclick="addToCart(${prodotto.idProdotto})" <c:if test="${prodotto.stock == 0}">disabled</c:if>>
		    Aggiungi al carrello
		</button>



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
                    <td><c:out value="${prodotto.nome}"/></td>
                </tr>
                
                <tr>
                    <td>Marca</td>
                    <td><c:out value="${prodotto.marca}"/></td>
                </tr>

                <tr>
                    <td>Modello</td>
                    <td><c:out value="${prodotto.modello}"/></td>
                </tr>
                
                <tr>
                    <td>Dimensioni</td>
                    <td><c:out value="${prodotto.dimensioni}"/></td>
                </tr>
             
                <tr>
                    <td>Peso</td>
                    <td><c:out value="${prodotto.peso}"/></td>
                </tr>
                <c:forEach var="spec" items="${prodotto.specifiche}">

	   				<tr>
	        			<td><c:out value="${spec.key}"/></td>
	        			<td><c:out value="${spec.value}"/></td>
	    				</tr>

				</c:forEach>     
				
            </table>

			 <h2>Descrizione</h2>
			 	
			 <table>
                <tr>
                    <td><c:out value="${prodotto.descrizione}"/></td>
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
                <div class="discount-badge"><c:out value="-${prod.sconto}%"/></div>
            </c:if>

            <img src="${pageContext.request.contextPath}/${prod.immagini[0].path}">

            <div class="card-content">

                <div class="product-brand"><c:out value="${prod.marca}"/></div>

                <div class="product-name">
                    <c:out value="${prod.nome} ${prod.modello}"/>
                </div>

                <c:set var="scontato" value="${prod.prezzo - (prod.prezzo * prod.sconto / 100)}" />

                <c:choose>

                    <c:when test="${prod.sconto > 0}">
                        <div class="old-price"><c:out value="${prod.prezzo} €"/></div>
                        <div class="product-price"><c:out value="${String.format('%.2f', scontato)} €"/></div>
                    </c:when>

                    <c:otherwise>
                        <div class="product-price"><c:out value="${prod.prezzo} €"/></div>
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


<script src="${pageContext.request.contextPath}/script/prodotto.js"></script>

</html>