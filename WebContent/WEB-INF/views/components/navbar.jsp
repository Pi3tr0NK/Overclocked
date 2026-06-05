<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<style>

/* ========================= NAVBAR ========================= */

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

</style>
<nav class="navbar">
	
    <div class="nav-left">
	        <a href=${pageContext.request.contextPath}/home><img src="${pageContext.request.contextPath}/img/logo.png" alt="Overclocked Logo"></a>
	</div>
	

    <div class="nav-center">
        <form action="${pageContext.request.contextPath}/Catalogo" method="get">
            <input type="text" name="cerca" placeholder="Cerca componenti...">
            <button>Cerca</button>
        </form>
    </div>

    <div class="nav-right">

        <c:choose>
            <c:when test="${not empty sessionScope.utente}">
                <a class="nav-btn" href="${pageContext.request.contextPath}/profilo">
                    ${sessionScope.utente.nome}
                </a>
                <a class="nav-btn" href="${pageContext.request.contextPath}/logout">
                    ESCI
                </a>
            </c:when>
            <c:otherwise>
                <a class="nav-btn" href="${pageContext.request.contextPath}/indexlogin">
                    ACCEDI
                </a>
            </c:otherwise>
        </c:choose>

        <a class="nav-btn" href="${pageContext.request.contextPath}/carrello">
            CARRELLO
        </a>

    </div>

</nav>