async function fetchMese2prenotazione() {
	
	const shopElement = document.getElementById("negozioData");
    const negozioId = shopElement.dataset.negozioId;
    const response = await fetch('/negozio/' + negozioId + '/prenotazioni');
    const mese2prenotazione = await response.json();
    return mese2prenotazione;
}

async function createChart() {
    const mese2prenotazione = await fetchMese2prenotazione();

    // Prepara i dati per Chart.js
    const labels = Object.keys(mese2prenotazione);
    
    const data = labels.map(mese => mese2prenotazione[mese].length);
    
 

    const ctx = document.getElementById('salesChart').getContext('2d');
    const chart = new Chart(ctx, {
        type: 'bar', // Tipo di grafico
        data: {
            labels: labels,
            datasets: [{
                label: 'Numero di Prenotazioni',
                data: data,
                backgroundColor: 'rgba(255, 94, 45, 0.2)',
                borderColor: 'rgba(255, 94, 45, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

// Crea il grafico quando la pagina è pronta
window.onload = createChart();