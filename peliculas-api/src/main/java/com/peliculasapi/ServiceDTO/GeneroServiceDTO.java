package com.peliculasapi.ServiceDTO;

import com.peliculasapi.dto.GeneroDTO;

import java.util.List;

public interface GeneroServiceDTO {
    List<GeneroDTO> getAllGeneros();
    GeneroDTO getGeneroById(Long id);
}