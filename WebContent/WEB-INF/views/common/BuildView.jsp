<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Overclocked</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/tema.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/pcBuilder.css">
    
    
</head>

<!-- LOGO SCHEDA -->
<jsp:include page="/WEB-INF/views/components/icon.jsp" />

<body>

<jsp:include page="/WEB-INF/views/components/navbar.jsp" />

<div class="builder-wrapper">

    <div class="builder-header">
        <h1>&#x1F5A5; PC Builder</h1>
        <p>Seleziona i componenti in ordine: ogni scelta filtra automaticamente i componenti compatibili.</p>
    </div>

    <div class="builder-layout">

        <!-- selezione componenti -->
        <div class="builder-col-left">

            <!-- CPU -->
            <div class="builder-card">
                <div class="step-label">
                    <div class="step-num" id="num-cpu">1</div>
                    <div class="step-name" id="lbl-cpu">CPU</div>
                </div>
                <select id="selCpu" onchange="onCpuChange()">
                    <option value="">-- Caricamento... --</option>
                </select>
                <div id="errCpu" class="builder-error"></div>
            </div>

            <!-- MOBO -->
            <div class="builder-card">
                <div class="step-label">
                    <div class="step-num disabled" id="num-mobo">2</div>
                    <div class="step-name disabled" id="lbl-mobo">Scheda Madre</div>
                </div>
                <select id="selMobo" onchange="onMoboChange()" disabled>
                    <option value="">-- Prima seleziona una CPU --</option>
                </select>
                <div id="errMobo" class="builder-error"></div>
            </div>

            <!-- RAM -->
            <div class="builder-card">
                <div class="step-label">
                    <div class="step-num disabled" id="num-ram">3</div>
                    <div class="step-name disabled" id="lbl-ram">RAM</div>
                </div>
                <select id="selRam" onchange="aggiornaRiepilogo()" disabled>
                    <option value="">-- Prima seleziona una scheda madre --</option>
                </select>
                <div id="errRam" class="builder-error"></div>
            </div>

            <!-- GPU -->
            <div class="builder-card">
                <div class="step-label">
                    <div class="step-num disabled" id="num-gpu">4</div>
                    <div class="step-name disabled" id="lbl-gpu">GPU</div>
                </div>
                <select id="selGpu" onchange="onGpuChange()" disabled>
                    <option value="">-- Prima seleziona una CPU --</option>
                </select>
                <div id="errGpu" class="builder-error"></div>
            </div>

            <!-- STORAGE -->
            <div class="builder-card">
                <div class="step-label">
                    <div class="step-num disabled" id="num-storage">5</div>
                    <div class="step-name disabled" id="lbl-storage">Storage</div>
                </div>
                <select id="selStorage" onchange="aggiornaRiepilogo()" disabled>
                    <option value="">-- Prima seleziona una scheda madre --</option>
                </select>
                <div id="errStorage" class="builder-error"></div>
            </div>

            <!-- PSU -->
            <div class="builder-card">
                <div class="step-label">
                    <div class="step-num disabled" id="num-psu">6</div>
                    <div class="step-name disabled" id="lbl-psu">Alimentatore (PSU)</div>
                </div>
                <select id="selPsu" onchange="aggiornaRiepilogo()" disabled>
                    <option value="">-- Prima seleziona CPU e GPU --</option>
                </select>
                <div id="errPsu" class="builder-error"></div>
            </div>

            <!-- CASE -->
            <div class="builder-card">
                <div class="step-label">
                    <div class="step-num disabled" id="num-case">7</div>
                    <div class="step-name disabled" id="lbl-case">Case</div>
                </div>
                <select id="selCase" onchange="aggiornaRiepilogo()" disabled>
                    <option value="">-- Prima seleziona una scheda madre --</option>
                </select>
                <div id="errCase" class="builder-error"></div>
            </div>

            <!-- DISSIPATORE -->
            <div class="builder-card">
                <div class="step-label">
                    <div class="step-num disabled" id="num-diss">8</div>
                    <div class="step-name disabled" id="lbl-diss">Dissipatore</div>
                </div>
                <select id="selDissipatore" onchange="aggiornaRiepilogo()" disabled>
                    <option value="">-- Prima seleziona una CPU --</option>
                </select>
                <div id="errDissipatore" class="builder-error"></div>
            </div>

        </div>

        <!-- RIEPILOGO -->
        <div class="builder-col-right">

            <div id="riepilogo">
                <div class="builder-card-title">&#x1F4CB; Riepilogo Build</div>
                <ul id="listaRiepilogo">
                    <li class="builder-empty">Nessun componente selezionato.</li>
                </ul>
                <div id="prezzoTotale"></div>

                <div id="wrapperCarrello">
                    <button class="btn-aggiungi-carrello" onclick="aggiungiTuttiAlCarrello()">
                        &#x1F6D2; Aggiungi tutto al carrello
                    </button>
                    <div id="msgCarrello" class="builder-cart-msg"></div>
                </div>
            </div>

        </div>

    </div>

</div>



<jsp:include page="/WEB-INF/views/components/footer.jsp" />


<script src="${pageContext.request.contextPath}/scripts/build.js"></script>
</body>

</html>
