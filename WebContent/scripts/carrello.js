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
            });
            return;
        }

        var cartItem = document.getElementById("cart-item-" + idProdotto);
        var qtyEl    = document.getElementById("qty-" + idProdotto);

        // Blocco preventivo: non superare lo stock disponibile
        if (action === "incrementa" && qtyEl) {
            var stock      = parseInt(qtyEl.dataset.stock, 10);
            var currentQty = parseInt(qtyEl.textContent, 10);

            if (!isNaN(stock) && currentQty >= stock) {
                return; 
            }
        }

        var url = contextPath + "/Carrello?action=" + action + "&idProdotto=" + idProdotto;

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
                var qtyEl2 = document.getElementById("qty-" + data.idProdotto);
                if (qtyEl2) qtyEl2.textContent = data.quantita;

                if (cartItem) {
                    cartItem.querySelectorAll("button").forEach(function(b) { b.disabled = false; });

                    var stockVal = qtyEl2 ? parseInt(qtyEl2.dataset.stock, 10) : NaN;
                    if (!isNaN(stockVal) && data.quantita >= stockVal) {
                        var incBtn = cartItem.querySelector('[data-action="incrementa"]');
                        if (incBtn) incBtn.disabled = true;
                    }
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
        });
    });
}