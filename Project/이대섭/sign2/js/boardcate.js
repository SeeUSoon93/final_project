const itemTab = document.querySelector('.item_tab');
const items = document.querySelector('.items');
const item = document.querySelectorAll('.item');

itemTab.addEventListener('click', (e) => {
    const filter = e.target.dataset.filter || e.target.parentNode.dataset.filter;
    if (filter == null) {
        return;
    }
    item.forEach((item) => {
        if (filter === '*' || filter === item.dataset.type) {
            item.style.display = "block"
        } else {
            item.style.display = "none"
        }
    });
});