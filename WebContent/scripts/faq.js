function toggleFaq(btn) {
    var item   = btn.closest('.faq-item');
    var answer = item.querySelector('.faq-answer');
    var isOpen = item.classList.contains('open');

    if (isOpen) {
        item.classList.remove('open');
        answer.style.maxHeight = null;
    } else {
        item.classList.add('open');
        answer.style.maxHeight = answer.scrollHeight + 'px';
    }
}

// ── Apertura automatica della domanda indicata nell'URL (#faq-...) ─────
window.addEventListener('DOMContentLoaded', function () {

    var hash = window.location.hash; // es. "#faq-garanzia-1"

    if (hash) {
        var target = document.querySelector(hash);

        if (target && target.classList.contains('faq-item')) {

            // apre la domanda
            var btn = target.querySelector('.faq-question');
            toggleFaq(btn);

            // evidenzia temporaneamente
            target.classList.add('highlight');
            setTimeout(function () {
                target.classList.remove('highlight');
            }, 2500);

            // scrolla fino alla domanda
            target.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    }
});