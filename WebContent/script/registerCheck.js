document.addEventListener("DOMContentLoaded", function () {
    var form = document.querySelector("form[action='register']");
    if (!form) return;
    var regole = {
        nome:      { regex: /^[A-Za-zÀ-ÿ\s']{2,50}$/, msg: "Il nome deve contenere solo lettere." },
        cognome:   { regex: /^[A-Za-zÀ-ÿ\s']{2,50}$/, msg: "Il cognome deve contenere solo lettere." },
        email:     { regex: /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/, msg: "Inserisci un indirizzo email valido." },
        password:  { regex: /^(?=.*[A-Z])(?=.*\d).{8,}$/, msg: "La password deve contenere almeno 8 caratteri, una maiuscola e un numero." },
		via: 	   {  regex: /^[A-Za-zÀ-ÿ\s']+\s+\d+$/, msg: "Inserisci la via seguita dal numero civico (es. Via Roma 12)." },
        citta:     { regex: /^[A-Za-zÀ-ÿ\s']{1,80}$/, msg: "Inserisci una città valida." },
        provincia: { regex: /^[A-Za-zÀ-ÿ\s']{1,80}$/, msg: "Inserisci una provincia/stato/regione valida." },
        cap:       { regex: /^[A-Za-z0-9\s-]{2,12}$/, msg: "Inserisci un codice postale valido." },
        paese:     { regex: /^[A-Za-zÀ-ÿ\s']{1,80}$/, msg: "Inserisci un paese valido." },
        cellulare: { regex: /^\+?[0-9][0-9\s-]{6,15}$/, msg: "Inserisci un numero di telefono valido (es. +39 333 1234567)." }
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
        var nome = input.name;
        var valore = input.value.trim();
        if (input.hasAttribute("required") && valore === "") {
            mostraErrore(input, "Questo campo è obbligatorio.");
            return false;
        }
        if (valore === "" && !input.hasAttribute("required")) {
            pulisciErrore(input);
            return true;
        }
		if (nome === "confermaPassword") {
		    var regolaPassword = regole.password;
		    if (!regolaPassword.regex.test(valore)) {
		        mostraErrore(input, regolaPassword.msg);
		        return false;
		    }
		    var password = form.querySelector("[name='password']").value;
		    if (input.value !== password) {
		        mostraErrore(input, "Le password non coincidono.");
		        return false;
		    }
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
    var campi = form.querySelectorAll("input[name]:not([name='datiPlus'])");
    campi.forEach(function (input) {
        input.addEventListener("change", function () {
            validaCampo(input);
            if (input.name === "password") {
                var conferma = form.querySelector("[name='confermaPassword']");
                if (conferma.value !== "") validaCampo(conferma);
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