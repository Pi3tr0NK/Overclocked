<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Errore - Overclocked</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/errore.css">
</head>

<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="container">

    <div class="error-page">

        <div class="error-code">
            <span class="error-number">${statusCode}</span>
        </div>

        <div class="error-divider"></div>

        <h1 class="error-title">Qualcosa è andato storto</h1>

        <p class="error-subtitle">
            La pagina che cercavi non esiste o si è verificato un problema imprevisto sul server.
        </p>

        <a class="error-btn" href="${pageContext.request.contextPath}/">
            ← Torna alla Home
        </a>

    </div>

</div>

<jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>