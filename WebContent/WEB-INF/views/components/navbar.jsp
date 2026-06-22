<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<head>
    <script src="${pageContext.request.contextPath}/scripts/numeroCarrello.js"></script>
    <script>const contextPath = "${pageContext.request.contextPath}";</script>
	<script src="${pageContext.request.contextPath}/scripts/ricercaLive.js"></script>
</head>

<nav class="navbar">
	
    <div class="nav-left">
        <a href="${pageContext.request.contextPath}/home">
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="Overclocked Logo">
            <h2>OVERCLOCKED</h2>
        </a>
	</div>
	
    <div class="nav-center">
	    <form action="${pageContext.request.contextPath}/Catalogo" method="get">
	        <div id="search-wrapper">
			    <input type="text" id="searchInput" name="cerca"
			           placeholder="Cerca componenti..."
			           value="${param.cerca}"
			           autocomplete="off"
			           oninput="cercaSuggerimenti(this.value)">
			    <div id="suggerimenti"></div>
			</div>
	        <button type="submit">Cerca</button>
	    </form>
	</div>

    <input type="checkbox" id="menu-toggle" class="menu-toggle-checkbox">
    <label for="menu-toggle" class="hamburger-label">
        <span></span>
        <span></span>
        <span></span>
    </label>

    <div class="nav-right">
    
		<c:if test="${not empty sessionScope.utente and sessionScope.utente.ruolo == 'ADMIN'}">
            <a class="nav-btn" href="${pageContext.request.contextPath}/admin/dashboard">
                <img src="${pageContext.request.contextPath}/images/admin.png" alt="">
                ADMIN
            </a>
        </c:if>
        
        <c:choose>
            <c:when test="${not empty sessionScope.utente}">
                <a class="nav-btn" href="${pageContext.request.contextPath}/common/profilo">
                    <img src="${pageContext.request.contextPath}/images/utente.png" alt=""> 
                    <c:out value="${sessionScope.utente.nome}"/>
                </a>
                <a class="nav-btn" href="${pageContext.request.contextPath}/common/logout">
                    <img src="${pageContext.request.contextPath}/images/logout.png" alt="">ESCI
                </a>
            </c:when>
            <c:otherwise>
                <a class="nav-btn" href="${pageContext.request.contextPath}/indexlogin">
                    <img src="${pageContext.request.contextPath}/images/utente.png" alt="">ACCEDI
                </a>
            </c:otherwise>
        </c:choose>
		
        <a class="nav-btn nav-cart" href="${pageContext.request.contextPath}/Carrello">
           <div class="cart-icon-wrapper">
               <img src="${pageContext.request.contextPath}/images/carrello.png" alt=""> 
               <c:if test="${sessionScope.cart != null and sessionScope.cart.totalQuantity != 0}">
                   <span class="cart-badge">${sessionScope.cart.totalQuantity}</span>
               </c:if>
           </div>
           CARRELLO
        </a>

    </div>

</nav>