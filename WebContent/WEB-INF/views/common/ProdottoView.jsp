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

<style type="text/css">

</style>

</head>
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

             ${prodotto.nome}


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
		        
		            € ${p.prezzo}
		        </div>
		
		        <div class="price">
					€ ${prodotto.prezzo - (prodotto.sconto * prodotto.prezzo / 100.0)}
		        </div>
		
		    </c:when>
		
		    <c:otherwise>
		
		        <div class="price">
		            € ${p.prezzo}
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
                    <td>${prodotto.nome}</td>
                </tr>
                
                <tr>
                    <td>Marca</td>
                    <td>${prodotto.marca}</td>
                </tr>

                <tr>
                    <td>Modello</td>
                    <td>${prodotto.modello}</td>
                </tr>
                
                <tr>
                    <td>Dimensioni</td>
                    <td>${prodotto.dimensioni}</td>
                </tr>
             
                <tr>
                    <td>Peso</td>
                    <td>${prodotto.peso}</td>
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
                    <td>${prodotto.descrizione}</td>
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

<script type="text/javascript">

// immagini

const mainImage = document.querySelector(".main-image img");
const thumbs = document.querySelectorAll(".thumb img");

thumbs.forEach(img => {

    img.addEventListener("mouseenter", function() {

        mainImage.src = this.src;

        document.querySelectorAll(".thumb").forEach(t =>
            t.classList.remove("active")
        );

        this.parentElement.classList.add("active");
    });

});

function addToCart(idProdotto) {
	var qty = document.getElementById("quantita").value;
    var params = "aggiungi=" + idProdotto + "&quantita=" + qty;
	
    loadAjaxDoc("carrello/add", "GET", params, handleAddCart);
}

function handleAddCart(request) {
    var response = JSON.parse(request.responseText);

    if (response.success) {
        
    } else {
        
    }
}

function createXMLHttpRequest() {
	var request;
	try {
		// Firefox 1+, Chrome 1+, Opera 8+, Safari 1.2+, Edge 12+, Internet Explorer 7+
		request = new XMLHttpRequest();
	} catch (e) {
		// past versions of Internet Explorer 
		try {
			request = new ActiveXObject("Msxml2.XMLHTTP");  
		} catch (e) {
			try {
				request = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
				alert("Il browser non supporta AJAX");
				return null;
			}
		}
	}
	return request;
}

function loadAjaxDoc(url, method, params, cFuction) {
	var request = createXMLHttpRequest();
	if(request){
		
		request.onreadystatechange = function() {
			if (this.readyState == 4) {
				if (this.status == 200) {
				    cFuction(this);
				} else {				
					if(this.status == 0){ // When aborting the request
						alert("Problemi nell'esecuzione della richiesta: nessuna risposta ricevuta nel tempo limite");
					} else { // Any other situation
						alert("Problemi nell'esecuzione della richiesta:\n" + this.statusText);
					}
					return null;
				}
		    }
		};
		
		setTimeout(function () {     // to abort after 15 sec
        	if (request.readyState < 4) {
            	request.abort();
        	}
    	}, 15000); 
		
		if(method.toLowerCase() == "get"){
			if(params){
				request.open("GET", url + "?" + params, true);
			} else {
				request.open("GET", url, true);
			}
			request.setRequestHeader("Connection", "close");
	        request.send(null);
	        
		} else {
			
			if(params){
				request.open("POST", url, true);
				request.setRequestHeader("Connection", "close");
				request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	        	request.send(params);
			} else {
				console.log("Usa GET se non ci sono parametri!");
				return null;
			}
			
		}
		
	}
}
</script>

</html>