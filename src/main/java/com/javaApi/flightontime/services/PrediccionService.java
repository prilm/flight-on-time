package com.javaApi.flightontime.services;

import com.javaApi.flightontime.dtos.BusquedaDto;
import com.javaApi.flightontime.dtos.RespuestaDto;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class PrediccionService {
    public RespuestaDto consultarPrediccion(BusquedaDto consulta) {
        // URL del microservicio de predicción
        String url = "http://127.0.0.1:5000/predict";
        URI direccion = URI.create(url);

        // Crear el mapa con los datos en el formato correcto
        Map<String, Object> map = new HashMap<>();
        map.put("fecha", consulta.getFecha());           // ← ACTUALIZADO
        map.put("hora", consulta.getHora());             // ← NUEVO
        map.put("aerolinea", consulta.getAerolinea());
        map.put("origen", consulta.getOrigen());
        map.put("destino", consulta.getDestino());
        map.put("distancia_km", consulta.getDistanciaKm());

        ObjectMapper mapper = new ObjectMapper();
        String jsonInput;

        try {
            jsonInput = mapper.writeValueAsString(map);
            System.out.println("Consultando microservicio de data science, input: " + jsonInput);
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar el JSON: " + e.getMessage());
        }

        // Enviar el request al servicio de Data Science
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Respuesta del microservicio: " + response.body());

            // Convertir la respuesta JSON a objeto DTO
            RespuestaDto respuestaDto = mapper.readValue(response.body(), RespuestaDto.class);
            return respuestaDto;

        } catch (Exception e) {
            throw new RuntimeException("Error al ejecutar la API de Data Science: " + e.getMessage());
        }
    }
}