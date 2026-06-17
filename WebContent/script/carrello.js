var cartItemsEl = document.querySelector(".cart-items");

if (cartItemsEl) {
    cartItemsEl.addEventListener("click", function (e) {
        var btn = e.target.closest("[data-action]");
        if (!btn) return;

        var action     = btn.dataset.action;
        var idProdotto = btn.dataset.id;
        var url        = contextPath + "/Carrello?action=" + action + "&idProdotto=" + idProdotto;

        var cartItem = document.getElementById("cart-item-" + idProdotto);
        if (cartItem) {
            cartItem.querySelectorAll("button").forEach(function(b) { b.disabled = true; });
        }

        fetch(url, {
            headers: { "X-Requested-With": "XMLHttpRequest" }
        })
        .then(function(r) {
            if (!r.ok) throw new Error("Risposta server non valida: " + r.status);
            return r.json();
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

            if (numEl)    numEl.textContent    = data.numProdotti;
            if (totaleEl) totaleEl.textContent = "€ " + data.totale.toFixed(2).replace(".", ",");
        })
        .catch(function(err) {
            console.error("Errore AJAX carrello:", err);
            if (cartItem) {
                cartItem.querySelectorAll("button").forEach(function(b) { b.disabled = false; });
            }
            alert("Si è verificato un errore. Riprova.");
        });
    });
}