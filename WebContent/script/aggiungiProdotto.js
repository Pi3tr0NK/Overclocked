window.addEventListener('DOMContentLoaded', function() {
    selezionaCategoria('CPU');
});

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

document.getElementById('formProdotto').addEventListener('submit', function(e) {
    if (!document.getElementById('inputCategoria').value) {
        e.preventDefault();
        alert('Seleziona una categoria prima di salvare.');
        return;
    }
    disabilitaSezionInattive();
});

//----- REGEX -------- //

document.addEventListener("DOMContentLoaded", function () {
    // Seleziona il form dei prodotti tramite ID
    var form = document.getElementById("formProdotto");
    if (!form) return;
	
		
	var regole = {
	    // --- DATI GENERALI (Tabella: prodotto) ---
	    categoria:     { regex: /^(CPU|GPU|RAM|STORAGE|MOBO|PSU|CASE|DISSIPATORE)$/, msg: "Seleziona una categoria valida tra quelle proposte." },
	    nome:          { regex: /^.{2,255}$/, msg: "Il nome deve essere compreso tra 2 e 255 caratteri." }, // varchar(255)
	    modello:       { regex: /^.{1,255}$/, msg: "Il modello deve essere compreso tra 1 e 255 caratteri." }, // varchar(255)
	    marca:         { regex: /^[A-Za-z0-9À-ÿ\s'.&-]{1,255}$/, msg: "La marca non può superare i 255 caratteri." }, // varchar(255)
	    attivo:        { regex: /^(true|false)$/, msg: "Seleziona uno stato valido." }, // boolean
	    descrizione:   { regex: /^[\s\S]{0,255}$/, msg: "La descrizione non può superare i 255 caratteri." }, // varchar(255)
	    prezzo:        { regex: /^\d{1,8}(\.\d{1,2})?$/, msg: "Inserisci un prezzo valido (decimal 10,2: max 8 cifre intere, es. 199.99)." }, // decimal(10,2)
	    sconto:        { regex: /^(0|[1-9][0-9]?|100)$/, msg: "Lo sconto deve essere un numero intero compreso tra 0 e 100." }, // CHECK (sconto >= 0 AND sconto <= 100)
	    stock:         { regex: /^(0|[1-9]\d*)$/, msg: "Lo stock deve essere un numero intero maggiore o uguale a 0." }, // int
	    dimensioni:    { regex: /^.{0,50}$/, msg: "Le dimensioni non possono superare i 50 caratteri." }, // varchar(50)
		peso: 		   { regex: /^(?=.{1,50}$)\d+(\.\d+)?\s?(Kg|g)$/i, msg: "Inserisci il peso seguito dall'unità di misura (max 50 caratteri, es. 1.4 Kg o 500g)."},

	    // --- SPECIFICHE CPU (Tabella: cpu) ---
	    core:          { regex: /^[1-9]\d*$/, msg: "Il numero di core deve essere maggiore di 0." }, // int
	    thread:        { regex: /^[1-9]\d*$/, msg: "Il numero di thread deve essere maggiore di 0." }, // int
	    tdp:           { regex: /^(0|[1-9]\d*)$/, msg: "Il TDP deve essere un numero intero valido." }, // int
	    frequenza:     { regex: /^\d+(\.\d+)?\s?(GHz|MHz|KHz|Hz)$/i, msg: "Inserisci la frequenza con l'unità di misura (max 20 caratteri, es. 3.6GHz)." }, // varchar(20) [Usata anche in gpu e ram]
	    socket:        { regex: /^[A-Za-z0-9\s-+,._]{2,20}$/, msg: "Inserisci un socket valido (max 20 caratteri, es. LGA1700)." }, // varchar(20) [Sincronizzato con tabelle cpu e mobo]
	    tiporam:       { regex: /^[A-Za-z0-9\s-]{2,10}$/, msg: "Specificare un tipo di RAM valido (max 10 caratteri, es. DDR5)." }, // varchar(10) [Sincronizzato con cpu, ram, mobo]
	    frequenzaram:  { regex: /^\d+(\.\d+)?\s?(GHz|MHz|KHz|Hz)$/i, msg: "Inserisci una frequenza RAM valida (max 20 caratteri, es. 5600 MHz)." }, // varchar(20)

	    // --- SPECIFICHE GPU (Tabella: gpu) ---
		vram: 		   { regex: /^(?=.{2,10}$)\d+(\.\d+)?\s?(GB|TB|MB|KB)$/i, msg: "Inserisci la VRAM seguita dall'unità di misura corretta (max 10 caratteri, es. 24 GB o 512 MB)." },
	    tipovram:      { regex: /^[A-Za-z0-9\s-]{2,20}$/, msg: "Specificare un tipo di VRAM valido (max 20 caratteri, es. GDDR6X)." }, // varchar(20)
	    pcie:          { regex: /^.{2,10}$/, msg: "Specificare l'interfaccia PCIe (max 10 caratteri, es. PCIe 4.0)." }, // varchar(10) [Sincronizzato con gpu e mobo]
	    video:         { regex: /^.{2,50}$/, msg: "Inserisci le uscite video (max 50 caratteri, es. 3x DP, 1x HDMI)." }, // varchar(50)
	    maxres:        { regex: /^\d+x\d+$/, msg: "Inserisci la risoluzione nel formato corretto (max 20 caratteri, es. 7680x4320)." }, // varchar(20)

	    // --- SPECIFICHE RAM & STORAGE (Tabelle: ram, memoria) ---
	    capacita:      { regex: /^(?=.{2,10}$)\d+(\.\d+)?\s?(GB|TB|MB|KB)$/i, msg: "Inserisci la VRAM seguita dall'unità di misura corretta (max 10 caratteri, es. 24 GB)." },
	    formato:       { regex: /^.{2,20}$/, msg: "Specificare un formato valido (max 20 caratteri, es. M.2 2280 o ATX)." }, // varchar(20) [Sincronizzato con memoria e psu]
	    lettura:       { regex: /^(0|[1-9]\d*)$/, msg: "La velocità di lettura deve essere un numero intero." }, // int
	    scrittura:     { regex: /^(0|[1-9]\d*)$/, msg: "La velocità di scrittura deve essere un numero intero." }, // int
	    tipo:          { regex: /^.{2,10}$/, msg: "Seleziona un tipo valido dal menu o specifica un valore consentito." }, // enum/varchar(10)
	    tecnologia:    { regex: /^(NVME|SATA)$/, msg: "Seleziona una tecnologia valida." }, // enum ('SATA','NVME')

	    // --- SPECIFICHE MOBO (Tabella: mobo) ---
	    chipset:       { regex: /^[A-Za-z0-9\s-]{2,20}$/, msg: "Inserisci un chipset valido (max 20 caratteri, es. Z790)." }, // varchar(20)
	    tipoRam:       { regex: /^[A-Za-z0-9\s-]{2,10}$/, msg: "Specificare un tipo di RAM valido (max 10 caratteri, es. DDR5)." }, // varchar(10)
	    maxFreq:       { regex: /^.{2,20}$/, msg: "La frequenza massima non può superare i 20 caratteri." }, // varchar(20)
	    slotRam:       { regex: /^[1-9]\d*$/, msg: "Gli slot RAM devono essere un numero intero valido." }, // int
	    porteSata:     { regex: /^(0|[1-9]\d*)$/, msg: "Inserisci un numero di porte SATA valido." }, // int
	    porteUsb:      { regex: /^(0|[1-9]\d*)$/, msg: "Inserisci un numero di porte USB valido." }, // int
	    nvme:          { regex: /^(true|false)$/, msg: "Seleziona un'opzione valida." }, // boolean

	    // --- SPECIFICHE PSU (Tabella: psu) ---
	    potenza:       { regex: /^[1-9]\d*$/, msg: "La potenza deve essere un numero intero maggiore di 0." }, // int
	    certificazione: {regex: /^[A-Za-z0-9\s+]{1,255}$/, msg: "Inserisci una certificazione valida (es. 80+ Gold)."},
	    modulare:      { regex: /^(MODULARE|SEMIMODULARE|NON_MODULARE)$/, msg: "Seleziona un'opzione di modularità valida dal menu." }, // enum
	    // formato (PSU) è gestito dal controllo accorpato sopra (max 20 caratteri per rispecchiare l'enum 'ATX','SFX' o il varchar del case)

	    // --- SPECIFICHE CASE (Tabella: chassis) ---
	    colore:        { regex: /^[A-Za-zÀ-ÿ\s']{2,20}$/, msg: "Il colore non può superare i 20 caratteri." }, // varchar(20)
	    materiale:     { regex: /^.{2,255}$/, msg: "Il materiale non può superare i 255 caratteri." }, // varchar(255)

	    // --- SPECIFICHE DISSIPATORE (Tabella: dissipatore) ---
	    rpm:           { regex: /^(0|[1-9]\d*)$/, msg: "I giri al minuto (RPM) devono essere un numero intero." }, // int
	    rumore:        { regex: /^(0|[1-9]\d*)$/, msg: "Il livello di rumore (dBA) deve essere un numero intero." } // int
	};

    function getErrorEl(input) {
        var id = "err-" + input.name;
        var el = document.getElementById(id);
        if (!el) {
            el = document.createElement("span");
            el.id = id;
            el.className = "field-error";
            // Stile per uniformarlo ai tuoi vecchi div di errore (es. rosso e a capo)
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
			
        // 1. Controllo obbligatorietà per i campi visibili o hidden fondamentali
        if (input.hasAttribute("required") && valore === "") {
            mostraErrore(input, "Questo campo è obbligatorio.");
            return false;
        }

        // 2. Se il campo non è obbligatorio ed è vuoto, è valido
        if (valore === "" && !input.hasAttribute("required")) {
            pulisciErrore(input);
            return true;
        }

        // 3. Validazione basata sulle espressioni regolari (Regex)
        var regola = regole[nome];
        if (regola && !regola.regex.test(valore)) {
            mostraErrore(input, regola.msg);
            return false;
        }

        pulisciErrore(input);
        return true;
    }

    // Seleziona tutti gli input, select e textarea validabili (escludendo i file immagine e i bottoni)
    var campi = form.querySelectorAll("input[name], select[name], textarea[name]");
    
    campi.forEach(function (input) {
        // Evitiamo di agganciare eventi standard di testo sui file input
        if (input.type === "file") return;
		
		if (input.type === "number") {
		    input.addEventListener("keydown", function (e) {
		        if (["e", "E", "+", "-"].includes(e.key)) {
		            e.preventDefault();
		        }
		    });
		}
		
        input.addEventListener("change", function () {
            validaCampo(input);
        });
    });

    // Controllo sul submit finale del form
    form.addEventListener("submit", function (e) {
        var valido = true;

        // Validiamo solo i campi che appartengono alla sezione generale o alla categoria attiva in quel momento
        campi.forEach(function (input) {
            if (input.type === "file") return;

            // Trova se l'input si trova dentro una sezione di categoria specifica (.cat-section)
            var parentSection = input.closest(".cat-section");
            
            if (parentSection) {
                // Se la sezione specifica non è visibile (in base a come le mostri/nascondi nel tuo JS), saltiamo il controllo
                if (window.getComputedStyle(parentSection).display === "none") {
                    return; 
                }
            }

            if (!validaCampo(input)) {
                valido = false;
            }
        });

        if (!valido) {
            e.preventDefault(); // Blocca l'invio se ci sono errori
            // Opzionale: scrolla fino al primo errore trovato
            var primoErrore = form.querySelector(".input-invalid");
            if (primoErrore) {
                primoErrore.scrollIntoView({ behavior: "smooth", block: "center" });
            }
        }
    });
});