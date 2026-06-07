<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aggiungi Prodotto - Overclocked Admin</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">

<style>

* { box-sizing: border-box; margin: 0; padding: 0; }

body {
    background: #080808;
    color: white;
    font-family: Arial, sans-serif;
}

.container {
    width: 800px;
    margin: auto;
    padding: 30px 0 60px;
}

.page-header {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    margin-bottom: 24px;
}

.page-header h1 { font-size: 20px; color: #ff7300; margin-bottom: 4px; }
.page-header p  { font-size: 13px; color: #444; }

.btn-back {
    font-size: 12px;
    color: #666;
    text-decoration: none;
    border: 1px solid #1f1f1f;
    border-radius: 7px;
    padding: 7px 14px;
    white-space: nowrap;
}
.btn-back:hover { border-color: #333; color: #e0e0e0; }

/* alert */
.alert {
    padding: 12px 16px;
    border-radius: 8px;
    font-size: 13px;
    margin-bottom: 18px;
}
.alert-error   { background: #1a0808; border: 1px solid #5a1a1a; color: #f44336; }
.alert-success { background: #0a1a0a; border: 1px solid #1a5a1a; color: #4caf50; }

/* card */
.card {
    background: #0f0f0f;
    border: 1px solid #1a1a1a;
    border-radius: 11px;
    padding: 20px;
    margin-bottom: 16px;
}

.card-title {
    font-size: 10px;
    color: #ff7300;
    letter-spacing: 2px;
    text-transform: uppercase;
    font-weight: bold;
    margin-bottom: 16px;
    padding-bottom: 10px;
    border-bottom: 1px solid #1a1a1a;
}

/* campi */
.field { margin-bottom: 14px; }
.field:last-child { margin-bottom: 0; }

.grid2 { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; margin-bottom: 14px; }
.grid2 .field { margin-bottom: 0; }
.grid3 { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 14px; margin-bottom: 14px; }
.grid3 .field { margin-bottom: 0; }

label {
    display: block;
    font-size: 12px;
    color: #666;
    margin-bottom: 6px;
}
.req { color: #ff7300; margin-left: 2px; }

input[type="text"],
input[type="number"],
select,
textarea {
    width: 100%;
    background: #080808;
    border: 1px solid #1f1f1f;
    border-radius: 7px;
    padding: 9px 11px;
    font-size: 13px;
    color: #e0e0e0;
    outline: none;
    font-family: Arial, sans-serif;
}
input:focus, select:focus, textarea:focus { border-color: #ff7300; }
input::placeholder, textarea::placeholder { color: #2a2a2a; }
select { appearance: none; cursor: pointer; color: #888; }
select option { background: #0f0f0f; color: #e0e0e0; }
textarea { resize: vertical; min-height: 80px; }

/* tab categoria */
.cat-tabs { display: flex; gap: 6px; flex-wrap: wrap; }
.cat-tab {
    font-size: 12px;
    padding: 5px 12px;
    border-radius: 5px;
    border: 1px solid #1f1f1f;
    color: #555;
    cursor: pointer;
    background: #080808;
    transition: all .15s;
}
.cat-tab:hover { border-color: #333; color: #aaa; }
.cat-tab.active { background: #110a00; border-color: #ff7300; color: #ff7300; }

/* sezioni categoria */
.cat-section { display: none; }
.cat-section.visible { display: block; }

/* slot immagini */
.img-slots {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 10px;
    margin-bottom: 10px;
}

.img-slot {
    aspect-ratio: 1;
    background: #080808;
    border: 1px dashed #1f1f1f;
    border-radius: 9px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: border-color .15s, background .15s;
}
.img-slot:hover { border-color: #ff7300; background: #110a00; }
.img-slot .plus { font-size: 20px; color: #2a2a2a; }

.img-slot .preview-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
    border-radius: 8px;
}

.img-slot .slot-num {
    position: absolute;
    bottom: 4px;
    left: 6px;
    font-size: 9px;
    color: #ff7300;
    font-weight: bold;
}

.img-slot .rm-btn {
    position: absolute;
    top: 4px;
    right: 4px;
    width: 16px;
    height: 16px;
    background: rgba(0,0,0,0.85);
    border: none;
    border-radius: 50%;
    color: #f44336;
    font-size: 10px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0;
}

.img-slot.filled { border-style: solid; border-color: #1f1f1f; }

.image-container {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 10px;
    margin-bottom: 10px;
}

.image-slot {
    aspect-ratio: 1;
    background: #080808;
    border: 1px dashed #1f1f1f;
    border-radius: 9px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: border-color .15s, background .15s;
}
.image-slot:hover { border-color: #ff7300; background: #110a00; }

.image-slot span {
    font-size: 22px;
    color: #2a2a2a;
    pointer-events: none;
}

.image-slot img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 8px;
    display: none;
}

.image-slot.filled { border-style: solid; border-color: #1f1f1f; cursor: default; }

.image-slot .rm-btn {
    position: absolute;
    top: 4px;
    right: 4px;
    width: 18px;
    height: 18px;
    background: rgba(0,0,0,0.85);
    border: none;
    border-radius: 50%;
    color: #f44336;
    font-size: 11px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0;
}

.upload-hint { font-size: 11px; color: #333; text-align: right; }

/* bottoni */
.btn-submit {
    width: 100%;
    height: 44px;
    background: #ff7300;
    color: #000;
    border: none;
    border-radius: 9px;
    font-size: 15px;
    font-weight: bold;
    cursor: pointer;
    margin-bottom: 10px;
}
.btn-submit:hover { opacity: .9; }

.btn-cancel {
    display: block;
    text-align: center;
    width: 100%;
    height: 38px;
    line-height: 38px;
    background: transparent;
    color: #555;
    border: 1px solid #1f1f1f;
    border-radius: 9px;
    font-size: 13px;
    text-decoration: none;
}
.btn-cancel:hover { border-color: #333; color: #888; }

.hint { font-size: 11px; color: #333; margin-bottom: 14px; }

</style>
</head>
<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="container">

    <div class="page-header">
        <div>
            <h1>Aggiungi Prodotto</h1>
            <p>Seleziona la categoria e compila i campi</p>
        </div>
        <a class="btn-back" href="${pageContext.request.contextPath}/admin/dashboard">&#8592; Torna al pannello</a>
    </div>

    <c:if test="${not empty errore}">
        <div class="alert alert-error">${errore}</div>
    </c:if>
    <c:if test="${not empty param.success}">
        <div class="alert alert-success">Prodotto aggiunto con successo!</div>
    </c:if>

    <form
        action="${pageContext.request.contextPath}/admin/aggiungiProdotto?action=insert"
        method="post"
        enctype="multipart/form-data"
        id="formProdotto">

        <%-- CATEGORIA --%>
        <div class="card">
            <div class="card-title">Categoria</div>
            <div class="cat-tabs">
                <div class="cat-tab" data-cat="CPU"         onclick="selezionaCategoria('CPU')">CPU</div>
                <div class="cat-tab" data-cat="GPU"         onclick="selezionaCategoria('GPU')">GPU</div>
                <div class="cat-tab" data-cat="RAM"         onclick="selezionaCategoria('RAM')">RAM</div>
                <div class="cat-tab" data-cat="STORAGE"     onclick="selezionaCategoria('STORAGE')">Storage</div>
                <div class="cat-tab" data-cat="MOBO"        onclick="selezionaCategoria('MOBO')">Scheda madre</div>
                <div class="cat-tab" data-cat="PSU"         onclick="selezionaCategoria('PSU')">Alimentatore</div>
                <div class="cat-tab" data-cat="CASE"        onclick="selezionaCategoria('CASE')">Case</div>
                <div class="cat-tab" data-cat="DISSIPATORE" onclick="selezionaCategoria('DISSIPATORE')">Dissipatore</div>
            </div>
            <input type="hidden" name="categoria" id="inputCategoria" value=""/>
        </div>

        <%-- INFORMAZIONI GENERALI --%>
        <div class="card">
            <div class="card-title">Informazioni generali</div>

            <div class="grid2">
                <div class="field">
                    <label>Nome <span class="req">*</span></label>
                    <input type="text" name="nome" placeholder="es. Core i9-14900K" required/>
                </div>
                <div class="field">
                    <label>Modello <span class="req">*</span></label>
                    <input type="text" name="modello" placeholder="es. i9-14900K" required/>
                </div>
            </div>

            <div class="grid2">
                <div class="field">
                    <label>Marca <span class="req">*</span></label>
                    <input type="text" name="marca" placeholder="es. Intel" required/>
                </div>
                <div class="field">
                    <label>Attivo</label>
                    <select name="attivo">
                        <option value="true">S&igrave;</option>
                        <option value="false">No</option>
                    </select>
                </div>
            </div>

            <div class="field">
                <label>Descrizione</label>
                <textarea name="descrizione" placeholder="Descrizione del prodotto..."></textarea>
            </div>
        </div>

        <%-- PREZZO E DISPONIBILITA --%>
        <div class="card">
            <div class="card-title">Prezzo e disponibilit&agrave;</div>

            <div class="grid3">
                <div class="field">
                    <label>Prezzo (&euro;) <span class="req">*</span></label>
                    <input type="number" name="prezzo" step="0.01" min="0" placeholder="es. 599.99" required/>
                </div>
                <div class="field">
                    <label>Sconto (%)</label>
                    <input type="number" name="sconto" min="0" max="100" value="0"/>
                </div>
                <div class="field">
                    <label>Stock <span class="req">*</span></label>
                    <input type="number" name="stock" min="0" placeholder="es. 20" required/>
                </div>
            </div>

            <div class="grid2">
                <div class="field">
                    <label>Peso</label>
                    <input type="text" name="peso" placeholder="es. 1.4 kg"/>
                </div>
                <div class="field">
                    <label>Dimensioni</label>
                    <input type="text" name="dimensioni" placeholder="es. 357x149x70 mm"/>
                </div>
            </div>
        </div>

        <%-- ===== CPU ===== --%>
        <div class="card cat-section" id="section-CPU">
            <div class="card-title">Specifiche CPU</div>
            <div class="grid3">
                <div class="field">
                    <label>Core <span class="req">*</span></label>
                    <input type="number" name="core" min="1" placeholder="es. 24"/>
                </div>
                <div class="field">
                    <label>Thread <span class="req">*</span></label>
                    <input type="number" name="thread" min="1" placeholder="es. 32"/>
                </div>
                <div class="field">
                    <label>TDP (W)</label>
                    <input type="number" name="tdp" min="0" placeholder="es. 125"/>
                </div>
            </div>
            <div class="grid3">
                <div class="field">
                    <label>Socket</label>
                    <input type="text" name="socket" placeholder="es. LGA1700"/>
                </div>
                <div class="field">
                    <label>Tipo RAM supportata</label>
                    <input type="text" name="tiporam" placeholder="es. DDR5"/>
                </div>
                <div class="field">
                    <label>Frequenza</label>
                    <input type="text" name="frequenza" placeholder="es. 3.2 GHz"/>
                </div>
            </div>
            <div class="field">
                <label>Frequenza RAM supportata</label>
                <input type="text" name="frequenzaram" placeholder="es. 5600 MHz"/>
            </div>
        </div>

        <%-- ===== GPU ===== --%>
        <div class="card cat-section" id="section-GPU">
            <div class="card-title">Specifiche GPU</div>
            <div class="grid3">
                <div class="field">
                    <label>VRAM</label>
                    <input type="text" name="vram" placeholder="es. 24 GB"/>
                </div>
                <div class="field">
                    <label>Tipo VRAM</label>
                    <input type="text" name="tipovram" placeholder="es. GDDR6X"/>
                </div>
                <div class="field">
                    <label>TDP (W)</label>
                    <input type="number" name="tdp" min="0" placeholder="es. 600"/>
                </div>
            </div>
            <div class="grid3">
                <div class="field">
                    <label>Frequenza boost</label>
                    <input type="text" name="frequenza" placeholder="es. 2640 MHz"/>
                </div>
                <div class="field">
                    <label>PCIe</label>
                    <input type="text" name="pcie" placeholder="es. PCIe 4.0 x16"/>
                </div>
                <div class="field">
                    <label>Uscite video</label>
                    <input type="text" name="video" placeholder="es. 3x DP, 1x HDMI"/>
                </div>
            </div>
            <div class="field">
                <label>Risoluzione massima</label>
                <input type="text" name="maxres" placeholder="es. 7680x4320"/>
            </div>
        </div>

        <%-- ===== RAM ===== --%>
        <div class="card cat-section" id="section-RAM">
            <div class="card-title">Specifiche RAM</div>
            <div class="grid3">
                <div class="field">
                    <label>Capacit&agrave;</label>
                    <input type="text" name="capacita" placeholder="es. 32 GB"/>
                </div>
                <div class="field">
                    <label>Frequenza</label>
                    <input type="text" name="frequenza" placeholder="es. 6000 MHz"/>
                </div>
                <div class="field">
                    <label>Tipo</label>
                    <input type="text" name="tipo" placeholder="es. DDR5"/>
                </div>
            </div>
        </div>

        <%-- ===== STORAGE ===== --%>
        <div class="card cat-section" id="section-STORAGE">
            <div class="card-title">Specifiche Storage</div>
            <div class="grid3">
                <div class="field">
                    <label>Capacit&agrave;</label>
                    <input type="text" name="capacita" placeholder="es. 2 TB"/>
                </div>
                <div class="field">
                    <label>Formato</label>
                    <input type="text" name="formato" placeholder="es. M.2 2280"/>
                </div>
                <div class="field">
                    <label>Tipo</label>
                    <select name="tipo">
                        <option value="SSD">SSD</option>
                        <option value="HDD">HDD</option>
                    </select>
                </div>
            </div>
            <div class="grid3">
                <div class="field">
                    <label>Vel. lettura (MB/s)</label>
                    <input type="number" name="lettura" min="0" placeholder="es. 7400"/>
                </div>
                <div class="field">
                    <label>Vel. scrittura (MB/s)</label>
                    <input type="number" name="scrittura" min="0" placeholder="es. 6900"/>
                </div>
                <div class="field">
                    <label>Tecnologia</label>
                    <select name="tecnologia">
                        <option value="NVME">NVMe</option>
                        <option value="SATA">SATA</option>
                    </select>
                </div>
            </div>
        </div>

        <%-- ===== MOBO ===== --%>
        <div class="card cat-section" id="section-MOBO">
            <div class="card-title">Specifiche Scheda Madre</div>
            <div class="grid3">
                <div class="field">
                    <label>Chipset</label>
                    <input type="text" name="chipset" placeholder="es. Z790"/>
                </div>
                <div class="field">
                    <label>Socket</label>
                    <input type="text" name="socket" placeholder="es. LGA1700"/>
                </div>
                <div class="field">
                    <label>Formato</label>
                    <input type="text" name="formato" placeholder="es. ATX"/>
                </div>
            </div>
            <div class="grid3">
                <div class="field">
                    <label>Tipo RAM</label>
                    <input type="text" name="tipoRam" placeholder="es. DDR5"/>
                </div>
                <div class="field">
                    <label>Freq. max RAM</label>
                    <input type="text" name="maxFreq" placeholder="es. 7200 MHz"/>
                </div>
                <div class="field">
                    <label>PCIe</label>
                    <input type="text" name="pcie" placeholder="es. PCIe 5.0"/>
                </div>
            </div>
            <div class="grid3">
                <div class="field">
                    <label>Slot RAM</label>
                    <input type="number" name="slotRam" min="1" placeholder="es. 4"/>
                </div>
                <div class="field">
                    <label>Porte SATA</label>
                    <input type="number" name="porteSata" min="0" placeholder="es. 6"/>
                </div>
                <div class="field">
                    <label>Porte USB</label>
                    <input type="number" name="porteUsb" min="0" placeholder="es. 10"/>
                </div>
            </div>
            <div class="field">
                <label>NVMe</label>
                <select name="nvme">
                    <option value="true">S&igrave;</option>
                    <option value="false">No</option>
                </select>
            </div>
        </div>

        <%-- ===== PSU ===== --%>
        <div class="card cat-section" id="section-PSU">
            <div class="card-title">Specifiche Alimentatore</div>
            <div class="grid2">
                <div class="field">
                    <label>Potenza (W)</label>
                    <input type="number" name="potenza" min="0" placeholder="es. 1000"/>
                </div>
                <div class="field">
                    <label>Certificazione</label>
                    <input type="text" name="certificazione" placeholder="es. 80+ Gold"/>
                </div>
            </div>
            <div class="grid2">
                <div class="field">
                    <label>Modulare</label>
                    <select name="modulare">
                        <option value="PIENO">Pieno</option>
                        <option value="SEMI">Semi-modulare</option>
                        <option value="NESSUNO">Non modulare</option>
                    </select>
                </div>
                <div class="field">
                    <label>Formato</label>
                    <select name="formato">
                        <option value="ATX">ATX</option>
                        <option value="SFX">SFX</option>
                        <option value="TFX">TFX</option>
                    </select>
                </div>
            </div>
        </div>

        <%-- ===== CASE ===== --%>
        <div class="card cat-section" id="section-CASE">
            <div class="card-title">Specifiche Case</div>
            <div class="grid3">
                <div class="field">
                    <label>Formato</label>
                    <input type="text" name="formato" placeholder="es. Full Tower"/>
                </div>
                <div class="field">
                    <label>Colore</label>
                    <input type="text" name="colore" placeholder="es. Nero"/>
                </div>
                <div class="field">
                    <label>Materiale</label>
                    <input type="text" name="materiale" placeholder="es. Acciaio + Vetro"/>
                </div>
            </div>
        </div>

        <%-- ===== DISSIPATORE ===== --%>
        <div class="card cat-section" id="section-DISSIPATORE">
            <div class="card-title">Specifiche Dissipatore</div>
            <div class="grid3">
                <div class="field">
                    <label>Tipo</label>
                    <select name="tipo">
                        <option value="ARIA">Aria</option>
                        <option value="LIQUIDO">Liquido</option>
                    </select>
                </div>
                <div class="field">
                    <label>TDP supportato (W)</label>
                    <input type="number" name="tdp" min="0" placeholder="es. 250"/>
                </div>
                <div class="field">
                    <label>Socket supportati</label>
                    <input type="text" name="socket" placeholder="es. LGA1700, AM5"/>
                </div>
            </div>
            <div class="grid2">
                <div class="field">
                    <label>RPM max</label>
                    <input type="number" name="rpm" min="0" placeholder="es. 1500"/>
                </div>
                <div class="field">
                    <label>Rumore (dBA)</label>
                    <input type="number" name="rumore" min="0" placeholder="es. 25"/>
                </div>
            </div>
        </div>

        <%-- IMMAGINI --%>
        <div class="card">
            <div class="card-title">
                Immagini
                <span style="font-size:10px;color:#444;text-transform:none;letter-spacing:0;font-weight:normal">
                    (max 5 &mdash; JPG / PNG / WEBP)
                </span>
            </div>

            <div class="image-container" id="imgSlots">
                <div class="image-slot" onclick="apriSlot(this)">
                    <input type="file" name="immagine1" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <span>&#43;</span>
                    <img alt=""/>
                </div>
                <div class="image-slot" onclick="apriSlot(this)">
                    <input type="file" name="immagine2" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <span>&#43;</span>
                    <img alt=""/>
                </div>
                <div class="image-slot" onclick="apriSlot(this)">
                    <input type="file" name="immagine3" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <span>&#43;</span>
                    <img alt=""/>
                </div>
                <div class="image-slot" onclick="apriSlot(this)">
                    <input type="file" name="immagine4" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <span>&#43;</span>
                    <img alt=""/>
                </div>
                <div class="image-slot" onclick="apriSlot(this)">
                    <input type="file" name="immagine5" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <span>&#43;</span>
                    <img alt=""/>
                </div>
            </div>

            <div class="upload-hint" id="uploadHint">0 / 5 immagini selezionate</div>
        </div>

        <%-- SALVA --%>
        <div class="card">
            <div class="card-title">Salva</div>
            <p class="hint">
                I campi con <span class="req">*</span> sono obbligatori.
                La sezione specifiche cambia in base alla categoria selezionata.
            </p>
            <button type="submit" class="btn-submit">Salva prodotto</button>
            <button type="reset" class="btn-submit">Annulla</button>
        </div>

    </form>
</div>


<script>

// ── INIZIALIZZAZIONE ─────────────────────────────────────────────────

window.addEventListener('DOMContentLoaded', function() {
    selezionaCategoria('CPU');
});

// ── CATEGORIA ────────────────────────────────────────────────────────

function selezionaCategoria(cat) {
    document.querySelectorAll('.cat-tab').forEach(function(t) {
        t.classList.toggle('active', t.dataset.cat === cat);
    });
    document.querySelectorAll('.cat-section').forEach(function(s) {
        s.classList.remove('visible');
    });
    var sezione = document.getElementById('section-' + cat);
    if (sezione) sezione.classList.add('visible');
    document.getElementById('inputCategoria').value = cat;
}

// ── IMMAGINI ─────────────────────────────────────────────────────────

function apriSlot(slot) {
    // se lo slot è già pieno non riaprire il picker
    if (slot.classList.contains('filled')) return;
    slot.querySelector('input[type="file"]').click();
}

function gestisciSlot(input) {
    if (!input.files || input.files.length === 0) return;

    var file = input.files[0];
    var slot = input.closest('.image-slot');
    var img  = slot.querySelector('img');
    var span = slot.querySelector('span');

    var reader = new FileReader();
    reader.onload = function(e) {
        img.src = e.target.result;
        img.style.display = 'block';
        span.style.display = 'none';
        slot.classList.add('filled');

        // bottone rimuovi (evita duplicati)
        if (!slot.querySelector('.rm-btn')) {
            var btn = document.createElement('button');
            btn.type = 'button';
            btn.className = 'rm-btn';
            btn.innerHTML = '&#x2715;';
            btn.onclick = function(e) {
                e.stopPropagation();
                svuotaSlot(slot);
            };
            slot.appendChild(btn);
        }

        aggiornaHint();
    };
    reader.readAsDataURL(file);
}

function svuotaSlot(slot) {
    var input = slot.querySelector('input[type="file"]');
    var img   = slot.querySelector('img');
    var span  = slot.querySelector('span');
    var btn   = slot.querySelector('.rm-btn');

    // reset input file
    input.value = '';

    img.src = '';
    img.style.display = 'none';
    span.style.display = 'block';
    slot.classList.remove('filled');

    if (btn) btn.remove();

    aggiornaHint();
}

function aggiornaHint() {
    var pieni = document.querySelectorAll('.image-slot.filled').length;
    document.getElementById('uploadHint').textContent =
        pieni + ' / 5 immagini selezionate';
}

// ── DISABILITA CAMPI CATEGORIE NON ATTIVE ────────────────────────────

function disabilitaSezionInattive() {
    document.querySelectorAll('.cat-section').forEach(function(sezione) {
        var attiva = sezione.classList.contains('visible');
        sezione.querySelectorAll('input, select, textarea').forEach(function(campo) {
            campo.disabled = !attiva;
        });
    });
}

// ── VALIDAZIONE SUBMIT ───────────────────────────────────────────────

document.getElementById('formProdotto').addEventListener('submit', function(e) {
    if (!document.getElementById('inputCategoria').value) {
        e.preventDefault();
        alert('Seleziona una categoria prima di salvare.');
        return;
    }
    disabilitaSezionInattive();
});

</script>

</body>
</html>