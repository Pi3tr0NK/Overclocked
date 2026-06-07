<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Dashboard Admin</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/admin/dashboard.css">

</head>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">

<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="admin-layout">

    <!-- SIDEBAR -->

    <aside class="sidebar">

        <div class="logo">
            ⚡ OVERCLOCKED
        </div>

        <div class="menu-title">
            PANORAMICA
        </div>

        <a href="#" class="menu-item active">
            Dashboard
        </a>

        <div class="menu-title">
            CATALOGO
        </div>

        <a href="#" class="menu-item">
            Prodotti
        </a>

        <a href="#" class="menu-item">
            Categorie
        </a>

        <div class="menu-title">
            VENDITE
        </div>

        <a href="#" class="menu-item">
            Ordini
        </a>

        <a href="#" class="menu-item">
            Utenti
        </a>

        <div class="sidebar-bottom">

            <a href="#" class="menu-item">
                Impostazioni
            </a>

            <a href="#" class="menu-item">
                Esci
            </a>

        </div>

    </aside>

    <!-- CONTENUTO -->

    <main class="content">

        <!-- HEADER -->

        <div class="page-header">

            <div>

                <h1>Gestione Prodotti</h1>

            </div>

            <a class="new-product-btn"
               href="${pageContext.request.contextPath}/admin/aggiungiProdotto">

                + Nuovo prodotto

            </a>

        </div>

        <!-- STATS -->

        <div class="stats-grid">

            <div class="stat-card">

                <h2>${numProdotti}</h2>

                <span>
                    Prodotti totali
                </span>

            </div>

            <div class="stat-card">

                <h2>${numOrdini}</h2>

                <span>
                    Ordini nell'ultimo mese
                </span>

            </div>

            <div class="stat-card">

                <h2>${numUtenti}</h2>

                <span>
                    Utenti registrati
                </span>

            </div>

            <div class="stat-card">

                <h2>${numProdottiEsauriti}</h2>

                <span>
                    Prodotti esauriti
                </span>

            </div>

        </div>

        <!-- FILTRI -->

        <div class="filters">

            <select>

                <option>
                    Tutte le categorie
                </option>

            </select>

            <select>

                <option>
                    Tutti gli stati
                </option>

            </select>

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

                        <td>${p.nome}</td>
                        <td>${p.marca}</td>
                        <td>${p.categoria}</td>
                        <td>${p.prezzo}</td>
                        <td>${p.stock}</td>
                        
                        
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

                            <button>
                                Modifica
                            </button>

                            <button>
                                Elimina
                            </button>

                        </td>

                  </tr>
				</c:forEach>
               
                </tbody>

            </table>

        </div>

        <!-- PAGINAZIONE -->

        <div class="pagination">

            <button>
                <
            </button>

            <button class="active">
                1
            </button>

            <button>
                2
            </button>

            <button>
                3
            </button>

            <button>
                >
            </button>

        </div>

    </main>

</div>

</body>
</html>