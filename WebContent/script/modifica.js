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
