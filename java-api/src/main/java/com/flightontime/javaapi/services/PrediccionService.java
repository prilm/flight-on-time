package com.flightontime.javaapi.services;

import com.flightontime.javaapi.dtos.BusquedaDto;
import com.flightontime.javaapi.dtos.RespuestaDto;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class PrediccionService {
    public RespuestaDto consultarPrediccion(BusquedaDto consulta) {
        // Hacer un request al servicio de Data Science (Flask) para obtener la probabilidad

        // TODO: Generar la URL de manera dinámica usando variables de entorno para configurar el host y el puerto
        String url = "http://127.0.0.1:5000/predict";
        URI direccion = URI.create(url);

        // Se serializa a JSON el input del usuario
        // TODO: este es un string JSON de prueba. Hay que modificarlo para que se genere de manera dinámica a través de los inputs del usuario y valores obtenidos de la BD
        Map<String, Object> map = new HashMap<>();
        map.put("aerolinea", consulta.getAerolinea());
        map.put("origen", consulta.getOrigen());
        map.put("destino", consulta.getDestino());
        map.put("fecha_partida", consulta.getFechaPartida());
        map.put("distancia_km", consulta.getDistanciaKm());
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(map);

        System.out.println("Consultando microservicio de data science, input: " + jsonInput);

        // Se envía el request al servicio de Data Science
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                .build();
        try {
            HttpResponse<String> response = null;
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Se convierte la respuesta que está string JSON a un objeto del DTO de respuesta
            RespuestaDto respuestaDto = mapper.readValue(response.body(), RespuestaDto.class);
            return respuestaDto;
        } catch (Exception e) {
            throw new RuntimeException("Error al ejecutar la API de Data Science");
        }
    }
}
