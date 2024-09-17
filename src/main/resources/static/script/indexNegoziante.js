

async function fetchMese2prenotazione() {
    const shopElement = document.getElementById("negozioData");
    const negozioId = shopElement.dataset.negozioId;
    
    try {
        const response = await fetch('/negozio/' + negozioId + '/prenotazioni');
        
        const responseText = await response.text();
        console.log("Raw response:", responseText);

        const mese2prenotazione = JSON.parse(responseText);
        
        return mese2prenotazione;
    } catch (error) {
        console.error('Errore nel fetchMese2prenotazione:', error);
        throw error;
    }
}

async function createChart() {
    const mese2prenotazioni = await fetchMese2prenotazione();

    const labels = Object.keys(mese2prenotazioni);
    
    const data = Object.values(mese2prenotazioni);
    
 

    const ctx = document.getElementById('salesChart').getContext('2d');
    const chart = new Chart(ctx, {
        type: 'bar', 
        data: {
            labels: labels,
            datasets: [{
                label: 'Numero di Prenotazioni ' + new Date().getFullYear(),
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

 function navigateToPrenotazione(button) {
        var prenotazioneId = button.getAttribute('data-id');
        prenotazioneId
        window.location.href = '/prenotazione/' + prenotazioneId;
    }


window.onload = createChart();