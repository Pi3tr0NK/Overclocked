<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Overclocked</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css">

<jsp:include page="/WEB-INF/views/components/icon.jsp" />

</head>




<body>

    <a href="${pageContext.request.contextPath}/" class="brand-header"><h2>OVERCLOCKED</h2></a>

<div class="login-container">

    <div class="logo">
        <img src="${pageContext.request.contextPath}/images/lock.png" alt="">
    </div>

    <h1>Bentornato</h1>

    <div class="orange-line"></div>

    <p class="subtitle">Accedi al tuo account Overclocked</p>

    <c:if test="${not empty errors}">
        <div class="error">
            <c:forEach var="e" items="${errors}">
                <p>${e}</p>
            </c:forEach>
        </div>
    </c:if>

	    <form action="login" method="post" novalidate>
	
		    <label>Email</label>
		    <input class="login-field" type="email" name="email" required>
		    <span id="err-email" class="field-error"></span>
		
		    <label>Password</label>
		    <input type="password" name="password" required>
		    <span id="err-password" class="field-error"></span>
		
		    <button class="login-btn" type="submit">Accedi</button>
	
		</form>

    <div class="separator">
        <span>oppure</span>
    </div>

    <div class="register">
        Non hai un account?
        <a class="link" href="register">Registrati</a>
    </div>


<script src="${pageContext.request.contextPath}/scripts/loginCheck.js"></script>

</div>

</body>
</html>