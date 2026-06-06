<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aggiungi Prodotto</title>

<style>

body{
    background:#0b0b0b;
    color:white;
    font-family:Arial;
    margin:0;
}

.container{
    width:1000px;
    margin:auto;
    padding:30px;
}

h1{
    color:#ff7300;
}

.form-group{
    margin-bottom:15px;
}

label{
    display:block;
    margin-bottom:5px;
}

input,
textarea,
select{
    width:100%;
    padding:10px;
    border:none;
    border-radius:6px;
    box-sizing:border-box;
}

textarea{
    min-height:120px;
}

.section{
    background:#111;
    padding:20px;
    border-radius:10px;
    margin-top:20px;
}

button{
    background:#ff7300;
    color:black;
    border:none;
    padding:12px 25px;
    font-weight:bold;
    border-radius:8px;
    cursor:pointer;
    margin-top:20px;
}

button:hover{
    opacity:.9;
}

.hidden{
    display:none;
}

.error{
    background:#c62828;
    padding:15px;
    border-radius:8px;
    margin-bottom:20px;
}

.success{
    background:#2e7d32;
    padding:15px;
    border-radius:8px;
    margin-bottom:20px;
}

.image-container{
    display:flex;
    gap:15px;
    flex-wrap:wrap;
    margin-top:10px;
}

.image-slot{
    width:150px;
    height:150px;

    border:2px dashed #666;
    border-radius:10px;

    background:#1a1a1a;

    cursor:pointer;
    overflow:hidden;
    position:relative;

    display:flex;
    justify-content:center;
    align-items:center;
}

.image-slot:hover{
    border-color:#ff7300;
}

.image-slot span{
    font-size:50px;
    color:#999;
    user-select:none;
}

.image-slot img{
    width:100%;
    height:100%;
    object-fit:cover;
    display:none;
    position:absolute;
    top:0;
    left:0;
}

</style>
</head>

<body>

<div class="container">

<h1>Aggiungi Prodotto</h1>

<% if(request.getAttribute("errore") != null){ %>
<div class="error">
    <%= request.getAttribute("errore") %>
</div>
<% } %>

<form action="${pageContext.request.contextPath}/admin/aggiungiProdotto"
      method="post" enctype="multipart/form-data">

    <!-- DATI COMUNI -->

    <div class="section">

        <h2>Dati Generali</h2>

        <div class="form-group">
            <label>Categoria</label>
            <select name="categoria" id="categoria">
                <option value="">Seleziona</option>
                <option value="CPU">CPU</option>
                <option value="GPU">GPU</option>
                <option value="RAM">RAM</option>
                <option value="STORAGE">Storage</option>
                <option value="PSU">PSU</option>
                <option value="CASE">Case</option>
                <option value="MOBO">Scheda Madre</option>
                <option value="DISSIPATORE">Dissipatore</option>
            </select>
        </div>

        <div class="form-group">
            <label>Nome</label>
            <input type="text" name="nome">
        </div>

        <div class="form-group">
            <label>Marca</label>
            <input type="text" name="marca">
        </div>

        <div class="form-group">
            <label>Modello</label>
            <input type="text" name="modello">
        </div>

        <div class="form-group">
            <label>Descrizione</label>
            <textarea name="descrizione"></textarea>
        </div>

        <div class="form-group">
            <label>Prezzo</label>
            <input type="number" step="0.01" name="prezzo">
        </div>

        <div class="form-group">
            <label>Stock</label>
            <input type="number" name="stock">
        </div>

        <div class="form-group">
            <label>Sconto (%)</label>
            <input type="number" name="sconto" value="0">
        </div>

        <div class="form-group">
            <label>Dimensioni</label>
            <input type="text" name="dimensioni">
        </div>

        <div class="form-group">
            <label>Peso</label>
            <input type="text" name="peso">
        </div>

        <div class="form-group">
            <label>Attivo</label>
            <input type="checkbox" name="attivo" value="true">
        </div>

		<div class="form-group">
    <label>Immagini (max 5)</label>

    <div class="image-container">

        <div class="image-slot">
            <input type="file" name="immagine1" accept="image/*" hidden>
            <span>+</span>
            <img alt="">
        </div>

        <div class="image-slot">
            <input type="file" name="immagine2" accept="image/*" hidden>
            <span>+</span>
            <img alt="">
        </div>

        <div class="image-slot">
            <input type="file" name="immagine3" accept="image/*" hidden>
            <span>+</span>
            <img alt="">
        </div>

        <div class="image-slot">
            <input type="file" name="immagine4" accept="image/*" hidden>
            <span>+</span>
            <img alt="">
        </div>

        <div class="image-slot">
            <input type="file" name="immagine5" accept="image/*" hidden>
            <span>+</span>
            <img alt="">
        </div>

    </div>
</div>
	

    </div>

    <!-- CPU -->

    <div id="CPU" class="section hidden">

        <h2>Specifiche CPU</h2>

        <input type="number" name="core" placeholder="Core">
        <input type="number" name="thread" placeholder="Thread">
        <input type="text" name="frequenza" placeholder="Frequenza">
        <input type="text" name="frequenzaram" placeholder="Frequenza RAM">
        <input type="text" name="tiporam" placeholder="Tipo RAM">
        <input type="text" name="socket" placeholder="Socket">
        <input type="number" name="tdp" placeholder="TDP">

    </div>

    <!-- GPU -->

    <div id="GPU" class="section hidden">

        <h2>Specifiche GPU</h2>

        <input type="text" name="vram" placeholder="VRAM">
        <input type="text" name="tipoVram" placeholder="Tipo VRAM">
        <input type="text" name="frequenza" placeholder="Frequenza">
        <input type="text" name="pcie" placeholder="PCI-E">
        <input type="text" name="video" placeholder="Uscite Video">
        <input type="number" name="tdp" placeholder="TDP">

    </div>

    <!-- RAM -->

    <div id="RAM" class="section hidden">

        <h2>Specifiche RAM</h2>

        <input type="text" name="capacita" placeholder="Capacità">
        <input type="text" name="frequenza" placeholder="Frequenza">
        <input type="text" name="tipo" placeholder="Tipo">

    </div>

    <!-- STORAGE -->

    <div id="STORAGE" class="section hidden">

        <h2>Specifiche Storage</h2>

        <input type="text" name="capacita" placeholder="Capacità">
        <input type="number" name="lettura" placeholder="Velocità Lettura">
        <input type="number" name="scrittura" placeholder="Velocità Scrittura">

        <select name="tecnologia">
            <option value="SATA">SATA</option>
            <option value="NVME">NVME</option>
        </select>

        <select name="tipo">
            <option value="SSD">SSD</option>
            <option value="HDD">HDD</option>
        </select>

        <input type="text" name="formato" placeholder="Formato">

    </div>

    <!-- PSU -->

    <div id="PSU" class="section hidden">

        <h2>Specifiche PSU</h2>

        <input type="number" name="potenza" placeholder="Potenza">

        <input type="text"
               name="certificazione"
               placeholder="80+ Gold">

        <select name="modulare">
            <option value="MODULARE">MODULARE</option>
            <option value="SEMIMODULARE">SEMIMODULARE</option>
            <option value="NON_MODULARE">NON_MODULARE</option>
        </select>

        <select name="formato">
            <option value="ATX">ATX</option>
            <option value="SFX">SFX</option>
        </select>

    </div>

    <!-- CASE -->

    <div id="CASE" class="section hidden">

        <h2>Specifiche Case</h2>

        <input type="text" name="formato" placeholder="Formato">
        <input type="text" name="colore" placeholder="Colore">
        <input type="text" name="materiale" placeholder="Materiale">

    </div>

    <!-- MOBO -->

    <div id="MOBO" class="section hidden">

        <h2>Specifiche Scheda Madre</h2>

        <input type="text" name="chipset" placeholder="Chipset">
        <input type="text" name="socket" placeholder="Socket">
        <input type="text" name="tipoRam" placeholder="Tipo RAM">
        <input type="text" name="maxFreq" placeholder="Max Frequenza">
        <input type="text" name="formato" placeholder="Formato">
        <input type="text" name="pcie" placeholder="PCI-E">

        <input type="number" name="slotRam" placeholder="Slot RAM">
        <input type="number" name="porteSata" placeholder="Porte SATA">
        <input type="number" name="porteUsb" placeholder="Porte USB">

        <label>
            NVMe
            <input type="checkbox" name="nvme" value="true">
        </label>

    </div>

    <!-- DISSIPATORE -->

    <div id="DISSIPATORE" class="section hidden">

        <h2>Specifiche Dissipatore</h2>

        <select name="tipo">
            <option value="ARIA">ARIA</option>
            <option value="LIQUIDO">LIQUIDO</option>
        </select>

        <input type="text" name="socket" placeholder="Socket Supportati">
        <input type="number" name="rpm" placeholder="RPM Max">
        <input type="number" name="rumore" placeholder="Rumore">
        <input type="number" name="tdp" placeholder="TDP Supportato">

    </div>

    <button type="submit">
        Salva prodotto
    </button>

</form>



</div>

<script>


document.querySelectorAll(".image-slot").forEach(slot => {

    const input = slot.querySelector("input");
    const img = slot.querySelector("img");
    const plus = slot.querySelector("span");

    slot.addEventListener("click", () => {
        input.click();
    });

    input.addEventListener("change", () => {

        const file = input.files[0];

        if(file) {

            const reader = new FileReader();

            reader.onload = e => {
                img.src = e.target.result;
                img.style.display = "block";
                plus.style.display = "none";
            };

            reader.readAsDataURL(file);
        }
    });

});


const categoria = document.getElementById("categoria");

/* all'avvio disabilita tutte le sezioni specifiche */
document.querySelectorAll(".section").forEach(div => {

    if(div.id){

        div.classList.add("hidden");

        div.querySelectorAll("input, select, textarea")
           .forEach(el => el.disabled = true);
    }

});

categoria.addEventListener("change", function(){

    document.querySelectorAll(".section").forEach(div => {

        if(div.id){

            div.classList.add("hidden");

            div.querySelectorAll("input, select, textarea")
               .forEach(el => el.disabled = true);
        }

    });

    const sezione = document.getElementById(this.value);

    if(sezione){

        sezione.classList.remove("hidden");

        sezione.querySelectorAll("input, select, textarea")
               .forEach(el => el.disabled = false);
    }
});

</script>

</body>
</html>