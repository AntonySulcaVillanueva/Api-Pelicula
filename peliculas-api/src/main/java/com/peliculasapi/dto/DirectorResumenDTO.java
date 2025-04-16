package com.peliculasapi.dto;

import lombok.Data;

@Data
public class DirectorResumenDTO {
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private String imagenUrl;
}