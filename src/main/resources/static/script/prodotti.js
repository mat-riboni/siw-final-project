let cart = [];

function addToCart(pId, pNome, pTaglia, pQuantita, pPrezzo) {
    const sizeSelect = document.getElementById(`size-select-${pId}`);
    const selectedOption = sizeSelect.querySelector(`option[value="${pTaglia}"]`);
    const availableQuantity = parseInt(selectedOption.getAttribute('data-quantity'));

    if (availableQuantity >= requestedQuantity) {
        const product = {
            id: pId,
            nome: pNome,
            taglia: pTaglia,
            quantita: pQuantita,
            prezzo: pPrezzo
        };
        cart.push(product);
        updateCartUI();
    } else {
        alert(`Quantità non disponibile! Solo ${pQuantita} pezzi disponibili per la taglia ${pTaglia}.`);
    }
}

function updateCartUI() {
    const cartItemsList = document.getElementById('cart-items');
    cartItemsList.innerHTML = '';

    cart.forEach(product => {
        const listItem = document.createElement('li');
        listItem.className = 'list-group-item';
        listItem.innerHTML = `<h5>${product.nome}</h5>
                              <p>Taglia: ${product.taglia}</p>
                              <p>Quantità: ${product.quantita}</p>
                              <p>Prezzo: ${product.prezzo}</p>`;
        cartItemsList.appendChild(listItem);
    });
}

function checkout() {
    fetch('/negozio/checkout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cart)
    })
    .then(response => response.json())
    .then(data => {
        alert('Prenotazione effettuata con successo!');
        cart = [];
        updateCartUI();
    })
    .catch(error => console.error('Errore durante la prenotazione:', error));
}
