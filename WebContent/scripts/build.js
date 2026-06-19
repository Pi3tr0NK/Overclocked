// ─────────────────────────────────────────────
//  AJAX
// ─────────────────────────────────────────────

function ajax(url, params, callback) {
    var req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (req.readyState !== 4) return;
        if (req.status === 200) {
            callback(req);
        } else {
            alert(req.status === 0
                ? "Nessuna risposta ricevuta nel tempo limite"
                : "Errore nella richiesta:\n" + req.statusText);
        }
    };
    setTimeout(function () { if (req.readyState < 4) req.abort(); }, 15000);
    req.open("GET", url + (params ? "?" + params : ""), true);
    req.setRequestHeader("Connection", "close");
    req.send(null);
}

function pcAjax(params, callback) {
    ajax("pcBuilder", params, callback);
}

// ─────────────────────────────────────────────
//  Prezzi
// ─────────────────────────────────────────────

function prezzoScontato(item) {
    var p = parseFloat(item.prezzo) || 0;
    var s = parseFloat(item.sconto) || 0;
    return p * (1 - s / 100);
}

function formattaPrezzoOption(item) {
    var scontato = prezzoScontato(item);
    if (item.sconto > 0) return "€" + scontato.toFixed(2) + " (-" + item.sconto + "%)";
    return "€" + parseFloat(item.prezzo).toFixed(2);
}

// ─────────────────────────────────────────────
//  Caricamento componenti
// ─────────────────────────────────────────────

function caricaCpus()            { pcAjax("action=getCpus",                              aggiornaCpus); }
function caricaMobos(cpuId)      { pcAjax("action=getMobos&cpuId=" + cpuId,              aggiornaMobos); }
function caricaRams(moboId)      { pcAjax("action=getRams&moboId=" + moboId,             aggiornaRams); }
function caricaGpus()            { pcAjax("action=getGpus",                              aggiornaGpus); }
function caricaStorages(moboId)  { pcAjax("action=getStorages&moboId=" + moboId,         aggiornaStorages); }
function caricaPsus(cpuId, gpuId){ pcAjax("action=getPsus&cpuId=" + cpuId + "&gpuId=" + gpuId, aggiornaPsus); }
function caricaCases(moboId)     { pcAjax("action=getCases&moboId=" + moboId,            aggiornaCases); }
function caricaDissipatori(cpuId){ pcAjax("action=getDissipatori&cpuId=" + cpuId,        aggiornaDissipatori); }

// ─────────────────────────────────────────────
//  Callback (una funzione generica + wrapper)
// ─────────────────────────────────────────────

function gestisciRisposta(request, errId, fn) {
    var response = JSON.parse(request.responseText);
    if (response.functionName === "errore") {
        document.getElementById(errId).innerHTML = response.result;
        return null;
    }
    return response.result;
}

function aggiornaCpus(req) {
    var items = gestisciRisposta(req, "errCpu");
    if (!items) return;
    popolaSelect("selCpu", items, "-- Seleziona una CPU --",
        function(i) { return i.idProdotto; },
        function(i) { return i.nome + " — " + i.socket + " — " + i.core + " core — " + formattaPrezzoOption(i); }
    );
}

function aggiornaMobos(req) {
    var items = gestisciRisposta(req, "errMobo");
    if (!items) return;
    popolaSelect("selMobo", items, "-- Seleziona una scheda madre --",
        function(i) { return i.idProdotto; },
        function(i) { return i.nome + " — " + i.chipset + " — " + i.formato + " — " + formattaPrezzoOption(i); }
    );
    abilitaSelect("selMobo", "num-mobo", "lbl-mobo");
}

function aggiornaRams(req) {
    var items = gestisciRisposta(req, "errRam");
    if (!items) return;
    popolaSelect("selRam", items, "-- Seleziona RAM --",
        function(i) { return i.idProdotto; },
        function(i) { return i.nome + " — " + i.capacita + " — " + i.tipo + " " + i.frequenza + " — " + formattaPrezzoOption(i); }
    );
    abilitaSelect("selRam", "num-ram", "lbl-ram");
}

function aggiornaGpus(req) {
    var items = gestisciRisposta(req, "errGpu");
    if (!items) return;
    popolaSelect("selGpu", items, "-- Seleziona una GPU --",
        function(i) { return i.idProdotto; },
        function(i) { return i.nome + " — " + i.vram + " " + i.tipoVram + " — " + formattaPrezzoOption(i); }
    );
    abilitaSelect("selGpu", "num-gpu", "lbl-gpu");
}

function aggiornaStorages(req) {
    var items = gestisciRisposta(req, "errStorage");
    if (!items) return;
    popolaSelect("selStorage", items, "-- Seleziona uno storage --",
        function(i) { return i.idProdotto; },
        function(i) { return i.nome + " — " + i.capacita + " — " + i.tipo + " " + i.tecnologia + " — " + formattaPrezzoOption(i); }
    );
    abilitaSelect("selStorage", "num-storage", "lbl-storage");
}

function aggiornaPsus(req) {
    var items = gestisciRisposta(req, "errPsu");
    if (!items) return;
    popolaSelect("selPsu", items, "-- Seleziona un alimentatore --",
        function(i) { return i.idProdotto; },
        function(i) { return i.nome + " — " + i.potenza + "W — " + i.certificazione + " — " + i.modulare + " — " + formattaPrezzoOption(i); }
    );
    abilitaSelect("selPsu", "num-psu", "lbl-psu");
}

function aggiornaCases(req) {
    var items = gestisciRisposta(req, "errCase");
    if (!items) return;
    popolaSelect("selCase", items, "-- Seleziona un case --",
        function(i) { return i.idProdotto; },
        function(i) { return i.nome + " — " + i.formato + " — " + i.colore + " — " + formattaPrezzoOption(i); }
    );
    abilitaSelect("selCase", "num-case", "lbl-case");
}

function aggiornaDissipatori(req) {
    var items = gestisciRisposta(req, "errDissipatore");
    if (!items) return;
    popolaSelect("selDissipatore", items, "-- Seleziona un dissipatore --",
        function(i) { return i.idProdotto; },
        function(i) { return i.nome + " — " + i.tipo + " — TDP " + i.tdpSupportato + "W — " + formattaPrezzoOption(i); }
    );
    abilitaSelect("selDissipatore", "num-diss", "lbl-diss");
}

// ─────────────────────────────────────────────
//  Cascata onchange
// ─────────────────────────────────────────────

function onCpuChange() {
    var cpuId = document.getElementById("selCpu").value;
    resetSelect("selMobo",        "-- Prima seleziona una CPU --",          "num-mobo",    "lbl-mobo");
    resetSelect("selRam",         "-- Prima seleziona una scheda madre --", "num-ram",     "lbl-ram");
    resetSelect("selGpu",         "-- Prima seleziona una CPU --",          "num-gpu",     "lbl-gpu");
    resetSelect("selStorage",     "-- Prima seleziona una scheda madre --", "num-storage", "lbl-storage");
    resetSelect("selPsu",         "-- Prima seleziona CPU e GPU --",        "num-psu",     "lbl-psu");
    resetSelect("selCase",        "-- Prima seleziona una scheda madre --", "num-case",    "lbl-case");
    resetSelect("selDissipatore", "-- Prima seleziona una CPU --",          "num-diss",    "lbl-diss");
    aggiornaRiepilogo();
    if (!cpuId) return;
    caricaMobos(cpuId);
    caricaGpus();
    caricaDissipatori(cpuId);
}

function onMoboChange() {
    var moboId = document.getElementById("selMobo").value;
    resetSelect("selRam",     "-- Prima seleziona una scheda madre --", "num-ram",     "lbl-ram");
    resetSelect("selStorage", "-- Prima seleziona una scheda madre --", "num-storage", "lbl-storage");
    resetSelect("selPsu",     "-- Prima seleziona CPU e GPU --",        "num-psu",     "lbl-psu");
    resetSelect("selCase",    "-- Prima seleziona una scheda madre --", "num-case",    "lbl-case");
    aggiornaRiepilogo();
    if (!moboId) return;
    caricaRams(moboId);
    caricaStorages(moboId);
    caricaCases(moboId);
}

function onGpuChange() {
    var cpuId = document.getElementById("selCpu").value;
    var gpuId = document.getElementById("selGpu").value;
    resetSelect("selPsu", "-- Prima seleziona CPU e GPU --", "num-psu", "lbl-psu");
    aggiornaRiepilogo();
    if (!cpuId || !gpuId) return;
    caricaPsus(cpuId, gpuId);
}

// ─────────────────────────────────────────────
//  Utilità DOM
// ─────────────────────────────────────────────

function popolaSelect(selectId, items, placeholder, fnId, fnLabel) {
    var sel = document.getElementById(selectId);
    if (!items || items.length === 0) {
        sel.innerHTML = "<option value=''>Nessun componente compatibile</option>";
        return;
    }
    sel.innerHTML = "<option value=''>" + placeholder + "</option>";
    items.forEach(function (item) {
        var scontato = prezzoScontato(item);   // ← FIX: sempre calcolato con parseFloat
        var opt = document.createElement("option");
        opt.value = fnId(item);
        opt.text  = fnLabel(item);
        opt.setAttribute("data-prezzo",           scontato.toFixed(2));
        opt.setAttribute("data-prezzo-originale", parseFloat(item.prezzo).toFixed(2));
        opt.setAttribute("data-sconto",           parseFloat(item.sconto) || 0);
        sel.appendChild(opt);
    });
}

function resetSelect(selectId, placeholder, numId, lblId) {
    var sel = document.getElementById(selectId);
    sel.innerHTML = "<option value=''>" + placeholder + "</option>";
    sel.disabled = true;
    document.getElementById(numId).classList.add("disabled");
    document.getElementById(lblId).classList.add("disabled");
}

function abilitaSelect(selectId, numId, lblId) {
    document.getElementById(selectId).disabled = false;
    document.getElementById(numId).classList.remove("disabled");
    document.getElementById(lblId).classList.remove("disabled");
}

// ─────────────────────────────────────────────
//  Riepilogo build
// ─────────────────────────────────────────────

var componenti = [
    { id: "selCpu",         label: "CPU" },
    { id: "selMobo",        label: "Scheda Madre" },
    { id: "selRam",         label: "RAM" },
    { id: "selGpu",         label: "GPU" },
    { id: "selStorage",     label: "Storage" },
    { id: "selPsu",         label: "Alimentatore" },
    { id: "selCase",        label: "Case" },
    { id: "selDissipatore", label: "Dissipatore" }
];

function aggiornaRiepilogo() {
    var lista    = document.getElementById("listaRiepilogo");
    var totale   = 0;
    var righe    = [];

    componenti.forEach(function (c) {
        var sel = document.getElementById(c.id);
        if (!sel.value) return;

        var opt             = sel.options[sel.selectedIndex];
        var prezzo          = parseFloat(opt.getAttribute("data-prezzo"))           || 0;
        var prezzoOriginale = parseFloat(opt.getAttribute("data-prezzo-originale")) || 0;
        var sconto          = parseFloat(opt.getAttribute("data-sconto"))            || 0;
        totale += prezzo;

        var nome       = opt.text.split(" — ")[0];
        var prezzoHtml = sconto > 0
            ? "€" + prezzo.toFixed(2) + " <s>€" + prezzoOriginale.toFixed(2) + "</s> (-" + sconto + "%)"
            : "€" + prezzo.toFixed(2);

        righe.push("<li><strong>" + c.label + ":</strong> " + nome + " — " + prezzoHtml + "</li>");
    });

    lista.innerHTML = righe.length
        ? righe.join("")
        : "<li class='builder-empty'>Nessun componente selezionato.</li>";

    document.getElementById("prezzoTotale").innerHTML =
        righe.length ? "Totale: €" + totale.toFixed(2) : "";

    aggiornaBottoneCarrello();
}

// ─────────────────────────────────────────────
//  Carrello
// ─────────────────────────────────────────────

function aggiornaBottoneCarrello() {
    var tuttiSelezionati = componenti.every(function (c) {
        return document.getElementById(c.id).value !== "";
    });
    document.getElementById("wrapperCarrello").style.display = tuttiSelezionati ? "block" : "none";
    document.getElementById("msgCarrello").innerHTML = "";
}

function aggiungiTuttiAlCarrello() {
    var ids = componenti.map(function (c) { return document.getElementById(c.id).value; });
    var msg = document.getElementById("msgCarrello");
    msg.innerHTML = "Aggiunta in corso...";
    msg.className = "builder-cart-msg";

    var errori = 0;

    function aggiungiUno(index) {
        if (index >= ids.length) {
            if (errori === 0) {
                msg.innerHTML = "✓ Tutti i componenti aggiunti al carrello!";
                msg.className = "builder-cart-msg success";
            } else {
                msg.innerHTML = "⚠ " + errori + " componente/i non aggiunto/i.";
                msg.className = "builder-cart-msg error";
            }
            return;
        }

        ajax("carrello/add", "aggiungi=" + ids[index] + "&quantita=1", function (req) {
            var res = JSON.parse(req.responseText);
            if (!res.success) errori++;
            if (index === ids.length - 1) {
                aggiornaBadgeCarrello(res.numProdotti);
            }
            aggiungiUno(index + 1);
        });
    }

    aggiungiUno(0);
}

// ─────────────────────────────────────────────
//  Avvio
// ─────────────────────────────────────────────

window.onload = caricaCpus;