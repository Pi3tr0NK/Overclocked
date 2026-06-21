<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Overclocked</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tema.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/register.css">
	
	<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />
</head>




<body>

    <a href="${pageContext.request.contextPath}/" class="brand-header"><h2>OVERCLOCKED</h2></a>

	<div class="register-container">

		<div class="logo">
			<img src="${pageContext.request.contextPath}/images/register.png" alt="">
		</div>

		<h1>Crea il tuo account</h1>

		<div class="orange-line"></div>

		<p class="subtitle">Unisciti a Overclocked e inizia a costruire la
			tua build</p>
		
		
					    
			    <c:if test="${not empty errors}">
			        <span class="field-error">
			            ${errors}
			        </span>
			    </c:if>
			    
		<form action="register" method="post" novalidate>

			<!-- DATI PERSONALI -->

			<div class="section-title">DATI PERSONALI</div>

			<div class="row">

				<div class="field">
					<label> Nome <span class="required">*</span>
					</label> <input type="text" name="nome" required>
				</div>

				<div class="field">
					<label> Cognome <span class="required">*</span>
					</label> <input type="text" name="cognome" required>
				</div>

			</div>

			<div class="field">
			    <label> Email <span class="required">*</span></label> 
			    <input type="email" name="email" required>
			</div>

			<div class="row">

				<div class="field">

					<label> Password <span class="required">*</span>
					</label> <input type="password" name="password" required>

				</div>

				<div class="field">

					<label> Conferma password <span class="required">*</span>
					</label> <input type="password" name="confermaPassword" required>

				</div>

			</div>

			<div class="small-text">Minimo 8 caratteri, una maiuscola e un
				numero</div>

			<div class="field">

				<label>Cellulare</label> <input type="text" name="cellulare">

			</div>

			<!-- INDIRIZZO -->

			<div class="section-title">INDIRIZZO DI SPEDIZIONE</div>

			<div class="field">

				<label> Via e numero civico <span class="required">*</span>
				</label> <input type="text" name="via" required>

			</div>

			<div class="field">

				<label>Dati aggiuntivi</label> <input type="text" name="datiPlus">

			</div>

			<div class="row">

				<div class="field">

					<label> Città <span class="required">*</span>
					</label> <input type="text" name="citta" required>

				</div>

				<div class="field">

					<label> Provincia <span class="required">*</span>
					</label> <input type="text" name="provincia" required>

				</div>

				<div class="field">

					<label> CAP <span class="required">*</span>
					</label> <input type="text" name="cap" required>

				</div>

			</div>

			<div class="field">

				<label> Paese <span class="required">*</span>
				</label> <input type="text" name="paese" value="Italia" required>

			</div>

			<button class="register-btn" type="submit">Crea account</button>

		</form>

		<div class="login-link">

			Hai già un account? <a href="indexlogin"> Accedi </a>

		</div>

	</div>

<script src="${pageContext.request.contextPath}/scripts/registerCheck.js"></script>

</body>


</html>