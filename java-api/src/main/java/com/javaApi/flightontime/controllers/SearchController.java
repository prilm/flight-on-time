package com.javaApi.flightontime.controllers;

import com.javaApi.flightontime.dtos.BusquedaDto;
import com.javaApi.flightontime.dtos.RespuestaDto;
import com.javaApi.flightontime.services.PrediccionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {

    @PostMapping
    public ResponseEntity<RespuestaDto> search(@Valid @RequestBody BusquedaDto consulta){
        System.out.println("Recibiendo búsqueda: " +
                "Aerolínea=" + consulta.getAerolinea() +
                ", Origen=" + consulta.getOrigen() +
                ", Destino=" + consulta.getDestino() +
                ", Fecha=" + consulta.getFecha() +
                ", Hora=" + consulta.getHora());

        // Crear instancia del servicio de predicción
        PrediccionService prediccionService = new PrediccionService();
        RespuestaDto resultadoPrediccion = prediccionService.consultarPrediccion(consulta);

        System.out.println("Resultados de predicción: " + resultadoPrediccion);

        // Retornar la respuesta
        return ResponseEntity.ok(resultadoPrediccion);
    }
}