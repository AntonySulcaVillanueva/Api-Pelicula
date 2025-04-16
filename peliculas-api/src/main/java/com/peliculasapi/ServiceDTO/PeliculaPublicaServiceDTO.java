package com.peliculasapi.ServiceDTO;

import com.peliculasapi.dto.PeliculaPublicaDTO;

import java.util.List;

public interface PeliculaPublicaServiceDTO {
    List<PeliculaPublicaDTO> getAllPeliculas();
    PeliculaPublicaDTO getPeliculaById(Long id);
}