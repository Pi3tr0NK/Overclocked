<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Overclocked - Admin</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/admin/dashboard.css">


<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagination.css">
</head>

<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="admin-layout">

    <!-- SIDEBAR -->

    <aside class="sidebar">

        <div class="menu-title">
            PANORAMICA
        </div>

        <a href="#" class="menu-item active">
            Dashboard Prodotti
        </a>


        <div class="menu-title">
            VENDITE
        </div>

        <a href="${pageContext.request.contextPath}/admin/ordini" class="menu-item">
            Ordini
        </a>

        <a href="${pageContext.request.contextPath}/admin/utenti" class="menu-item">
            Utenti
        </a>

    </aside>

    <!-- CONTENUTO -->

    <main class="content">

        <!-- HEADER -->

        <div class="page-header">

            <div>

                <h1>Gestione Prodotti</h1>

            </div>


             <form class="act-button" action="${pageContext.request.contextPath}/admin/aggiungiProdotto?action=aggiungiView" method="post">
    				<button class="new-product-btn" type="submit">+ Nuovo prodotto</button>
			</form>

        </div>

        <!-- STATS -->

        <div class="stats-grid">

            <div class="stat-card">

                <h2><c:out value="${numProdotti}"/></h2>

                <span>
                    Prodotti caricati
                </span>

            </div>

            <div class="stat-card">

                <h2><c:out value="${numOrdini}"/></h2>

                <span>
                    Ordini nell'ultimo mese
                </span>

            </div>

            <div class="stat-card">

                <h2><c:out value="${numUtenti}"/></h2>

                <span>
                    Utenti registrati
                </span>

            </div>

            <div class="stat-card">

                <h2><c:out value="${numProdottiEsauriti}"/></h2>

                <span>
                    Prodotti esauriti
                </span>

            </div>

        </div>

		<div class="filters">
		
		    <form method="get"
		          action="${pageContext.request.contextPath}/admin/dashboard">
		
		        <!-- Categoria -->
		        <select name="categoria" onchange="this.form.submit()">
		
		            <option value="">
		                Tutte le categorie
		            </option>
		
		            <option value="CPU"
		                ${param.categoria == 'CPU' ? 'selected' : ''}>
		                CPU
		            </option>
		
		            <option value="GPU"
		                ${param.categoria == 'GPU' ? 'selected' : ''}>
		                GPU
		            </option>
		
		            <option value="RAM"
		                ${param.categoria == 'RAM' ? 'selected' : ''}>
		                RAM
		            </option>
		
		            <option value="STORAGE"
		                ${param.categoria == 'STORAGE' ? 'selected' : ''}>
		                Storage
		            </option>
		
		            <option value="MOBO"
		                ${param.categoria == 'MOBO' ? 'selected' : ''}>
		                Scheda Madre
		            </option>
		
		            <option value="PSU"
		                ${param.categoria == 'PSU' ? 'selected' : ''}>
		                Alimentatore
		            </option>
		
		            <option value="CASE"
		                ${param.categoria == 'CASE' ? 'selected' : ''}>
		                Case
		            </option>
		
		            <option value="DISSIPATORE"
		                ${param.categoria == 'DISSIPATORE' ? 'selected' : ''}>
		                Dissipatore
		            </option>
		
		        </select>
		
		        <!-- Attivo -->
		        <select name="attivo" onchange="this.form.submit()">
		
		            <option value="">
		                Tutti gli stati
		            </option>
		
		            <option value="true"
		                ${param.attivo == 'true' ? 'selected' : ''}>
		                Attivo
		            </option>
		
		            <option value="false"
		                ${param.attivo == 'false' ? 'selected' : ''}>
		                Disattivato
		            </option>
		
		        </select>
		
		    </form>
		
		</div>

        <!-- TABELLA -->

        <div class="table-container">

            <table class="product-table">

                <thead>

                    <tr>

                        <th>Prodotto</th>
                        <th>Brand</th>
                        <th>Categoria</th>
                        <th>Prezzo</th>
                        <th>Stock</th>
                        <th>Attivo</th>
                        <th>Azioni</th>

                    </tr>

                </thead>

                <tbody>
				
				<c:forEach var="p" items="${prodotto}">
				 <tr>

                        <td> <c:out value="${p.nome}"/> </td>
                        <td><c:out value="${p.marca}"/> </td>
                        <td><c:out value="${p.categoria}"/></td>
                        <td><c:out value="${p.prezzo}"/></td>
                        <td><c:out value="${p.stock}"/></td>
                        
                        
                        <td> 
                        <c:choose>       
                        	<c:when test="${p.attivo}">
                				Si
            				</c:when>
            				<c:otherwise>
            					No
            				</c:otherwise>
            			</c:choose>
            			</td>

                        <td>
                        
                			<form class ="act-button" action="${pageContext.request.contextPath}/admin/aggiungiProdotto?action=modificaView&id=${p.idProdotto}&categoria=${p.categoria}" method="post">
    							<button type="submit">Modifica</button>
							</form>
                            
                        <c:choose>       
                        	<c:when test="${p.attivo}">

                			<form class ="act-button" action="${pageContext.request.contextPath}/admin/aggiungiProdotto?action=disattiva&id=${p.idProdotto}" method="post">
    							<button type="submit">Disattiva</button>
							</form>
							
            				</c:when>
            				<c:otherwise>
            				
            					<form class ="act-button" action="${pageContext.request.contextPath}/admin/aggiungiProdotto?action=attiva&id=${p.idProdotto}" method="post">
            					<button>Attiva</button>
            					</form>
            					
            				</c:otherwise>
            			</c:choose>


                        </td>

                  </tr>
				</c:forEach>
               
                </tbody>

            </table>

        </div>

		<jsp:include page="/WEB-INF/views/components/pagination.jsp" />

    </main>

</div>

</body>
</html>