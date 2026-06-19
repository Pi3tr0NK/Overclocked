document.addEventListener("DOMContentLoaded", function () {
    var form = document.querySelector("form[action='login']");
    if (!form) return;
    var regole = {

        email:     { regex: /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/, msg: "Inserisci un indirizzo email valido." },

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