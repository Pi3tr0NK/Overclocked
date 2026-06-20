<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div id="cartOverlay"></div>

<div id="cartSidebar" role="dialog" aria-modal="true" aria-label="Riepilogo carrello">

  <div class="sidebar-header">
    <h2 style="margin:0">🛒 Carrello</h2>
    <button class="sidebar-close" onclick="closeCartSidebar()" aria-label="Chiudi">✕</button>
  </div>

  <div class="sidebar-items" id="sidebarItems"></div>

  <div class="sidebar-footer">
    <div class="sidebar-total-row">
      <span class="sidebar-total-label">Totale</span>
      <%-- Riusa .product-price del tema per il colore arancione --%>
      <span class="product-price" id="sidebarTotal">€ 0,00</span>
    </div>
    <a href="${pageContext.request.contextPath}/common/pagamento" class="sidebar-btn-checkout">
      Vai al checkout →
    </a>
    <a href="${pageContext.request.contextPath}/Carrello" class="sidebar-btn-view">
      Visualizza carrello completo
    </a>
  </div>

</div>

