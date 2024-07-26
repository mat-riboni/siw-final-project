async function fetchSalesData() {
    const response = await fetch('/api/sales');
    const data = await response.json();
    return data;
}

// Funzione per creare il grafico
async function createChart() {
    //const salesData = await fetchSalesData();

    // Prepara i dati per Chart.js
    const labels = ['genn','feb','mar','apr','mag','giugn', 'lugl'] ;//salesData.map(sale => sale.product);
    const data = [1,2,3,4,5,6,7] //salesData.map(sale => sale.amount);

    const ctx = document.getElementById('salesChart').getContext('2d');
    const chart = new Chart(ctx, {
        type: 'bar', // Tipo di grafico
        data: {
            labels: labels,
            datasets: [{
                label: 'Vendite',
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