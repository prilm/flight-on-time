package com.flightontime.javaapi.controllers;

import com.flightontime.javaapi.dtos.BusquedaDto;
import com.flightontime.javaapi.dtos.RespuestaDto;
import com.flightontime.javaapi.services.PrediccionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {

    @PostMapping
    public RespuestaDto search(@RequestBody BusquedaDto consulta){
        // se crea una instancia de la clase PrediccionService y luego se llama a consultarPredicción()
        PrediccionService prediccion = new PrediccionService();
        RespuestaDto resultadoPrediccion = prediccion.consultarPrediccion(consulta);

        System.out.println("Resultados de prediccion: " + resultadoPrediccion);

        // Se retorna el DTO de respuesta
        return resultadoPrediccion;
    }
}
