document.addEventListener("DOMContentLoaded", function () {
    var form = document.querySelector("form[action$='pagamento']");
    if (!form) return;

    var regole = {
        via:          { regex: /^[A-Za-zÀ-ÿ\s']+\s+\d+$/, msg: "Inserisci la via seguita dal numero civico (es. Via Roma 12)." },
        citta:        { regex: /^[A-Za-zÀ-ÿ\s']{1,80}$/, msg: "Inserisci una città valida." },
        provincia:    { regex: /^[A-Za-zÀ-ÿ\s']{1,80}$/, msg: "Inserisci una provincia/stato/regione valida." },
        cap:          { regex: /^[A-Za-z0-9\s-]{2,12}$/, msg: "Inserisci un codice postale valido." },
        paese:        { regex: /^[A-Za-zÀ-ÿ\s']{1,80}$/, msg: "Inserisci un paese valido." },
        numeroCarta:  { regex: /^[0-9]{16}$/, msg: "Il numero carta deve contenere 16 cifre." },
        intestatario: { regex: /^[A-Za-zÀ-ÿ\s']{2,50}$/, msg: "Inserisci il nome come riportato sulla carta." },
        mese:         { regex: /^(0?[1-9]|1[0-2])$/, msg: "Inserisci un mese valido (1-12)." },
        anno: { regex: /^[0-9]{4}$/, msg: "Inserisci un anno valido a 4 cifre (es. 2025)." },
        cvv:          { regex: /^[0-9]{3}$/, msg: "Il CVV deve contenere 3 cifre." }
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

        var regola = regole[nome];
        if (regola && !regola.regex.test(valore)) {
            mostraErrore(input, regola.msg);
            return false;
        }

        pulisciErrore(input);
        return true;
    }

    var campi = form.querySelectorAll("input[name]");
    campi.forEach(function (input) {
        input.addEventListener("change", function () {
            validaCampo(input);
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