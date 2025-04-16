package com.peliculasapi.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FichaTecnicaDetalladaDTO {
    private String titulo;
    private String clasificacion;
    private String idiomaOriginal;
    private LocalDate fechaLanzamientoPublico;
    private String genero;

    private String directorNombreCompleto;
    private String directorNacionalidad;
    private String directorFoto;

    private List<ParticipacionActorDTO> repartoTecnico;
}