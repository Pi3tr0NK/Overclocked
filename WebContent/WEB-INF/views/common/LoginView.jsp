<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login - Overclocked</title>

<style>

*{
    margin:0;
    padding:0;
    box-sizing:border-box;
    font-family:Arial;
}

body{
    background:#000;
    display:flex;
    justify-content:center;
    align-items:center;
    height:100vh;
}

.login-container{
    width:450px;
    background:#0a0a0a;
    border:1px solid #222;
    border-radius:25px;
    padding:40px;
    color:white;
    box-shadow:0 0 20px rgba(255,140,0,0.1);
}

.logo{
    width:65px;
    height:65px;
    border-radius:15px;
    background:#1a0d00;
    border:1px solid #ff7300;
    margin:auto;
    margin-bottom:20px;
}

h1{
    text-align:center;
    margin-bottom:10px;
}

.subtitle{
    text-align:center;
    color:#777;
    margin-bottom:40px;
}

.orange-line{
    width:70px;
    height:3px;
    background:#ff7300;
    margin:10px auto 30px auto;
    border-radius:10px;
}

label{
    display:block;
    margin-bottom:10px;
    color:#999;
}

input[type="email"],
input[type="password"]{
    width:100%;
    padding:16px;
    background:#050505;
    border:1px solid #222;
    border-radius:12px;
    color:white;
    margin-bottom:25px;
    font-size:15px;
}

input:focus{
    outline:none;
    border:1px solid #ff7300;
}

.row{
    display:flex;
    justify-content:space-between;
    align-items:center;
    margin-bottom:30px;
}

.link{
    color:#ff7300;
    text-decoration:none;
}

.login-btn{
    width:100%;
    padding:16px;
    border:none;
    border-radius:12px;
    background:#ff7300;
    color:black;
    font-size:20px;
    cursor:pointer;
    font-weight:bold;
}

.login-btn:hover{
    opacity:0.9;
}

.separator{
    margin:35px 0;
    display:flex;
    align-items:center;
    color:#555;
}

.separator::before,
.separator::after{
    content:"";
    flex:1;
    height:1px;
    background:#222;
}

.separator span{
    margin:0 15px;
}

.google-btn{
    width:100%;
    padding:15px;
    border-radius:12px;
    background:transparent;
    border:1px solid #222;
    color:white;
    cursor:pointer;
}

.register{
    text-align:center;
    margin-top:30px;
    color:#777;
}

.error{
    color:red;
    text-align:center;
    margin-bottom:20px;
}

</style>

</head>
<body>

<div class="login-container">

    <div class="logo"></div>

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

            <div>
                <input type="checkbox">
                Ricordami
            </div>

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

    <button class="google-btn">
        Continua con Google
    </button>

    <div class="register">
        Non hai un account?
        <a class="link" href="register">Registrati</a>
    </div>

</div>

</body>
</html>
