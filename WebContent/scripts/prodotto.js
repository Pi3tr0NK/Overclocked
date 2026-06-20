const mainImage = document.querySelector(".main-image img");
const thumbs = document.querySelectorAll(".thumb img");

thumbs.forEach(img => {
    img.addEventListener("mouseenter", function() {
        mainImage.src = this.src;
        document.querySelectorAll(".thumb").forEach(t =>
            t.classList.remove("active")
        );
        this.parentElement.classList.add("active");
    });
});


// Aggiunta al carrello
function addToCart(idProdotto) {
    var qty = document.getElementById("quantita").value;
    var params = "aggiungi=" + idProdotto + "&quantita=" + qty;
    loadAjaxDoc(contextPath + "/carrello/add", "GET", params, handleAddCart);
}

function handleAddCart(request) {
    var response = JSON.parse(request.responseText);
    if (response.success) {
		aggiornaBadgeCarrello(response.numProdotti);
        loadAjaxDoc(contextPath + "/carrello/summary", "GET", null, handleCartSummary);
    } else {
        console.error("Errore durante l'aggiunta al carrello.");
    }
}


// barra laterale del carrello
function handleCartSummary(request) {
    var data = JSON.parse(request.responseText);
    renderCartSidebar(data);
    openCartSidebar();
}

function renderCartSidebar(data) {
	console.log(data);
	
    var itemsEl = document.getElementById("sidebarItems");
    var totalEl = document.getElementById("sidebarTotal");
    var badgeEl = document.getElementById("cartBadge");

    if (!itemsEl) return;

    if (data.items.length === 0) {
        itemsEl.innerHTML = '<p class="sidebar-empty">Il carrello è vuoto.</p>';
    } else {
        itemsEl.innerHTML = data.items.map(function(item) {
            return '<div class="sidebar-item">'
                + '<img src="' + item.immagine + '" alt="' + item.nome + '" class="sidebar-item-img">'
                + '<div class="sidebar-item-info">'
                +   '<div class="sidebar-item-name">' + item.marca + ' ' + item.nome + '</div>'
                +   '<div class="sidebar-item-qty">Qtà: ' + item.quantita + '</div>'
                +   '<div class="sidebar-item-price">€ ' + item.prezzoScontato + '</div>'
                + '</div>'
                + '</div>';
        }).join("");
    }

    totalEl.textContent = "€ " + data.totale;

    if (badgeEl) {
        badgeEl.textContent = data.numProdotti;
        badgeEl.style.display = data.numProdotti > 0 ? "flex" : "none";
    }
}

function openCartSidebar() {
    document.getElementById("cartSidebar").classList.add("open");
    document.getElementById("cartOverlay").classList.add("visible");
    document.body.classList.add("sidebar-open");
}

function closeCartSidebar() {
    document.getElementById("cartSidebar").classList.remove("open");
    document.getElementById("cartOverlay").classList.remove("visible");
    document.body.classList.remove("sidebar-open");
}

document.addEventListener("DOMContentLoaded", function() {
    var overlay = document.getElementById("cartOverlay");
    if (overlay) {
        overlay.addEventListener("click", closeCartSidebar);
    }
});


// AJAX
function createXMLHttpRequest() {
    var request;
    try {
        request = new XMLHttpRequest();
    } catch (e) {
        try {
            request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
                console.log("Il browser non supporta AJAX");
                return null;
            }
        }
    }
    return request;
}

function loadAjaxDoc(url, method, params, cFuction) {
    var request = createXMLHttpRequest();
    if (request) {

        request.onreadystatechange = function() {
            if (this.readyState == 4) {
                if (this.status == 200) {
                    cFuction(this);
                } else {
                    if (this.status == 0) {
                        console.log("Problemi nell'esecuzione della richiesta: nessuna risposta ricevuta nel tempo limite");
                    } else {
                        console.log("Problemi nell'esecuzione della richiesta:\n" + this.statusText);
                    }
                    return null;
                }
            }
        };

        setTimeout(function() {    
            if (request.readyState < 4) {
                request.abort();
            }
        }, 15000);

        if (method.toLowerCase() == "get") {
            if (params) {
                request.open("GET", url + "?" + params, true);
            } else {
                request.open("GET", url, true);
            }
            request.setRequestHeader("Connection", "close");
            request.setRequestHeader("X-Requested-With", "XMLHttpRequest"); 
            request.send(null);
        } else {
            if (params) {
                request.open("POST", url, true);
                request.setRequestHeader("Connection", "close");
                request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                request.send(params);
            } else {
                console.log("Usa GET se non ci sono parametri!");
                return null;
            }
        }
 	}
 }
