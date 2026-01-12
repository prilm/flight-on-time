package com.javaApi.flightontime.dtos;

import jakarta.validation.constraints.NotNull;

public class BusquedaDto {
    @NotNull(message = "Falta la aerolínea")
    private String aerolinea;

    @NotNull(message = "Falta el aeropuerto de origen")
    private String origen;

    @NotNull(message = "Falta el aeropuerto de destino")
    private String destino;

    @NotNull(message = "Falta la fecha de partida")
    private String fecha;  // ← NUEVO: formato "2024-06-15"

    @NotNull(message = "Falta la hora de partida")
    private String hora;   // ← NUEVO: formato "09:00"

    @NotNull(message = "Falta la distancia")
    private Integer distanciaKm;

    // Getters y Setters
    public String getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(String aerolinea) {
        this.aerolinea = aerolinea;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Integer distanciaKm) {
        this.distanciaKm = distanciaKm;
    }
}