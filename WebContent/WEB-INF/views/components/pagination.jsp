<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>


<c:set var="inizio"
	value="${paginaCorrente - 2 > 1 ? paginaCorrente - 2 : 1}" />

<c:set var="fine" value="${inizio + 4}" />

<c:if test="${fine > totalePagine}">
	<c:set var="fine" value="${totalePagine}" />
</c:if>

<div class="pagination">

	<c:if test="${paginaCorrente > 1}">
		<a href="?pagina=${paginaCorrente-1}"> &lt; </a>
	</c:if>

	<c:forEach begin="${inizio}" end="${fine}" var="p">

		<c:url var="pageUrl" value="">
			<c:param name="pagina" value="${p}" />

			<c:if test="${not empty param.categoria}">
				<c:param name="categoria" value="${param.categoria}" />
			</c:if>

			<c:if test="${not empty param.marca}">
				<c:param name="marca" value="${param.marca}" />
			</c:if>

			<c:if test="${not empty param.prezzo}">
				<c:param name="prezzo" value="${param.prezzo}" />
			</c:if>

			<c:if test="${not empty param.core}">
				<c:param name="core" value="${param.core}" />
			</c:if>

			<c:if test="${not empty param.cerca}">
				<c:param name="cerca" value="${param.cerca}" />
			</c:if>

			<c:if test="${not empty param.attivo}">
				<c:param name="attivo" value="${param.attivo}" />
			</c:if>

			<c:if test="${not empty param.frequenza}">
				<c:param name="frequenza" value="${param.frequenza}" />
			</c:if>

			<c:if test="${not empty param.certificazione}">
				<c:param name="certificazione" value="${param.certificazione}" />
			</c:if>

			<c:if test="${not empty param.potenza}">
				<c:param name="potenza" value="${param.potenza}" />
			</c:if>

			<c:if test="${not empty param.modulare}">
				<c:param name="modulare" value="${param.modulare}" />
			</c:if>

			<c:if test="${not empty param.tipo}">
				<c:param name="tipo" value="${param.tipo}" />
			</c:if>
			
			<c:if test="${not empty param.capacita}">
				<c:param name="capacita" value="${param.capacita}" />
			</c:if>
			
			<c:if test="${not empty param.formato}">
				<c:param name="formato" value="${param.formato}" />
			</c:if>
			
			<c:if test="${not empty param.colore}">
				<c:param name="colore" value="${param.colore}" />
			</c:if>
			
			<c:if test="${not empty param.vram}">
				<c:param name="vram" value="${param.vram}" />
			</c:if>
			
			<c:if test="${not empty param.pcie}">
				<c:param name="pcie" value="${param.pcie}" />
			</c:if>
			
			<c:if test="${not empty param.slotram}">
				<c:param name="slotram" value="${param.slotram}" />
			</c:if>
			
			<c:if test="${not empty param.nvme}">
				<c:param name="nvme" value="${param.nvme}" />
			</c:if>
			
			<c:if test="${not empty param.tecnologia}">
				<c:param name="tecnologia" value="${param.tecnologia}" />
			</c:if>
			
			<c:if test="${not empty param.ordinamento}">
				<c:param name="ordinamento" value="${param.ordinamento}" />
			</c:if>

			<c:if test="${not empty param.cercaNome}">
				<c:param name="cercaNome" value="${param.cercaNome}" />
			</c:if>
			
			<c:if test="${not empty param.cercaCognome}">
				<c:param name="cercaCognome" value="${param.cercaCognome}" />
			</c:if>
			
			<c:if test="${not empty param.cercaEmail}">
				<c:param name="cercaEmail" value="${param.cercaEmail}" />
			</c:if>

			<c:if test="${not empty param.dataInizio}">
				<c:param name="dataInizio" value="${param.dataInizio}" />
			</c:if>

			<c:if test="${not empty param.dataFine}">
				<c:param name="dataFine" value="${param.dataFine}" />
			</c:if>
			
			<c:if test="${not empty param.stato}">
				<c:param name="stato" value="${param.stato}" />
			</c:if>
						
		</c:url>

		<a href="${pageUrl}" class="${p == paginaCorrente ? 'active' : ''}">
			${p} </a>

	</c:forEach>

	<c:if test="${paginaCorrente < totalePagine}">
		<a href="?pagina=${paginaCorrente+1}"> &gt; </a>
	</c:if>

</div>