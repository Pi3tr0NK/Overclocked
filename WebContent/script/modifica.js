function apriSlot(slot) {
    if (slot.classList.contains('filled')) return;
    slot.querySelector('input[type="file"]').click();
}

function creaBottoneRimozione(slot) {
    if (slot.querySelector('.rm-btn')) return; // già presente
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

function gestisciSlot(input) {
    if (!input.files || input.files.length === 0) return;
    var slot   = input.closest('.image-slot');
    var img    = slot.querySelector('img');
    var span   = slot.querySelector('span');
    var reader = new FileReader();
    reader.onload = function(e) {
        img.src = e.target.result;
        img.style.display = 'block';
        if (span) span.style.display = 'none';
        slot.classList.add('filled');

        // se sto sostituendo un'immagine esistente con un file nuovo,
        // non deve restare marcata per la rimozione
        var flag = slot.querySelector('.rimuovi-flag');
        if (flag) flag.value = 'false';

        creaBottoneRimozione(slot);
    };
    reader.readAsDataURL(input.files[0]);
}

function svuotaSlot(slot) {
    slot.querySelector('input[type="file"]').value = '';
    var img  = slot.querySelector('img');
    var span = slot.querySelector('span');
    var btn  = slot.querySelector('.rm-btn');
    img.src = '';
    img.style.display = 'none';
    if (span) span.style.display = 'block';
    slot.classList.remove('filled');
    if (btn) btn.remove();

    // se lo slot conteneva un'immagine già salvata sul server, marcala per la rimozione
    if (slot.classList.contains('existing')) {
        var flag = slot.querySelector('.rimuovi-flag');
        if (flag) flag.value = 'true';
    }
}

// al caricamento della pagina, aggiungi il pulsante "✕" anche alle immagini già esistenti
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.image-slot.existing').forEach(function(slot) {
        creaBottoneRimozione(slot);
    });
});

document.addEventListener("DOMContentLoaded", function () {
    // Seleziona il form di modifica tramite ID
    var form = document.getElementById("formModifica");
    if (!form) return;
	
    var regole = {
        // --- DATI GENERALI (Tabella: prodotto) ---
        nome:          { regex: /^.{2,255}$/, msg: "Il nome deve essere compreso tra 2 e 255 caratteri." }, 
        modello:       { regex: /^.{1,255}$/, msg: "Il modello deve essere compreso tra 1 e 255 caratteri." }, 
        marca:         { regex: /^[A-Za-z0-9À-ÿ\s'.&-]{1,255}$/, msg: "La marca non può superare i 255 caratteri." }, 
        attivo:        { regex: /^(true|false)$/, msg: "Seleziona uno stato valido." }, 
        descrizione:   { regex: /^[\s\S]{0,255}$/, msg: "La descrizione non può superare i 255 caratteri." }, 
        prezzo:        { regex: /^\d{1,8}(\.\d{1,2})?$/, msg: "Inserisci un prezzo valido (max 8 cifre intere, es. 199.99)." }, 
        sconto:        { regex: /^(0|[1-9][0-9]?|100)$/, msg: "Lo sconto deve essere un numero intero compreso tra 0 e 100." }, 
        stock:         { regex: /^(0|[1-9]\d*)$/, msg: "Lo stock deve essere un numero intero maggiore o uguale a 0." }, 
        dimensioni:    { regex: /^.{0,50}$/, msg: "Le dimensioni non possono superare i 50 caratteri." }, 
        peso:          { regex: /^(?=.{1,50}$)\d+(\.\d+)?\s?(Kg|g)$/i, msg: "Inserisci il peso seguito dall'unità di misura (max 50 caratteri, es. 1.4 Kg o 500g)." },

        // --- SPECIFICHE CPU (Tabella: cpu) ---
        core:          { regex: /^[1-9]\d*$/, msg: "Il numero di core deve essere maggiore di 0." }, 
        thread:        { regex: /^[1-9]\d*$/, msg: "Il numero di thread deve essere maggiore di 0." }, 
        tdp:           { regex: /^(0|[1-9]\d*)$/, msg: "Il TDP deve essere un numero intero valido." }, 
        frequenza:     { regex: /^\d+(\.\d+)?\s?(GHz|MHz|KHz|Hz)$/i, msg: "Inserisci la frequenza con l'unità di misura (max 20 caratteri, es. 3.6GHz)." }, 
        socket:        { regex: /^[A-Za-z0-9\s-+,._]{2,20}$/, msg: "Inserisci un socket valido (max 20 caratteri, es. LGA1700)." }, 
        tiporam:       { regex: /^[A-Za-z0-9\s-]{2,10}$/, msg: "Specificare un tipo di RAM valido (max 10 caratteri, es. DDR5)." }, 
        frequenzaram:  { regex: /^\d+(\.\d+)?\s?(GHz|MHz|KHz|Hz)$/i, msg: "Inserisci una frequenza RAM valida (max 20 caratteri, es. 5600 MHz)." },

        // --- SPECIFICHE GPU (Tabella: gpu) ---
        vram:          { regex: /^(?=.{2,10}$)\d+(\.\d+)?\s?(GB|TB|MB|KB)$/i, msg: "Inserisci la VRAM seguita dall'unità di misura corretta (max 10 caratteri, es. 24 GB o 512 MB)." },
        tipovram:      { regex: /^[A-Za-z0-9\s-]{2,20}$/, msg: "Specificare un tipo di VRAM valido (max 20 caratteri, es. GDDR6X)." }, 
        pcie:          { regex: /^.{2,10}$/, msg: "Specificare l'interfaccia PCIe (max 10 caratteri, es. PCIe 4.0)." }, 
        video:         { regex: /^.{2,50}$/, msg: "Inserisci le uscite video (max 50 caratteri, es. 3x DP, 1x HDMI)." }, 
        maxres:        { regex: /^\d+x\d+$/, msg: "Inserisci la Copy-Risoluzione nel formato corretto (max 20 caratteri, es. 7680x4320)." },

        // --- SPECIFICHE RAM & STORAGE (Tabelle: ram, memoria) ---
        capacita:      { regex: /^(?=.{2,10}$)\d+(\.\d+)?\s?(GB|TB|MB|KB)$/i, msg: "Inserisci la capacità seguita dall'unità di misura (max 10 caratteri, es. 32 GB)." },
        formato:       { regex: /^.{2,20}$/, msg: "Specificare un formato valido (max 20 caratteri, es. M.2 2280 o ATX)." }, 
        lettura:       { regex: /^(0|[1-9]\d*)$/, msg: "La velocità di lettura deve essere un numero intero." }, 
        scrittura:     { regex: /^(0|[1-9]\d*)$/, msg: "La velocità di scrittura deve essere un numero intero." }, 
        tipo:          { regex: /^.{2,10}$/, msg: "Seleziona o inserisci un tipo valido." }, 
        tecnologia:    { regex: /^(NVME|SATA)$/, msg: "Seleziona una tecnologia valida." },

        // --- SPECIFICHE MOBO (Tabella: mobo) ---
        chipset:       { regex: /^[A-Za-z0-9\s-]{2,20}$/, msg: "Inserisci un chipset valido (max 20 caratteri, es. Z790)." }, 
        tipoRam:       { regex: /^[A-Za-z0-9\s-]{2,10}$/, msg: "Specificare un tipo di RAM valido (max 10 caratteri, es. DDR5)." }, 
        maxFreq:       { regex: /^.{2,20}$/, msg: "La frequenza massima non può superare i 20 caratteri." }, 
        slotRam:       { regex: /^[1-9]\d*$/, msg: "Gli slot RAM devono essere un numero intero valido." }, 
        porteSata:     { regex: /^(0|[1-9]\d*)$/, msg: "Inserisci un numero di porte SATA valido." }, 
        porteUsb:      { regex: /^(0|[1-9]\d*)$/, msg: "Inserisci un numero di porte USB valido." }, 
        nvme:          { regex: /^(true|false)$/, msg: "Seleziona un'opzione valida." },

        // --- SPECIFICHE PSU (Tabella: psu) ---
        potenza:       { regex: /^[1-9]\d*$/, msg: "La potenza deve essere un numero intero maggiore di 0." }, 
        certificazione:{ regex: /^[A-Za-z0-9\s+]{1,255}$/, msg: "Inserisci una certificazione valida (max 255 caratteri, es. 80+ Gold)." },
        modulare:      { regex: /^(MODULARE|SEMIMODULARE|NON_MODULARE)$/, msg: "Seleziona un'opzione di modularità valida dal menu." }, 

        // --- SPECIFICHE CASE (Tabella: chassis) ---
        colore:        { regex: /^[A-Za-zÀ-ÿ\s']{2,20}$/, msg: "Il colore non può superare i 20 caratteri." }, 
        materiale:     { regex: /^.{2,255}$/, msg: "Il materiale non può superare i 255 caratteri." }, 

        // --- SPECIFICHE DISSIPATORE (Tabella: dissipatore) ---
        rpm:           { regex: /^(0|[1-9]\d*)$/, msg: "I giri al minuto (RPM) devono essere un numero intero." }, 
        rumore:        { regex: /^(0|[1-9]\d*)$/, msg: "Il livello di rumore (dBA) deve essere un numero intero." } 
    };

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

    function validaCampo(input) {
        var nome = input.name;
        var valore = input.value.trim();
		
        if (input.validity && input.validity.badInput) {
            mostraErrore(input, "Il valore inserito contiene caratteri non validi (es. la lettera 'e').");
            return false;
        }
			
        if (input.hasAttribute("required") && valore === "") {
            mostraErrore(input, "Questo campo è obbligatorio.");
            return false;
        }

        if (valore === "" && !input.hasAttribute("required")) {
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

    // Seleziona gli input validabili presenti nel form (escludendo file e hidden strutturali)
    var campi = form.querySelectorAll("input[name], select[name], textarea[name]");
    
    campi.forEach(function (input) {
        if (input.type === "file" || input.type === "hidden") return;

        // Blocca preventivamente la digitazione di caratteri esponenziali o segni nei campi esclusivamente interi
        var campiInteri = ['core', 'thread', 'stock', 'slotRam', 'porteSata', 'porteUsb', 'rpm', 'rumore', 'tdp', 'lettura', 'scrittura', 'potenza', 'sconto'];
        if (campiInteri.includes(input.name)) {
            input.addEventListener("keydown", function(e) {
                if (["e", "E", "+", "-", ",", "."].includes(e.key)) {
                    e.preventDefault();
                }
            });
        }

        input.addEventListener("change", function () {
            validaCampo(input);
        });
        
		if (input.type === "number") {
		    input.addEventListener("keydown", function (e) {
		        if (["e", "E", "+", "-"].includes(e.key)) {
		            e.preventDefault();
		        }
		    });
		}
    });

    // Validazione al Submit
    form.addEventListener("submit", function (e) {
        var valido = true;

        campi.forEach(function (input) {
            if (input.type === "file" || input.type === "hidden") return;

            // Valida direttamente tutti i campi visibili stampati nel DOM da JSTL
            if (!validaCampo(input)) {
                valido = false;
            }
        });

        if (!valido) {
            e.preventDefault(); 
            var primoErrore = form.querySelector(".input-invalid");
            if (primoErrore) {
                primoErrore.scrollIntoView({ behavior: "smooth", block: "center" });
            }
        }
    });
});