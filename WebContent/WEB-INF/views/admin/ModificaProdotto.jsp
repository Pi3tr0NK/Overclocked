<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Modifica Prodotto</title>


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
        Modifica Prodotto
        <span class="cat-badge">${prodotto.categoria}</span>
    </h1>

    <c:if test="${not empty errore}">
        <div class="alert alert-error">${errore}</div>
    </c:if>
    <c:if test="${not empty param.success}">
        <div class="alert alert-success">Prodotto modificato con successo!</div>
    </c:if>

    <form
        action="${pageContext.request.contextPath}/admin/aggiungiProdotto?action=modifica"
        method="post"
        enctype="multipart/form-data">

        <input type="hidden" name="idProdotto" value="${prodotto.idProdotto}"/>
        <input type="hidden" name="categoria"  value="${prodotto.categoria}"/>

        <%-- ===== DATI GENERALI ===== --%>
        <div class="section">
            <h2>Dati Generali</h2>

            <div class="row2">
                <div class="form-group">
                    <label>Nome</label>
                    <input type="text" name="nome" value="${prodotto.nome}"/>
                </div>
                <div class="form-group">
                    <label>Modello</label>
                    <input type="text" name="modello" value="${prodotto.modello}"/>
                </div>
            </div>

            <div class="row2">
                <div class="form-group">
                    <label>Marca</label>
                    <input type="text" name="marca" value="${prodotto.marca}"/>
                </div>
                <div class="form-group">
                    <label>Attivo</label>
                    <select name="attivo">
                        <option value="true"  ${prodotto.attivo ? 'selected' : ''}>S&igrave;</option>
                        <option value="false" ${prodotto.attivo ? '' : 'selected'}>No</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label>Descrizione</label>
                <textarea name="descrizione">${prodotto.descrizione}</textarea>
            </div>

            <div class="row3">
                <div class="form-group">
                    <label>Prezzo (&euro;)</label>
                    <input type="number" step="0.01" name="prezzo" value="${prodotto.prezzo}"/>
                </div>
                <div class="form-group">
                    <label>Sconto (%)</label>
                    <input type="number" name="sconto" value="${prodotto.sconto}"/>
                </div>
                <div class="form-group">
                    <label>Stock</label>
                    <input type="number" name="stock" value="${prodotto.stock}"/>
                </div>
            </div>

            <div class="row2">
                <div class="form-group">
                    <label>Dimensioni</label>
                    <input type="text" name="dimensioni" value="${prodotto.dimensioni}"/>
                </div>
                <div class="form-group">
                    <label>Peso</label>
                    <input type="text" name="peso" value="${prodotto.peso}"/>
                </div>
            </div>
        </div>

        <%-- ===== CPU ===== --%>
        <c:if test="${prodotto.categoria == 'CPU'}">
        <div class="section">
            <h2>Specifiche CPU</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Core</label>
                    <input type="number" name="core" value="${prodotto.core}"/>
                    <input type="hidden" name="idCpu" value="${prodotto.idCpu}"/>
                </div>
                <div class="form-group">
                    <label>Thread</label>
                    <input type="number" name="thread" value="${prodotto.thread}"/>
                </div>
                <div class="form-group">
                    <label>TDP (W)</label>
                    <input type="number" name="tdp" value="${prodotto.tdp}"/>
                </div>
            </div>
            <div class="row3">
                <div class="form-group">
                    <label>Frequenza</label>
                    <input type="text" name="frequenza" value="${prodotto.frequenza}"/>
                </div>
                <div class="form-group">
                    <label>Socket</label>
                    <input type="text" name="socket" value="${prodotto.socket}"/>
                </div>
                <div class="form-group">
                    <label>Tipo RAM</label>
                    <input type="text" name="tiporam" value="${prodotto.tiporam}"/>
                </div>
            </div>
            <div class="form-group">
                <label>Frequenza RAM</label>
                <input type="text" name="frequenzaram" value="${prodotto.frequenza_ram}"/>
            </div>
        </div>
        </c:if>

        <%-- ===== GPU ===== --%>
        <c:if test="${prodotto.categoria == 'GPU'}">
        <div class="section">
            <h2>Specifiche GPU</h2>
            <div class="row3">
                <div class="form-group">
                    <label>VRAM</label>
                    <input type="text" name="vram" value="${prodotto.vram}"/>
                    <input type="hidden" name="idGpu" value="${prodotto.idGpu}"/>
                </div>
                <div class="form-group">
                    <label>Tipo VRAM</label>
                    <input type="text" name="tipovram" value="${prodotto.tipoVram}"/>
                </div>
                <div class="form-group">
                    <label>TDP (W)</label>
                    <input type="number" name="tdp" value="${prodotto.tdp}"/>
                </div>
            </div>
            <div class="row3">
                <div class="form-group">
                    <label>Frequenza boost</label>
                    <input type="text" name="frequenza" value="${prodotto.frequenza}"/>
                </div>
                <div class="form-group">
                    <label>PCIe</label>
                    <input type="text" name="pcie" value="${prodotto.pcie}"/>
                </div>
                <div class="form-group">
                    <label>Uscite video</label>
                    <input type="text" name="video" value="${prodotto.video}"/>
                </div>
            </div>
            <div class="form-group">
                <label>Risoluzione massima</label>
                <input type="text" name="maxres" value="${prodotto.maxRes}"/>
            </div>
        </div>
        </c:if>

        <%-- ===== RAM ===== --%>
        <c:if test="${prodotto.categoria == 'RAM'}">
        <div class="section">
            <h2>Specifiche RAM</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Capacit&agrave;</label>
                    <input type="text" name="capacita" value="${prodotto.capacita}"/>
                    <input type="hidden" name="idRam" value="${prodotto.idRam}"/>
                </div>
                <div class="form-group">
                    <label>Frequenza</label>
                    <input type="text" name="frequenza" value="${prodotto.frequenza}"/>
                </div>
                <div class="form-group">
                    <label>Tipo</label>
                    <input type="text" name="tipo" value="${prodotto.tipo}"/>
                </div>
            </div>
        </div>
        </c:if>

        <%-- ===== STORAGE ===== --%>
        <c:if test="${prodotto.categoria == 'STORAGE'}">
        <div class="section">
            <h2>Specifiche Storage</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Capacit&agrave;</label>
                    <input type="text" name="capacita" value="${prodotto.capacita}"/>
                    <input type="hidden" name="idMemoria" value="${prodotto.idMemoria}"/>
                </div>
                <div class="form-group">
                    <label>Formato</label>
                    <input type="text" name="formato" value="${prodotto.formato}"/>
                </div>
                <div class="form-group">
                    <label>Vel. lettura (MB/s)</label>
                    <input type="number" name="lettura" value="${prodotto.velLettura}"/>
                </div>
            </div>
            <div class="row3">
                <div class="form-group">
                    <label>Vel. scrittura (MB/s)</label>
                    <input type="number" name="scrittura" value="${prodotto.velScrittura}"/>
                </div>
                <div class="form-group">
                    <label>Tipo</label>
                    <select name="tipo">
                        <option value="SSD" ${prodotto.tipo == 'SSD' ? 'selected' : ''}>SSD</option>
                        <option value="HDD" ${prodotto.tipo == 'HDD' ? 'selected' : ''}>HDD</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Tecnologia</label>
                    <select name="tecnologia">
                        <option value="NVME" ${prodotto.tecnologia == 'NVME' ? 'selected' : ''}>NVMe</option>
                        <option value="SATA" ${prodotto.tecnologia == 'SATA' ? 'selected' : ''}>SATA</option>
                    </select>
                </div>
            </div>
        </div>
        </c:if>

        <%-- ===== MOBO ===== --%>
        <c:if test="${prodotto.categoria == 'MOBO'}">
        <div class="section">
            <h2>Specifiche Scheda Madre</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Chipset</label>
                    <input type="text" name="chipset" value="${prodotto.chipset}"/>
                    <input type="hidden" name="idMobo" value="${prodotto.idMobo}"/>
                </div>
                <div class="form-group">
                    <label>Socket</label>
                    <input type="text" name="socket" value="${prodotto.socket}"/>
                </div>
                <div class="form-group">
                    <label>Formato</label>
                    <input type="text" name="formato" value="${prodotto.formato}"/>
                </div>
            </div>
            <div class="row3">
                <div class="form-group">
                    <label>Tipo RAM</label>
                    <input type="text" name="tipoRam" value="${prodotto.tipoRam}"/>
                </div>
                <div class="form-group">
                    <label>Freq. max RAM</label>
                    <input type="text" name="maxFreq" value="${prodotto.maxFreq}"/>
                </div>
                <div class="form-group">
                    <label>PCIe</label>
                    <input type="text" name="pcie" value="${prodotto.pcie}"/>
                </div>
            </div>
            <div class="row3">
                <div class="form-group">
                    <label>Slot RAM</label>
                    <input type="number" name="slotRam" value="${prodotto.slotRam}"/>
                </div>
                <div class="form-group">
                    <label>Porte SATA</label>
                    <input type="number" name="porteSata" value="${prodotto.porteSata}"/>
                </div>
                <div class="form-group">
                    <label>Porte USB</label>
                    <input type="number" name="porteUsb" value="${prodotto.porteUsb}"/>
                </div>
            </div>
            <div class="form-group">
                <label>NVMe</label>
                <select name="nvme">
                    <option value="true"  ${prodotto.nvme ? 'selected' : ''}>S&igrave;</option>
                    <option value="false" ${prodotto.nvme ? '' : 'selected'}>No</option>
                </select>
            </div>
        </div>
        </c:if>

        <%-- ===== PSU ===== --%>
        <c:if test="${prodotto.categoria == 'PSU'}">
        <div class="section">
            <h2>Specifiche Alimentatore</h2>
            <div class="row2">
                <div class="form-group">
                    <label>Potenza (W)</label>
                    <input type="number" name="potenza" value="${prodotto.potenza}"/>
                    <input type="hidden" name="idPsu" value="${prodotto.idPsu}"/>
                </div>
                <div class="form-group">
                    <label>Certificazione</label>
                    <input type="text" name="certificazione" value="${prodotto.certificazione}"/>
                </div>
            </div>
            <div class="row2">
                <div class="form-group">
                    <label>Modulare</label>
                    <select name="modulare">
                        <option value="MODULARE"   ${prodotto.modulare == 'MODULARE'   ? 'selected' : ''}>Modulare</option>
                        <option value="SEMIMODULARE"    ${prodotto.modulare == 'SEMIMODULARE'    ? 'selected' : ''}>Semi-modulare</option>
                        <option value="NON_MODULARE" ${prodotto.modulare == 'NON_MODULARE' ? 'selected' : ''}>Non modulare</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Formato</label>
                    <select name="formato">
                        <option value="ATX" ${prodotto.formato == 'ATX' ? 'selected' : ''}>ATX</option>
                        <option value="SFX" ${prodotto.formato == 'SFX' ? 'selected' : ''}>SFX</option>
                    </select>
                </div>
            </div>
        </div>
        </c:if>

        <%-- ===== CASE ===== --%>
        <c:if test="${prodotto.categoria == 'CASE'}">
        <div class="section">
            <h2>Specifiche Case</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Formato</label>
                    <input type="text" name="formato" value="${prodotto.formato}"/>
                    <input type="hidden" name="idCase" value="${prodotto.idCASE}"/>
                </div>
                <div class="form-group">
                    <label>Colore</label>
                    <input type="text" name="colore" value="${prodotto.colore}"/>
                </div>
                <div class="form-group">
                    <label>Materiale</label>
                    <input type="text" name="materiale" value="${prodotto.materiale}"/>
                </div>
            </div>
        </div>
        </c:if>

        <%-- ===== DISSIPATORE ===== --%>
        <c:if test="${prodotto.categoria == 'DISSIPATORE'}">
        <div class="section">
            <h2>Specifiche Dissipatore</h2>
            <div class="row3">
                <div class="form-group">
                    <label>Tipo</label>
                    <select name="tipo">
                        <option value="ARIA"    ${prodotto.tipo == 'ARIA'    ? 'selected' : ''}>Aria</option>
                        <option value="LIQUIDO" ${prodotto.tipo == 'LIQUIDO' ? 'selected' : ''}>Liquido</option>
                        
                    </select>
                </div>
                <div class="form-group">
                    <label>TDP supportato (W)</label>
                    <input type="number" name="tdp" value="${prodotto.tdpSupportato}"/>
                    <input type="hidden" name="idDissipatore" value="${prodotto.idDissipatore}"/>
                </div>
                <div class="form-group">
                    <label>Socket supportati</label>
                    <input type="text" name="socket" value="${prodotto.socketSupportati}"/>
                </div>
            </div>
            <div class="row2">
                <div class="form-group">
                    <label>RPM max</label>
                    <input type="number" name="rpm" value="${prodotto.rpmMax}"/>
                </div>
                <div class="form-group">
                    <label>Rumore (dBA)</label>
                    <input type="number" name="rumore" value="${prodotto.rumore}"/>
                </div>
            </div>
        </div>
        </c:if>

        <%-- ===== IMMAGINI ===== --%>
        <div class="section">
            <h2>Immagini</h2>
            <p style="font-size:12px;color:#666;margin-bottom:10px">
                Carica una nuova immagine per sostituire quella esistente. Lascia vuoto per mantenerla.
            </p>

            <div class="image-container">

                <c:forEach var="img" items="${prodotto.immagini}" varStatus="s">
                
                <div class="image-slot" onclick="apriSlot(this)">
                
                    <input type="file"   name="immagine${s.count}" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <input type="hidden" name="pathEsistente${s.count}" value="${img.path}"/>
                    <input type="hidden" name="idImmagine${s.count}" value="${img.idImmagine}"/>
                    <span style="display:none">&#43;</span>
                    <img src="${pageContext.request.contextPath}/${img.path}" alt="" style="display:block"/>
                </div>
                </c:forEach>

                <%-- slot vuoti per nuove immagini aggiuntive --%>
                <c:forEach begin="1" end="${5 - prodotto.immagini.size()}" varStatus="s">
                <div class="image-slot" onclick="apriSlot(this)">
                    <input type="file" name="immagine${prodotto.immagini.size() + s.count}" accept="image/*" hidden onchange="gestisciSlot(this)"/>
                    <span>&#43;</span>
                    <img alt=""/>
                </div>
                </c:forEach>

            </div>
        </div>

        <%-- ===== BOTTONI ===== --%>
        <div class="btn-row">
            <button type="submit">Salva modifiche</button>
            <a class="btn-cancel" href="${pageContext.request.contextPath}/admin/dashboard">Annulla</a>
        </div>

    </form>
</div>


<script>

function apriSlot(slot) {
    slot.querySelector('input[type="file"]').click();
}

function gestisciSlot(input) {
    if (!input.files || input.files.length === 0) return;
    var slot = input.closest('.image-slot');
    var img  = slot.querySelector('img');
    var span = slot.querySelector('span');
    var reader = new FileReader();
    reader.onload = function(e) {
        img.src = e.target.result;
        img.style.display = 'block';
        span.style.display = 'none';
    };
    reader.readAsDataURL(input.files[0]);
}

</script>

</body>
</html>