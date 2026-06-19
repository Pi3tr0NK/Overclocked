var timerRicerca = null;

function cercaSuggerimenti(query) {
    var box = document.getElementById('suggerimenti');
    clearTimeout(timerRicerca);

    if (!query.trim()) {
        box.innerHTML = '';
        return;
    }

    timerRicerca = setTimeout(function () {
        var req = new XMLHttpRequest();
        req.onreadystatechange = function () {
            if (req.readyState !== 4 || req.status !== 200) return;
            var risultati = JSON.parse(req.responseText);
            box.innerHTML = '';
            risultati.forEach(function (testo) {
                var a = document.createElement('a');
                a.href = contextPath + '/Catalogo?cerca=' + encodeURIComponent(testo);
                a.textContent = testo;
                a.onclick = function () {
                    document.getElementById('searchInput').value = testo;
                    box.innerHTML = '';
                };
                box.appendChild(a);
            });
        };
        req.open('GET', contextPath + '/Catalogo?action=suggest&cerca=' + encodeURIComponent(query), true);
        req.send(null);
    }, 250);
}

document.addEventListener('click', function (e) {
    if (!document.getElementById('searchInput').contains(e.target)) {
        document.getElementById('suggerimenti').innerHTML = '';
    }
});