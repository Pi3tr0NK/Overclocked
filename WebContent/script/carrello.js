const ctx = "${pageContext.request.contextPath}";

    document.querySelector(".cart-items")?.addEventListener("click", function (e) {
        const btn = e.target.closest("[data-action]");
        if (!btn) return;

        const action     = btn.dataset.action;
        const idProdotto = btn.dataset.id;
        const url        = ctx + "/Carrello?action=" + action + "&idProdotto=" + idProdotto;

        // Disabilita temporaneamente i bottoni dell'item per evitare click multipli
        const cartItem = document.getElementById("cart-item-" + idProdotto);
        if (cartItem) {
            cartItem.querySelectorAll("button").forEach(b => b.disabled = true);
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
                // Rimuovi la riga dal DOM
                const itemEl = document.getElementById("cart-item-" + data.idProdotto);
                if (itemEl) itemEl.remove();

                // Se il carrello è ora completamente vuoto ricarica per
                // mostrare la schermata "carrello vuoto"
                if (data.numProdotti === 0) {
                    location.reload();
                    return;
                }
            } else {
                // Aggiorna solo il contatore della quantità
                const qtyEl = document.getElementById("qty-" + data.idProdotto);
                if (qtyEl) qtyEl.textContent = data.quantita;

                // Riabilita i bottoni
                if (cartItem) {
                    cartItem.querySelectorAll("button").forEach(b => b.disabled = false);
                }
            }

            // Aggiorna il riepilogo laterale
            const numEl    = document.getElementById("summary-num-prodotti");
            const totaleEl = document.getElementById("summary-totale");

            if (numEl)    numEl.textContent    = data.numProdotti;
            if (totaleEl) totaleEl.textContent = "€ " + data.totale.toFixed(2).replace(".", ",");
        })
        .catch(function(err) {
            console.error("Errore AJAX carrello:", err);
            // In caso di errore riabilita i bottoni e avvisa l'utente
            if (cartItem) {
                cartItem.querySelectorAll("button").forEach(b => b.disabled = false);
            }
            alert("Si è verificato un errore. Riprova.");
        });
    });