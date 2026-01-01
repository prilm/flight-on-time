package com.flightontime.javaapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    @PostMapping
    public ResponseEntity hello(){
        // Hacer un request a Flask para obtener la probabilidad
        // TODO: Generar la URL de manera dinámica usando variables de entorno para configurar el host y el puerto
        String url = "http://127.0.0.1:5000/predict";

        URI direccion = URI.create(url);
        // TODO: este es un string JSON de prueba. Hay que modificarlo para que se genere de manera dinámica a través de los inputs del usuario y valores obtenidos de la BD
        String jsonInput = "{\"aerolinea\":\"AA\",\"origen\":\"ATL\",\"destino\":\"LAX\",\"fecha_partida\":\"2025-01-15 14:30:00\",\"distancia_km\":3000,\"hora\":14,\"hora\":14}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                .build();
        try {
            HttpResponse<String> response = null;
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            //return new Gson().fromJson(response.body(), TasaDeConversion.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al ejecutar la API de data science");
        }


        // Retornar respuesta al usuario
        return ResponseEntity.ok(Map.of());
    }
}
