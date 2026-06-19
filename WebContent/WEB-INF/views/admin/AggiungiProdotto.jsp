<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Overclocked - Admin</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tema.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/modificaProdotto.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
</head>

<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="container">

    <h1>
        Aggiungi Prodotto
        <a class="btn-cancel" href="${pageContext.request.contextPath}/admin/dashboard"
           style="font-size:14px;margin-left:16px;">&#8592; Torna al pannello</a>
    </h1>

    <c:if test="${not empty param.errore}">
    	<div class="alert alert-error">${param.errore}</div>
	</c:if>
    <c:if test="${not empty param.success}">
        <div class="alert alert-success">Prodotto aggiunto con successo!</div>
    </c:if>

    <form
        action="${pageContext.request.contextPath}/admin/aggiungiProdotto?action=aggiungi"
        method="post"
        enctype="multipart/form-data"
        id="formProdotto"
        novalidate>

        <%-- CATEGORIA --%>
        <div class="section">
            <h2>Categoria</h2>
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

        <%-- DATI GENERALI --%>
        <div class="section">
            <h2>Dati Generali</h2>

            <div class="row2">
                <div class="form-group">
                    <label>Nome</label>
                    <input type="text" name="nome" placeholder="es. Core i9-14900K" required/>
                </div>
                <div class="form-group">
                    <label>Modello</label>
                    <input type="text" name="modello" placeholder="es. i9-14900K" required/>
                </div>
            </div>

            <div class="row2">
                <div class="form-group">
                    <label>Marca</label>
                    <input type="text" name="marca" placeholder="es. Intel" required/>
                </div>
                <div class="form-group">
                    <label>Attivo</label>
                    <select name="attivo">
                        <option value="true">S&igrave;</option>
                        <option value="false">No</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label>Descrizione</label>
                <textarea name="descrizione" placeholder="Descrizione del prodotto..."></textarea>
            </div>

            <div class="row3">
                <div class="form-group">
                    <label>Prezzo (&euro;)</label>
                    <input type="number" step="0.01" min="0" name="prezzo" placeholder="es. 599.99" required/>
                </div>
                <div class="form-group">
                    <label>Sconto (%)</label>
                    <input type="number" name="sconto" min="0" max="100" value="0"/>
                </div>
                <div class="form-group">
                    <label>Stock</label>
                    <input type="number" name="stock" min="0" placeholder="es. 20" required/>
                </div>
            </div>

            <div class="row2">
                <div class="form-group">
                    <label>Dimensioni</label>
                    <input type="text" name="dimensioni" placeholder="es. 357x149x70 mm" required/>
                </div>
                <div class="form-group">
                    <label>Peso</label>
                    <input type="text" name="peso" placeholder="es. 1.4 kg" required/>
                </div>
            </div>
        </div>

        <%-- ===== CPU ===== --%>
        <div class="section cat-section" id="section-CPU">
            <h2>Specifiche CPU</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Core</label>
                    <input type="number" name="core" min="1" placeholder="es. 24" required/>
                </div>
                <div class="form-group">
                    <label>Thread</label>
                    <input type="number" name="thread" min="1" placeholder="es. 32" required/>
                </div>
                <div class="form-group">
                    <label>TDP (W)</label>
                    <input type="number" name="tdp" min="0" placeholder="es. 125" required/>
                </div>
            </div>
            <div class="row3">
                <div class="form-group">
                    <label>Frequenza</label>
                    <input type="text" name="frequenza" placeholder="es. 3.2 GHz" required/>
                </div>
                <div class="form-group">
                    <label>Socket</label>
                    <input type="text" name="socket" placeholder="es. LGA1700" required/>
                </div>
                <div class="form-group">
                    <label>Tipo RAM</label>
                    <input type="text" name="tiporam" placeholder="es. DDR5" required/>
                </div>
            </div>
            <div class="form-group">
                <label>Frequenza RAM supportata</label>
                <input type="text" name="frequenzaram" placeholder="es. 5600 MHz" required/>
            </div>
        </div>

        <%-- ===== GPU ===== --%>
        <div class="section cat-section" id="section-GPU">
            <h2>Specifiche GPU</h2>
            <div class="row3">
                <div class="form-group">
                    <label>VRAM</label>
                    <input type="text" name="vram" placeholder="es. 24 GB" required/>
                </div>
                <div class="form-group">
                    <label>Tipo VRAM</label>
                    <input type="text" name="tipovram" placeholder="es. GDDR6X" required/>
                </div>
                <div class="form-group">
                    <label>TDP (W)</label>
                    <input type="number" name="tdp" min="0" placeholder="es. 600" required/>
                </div>
            </div>
            <div class="row3">
                <div class="form-group">\
                    <label>Frequenza boost</label>
                    <input type="text" name="frequenza" placeholder="es. 2640 MHz" required/>
                </div>
                <div class="form-group">
                    <label>PCIe</label>
                    <input type="text" name="pcie" placeholder="es. PCIe 4.0 x16" required/>
                </div>
                <div class="form-group">
                    <label>Uscite video</label>
                    <input type="text" name="video" placeholder="es. 3x DP, 1x HDMI" required/>
                </div>
            </div>
            <div class="form-group">
                <label>Risoluzione massima</label>
                <input type="text" name="maxres" placeholder="es. 7680x4320" required/>
            </div>
        </div>

        <%-- ===== RAM ===== --%>
        <div class="section cat-section" id="section-RAM">
            <h2>Specifiche RAM</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Capacit&agrave;</label>
                    <input type="text" name="capacita" placeholder="es. 32 GB" required/>
                </div>
                <div class="form-group">
                    <label>Frequenza</label>
                    <input type="text" name="frequenza" placeholder="es. 6000 MHz" required/>
                </div>
                <div class="form-group">
                    <label>Tipo</label>
                    <input type="text" name="tipo" placeholder="es. DDR5" required/>
                </div>
            </div>
        </div>

        <%-- ===== STORAGE ===== --%>
        <div class="section cat-section" id="section-STORAGE">
            <h2>Specifiche Storage</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Capacit&agrave;</label>
                    <input type="text" name="capacita" placeholder="es. 2 TB" required/>
                </div>
                <div class="form-group">
                    <label>Formato</label>
                    <input type="text" name="formato" placeholder="es. M.2 2280" required/>
                </div>
                <div class="form-group">
                    <label>Vel. lettura (MB/s)</label>
                    <input type="number" name="lettura" min="0" placeholder="es. 7400" required/>
                </div>
            </div>
            <div class="row3">
                <div class="form-group">
                    <label>Vel. scrittura (MB/s)</label>
                    <input type="number" name="scrittura" min="0" placeholder="es. 6900" required/>
                </div>
                <div class="form-group">
                    <label>Tipo</label>
                    <select name="tipo">
                        <option value="SSD">SSD</option>
                        <option value="HDD">HDD</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Tecnologia</label>
                    <select name="tecnologia">
                        <option value="NVME">NVMe</option>
                        <option value="SATA">SATA</option>
                    </select>
                </div>
            </div>
        </div>

        <%-- ===== MOBO ===== --%>
        <div class="section cat-section" id="section-MOBO">
            <h2>Specifiche Scheda Madre</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Chipset</label>
                    <input type="text" name="chipset" placeholder="es. Z790" required/>
                </div>
                <div class="form-group">
                    <label>Socket</label>
                    <input type="text" name="socket" placeholder="es. LGA1700" required/>
                </div>
                <div class="form-group">
                    <label>Formato</label>
                    <input type="text" name="formato" placeholder="es. ATX" required/>
                </div>
            </div>
            <div class="row3">
                <div class="form-group">
                    <label>Tipo RAM</label>
                    <input type="text" name="tipoRam" placeholder="es. DDR5" required/>
                </div>
                <div class="form-group">
                    <label>Freq. max RAM</label>
                    <input type="text" name="maxFreq" placeholder="es. 7200 MHz" required/>
                </div>
                <div class="form-group">
                    <label>PCIe</label>
                    <input type="text" name="pcie" placeholder="es. PCIe 5.0" required/>
                </div>
            </div>
            <div class="row3">
                <div class="form-group">
                    <label>Slot RAM</label>
                    <input type="number" name="slotRam" min="1" placeholder="es. 4" required/>
                </div>
                <div class="form-group">
                    <label>Porte SATA</label>
                    <input type="number" name="porteSata" min="0" placeholder="es. 6" required/>
                </div>
                <div class="form-group">
                    <label>Porte USB</label>
                    <input type="number" name="porteUsb" min="0" placeholder="es. 10" required/>
                </div>
            </div>
            <div class="form-group">
                <label>NVMe</label>
                <select name="nvme">
                    <option value="true">S&igrave;</option>
                    <option value="false">No</option>
                </select>
            </div>
        </div>

        <%-- ===== PSU ===== --%>
        <div class="section cat-section" id="section-PSU">
            <h2>Specifiche Alimentatore</h2>
            <div class="row2">
                <div class="form-group">
                    <label>Potenza (W)</label>
                    <input type="number" name="potenza" min="0" placeholder="es. 1000" required/>
                </div>
                <div class="form-group">
                    <label>Certificazione</label>
                    <input type="text" name="certificazione" placeholder="es. 80+ Gold" required/>
                </div>
            </div>
            <div class="row2">
                <div class="form-group">
                    <label>Modulare</label>
                    <select name="modulare">
                        <option value="MODULARE">Modulare</option>
                        <option value="SEMIMODULARE">Semi-modulare</option>
                        <option value="NON_MODULARE">Non modulare</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Formato</label>
                    <select name="formato">
                        <option value="ATX">ATX</option>
                        <option value="SFX">SFX</option>
                    </select>
                </div>
            </div>
        </div>

        <%-- ===== CASE ===== --%>
        <div class="section cat-section" id="section-CASE">
            <h2>Specifiche Case</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Formato</label>
                    <input type="text" name="formato" placeholder="es. Full Tower" required/>
                </div>
                <div class="form-group">
                    <label>Colore</label>
                    <input type="text" name="colore" placeholder="es. Nero" required/>
                </div>
                <div class="form-group">
                    <label>Materiale</label>
                    <input type="text" name="materiale" placeholder="es. Acciaio + Vetro" required/>
                </div>
            </div>
        </div>

        <%-- ===== DISSIPATORE ===== --%>
        <div class="section cat-section" id="section-DISSIPATORE">
            <h2>Specifiche Dissipatore</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Tipo</label>
                    <select name="tipo">
                        <option value="ARIA">Aria</option>
                        <option value="LIQUIDO">Liquido</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>TDP supportato (W)</label>
                    <input type="number" name="tdp" min="0" placeholder="es. 250" required/>
                </div>
                <div class="form-group">
                    <label>Socket supportati</label>
                    <input type="text" name="socket" placeholder="es. LGA1700, AM5" required/>
                </div>
            </div>
            <div class="row2">
                <div class="form-group">
                    <label>RPM max</label>
                    <input type="number" name="rpm" min="0" placeholder="es. 1500" required/>
                </div>
                <div class="form-group">
                    <label>Rumore (dBA)</label>
                    <input type="number" name="rumore" min="0" placeholder="es. 25" required/>
                </div>
            </div>
        </div>

        <%-- IMMAGINI --%>
        <div class="section">
            <h2>Immagini</h2>
            <p style="font-size:12px;color:#666;margin-bottom:10px">
                Puoi caricare fino a 5 immagini (JPG / PNG / WEBP).
            </p>
            <div class="image-container">
                <div class="image-slot" onclick="apriSlot(this)">
                    <input type="file" name="immagine1" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <span>&#43;</span><img alt=""/>
                </div>
                <div class="image-slot" onclick="apriSlot(this)">
                    <input type="file" name="immagine2" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <span>&#43;</span><img alt=""/>
                </div>
                <div class="image-slot" onclick="apriSlot(this)">
                    <input type="file" name="immagine3" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <span>&#43;</span><img alt=""/>
                </div>
                <div class="image-slot" onclick="apriSlot(this)">
                    <input type="file" name="immagine4" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <span>&#43;</span><img alt=""/>
                </div>
                <div class="image-slot" onclick="apriSlot(this)">
                    <input type="file" name="immagine5" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <span>&#43;</span><img alt=""/>
                </div>
            </div>
        </div>

        <%-- BOTTONI --%>
        <div class="btn-row">
            <button type="submit">Salva prodotto</button>
            <a class="btn-cancel" href="${pageContext.request.contextPath}/admin/dashboard">Annulla</a>
        </div>

    </form>

</div>
 
 
<script src="${pageContext.request.contextPath}/script/aggiungiProdotto.js"></script>
 
</body>
</html>