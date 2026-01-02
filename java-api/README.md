### Ejemplo para ejecutar una búsqueda:

```bash
curl --location 'http://127.0.0.1:8080/search' \
--header 'Content-Type: application/json' \
--data '{
    "aerolinea": "Latam",
    "origen": "SCL",
    "destino": "PMC",
    "fechaPartida": "2026-02-10T14:30:00",
    "distanciaKm": 1000
}'
```

