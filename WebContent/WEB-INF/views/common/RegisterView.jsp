<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registrazione - Overclocked</title>

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
    min-height:100vh;
    padding:40px 0;
}

.register-container{
    width:500px;
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

    display:flex;
    justify-content:center;
    align-items:center;

    color:#ff7300;
    font-size:28px;
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

.section-title{
    display:flex;
    align-items:center;
    gap:10px;

    color:#ff7300;
    font-size:14px;
    font-weight:bold;

    margin:30px 0 20px 0;
}

.section-title::after{
    content:"";
    flex:1;
    height:1px;
    background:#222;
}

.row{
    display:flex;
    gap:15px;
}

.field{
    width:100%;
    margin-bottom:20px;
}

label{
    display:block;
    margin-bottom:10px;
    color:#999;
    font-size:14px;
}

.required{
    color:#ff7300;
}

input{
    width:100%;
    padding:15px;
    background:#111;
    border:1px solid #222;
    border-radius:10px;
    color:white;
    font-size:14px;
}

input:focus{
    outline:none;
    border:1px solid #ff7300;
}

.small-text{
    color:#555;
    font-size:12px;
    margin-top:-10px;
    margin-bottom:20px;
}

.register-btn{
    width:100%;
    padding:16px;
    background:transparent;
    border:1px solid #444;
    border-radius:12px;
    color:white;
    font-size:16px;
    font-weight:bold;
    cursor:pointer;
    transition:0.3s;
}

.register-btn:hover{
    border-color:#ff7300;
    color:#ff7300;
}

.login-link{
    text-align:center;
    margin-top:25px;
    color:#777;
}

.login-link a{
    color:#ff7300;
    text-decoration:none;
    font-weight:bold;
}

.error{
    background:#2b0000;
    border:1px solid red;
    padding:15px;
    border-radius:10px;
    margin-bottom:25px;
}

.error p{
    color:#ff8080;
    margin-bottom:5px;
}

@media(max-width:600px){

    .register-container{
        width:95%;
        padding:25px;
    }

    .row{
        flex-direction:column;
        gap:0;
    }
}

</style>

</head>
<body>

<div class="register-container">

    <div class="logo">
        ⍟
    </div>

    <h1>Crea il tuo account</h1>

    <div class="orange-line"></div>

    <p class="subtitle">
        Unisciti a Overclocked e inizia a costruire la tua build
    </p>

    <%
    List<String> errors =
    (List<String>) request.getAttribute("errors");

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

    <form action="register" method="post">

        <!-- DATI PERSONALI -->

        <div class="section-title">
            DATI PERSONALI
        </div>

        <div class="row">

            <div class="field">
                <label>
                    Nome <span class="required">*</span>
                </label>

                <input type="text"
                       name="nome"
                       required>
            </div>

            <div class="field">
                <label>
                    Cognome <span class="required">*</span>
                </label>

                <input type="text"
                       name="cognome"
                       required>
            </div>

        </div>

        <div class="field">

            <label>
                Email <span class="required">*</span>
            </label>

            <input type="email"
                   name="email"
                   required>

        </div>

        <div class="row">

            <div class="field">

                <label>
                    Password <span class="required">*</span>
                </label>

                <input type="password"
                       name="password"
                       required>

            </div>

            <div class="field">

                <label>
                    Conferma password
                    <span class="required">*</span>
                </label>

                <input type="password"
                       name="confermaPassword"
                       required>

            </div>

        </div>

        <div class="small-text">
            Minimo 8 caratteri,
            una maiuscola e un numero
        </div>

        <div class="field">

            <label>Cellulare</label>

            <input type="text"
                   name="cellulare">

        </div>

        <!-- INDIRIZZO -->

        <div class="section-title">
            INDIRIZZO DI SPEDIZIONE
        </div>

        <div class="field">

            <label>
                Via e numero civico
                <span class="required">*</span>
            </label>

            <input type="text"
                   name="via"
                   required>

        </div>

        <div class="field">

            <label>Dati aggiuntivi</label>

            <input type="text"
                   name="datiPlus">

        </div>

        <div class="row">

            <div class="field">

                <label>
                    Città
                    <span class="required">*</span>
                </label>

                <input type="text"
                       name="citta"
                       required>

            </div>

            <div class="field">

                <label>
                    Provincia
                    <span class="required">*</span>
                </label>

                <input type="text"
                       name="provincia"
                       required>

            </div>

            <div class="field">

                <label>
                    CAP
                    <span class="required">*</span>
                </label>

                <input type="text"
                       name="cap"
                       required>

            </div>

        </div>

        <div class="field">

            <label>
                Paese
                <span class="required">*</span>
            </label>

            <input type="text"
                   name="paese"
                   value="Italia"
                   required>

        </div>

        <button class="register-btn"
                type="submit">

            Crea account

        </button>

    </form>

    <div class="login-link">

        Hai già un account?

        <a href="indexlogin">
            Accedi
        </a>

    </div>

</div>

</body>
</html>