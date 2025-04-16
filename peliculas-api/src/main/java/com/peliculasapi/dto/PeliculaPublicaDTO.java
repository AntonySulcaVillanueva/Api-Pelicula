package com.peliculasapi.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PeliculaPublicaDTO {
    private String imagenUrl;
    private String titulo;
    private String subtitulo;
    private Integer año;
    private Integer duracion;
    private String clasificacion;
    private String idiomaOriginal;
    private String sinopsis;
    private String estado;
    private LocalDate fechaLanzamientoPublico;
    private GeneroDTO genero;
    private DirectorResumenDTO director;
    private List<ActorResumenDTO> actores;
}