document.addEventListener("DOMContentLoaded", function () {
    var form = document.getElementById("filterForm");
    if (!form) return;

    var regole = {
        prezzo: {regex: /^\d+(\.\d{1,2})?$/,msg: "Inserisci un prezzo valido (es. 199.99)."},
        marca: {regex: /^[A-Za-z0-9\s'-]{1,50}$/,msg: "Inserisci una marca valida."},

        /* ===== CPU ===== */
        core: {regex: /^[1-9][0-9]{0,2}$/,msg: "Inserisci un numero di core valido (es. 6)."},
        frequenza: {regex: /^\d+(\.\d+)?\s?(GHz|MHz|KHz|Hz)$/i,msg: "Inserisci la frequenza con l'unità di misura (es. 3.6GHz)."},

        /* ===== GPU ===== */
        vram: {regex: /^\d+\s?(GB|MB|TB)$/i,msg: "Inserisci la VRAM con l'unità di misura (es. 8GB)."},
        pcie: {regex: /^[1-5](\.\d)?$/,msg: "Inserisci una versione PCIe valida (es. 4.0)."},

        /* ===== RAM / STORAGE ===== */
        capacita: {regex: /^\d+\s?(GB|MB|TB)$/i,msg: "Inserisci la capacità con l'unità di misura (es. 16GB)."},
        tipo: {regex: /^DDR[1-5]$/,msg: "Il tipo deve essere DDR (maiuscolo) seguito da un numero da 1 a 5, es. DDR4."},

        /* ===== MOBO / CASE ===== */
        formato: {regex: /^[A-Za-z0-9\s'-]{1,30}$/,msg: "Inserisci un formato valido (es. ATX, Mid Tower)."},
        slotram: {regex: /^[1-9][0-9]?$/,msg: "Inserisci un numero di slot RAM valido."},
        colore: {regex: /^[A-Za-z\s'-]{1,30}$/,msg: "Inserisci un colore valido (es. Nero)."},

        /* ===== PSU ===== */
        potenza: {regex: /^\d{1,5}$/,msg: "Inserisci una potenza valida in Watt (es. 650)."},
        certificazione: {regex: /^[A-Za-z0-9\s+]{1,30}$/, msg: "Inserisci una certificazione valida (es. 80+ Gold)."}
    };

    function getErrorEl(input) {
        var id = "err-" + input.name;
        var el = document.getElementById(id);
        if (!el) {
            el = document.createElement("span");
            el.id = id;
            el.className = "field-error";
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
        var valore = input.value.trim();

        // sono tutti filtri opzionali: campo vuoto = nessun filtro, nessun errore
        if (valore === "") {
            pulisciErrore(input);
            return true;
        }

        var regola = regole[input.name];
        if (regola && !regola.regex.test(valore)) {
            mostraErrore(input, regola.msg);
            return false;
        }

        pulisciErrore(input);
        return true;
    }

    // validiamo solo gli input testuali/numerici (escluso il campo hidden "cerca");
    // le select (categoria, ordinamento, nvme, modulare, tipo per DISSIPATORE/STORAGE, ecc.)
    // hanno valori già vincolati dalle <option> e non necessitano di controllo
	// validiamo solo gli input testuali/numerici (escluso il campo hidden "cerca");
	    var campi = form.querySelectorAll("input[name]:not([type='hidden'])");

	    campi.forEach(function (input) {
	        // Disattiviamo il submit inline ereditato dalla JSP
	        input.onchange = null;

	        input.addEventListener("change", function () {
	            // 1. Valuta il campo corrente per mostrare/nascondere l'errore visivo
	            validaCampo(input);

	            // 2. Controlla lo stato di TUTTI i campi prima di fare il submit
	            var formValido = true;
	            campi.forEach(function (campoOgni) {
	                // Se anche un solo campo non è valido, formValido diventa false
	                if (!validaCampo(campoOgni)) {
	                    formValido = false;
	                }
	            });

	            // 3. Invia il form solo se non ci sono errori in tutta la pagina
	            if (formValido) {
	                form.submit();
	            }
	        });
	    });
		
	    form.addEventListener("submit", function (e) {
	        var valido = true;
	        campi.forEach(function (input) {
	            if (!validaCampo(input)) valido = false;
	        });
	        if (!valido) {
	            e.preventDefault();
	        }
	    });
	});