<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Gestione Utenti - Admin</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagination.css">

</head>
<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="admin-layout">

    <!-- SIDEBAR -->
    <aside class="sidebar">

        <div class="menu-title">PANORAMICA</div>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item">
            Dashboard Prodotti
        </a>

        <div class="menu-title">VENDITE</div>
        <a href="${pageContext.request.contextPath}/admin/ordini" class="menu-item">
            Ordini
        </a>
        <a href="${pageContext.request.contextPath}/admin/utenti" class="menu-item active">
            Utenti
        </a>

    </aside>

    <!-- CONTENUTO -->
    <main class="content">

        <!-- HEADER -->
        <div class="page-header">
            <div>
                <h1>Gestione Utenti</h1>
                <p>Amministra gli account e i permessi</p>
            </div>
        </div>

        <!-- ALERT -->
        <c:if test="${not empty errore}">
            <div class="error">${errore}</div>
        </c:if>
        <c:if test="${not empty param.success}">
            <div class="success">Operazione completata con successo!</div>
        </c:if>

        <!-- STATS -->
        <div class="stats-grid">
            <div class="stat-card">
                <h2>${numUtenti}</h2>
                <span>Utenti totali</span>
            </div>
            <div class="stat-card">
                <h2>${numAdmin}</h2>
                <span>Amministratori</span>
            </div>
            <div class="stat-card">
                <h2>${numUser}</h2>
                <span>Clienti</span>
            </div>
        </div>

        <!-- FILTRI -->
        <div class="filters">
            <select id="filtroRuolo" onchange="filtraTabella()">
                <option value="">Tutti i ruoli</option>
                <option value="ADMIN">Admin</option>
                <option value="USER">User</option>
            </select>
            <select id="filtroStato" onchange="filtraTabella()">
                <option value="">Tutti gli stati</option>
                <option value="attivo">Attivo</option>
                <option value="disattivato">Disattivato</option>
            </select>
        </div>

        <!-- TABELLA -->
        <div class="table-container">
            <table class="product-table" id="tabellaUtenti">

                <thead>
                    <tr>
                        <th>Utente</th>
                        <th>Ruolo</th>
                        <th>Azioni</th>
                    </tr>
                </thead>

                <tbody>
                <c:forEach var="u" items="${utenti}">
                    <tr
                        data-ruolo="${u.ruolo}"
                    >

                        <td>
                            <div class="user-cell">
                                <div class="avatar">
                                    ${u.nome.charAt(0)}${u.cognome.charAt(0)}
                                </div>
                                <div>
                                    <div class="user-name">${u.nome} ${u.cognome}</div>
                                    <div class="user-email">${u.email}</div>
                                </div>
                            </div>
                        </td>

                        <td>
                            <span class="role-badge role-${u.ruolo}">
                                ${u.ruolo}
                            </span>
                        </td>



                        <td>

                            <%-- Modifica --%>
                            <form action="${pageContext.request.contextPath}/admin/utenti" method="post" style="display:inline;">
                                <input type="hidden" name="action"    value="modificaView"/>
                                <input type="hidden" name="idUtente"  value="${u.idUtente}"/>
                                <button type="submit" class="action-btn">Modifica</button>
                            </form>

							<%-- Visualizza gli ordini --%>
                            <form action="${pageContext.request.contextPath}/admin/ordini" method="post" style="display:inline;">
                                <input type="hidden" name="idUtente"  value="${u.idUtente}"/>
                                <button type="submit" class="action-btn">Visualizza Ordini</button>
                            </form>
                            
                           <%-- Promuovi admin (solo per clienti) --%>
                            <c:if test="${u.ruolo == 'USER'}">
                                <form action="${pageContext.request.contextPath}/admin/utenti" method="post" style="display:inline;">
                                    <input type="hidden" name="action"   value="promuovi"/>
                                    <input type="hidden" name="idUtente" value="${u.idUtente}"/>
                                    <button type="submit" class="action-btn promote">Promuovi admin</button>
                                </form>
                            </c:if>

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