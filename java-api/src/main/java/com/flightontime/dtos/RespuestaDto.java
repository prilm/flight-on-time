package com.flightontime.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RespuestaDto {
    @JsonProperty("prevision")
    private String prevision;

    @JsonProperty("probabilidad")
    private Float probabilidad;

    // Getters
    public String getPrevision() {
        return prevision;
    }

    public Float getProbabilidad() {
        return probabilidad;
    }

    // Setters (necesarios para Jackson)
    public void setPrevision(String prevision) {
        this.prevision = prevision;
    }

    public void setProbabilidad(Float probabilidad) {
        this.probabilidad = probabilidad;
    }

    @Override
    public String toString() {
        return "RespuestaDto{" +
                "prevision='" + prevision + '\'' +
                ", probabilidad=" + probabilidad +
                '}';
    }
}