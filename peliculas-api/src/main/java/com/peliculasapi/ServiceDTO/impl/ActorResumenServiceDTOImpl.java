package com.peliculasapi.ServiceDTO.impl;

import com.peliculasapi.ServiceDTO.ActorResumenServiceDTO;
import com.peliculasapi.dto.ActorResumenDTO;
import com.peliculasapi.entity.Actor;
import com.peliculasapi.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorResumenServiceDTOImpl implements ActorResumenServiceDTO {

    @Autowired
    private ActorRepository actorRepository;

    @Override
    public List<ActorResumenDTO> getAllActors() {
        return actorRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ActorResumenDTO getActorById(Long id) {
        return actorRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    private ActorResumenDTO convertToDTO(Actor actor) {
        ActorResumenDTO dto = new ActorResumenDTO();
        dto.setNombre(actor.getNombre());
        dto.setApellido(actor.getApellido());
        dto.setFotoUrl(actor.getFotoUrl());
        return dto;
    }
}