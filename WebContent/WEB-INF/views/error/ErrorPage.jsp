<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Errore - Overclocked</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
</head>

<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

<div class="register-container" style="text-align: center;">

    <div class="logo" style="border-color: #ff3333;">
        <span style="color: #ff3333; font-weight: bold; font-size: 24px;">!</span>
    </div>

    <h1>Qualcosa è andato storto</h1>

    <div class="orange-line" style="background: #ff3333;"></div>

    <p class="subtitle">Codice Errore: <strong>${statusCode}</strong></p>
    
    <p style="color: #999; margin-bottom: 30px;">
        La pagina che cercavi non esiste o si è verificato un problema imprevisto sul server.
    </p>

    <a class="register-btn" href="${pageContext.request.contextPath}/" style="display: block; text-decoration: none; line-height: 20px;">
        Torna alla Home
    </a>

</div>

</body>
</html>