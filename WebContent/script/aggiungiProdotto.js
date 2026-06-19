// ─── FUNZIONI GLOBALI (chiamate da onclick nel JSP) ───────────────────────────

function selezionaCategoria(cat) {
    document.querySelectorAll('.cat-tab').forEach(function(t) {
        t.classList.toggle('active', t.dataset.cat === cat);
    });
    document.querySelectorAll('.cat-section').forEach(function(s) {
        s.style.display = 'none';
    });
    var sezione = document.getElementById('section-' + cat);
    if (sezione) sezione.style.display = 'block';
    document.getElementById('inputCategoria').value = cat;
}

function apriSlot(slot) {
    if (slot.classList.contains('filled')) return;
    slot.querySelector('input[type="file"]').click();
}

function gestisciSlot(input) {
    if (!input.files || input.files.length === 0) return;
    var slot   = input.closest('.image-slot');
    var img    = slot.querySelector('img');
    var span   = slot.querySelector('span');
    var reader = new FileReader();
    reader.onload = function(e) {
        img.src = e.target.result;
        img.style.display = 'block';
        span.style.display = 'none';
        slot.classList.add('filled');
        if (!slot.querySelector('.rm-btn')) {
            var btn = document.createElement('button');
            btn.type = 'button';
            btn.className = 'rm-btn';
            btn.innerHTML = '&#x2715;';
            btn.style.cssText = 'position:absolute;top:4px;right:4px;background:#ff7300;border:none;' +
                                'color:#000;border-radius:50%;width:20px;height:20px;cursor:pointer;' +
                                'font-size:11px;line-height:20px;padding:0;z-index:10;';
            btn.onclick = function(e) { e.stopPropagation(); svuotaSlot(slot); };
            slot.appendChild(btn);
        }
    };
    reader.readAsDataURL(input.files[0]);
}

function svuotaSlot(slot) {
    slot.querySelector('input[type="file"]').value = '';
    var img  = slot.querySelector('img');
    var span = slot.querySelector('span');
    var btn  = slot.querySelector('.rm-btn');
    img.src = ''; img.style.display = 'none';
    span.style.display = 'block';
    slot.classList.remove('filled');
    if (btn) btn.remove();
}

function disabilitaSezionInattive() {
    document.querySelectorAll('.cat-section').forEach(function(sezione) {
        var attiva = sezione.style.display !== 'none';
        sezione.querySelectorAll('input, select, textarea').forEach(function(campo) {
            campo.disabled = !attiva;
        });
    });
}


// ─── INIZIALIZZAZIONE + VALIDAZIONE (unico DOMContentLoaded) ─────────────────

document.addEventListener("DOMContentLoaded", function () {

    // Seleziona CPU come categoria di default
    selezionaCategoria('CPU');

    var form = document.getElementById("formProdotto");
    if (!form) return;

    var regole = {
        // --- DATI GENERALI ---
        categoria:      { regex: /^(CPU|GPU|RAM|STORAGE|MOBO|PSU|CASE|DISSIPATORE)$/, msg: "Seleziona una categoria valida tra quelle proposte." },
        nome:           { regex: /^.{2,255}$/, msg: "Il nome deve essere compreso tra 2 e 255 caratteri." },
        modello:        { regex: /^.{1,255}$/, msg: "Il modello deve essere compreso tra 1 e 255 caratteri." },
        marca:          { regex: /^[A-Za-z0-9À-ÿ\s'.&-]{1,255}$/, msg: "La marca non può superare i 255 caratteri." },
        attivo:         { regex: /^(true|false)$/, msg: "Seleziona uno stato valido." },
        descrizione:    { regex: /^[\s\S]{0,255}$/, msg: "La descrizione non può superare i 255 caratteri." },
        prezzo:         { regex: /^\d{1,8}(\.\d{1,2})?$/, msg: "Inserisci un prezzo valido (es. 199.99)." },
        sconto:         { regex: /^(0|[1-9][0-9]?|100)$/, msg: "Lo sconto deve essere un numero intero compreso tra 0 e 100." },
        stock:          { regex: /^(0|[1-9]\d*)$/, msg: "Lo stock deve essere un numero intero maggiore o uguale a 0." },
        dimensioni:     { regex: /^.{0,50}$/, msg: "Le dimensioni non possono superare i 50 caratteri." },
        peso:           { regex: /^(?=.{1,50}$)\d+(\.\d+)?\s?(Kg|g)$/i, msg: "Inserisci il peso seguito dall'unità di misura (es. 1.4 Kg o 500g)." },

        // --- CPU ---
        core:           { regex: /^[1-9]\d*$/, msg: "Il numero di core deve essere maggiore di 0." },
        thread:         { regex: /^[1-9]\d*$/, msg: "Il numero di thread deve essere maggiore di 0." },
        tdp:            { regex: /^(0|[1-9]\d*)$/, msg: "Il TDP deve essere un numero intero valido." },
        frequenza:      { regex: /^\d+(\.\d+)?\s?(GHz|MHz|KHz|Hz)$/i, msg: "Inserisci la frequenza con l'unità di misura (es. 3.6 GHz)." },
        socket:         { regex: /^[A-Za-z0-9\s\-+,._]{2,20}$/, msg: "Inserisci un socket valido (es. LGA1700)." },
        tiporam:        { regex: /^[A-Za-z0-9\s-]{2,10}$/, msg: "Specificare un tipo di RAM valido (es. DDR5)." },
        frequenzaram:   { regex: /^\d+(\.\d+)?\s?(GHz|MHz|KHz|Hz)$/i, msg: "Inserisci una frequenza RAM valida (es. 5600 MHz)." },

        // --- GPU ---
        vram:           { regex: /^(?=.{2,10}$)\d+(\.\d+)?\s?(GB|TB|MB|KB)$/i, msg: "Inserisci la VRAM con l'unità di misura (es. 24 GB)." },
        tipovram:       { regex: /^[A-Za-z0-9\s-]{2,20}$/, msg: "Specificare un tipo di VRAM valido (es. GDDR6X)." },
        pcie:           { regex: /^.{2,10}$/, msg: "Specificare l'interfaccia PCIe (es. PCIe 4.0)." },
        video:          { regex: /^.{2,50}$/, msg: "Inserisci le uscite video (es. 3x DP, 1x HDMI)." },
        maxres:         { regex: /^\d+x\d+$/, msg: "Inserisci la risoluzione nel formato corretto (es. 7680x4320)." },

        // --- RAM & STORAGE ---
        capacita:       { regex: /^(?=.{2,10}$)\d+(\.\d+)?\s?(GB|TB|MB|KB)$/i, msg: "Inserisci la capacità con l'unità di misura (es. 16 GB)." },
        formato:        { regex: /^.{2,20}$/, msg: "Specificare un formato valido (es. M.2 2280 o ATX)." },
        lettura:        { regex: /^(0|[1-9]\d*)$/, msg: "La velocità di lettura deve essere un numero intero." },
        scrittura:      { regex: /^(0|[1-9]\d*)$/, msg: "La velocità di scrittura deve essere un numero intero." },
        tipo:           { regex: /^.{2,10}$/, msg: "Seleziona un tipo valido." },
        tecnologia:     { regex: /^(NVME|SATA)$/, msg: "Seleziona una tecnologia valida." },

        // --- MOBO ---
        chipset:        { regex: /^[A-Za-z0-9\s-]{2,20}$/, msg: "Inserisci un chipset valido (es. Z790)." },
        tipoRam:        { regex: /^[A-Za-z0-9\s-]{2,10}$/, msg: "Specificare un tipo di RAM valido (es. DDR5)." },
        maxFreq:        { regex: /^.{2,20}$/, msg: "La frequenza massima non può superare i 20 caratteri." },
        slotRam:        { regex: /^[1-9]\d*$/, msg: "Gli slot RAM devono essere un numero intero valido." },
        porteSata:      { regex: /^(0|[1-9]\d*)$/, msg: "Inserisci un numero di porte SATA valido." },
        porteUsb:       { regex: /^(0|[1-9]\d*)$/, msg: "Inserisci un numero di porte USB valido." },
        nvme:           { regex: /^(true|false)$/, msg: "Seleziona un'opzione valida." },

        // --- PSU ---
        potenza:        { regex: /^[1-9]\d*$/, msg: "La potenza deve essere un numero intero maggiore di 0." },
        certificazione: { regex: /^[A-Za-z0-9\s+]{1,255}$/, msg: "Inserisci una certificazione valida (es. 80+ Gold)." },
        modulare:       { regex: /^(MODULARE|SEMIMODULARE|NON_MODULARE)$/, msg: "Seleziona un'opzione di modularità valida." },

        // --- CASE ---
        colore:         { regex: /^[A-Za-zÀ-ÿ\s']{2,20}$/, msg: "Il colore non può superare i 20 caratteri." },
        materiale:      { regex: /^.{2,255}$/, msg: "Il materiale non può superare i 255 caratteri." },

        // --- DISSIPATORE ---
        rpm:            { regex: /^(0|[1-9]\d*)$/, msg: "I giri al minuto (RPM) devono essere un numero intero." },
        rumore:         { regex: /^(0|[1-9]\d*)$/, msg: "Il livello di rumore (dBA) deve essere un numero intero." }
    };

    // ── Helpers errore ────────────────────────────────────────────────────────

    function getErrorEl(input) {
        var id = "err-" + input.name;
        var el = document.getElementById(id);
        if (!el) {
            el = document.createElement("span");
            el.id = id;
            el.className = "field-error";
            el.style.color = "red";
            el.style.display = "block";
            el.style.fontSize = "12px";
            el.style.marginTop = "4px";
            input.insertAdjacentElement("afterend", el);
        }
        return el;
    }

    function mostraErrore(input, messaggio) {
        getErrorEl(input).textContent = messaggio;
        input.classList.add("input-invalid");
    }

    function pulisciErrore(input) {
        getErrorEl(input).textContent = "";
        input.classList.remove("input-invalid");
    }

    // ── Validazione singolo campo ─────────────────────────────────────────────

    function validaCampo(input) {
        var nome   = input.name;
        var valore = input.value.trim();

        if (input.validity && input.validity.badInput) {
            mostraErrore(input, "Il valore inserito non è valido (es. la lettera 'e' in un campo numerico).");
            return false;
        }

        if (input.hasAttribute("required") && valore === "") {
            mostraErrore(input, "Questo campo è obbligatorio.");
            return false;
        }

        if (valore === "") {
            pulisciErrore(input);
            return true;
        }

        var regola = regole[nome];
        if (regola && !regola.regex.test(valore)) {
            mostraErrore(input, regola.msg);
            return false;
        }

        pulisciErrore(input);
        return true;
    }

    // ── Aggancio eventi sui campi ─────────────────────────────────────────────

    var campi = form.querySelectorAll("input[name], select[name], textarea[name]");

    campi.forEach(function(input) {
        if (input.type === "file") return;

        if (input.type === "number") {
            input.addEventListener("keydown", function(e) {
                if (["e", "E", "+", "-"].includes(e.key)) {
                    e.preventDefault();
                }
            });
        }

        input.addEventListener("change", function() {
            validaCampo(input);
        });
    });

    // ── Submit: unico listener ────────────────────────────────────────────────

    form.addEventListener("submit", function(e) {

        // 1. Categoria selezionata?
        if (!document.getElementById('inputCategoria').value) {
            e.preventDefault();
            alert('Seleziona una categoria prima di salvare.');
            return;
        }

        // 2. Valida tutti i campi visibili
        var valido = true;
        campi.forEach(function(input) {
            if (input.type === "file") return;
            var parentSection = input.closest(".cat-section");
            if (parentSection && window.getComputedStyle(parentSection).display === "none") return;
            if (!validaCampo(input)) valido = false;
        });

        if (!valido) {
            e.preventDefault();
            var primoErrore = form.querySelector(".input-invalid");
            if (primoErrore) primoErrore.scrollIntoView({ behavior: "smooth", block: "center" });
            return;
        }

        // 3. Solo se tutto è valido, disabilita le sezioni inattive prima dell'invio
        disabilitaSezionInattive();
    });

});