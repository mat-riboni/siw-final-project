let cart = [];

   function handleButtonClick(button) {
   		const id = button.dataset.id;
        const nome = button.dataset.nome;
        const prezzo = button.dataset.prezzo;
        
        const sizeSelectId = button.dataset.sizeId;
        const quantityInputId = button.dataset.quantityId;

        const sizeSelect = document.getElementById(sizeSelectId);
        const quantityInput = document.getElementById(quantityInputId);

        if (sizeSelect && quantityInput) {
            const size = sizeSelect.value;
            const quantity = parseInt(quantityInput.value, 10);
            addToCart(id, nome, size, quantity, prezzo);
        } else {
        	console.error('Elementi non trovati:', sizeSelectId, quantityInputId);
		}
    }

function addToCart(pId, pNome, pTaglia, pQuantita, pPrezzo) {
    const sizeSelect = document.getElementById(`size-select-${pId}`);
    const selectedOption = sizeSelect.querySelector(`option[value="${pTaglia}"]`);
    const availableQuantity = parseInt(selectedOption.getAttribute('data-quantity'));

    if (availableQuantity >= pQuantita) {
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
        alert('Quantità non disponibile!.');
    }
}

function updateCartUI() {
    const cartItemsList = document.getElementById('cart-items');
    cartItemsList.innerHTML = '';
    let sum = 0;
	
    cart.forEach(product => {
        const listItem = document.createElement('li');
        listItem.className = 'list-group-item';
        listItem.innerHTML = `<h5>${product.nome}</h5>
                              <p>Taglia: ${product.taglia}</p>
                              <p>Quantità: ${product.quantita}</p>
                              <p>Prezzo: ${product.prezzo}</p>`;
        cartItemsList.appendChild(listItem);
        const price = parseFloat(product.prezzo);
        const quantity = parseInt(product.quantita, 10);
        if (!isNaN(price) && !isNaN(quantity)) {
            sum += price * quantity;
        }    });
    document.getElementById('totale').innerHTML = `${sum.toFixed(2)}`;
}

function checkout() {
    fetch('/negozio/checkout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cart)
    })
    .then(response => {
		if (!response.ok) {
            throw new Error('Errore durante la prenotazione: quantità non sufficienti per uno dei prodotti');
        }
		return response.text()
		})
    .then(data => {
		appendAlert('Prenotazione effettuata con successo!', 'success')
        cart = [];
        updateCartUI();
    })
    .catch(error => appendAlert('Errore durante la prenotazione: quantità non sufficienti per uno dei prodotti', 'danger'));
}

const alertPlaceholder = document.getElementById('liveAlertPlaceholder')
const appendAlert = (message, type) => {
  const wrapper = document.createElement('div')
  wrapper.innerHTML = [
    `<div class="alert alert-${type} alert-dismissible" role="alert">`,
    `   <div>${message}</div>`,
    '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
    '</div>'
  ].join('')

  alertPlaceholder.append(wrapper)
}
