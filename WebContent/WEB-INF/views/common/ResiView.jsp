<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Il mio account — Overclocked</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profilo.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="profilo-wrapper">

    <h1 class="profilo-titolo">Il mio account</h1>

    <div class="profilo-layout">

        <%-- ── SIDEBAR SINISTRA ── --%>
        <nav class="profilo-nav">
            <a href="${pageContext.request.contextPath}/common/profilo?view=ordini"
               class="profilo-nav-link">Ordini e fatture</a>
            <a href="${pageContext.request.contextPath}/common/profilo?view=resi"
               class="profilo-nav-link active">Resi</a>
            <a href="${pageContext.request.contextPath}/common/profilo?view=dati"
               class="profilo-nav-link">I miei dati</a>
            <a href="${pageContext.request.contextPath}/logout"
               class="profilo-nav-link logout">Logout &#x2192;</a>
        </nav>

        <%-- ── CONTENUTO DESTRA ── --%>
        <div class="profilo-content">

            <div class="profilo-card-title">Resi</div>

            <c:choose>
                <c:when test="${empty resi}">
                    <p class="profilo-empty">Nessun reso effettuato.</p>
                </c:when>
                <c:otherwise>
                    <c:forEach var="ordine" items="${resi}">
                        <div class="ordine-card">

                            <div class="ordine-header">
                                <div>
                                    <span class="ordine-label">Ordine #${ordine.idOrdine}</span>
                                    <span class="ordine-data">${ordine.data}</span>
                                </div>
                                <span class="ordine-stato stato-RIMBORSATO">RIMBORSATO</span>
                            </div>

                            <div class="ordine-body">
                                <div class="ordine-field">
                                    <span class="ordine-field-label">Totale rimborsato</span>
                                    <span class="ordine-field-value">${String.format('%.2f', ordine.totale)} €</span>
                                </div>
                                <div class="ordine-field">
                                    <span class="ordine-field-label">Indirizzo</span>
                                    <span class="ordine-field-value">
                                        ${ordine.indirizzo.viaNumciv},
                                        ${ordine.indirizzo.citta}
                                        (${ordine.indirizzo.provincia})
                                    </span>
                                </div>
                            </div>

                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>

        </div><%-- fine profilo-content --%>

    </div><%-- fine profilo-layout --%>

</div>

<jsp:include page="/WEB-INF/views/components/footer.jsp" />
</body>
</html>
