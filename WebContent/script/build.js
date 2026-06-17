
    function createXMLHttpRequest() {
        var request;
        try {
            request = new XMLHttpRequest();
        } catch (e) {
            try {
                request = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try {
                    request = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (e) {
                    alert("Il browser non supporta AJAX");
                    return null;
                }
            }
        }
        return request;
    }

    function loadAjaxDoc(url, method, params, cFunction) {
        var request = createXMLHttpRequest();
        if (request) {
            request.onreadystatechange = function () {
                if (this.readyState == 4) {
                    if (this.status == 200) {
                        cFunction(this);
                    } else {
                        if (this.status == 0) {
                            alert("Problemi nell'esecuzione della richiesta: nessuna risposta ricevuta nel tempo limite");
                        } else {
                            alert("Problemi nell'esecuzione della richiesta:\n" + this.statusText);
                        }
                        return null;
                    }
                }
            };

            setTimeout(function () {
                if (request.readyState < 4) {
                    request.abort();
                }
            }, 15000);

            if (method.toLowerCase() == "get") {
                if (params) {
                    request.open("GET", url + "?" + params, true);
                } else {
                    request.open("GET", url, true);
                }
                request.setRequestHeader("Connection", "close");
                request.send(null);
            } else {
                if (params) {
                    request.open("POST", url, true);
                    request.setRequestHeader("Connection", "close");
                    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                    request.send(params);
                } else {
                    console.log("Usa GET se non ci sono parametri!");
                    return null;
                }
            }
        }
    }

    // ─────────────────────────────────────────────
    //  Funzioni di chiamata AJAX
    // ─────────────────────────────────────────────
	
    
    function caricaCpus() {
        loadAjaxDoc('pcBuilder', 'GET', 'action=getCpus', aggiornaCpus);
    }
    function caricaMobos(cpuId) {
        loadAjaxDoc('pcBuilder', 'GET', 'action=getMobos&cpuId=' + cpuId, aggiornaMobos);
    }
    function caricaRams(moboId) {
        loadAjaxDoc('pcBuilder', 'GET', 'action=getRams&moboId=' + moboId, aggiornaRams);
    }
    function caricaGpus() {
        loadAjaxDoc('pcBuilder', 'GET', 'action=getGpus', aggiornaGpus);
    }
    function caricaStorages(moboId) {
        loadAjaxDoc('pcBuilder', 'GET', 'action=getStorages&moboId=' + moboId, aggiornaStorages);
    }
    function caricaPsus(cpuId, gpuId) {
        loadAjaxDoc('pcBuilder', 'GET', 'action=getPsus&cpuId=' + cpuId + '&gpuId=' + gpuId, aggiornaPsus);
    }
    function caricaCases(moboId) {
        loadAjaxDoc('pcBuilder', 'GET', 'action=getCases&moboId=' + moboId, aggiornaCases);
    }
    function caricaDissipatori(cpuId) {
        loadAjaxDoc('pcBuilder', 'GET', 'action=getDissipatori&cpuId=' + cpuId, aggiornaDissipatori);
    }

    // ─────────────────────────────────────────────
    //  Callback handlers
    // ─────────────────────────────────────────────

    function aggiornaCpus(request) {
        var response = JSON.parse(request.responseText);
        if (response.functionName === 'errore') { document.getElementById('errCpu').innerHTML = response.result; return; }
        popolaSelect('selCpu', response.result, '-- Seleziona una CPU --',
            function(item) { return item.idProdotto; },
            function(item) { return item.nome + ' \u2014 ' + item.socket + ' \u2014 ' + item.core + ' core \u2014 \u20ac' + item.prezzo.toFixed(2); },
            function(item) { return item.prezzo; }
        );
    }

    function aggiornaMobos(request) {
        var response = JSON.parse(request.responseText);
        if (response.functionName === 'errore') { document.getElementById('errMobo').innerHTML = response.result; return; }
        popolaSelect('selMobo', response.result, '-- Seleziona una scheda madre --',
            function(item) { return item.idProdotto; },
            function(item) { return item.nome + ' \u2014 ' + item.chipset + ' \u2014 ' + item.formato + ' \u2014 \u20ac' + item.prezzo.toFixed(2); },
            function(item) { return item.prezzo; }
        );
        abilitaSelect('selMobo', 'num-mobo', 'lbl-mobo');
    }

    function aggiornaRams(request) {
        var response = JSON.parse(request.responseText);
        if (response.functionName === 'errore') { document.getElementById('errRam').innerHTML = response.result; return; }
        popolaSelect('selRam', response.result, '-- Seleziona RAM --',
            function(item) { return item.idProdotto; },
            function(item) { return item.nome + ' \u2014 ' + item.capacita + ' \u2014 ' + item.tipo + ' ' + item.frequenza + ' \u2014 \u20ac' + item.prezzo.toFixed(2); },
            function(item) { return item.prezzo; }
        );
        abilitaSelect('selRam', 'num-ram', 'lbl-ram');
    }

    function aggiornaGpus(request) {
        var response = JSON.parse(request.responseText);
        if (response.functionName === 'errore') { document.getElementById('errGpu').innerHTML = response.result; return; }
        popolaSelect('selGpu', response.result, '-- Seleziona una GPU --',
            function(item) { return item.idProdotto; },
            function(item) { return item.nome + ' \u2014 ' + item.vram + ' ' + item.tipoVram + ' \u2014 \u20ac' + item.prezzo.toFixed(2); },
            function(item) { return item.prezzo; }
        );
        abilitaSelect('selGpu', 'num-gpu', 'lbl-gpu');
    }

    function aggiornaStorages(request) {
        var response = JSON.parse(request.responseText);
        if (response.functionName === 'errore') { document.getElementById('errStorage').innerHTML = response.result; return; }
        popolaSelect('selStorage', response.result, '-- Seleziona uno storage --',
            function(item) { return item.idProdotto; },
            function(item) { return item.nome + ' \u2014 ' + item.capacita + ' \u2014 ' + item.tipo + ' ' + item.tecnologia + ' \u2014 \u20ac' + item.prezzo.toFixed(2); },
            function(item) { return item.prezzo; }
        );
        abilitaSelect('selStorage', 'num-storage', 'lbl-storage');
    }

    function aggiornaPsus(request) {
        var response = JSON.parse(request.responseText);
        if (response.functionName === 'errore') { document.getElementById('errPsu').innerHTML = response.result; return; }
        popolaSelect('selPsu', response.result, '-- Seleziona un alimentatore --',
            function(item) { return item.idProdotto; },
            function(item) { return item.nome + ' \u2014 ' + item.potenza + 'W \u2014 ' + item.certificazione + ' \u2014 ' + item.modulare + ' \u2014 \u20ac' + item.prezzo.toFixed(2); },
            function(item) { return item.prezzo; }
        );
        abilitaSelect('selPsu', 'num-psu', 'lbl-psu');
    }

    function aggiornaCases(request) {
        var response = JSON.parse(request.responseText);
        if (response.functionName === 'errore') { document.getElementById('errCase').innerHTML = response.result; return; }
        popolaSelect('selCase', response.result, '-- Seleziona un case --',
            function(item) { return item.idProdotto; },
            function(item) { return item.nome + ' \u2014 ' + item.formato + ' \u2014 ' + item.colore + ' \u2014 \u20ac' + item.prezzo.toFixed(2); },
            function(item) { return item.prezzo; }
        );
        abilitaSelect('selCase', 'num-case', 'lbl-case');
    }

    function aggiornaDissipatori(request) {
        var response = JSON.parse(request.responseText);
        if (response.functionName === 'errore') { document.getElementById('errDissipatore').innerHTML = response.result; return; }
        popolaSelect('selDissipatore', response.result, '-- Seleziona un dissipatore --',
            function(item) { return item.idProdotto; },
            function(item) { return item.nome + ' \u2014 ' + item.tipo + ' \u2014 TDP ' + item.tdpSupportato + 'W \u2014 \u20ac' + item.prezzo.toFixed(2); },
            function(item) { return item.prezzo; }
        );
        abilitaSelect('selDissipatore', 'num-diss', 'lbl-diss');
    }

    // ─────────────────────────────────────────────
    //  Logica di cascata onchange
    // ─────────────────────────────────────────────

    function onCpuChange() {
        var cpuId = document.getElementById('selCpu').value;
        resetSelect('selMobo',        '-- Prima seleziona una CPU --',             'num-mobo',    'lbl-mobo');
        resetSelect('selRam',         '-- Prima seleziona una scheda madre --',     'num-ram',     'lbl-ram');
        resetSelect('selGpu',         '-- Prima seleziona una CPU --',              'num-gpu',     'lbl-gpu');
        resetSelect('selStorage',     '-- Prima seleziona una scheda madre --',     'num-storage', 'lbl-storage');
        resetSelect('selPsu',         '-- Prima seleziona CPU e GPU --',            'num-psu',     'lbl-psu');
        resetSelect('selCase',        '-- Prima seleziona una scheda madre --',     'num-case',    'lbl-case');
        resetSelect('selDissipatore', '-- Prima seleziona una CPU --',              'num-diss',    'lbl-diss');
        aggiornaRiepilogo();
        if (!cpuId) return;
        caricaMobos(cpuId);
        caricaGpus();
        caricaDissipatori(cpuId);
    }

    function onMoboChange() {
        var moboId = document.getElementById('selMobo').value;
        resetSelect('selRam',     '-- Prima seleziona una scheda madre --', 'num-ram',     'lbl-ram');
        resetSelect('selStorage', '-- Prima seleziona una scheda madre --', 'num-storage', 'lbl-storage');
        resetSelect('selPsu',     '-- Prima seleziona CPU e GPU --',        'num-psu',     'lbl-psu');
        resetSelect('selCase',    '-- Prima seleziona una scheda madre --', 'num-case',    'lbl-case');
        aggiornaRiepilogo();
        if (!moboId) return;
        caricaRams(moboId);
        caricaStorages(moboId);
        caricaCases(moboId);
    }

    function onGpuChange() {
        var cpuId = document.getElementById('selCpu').value;
        var gpuId = document.getElementById('selGpu').value;
        resetSelect('selPsu', '-- Prima seleziona CPU e GPU --', 'num-psu', 'lbl-psu');
        aggiornaRiepilogo();
        if (!cpuId || !gpuId) return;
        caricaPsus(cpuId, gpuId);
    }

    // ─────────────────────────────────────────────
    //  Utilità DOM
    // ─────────────────────────────────────────────

    function popolaSelect(selectId, items, placeholder, fnId, fnLabel, fnPrezzo) {
        var sel = document.getElementById(selectId);
        sel.innerHTML = '<option value="">' + placeholder + '</option>';
        if (!items || items.length === 0) {
            sel.innerHTML = '<option value="">Nessun componente compatibile</option>';
            return;
        }
        items.forEach(function (item) {
            var opt = document.createElement('option');
            opt.value = fnId(item);
            opt.text  = fnLabel(item);
            opt.setAttribute('data-prezzo', fnPrezzo(item) || 0);
            sel.appendChild(opt);
        });
    }

    function resetSelect(selectId, placeholder, numId, lblId) {
        var sel = document.getElementById(selectId);
        sel.innerHTML = '<option value="">' + placeholder + '</option>';
        sel.disabled = true;
        if (numId) {
            document.getElementById(numId).classList.add('disabled');
            document.getElementById(lblId).classList.add('disabled');
        }
    }

    function abilitaSelect(selectId, numId, lblId) {
        document.getElementById(selectId).disabled = false;
        if (numId) {
            document.getElementById(numId).classList.remove('disabled');
            document.getElementById(lblId).classList.remove('disabled');
        }
    }

    // ─────────────────────────────────────────────
    //  Riepilogo build
    // ─────────────────────────────────────────────

    var componenti = [
        { id: 'selCpu',         label: 'CPU' },
        { id: 'selMobo',        label: 'Scheda Madre' },
        { id: 'selRam',         label: 'RAM' },
        { id: 'selGpu',         label: 'GPU' },
        { id: 'selStorage',     label: 'Storage' },
        { id: 'selPsu',         label: 'Alimentatore' },
        { id: 'selCase',        label: 'Case' },
        { id: 'selDissipatore', label: 'Dissipatore' }
    ];

    function aggiornaRiepilogo() {
        var lista = document.getElementById('listaRiepilogo');
        lista.innerHTML = '';
        var totale = 0;
        var qualcosa = false;

        componenti.forEach(function (c) {
            var sel = document.getElementById(c.id);
            if (sel.value) {
                qualcosa = true;
                var opt    = sel.options[sel.selectedIndex];
                var prezzo = parseFloat(opt.getAttribute('data-prezzo')) || 0;
                totale += prezzo;
                var li = document.createElement('li');
                li.innerHTML = '<strong>' + c.label + '</strong>' + opt.text;
                lista.appendChild(li);
            }
        });

        if (!qualcosa) {
            lista.innerHTML = '<li class="builder-empty">Nessun componente selezionato.</li>';
        }

        document.getElementById('prezzoTotale').innerHTML =
            qualcosa ? 'Totale: \u20ac' + totale.toFixed(2) : '';

        aggiornaBottoneCarrello();
    }

    // ─────────────────────────────────────────────
    //  Carrello
    // ─────────────────────────────────────────────

    function aggiornaBottoneCarrello() {
        var tuttiSelezionati = componenti.every(function(c) {
            return document.getElementById(c.id).value !== '';
        });
        document.getElementById('wrapperCarrello').style.display =
            tuttiSelezionati ? 'block' : 'none';
        document.getElementById('msgCarrello').innerHTML = '';
    }

    function aggiungiTuttiAlCarrello() {
        var ids = componenti.map(function(c) {
            return document.getElementById(c.id).value;
        });

        var msg = document.getElementById('msgCarrello');
        msg.innerHTML = 'Aggiunta in corso...';
        msg.className = 'builder-cart-msg';

        var completati = 0;
        var errori = 0;

        ids.forEach(function(idProdotto) {
            loadAjaxDoc(
                'carrello/add', 'GET',
                'aggiungi=' + idProdotto + '&quantita=1',
                function(request) {
                    var response = JSON.parse(request.responseText);
				    console.log(response);
                    if (response.success) { completati++; } else { errori++; }
                    if (completati + errori === ids.length) {
                        if (errori === 0) {
                            msg.innerHTML = '&#x2713; Tutti i componenti aggiunti al carrello!';
                            msg.className = 'builder-cart-msg success';
						    aggiornaBadgeCarrello(response.numProdotti);
                        } else {
                            msg.innerHTML = '&#x26A0; ' + errori + ' componente/i non aggiunto/i.';
                            msg.className = 'builder-cart-msg error';
                        }
                    }
                }
            );
        });
    }

    // ─────────────────────────────────────────────
    //  Avvio
    // ─────────────────────────────────────────────
    window.onload = function () {
        caricaCpus();
    };