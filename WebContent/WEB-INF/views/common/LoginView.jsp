<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login - Overclocked</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>

<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

<div class="login-container">

    <div class="logo">
        <img src="${pageContext.request.contextPath}/img/lock.png" alt="">
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

    <form action="login" method="post">

        <label>Email</label>
        <input type="email" name="email" required>

        <label>Password</label>
        <input type="password" name="password" required>

        <div class="row">
            <a class="link" href="#">Password dimenticata?</a>
        </div>

        <button class="login-btn" type="submit">Accedi</button>

    </form>

    <div class="separator">
        <span>oppure</span>
    </div>

    <div class="register">
        Non hai un account?
        <a class="link" href="register">Registrati</a>
    </div>

</div>

</body>
</html>