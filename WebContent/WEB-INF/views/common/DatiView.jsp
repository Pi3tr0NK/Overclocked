<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Overclocked</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/tema.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profilo.css">
</head>

<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>


<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="profilo-wrapper">

    <h1 class="profilo-titolo">Il mio account</h1>

    <div class="profilo-layout">

        <!-- NAVBAR SINISTRA -->
        <nav class="profilo-nav">
            <a href="${pageContext.request.contextPath}/common/profilo?view=ordini"
               class="profilo-nav-link">Ordini e fatture</a>
            <a href="${pageContext.request.contextPath}/common/profilo?view=resi"
               class="profilo-nav-link">Resi</a>
            <a href="${pageContext.request.contextPath}/common/profilo?view=dati"
               class="profilo-nav-link active">I miei dati</a>
            <a href="${pageContext.request.contextPath}/logout"
               class="profilo-nav-link logout">Logout &#x2192;</a>
        </nav>

        <!-- CONTENUTO A DESTRA -->
        <div class="profilo-content">

            <div class="profilo-card-title">I miei dati</div>

            <c:if test="${not empty successo}">
                <div class="profilo-alert success">${successo}</div>
            </c:if>
            <c:if test="${not empty errore}">
                <div class="profilo-alert error">${errore}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/common/profilo" method="post" novalidate>
                <input type="hidden" name="action" value="aggiornaDati"/>

                <!-- DATO PERSONALI -->
                <div class="profilo-section-title">Dati personali</div>

                <div class="profilo-grid2">
                    <div class="profilo-field">
                        <label>Nome</label>
                        <input type="text" name="nome" value="${utente.nome}" required/>
                    </div>
                    <div class="profilo-field">
                        <label>Cognome</label>
                        <input type="text" name="cognome" value="${utente.cognome}" required/>
                    </div>
                </div>

                <div class="profilo-grid2">
                    <div class="profilo-field">
                        <label>Email <span class="profilo-readonly">(non modificabile)</span></label>
                        <input type="text" value="${utente.email}" disabled/>
                    </div>
                    <div class="profilo-field">
                        <label>Cellulare</label>
                        <input type="text" name="cellulare" value="${utente.cellulare}"/>
                    </div>
                </div>

                
                <div class="profilo-section-title">Indirizzo</div>

                <div class="profilo-field">
                    <label>Via e numero civico</label>
                    <input type="text" name="viaNumciv"
                           value="${utente.indirizzo.viaNumciv}" required/>
                </div>

                <div class="profilo-grid2">
                    <div class="profilo-field">
                        <label>Città</label>
                        <input type="text" name="citta"
                               value="${utente.indirizzo.citta}" required/>
                    </div>
                    <div class="profilo-field">
                        <label>Provincia</label>
                        <input type="text" name="provincia"
                               value="${utente.indirizzo.provincia}" required/>
                    </div>
                </div>

                <div class="profilo-grid2">
                    <div class="profilo-field">
                        <label>CAP</label>
                        <input type="text" name="codicePostale"
                               value="${utente.indirizzo.codicePostale}" required/>
                    </div>
                    <div class="profilo-field">
                        <label>Paese</label>
                        <input type="text" name="paese"
                               value="${utente.indirizzo.paese}" required/>
                    </div>
                </div>

                <div class="profilo-field">
                    <label>Informazioni aggiuntive</label>
                    <input type="text" name="datiPlus"
                           value="${utente.indirizzo.datiPlus}"
                           placeholder="Es. Scala B, interno 4"/>
                </div>

                <button type="submit" class="btn-salva">Salva modifiche</button>

            </form>

        </div>

    </div>

</div>

<jsp:include page="/WEB-INF/views/components/footer.jsp" />
<script src="${pageContext.request.contextPath}/scripts/profilo.js"></script>
</body>
</html>
