var cartItemsEl = document.querySelector(".cart-items");

if (cartItemsEl) {
    cartItemsEl.addEventListener("click", function(e) {

        var btn = e.target.closest("[data-action]");
        if (!btn) return;

        var action     = btn.dataset.action;
        var idProdotto = btn.dataset.id;

        // Svuota carrello
        if (action === "svuota") {
            fetch(contextPath + "/Carrello?action=svuota", {
                headers: { "X-Requested-With": "XMLHttpRequest" }
            })
            .then(function(res) {
                if (!res.ok) throw new Error("Risposta server non valida: " + res.status);
                location.reload();
            })
            .catch(function(err) {
                console.error("Errore svuota carrello:", err);
                alert("Si e' verificato un errore. Riprova.");
            });
            return;
        }

        // Incrementa / Decrementa / Rimuovi
        var cartItem = document.getElementById("cart-item-" + idProdotto);
        var url      = contextPath + "/Carrello?action=" + action + "&idProdotto=" + idProdotto;

        if (cartItem) {
            cartItem.querySelectorAll("button").forEach(function(b) { b.disabled = true; });
        }

        fetch(url, { headers: { "X-Requested-With": "XMLHttpRequest" } })
        .then(function(res) {
            if (!res.ok) throw new Error("Risposta server non valida: " + res.status);
            return res.json();
        })
        .then(function(data) {

            if (data.quantita === 0) {
                var itemEl = document.getElementById("cart-item-" + data.idProdotto);
                if (itemEl) itemEl.remove();

                if (data.numProdotti === 0) {
                    location.reload();
                    return;
                }
            } else {
                var qtyEl = document.getElementById("qty-" + data.idProdotto);
                if (qtyEl) qtyEl.textContent = data.quantita;

                if (cartItem) {
                    cartItem.querySelectorAll("button").forEach(function(b) { b.disabled = false; });
                }
            }

            var numEl    = document.getElementById("summary-num-prodotti");
            var totaleEl = document.getElementById("summary-totale");
			aggiornaBadgeCarrello(data.numProdotti);

            if (numEl)    numEl.textContent    = data.numProdotti;
            if (totaleEl) totaleEl.textContent = "\u20AC " + data.totale.toFixed(2).replace(".", ",");
        })
        .catch(function(err) {
            console.error("Errore AJAX carrello:", err);
            if (cartItem) {
                cartItem.querySelectorAll("button").forEach(function(b) { b.disabled = false; });
            }
            alert("Si e' verificato un errore. Riprova.");
        });
    });
}