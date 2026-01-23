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

    document.getElementById('origen').addEventListener('change', calcularDistanciaKm);
    document.getElementById('destino').addEventListener('change', calcularDistanciaKm);
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

const coordenadas = {}

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

            coordenadas[cols[0]] = [cols[5], cols[6]];

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
        distancia_km: parseInt(document.getElementById('distancia_km').value)
    };

    try {
        const response = await fetch('http://127.0.0.1:8080/search', {
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

function calcularDistanciaKm(e) {
    const origen = document.getElementById('origen').value;
    const destino = document.getElementById('destino').value;

    if (origen === '' || destino === '') {
        return;
    }

    console.log('Calcular distancia entre ', origen, destino);

    function haversineDistance(lat1, lon1, lat2, lon2) {
    const R = 6371; // Radio de la Tierra en kilómetros

    const toRad = deg => deg * Math.PI / 180;

    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);

    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
    }

    // const distancia = haversineDistance(coordenadaOrigen, coordenadaDestino);
    const distancia = parseInt(haversineDistance(coordenadas[origen][0], coordenadas[origen][1], coordenadas[destino][0], coordenadas[destino][1]));
    
    document.getElementById('distancia_km').value = distancia;
    document.getElementById('valor_distancia_km').innerHTML = distancia;
}