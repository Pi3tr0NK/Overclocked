<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar">
	
    <div class="nav-left">
	        <a href=${pageContext.request.contextPath}/home><img src="${pageContext.request.contextPath}/img/logo.png" alt="Overclocked Logo"><h2>OVERCLOCKED</h2></a>
	</div>
	

    <div class="nav-center">
        <form action="${pageContext.request.contextPath}/Catalogo" method="get">
            <input type="text" name="cerca" placeholder="Cerca componenti..." value="${param.cerca}">
            <button>Cerca</button>
        </form>
    </div>

    <div class="nav-right">
    
		<c:if test="${not empty sessionScope.utente and sessionScope.utente.ruolo == 'ADMIN'}">
		<a class="nav-btn" href="${pageContext.request.contextPath}/admin/dashboard">
		 <img src="${pageContext.request.contextPath}/img/admin.png" alt="">
            ADMIN
        </a>
        </c:if>
        
        
        <c:choose>
            <c:when test="${not empty sessionScope.utente}">
                <a class="nav-btn" href="${pageContext.request.contextPath}/common/profilo">
                    <img src="${pageContext.request.contextPath}/img/utente.png" alt="">${sessionScope.utente.nome}
                </a>
                <a class="nav-btn" href="${pageContext.request.contextPath}/logout">
                     <img src="${pageContext.request.contextPath}/img/logout.png" alt="">ESCI
                </a>
            </c:when>
            <c:otherwise>
                <a class="nav-btn" href="${pageContext.request.contextPath}/indexlogin">
                    <img src="${pageContext.request.contextPath}/img/utente.png" alt="">ACCEDI
                </a>
            </c:otherwise>
        </c:choose>
		

    
        
        <a class="nav-btn" href="${pageContext.request.contextPath}/Carrello">
           <img src="${pageContext.request.contextPath}/img/carrello.png" alt=""> 
           <c:if test="${sessionScope.cart != null}">
        		<span class="cart-badge">${sessionScope.cart.totalQuantity}</span>
    		</c:if>
           CARRELLO
        </a>

    </div>

</nav>