window.addEventListener('DOMContentLoaded', function() {
    selezionaCategoria('CPU');
});

function selezionaCategoria(cat) {
    document.querySelectorAll('.cat-tab').forEach(function(t) {
        t.classList.toggle('active', t.dataset.cat === cat);
    });
    document.querySelectorAll('.cat-section').forEach(function(s) {
        s.style.display = 'none';
    });
    var sezione = document.getElementById('section-' + cat);
    if (sezione) sezione.style.display = 'block';
    document.getElementById('inputCategoria').value = cat;
}

function apriSlot(slot) {
    if (slot.classList.contains('filled')) return;
    slot.querySelector('input[type="file"]').click();
}

function gestisciSlot(input) {
    if (!input.files || input.files.length === 0) return;
    var slot   = input.closest('.image-slot');
    var img    = slot.querySelector('img');
    var span   = slot.querySelector('span');
    var reader = new FileReader();
    reader.onload = function(e) {
        img.src = e.target.result;
        img.style.display = 'block';
        span.style.display = 'none';
        slot.classList.add('filled');
        if (!slot.querySelector('.rm-btn')) {
            var btn = document.createElement('button');
            btn.type = 'button';
            btn.className = 'rm-btn';
            btn.innerHTML = '&#x2715;';
            btn.style.cssText = 'position:absolute;top:4px;right:4px;background:#ff7300;border:none;' +
                                'color:#000;border-radius:50%;width:20px;height:20px;cursor:pointer;' +
                                'font-size:11px;line-height:20px;padding:0;z-index:10;';
            btn.onclick = function(e) { e.stopPropagation(); svuotaSlot(slot); };
            slot.appendChild(btn);
        }
    };
    reader.readAsDataURL(input.files[0]);
}

function svuotaSlot(slot) {
    slot.querySelector('input[type="file"]').value = '';
    var img  = slot.querySelector('img');
    var span = slot.querySelector('span');
    var btn  = slot.querySelector('.rm-btn');
    img.src = ''; img.style.display = 'none';
    span.style.display = 'block';
    slot.classList.remove('filled');
    if (btn) btn.remove();
}

function disabilitaSezionInattive() {
    document.querySelectorAll('.cat-section').forEach(function(sezione) {
        var attiva = sezione.style.display !== 'none';
        sezione.querySelectorAll('input, select, textarea').forEach(function(campo) {
            campo.disabled = !attiva;
        });
    });
}

document.getElementById('formProdotto').addEventListener('submit', function(e) {
    if (!document.getElementById('inputCategoria').value) {
        e.preventDefault();
        alert('Seleziona una categoria prima di salvare.');
        return;
    }
    disabilitaSezionInattive();
});