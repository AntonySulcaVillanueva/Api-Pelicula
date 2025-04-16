package com.peliculasapi.ServiceDTO;

import com.peliculasapi.dto.ActorResumenDTO;

import java.util.List;

public interface ActorResumenServiceDTO {
    List<ActorResumenDTO> getAllActors();
    ActorResumenDTO getActorById(Long id);
}