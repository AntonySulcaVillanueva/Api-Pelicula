package com.peliculasapi.ServiceDTO;

import com.peliculasapi.dto.DirectorResumenDTO;

import java.util.List;

public interface DirectorResumenServiceDTO {
    List<DirectorResumenDTO> getAllDirectors();
    DirectorResumenDTO getDirectorById(Long id);
}