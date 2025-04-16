package com.peliculasapi.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ParticipacionActorDTO {
    private String nombreCompleto;
    private String personaje;
    private Boolean contratoFirmado;
    private LocalDate fechaContrato;
    private String comentarios;
}
