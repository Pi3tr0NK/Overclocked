document.addEventListener("DOMContentLoaded", function () {
    var form = document.querySelector("form[action='login']");
    if (!form) return;

    var regole = {
        email: {
            regex: /^[^\s@]+@[^\s@]+\.[^\s@]{1,}$/,
            msg: "Inserisci un indirizzo email valido."
        }
        // "password" non ha una regex di formato in fase di login:
        // basta il controllo "required", già gestito da validaCampo.
    };

	function getErrorEl(input) {
	    var id = "err-" + input.name;
	    return document.getElementById(id); 
	    // Se non lo trova (es. un domani cambi nomi), restituisce null, ma ora i tag ci sono!
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