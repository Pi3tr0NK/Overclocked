<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.ProdottoBean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
ProdottoBean p = (ProdottoBean) request.getAttribute("prodotto");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= p.getNome() %></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/prodotto.css">


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
                        <div class="product-price">${String.format('%.2f', scontato)} €</div>
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