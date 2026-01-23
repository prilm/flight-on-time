document.addEventListener('DOMContentLoaded', () => {
    loadAirlines();
    loadAirports();

    document.getElementById('predictionForm').addEventListener('submit', handlePrediction);
    document.querySelector('.close-button').addEventListener('click', closeModal);

    // Cerrar modal al hacer click fuera
    window.addEventListener('click', (e) => {
        const modal = document.getElementById('resultModal');
        if (e.target === modal) {
            closeModal();
        }
    });
});

async function loadAirlines() {
    try {
        const response = await fetch('airlines.csv');
        const text = await response.text();
        const rows = text.split('\n').slice(1); // Saltar header

        const select = document.getElementById('aerolinea');

        rows.forEach(row => {
            if (!row.trim()) return;
            // Manejar comas dentro de comillas si es necesario, 
            // pero asumiremos formato simple IATA,AIRLINE según el archivo visto
            const [iata, name] = row.split(',');

            if (iata && name) {
                const option = document.createElement('option');
                option.value = iata.trim();
                option.textContent = `${name.trim()} (${iata.trim()})`;
                select.appendChild(option);
            }
        });
    } catch (error) {
        console.error('Error cargando aerolíneas:', error);
    }
}

async function loadAirports() {
    try {
        const response = await fetch('airports.csv');
        const text = await response.text();
        const rows = text.split('\n').slice(1);

        const origenSelect = document.getElementById('origen');
        const destinoSelect = document.getElementById('destino');

        // Ordenar alfabéticamente por ciudad o código
        const airports = rows.map(row => {
            if (!row.trim()) return null;
            const cols = row.split(',');
            // CSV: IATA_CODE,AIRPORT,CITY,...
            return {
                code: cols[0],
                name: cols[1],
                city: cols[2]
            };
        }).filter(a => a !== null).sort((a, b) => a.code.localeCompare(b.code));

        airports.forEach(airport => {
            const label = `${airport.code} - ${airport.city} (${airport.name})`;

            const opt1 = document.createElement('option');
            opt1.value = airport.code;
            opt1.textContent = label;

            const opt2 = opt1.cloneNode(true);

            origenSelect.appendChild(opt1);
            destinoSelect.appendChild(opt2);
        });

    } catch (error) {
        console.error('Error cargando aeropuertos:', error);
    }
}

async function handlePrediction(e) {
    e.preventDefault();

    // Mostrar modal cargando
    const modal = document.getElementById('resultModal');
    const resultDisplay = document.getElementById('resultDisplay');
    const title = document.getElementById('predictionTitle');

    modal.classList.remove('hidden');
    title.textContent = "Consultando al Oráculo...";
    resultDisplay.classList.add('hidden');

    // Recolectar datos
    const formData = new FormData(e.target);
    const data = {
        fecha: formData.get('fecha'),
        hora: formData.get('hora'),
        aerolinea: formData.get('aerolinea'),
        origen: formData.get('origen'),
        destino: formData.get('destino'),
        distancia_km: parseInt(formData.get('distancia_km'))
    };

    try {
        const response = await fetch('/search', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) throw new Error('Error en la predicción');

        const result = await response.json();
        showResult(result);

    } catch (error) {
        console.error(error);
        title.textContent = "Error en la consulta";
        // Podrías mostrar detalles del error aquí
    }
}

function showResult(result) {
    const title = document.getElementById('predictionTitle');
    const resultDisplay = document.getElementById('resultDisplay');
    const predictionValue = document.getElementById('predictionValue');
    const probabilityBar = document.getElementById('probabilityBar');
    const probabilityValue = document.getElementById('probabilityValue');

    title.textContent = "Resultado de la Predicción";
    resultDisplay.classList.remove('hidden');

    // Set text
    predictionValue.textContent = result.prevision;
    probabilityValue.textContent = (result.probabilidad * 100).toFixed(0);

    // Set bar width and color
    const percentage = result.probabilidad * 100;
    probabilityBar.style.width = `${percentage}%`;

    // Reset colors
    probabilityBar.className = 'progress-bar';

    if (percentage < 40) {
        probabilityBar.classList.add('bg-red');
    } else if (percentage < 75) {
        probabilityBar.classList.add('bg-yellow');
    } else {
        probabilityBar.classList.add('bg-green');
    }
}

function closeModal() {
    document.getElementById('resultModal').classList.add('hidden');
}
