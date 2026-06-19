window.aggiornaBadgeCarrello = function(nuovoValore) {
    var badge = document.querySelector(".cart-badge");
    if (nuovoValore > 0) {
        if (badge) {
            badge.textContent = nuovoValore;
        } else {
			var link = document.querySelector(".nav-right a[href*='Carrello']");
			if (link) {
			    var span = document.createElement("span");
			    span.className = "cart-badge";
			    span.textContent = nuovoValore;
			    var img = link.querySelector("img");
			    img.insertAdjacentElement("afterend", span);
			}
        }
    } else {
        if (badge) badge.remove();
    }
};