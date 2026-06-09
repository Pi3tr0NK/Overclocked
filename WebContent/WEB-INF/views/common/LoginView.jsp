<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login - Overclocked</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">

</head>
<body>

<div class="login-container">

    <div class="logo"><img src="${pageContext.request.contextPath}/img/lock.png" alt="CPU"></div>

    <h1>Bentornato</h1>

    <div class="orange-line"></div>
	
    <p class="subtitle">
        Accedi al tuo account Overclocked
    </p>

 <%@ page import="java.util.List" %>

	<%
	List<String> errors =(List<String>) request.getAttribute("errors");
	
	if(errors != null){
	%>
	
	    <div class="error">
	
	        <%
	        for(String e : errors){
	        %>
	            <p><%= e %></p>
	        <%
	        }
	        %>
	    </div>
	<%
	}
	%>

    <form action="login" method="post">

        <label>Email</label>
        <input type="email" name="email" required>

        <label>Password</label>
        <input type="password" name="password" required>

        <div class="row">
            <a class="link" href="#">
                Password dimenticata?
            </a>

        </div>

        <button class="login-btn" type="submit">
            Accedi
        </button>

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
