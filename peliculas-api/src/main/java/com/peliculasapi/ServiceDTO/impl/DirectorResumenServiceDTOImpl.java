package com.peliculasapi.ServiceDTO.impl;

import com.peliculasapi.ServiceDTO.DirectorResumenServiceDTO;
import com.peliculasapi.dto.DirectorResumenDTO;
import com.peliculasapi.entity.Director;
import com.peliculasapi.repository.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectorResumenServiceDTOImpl implements DirectorResumenServiceDTO {

    @Autowired
    private DirectorRepository directorRepository;

    @Override
    public List<DirectorResumenDTO> getAllDirectors() {
        return directorRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public DirectorResumenDTO getDirectorById(Long id) {
        return directorRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    private DirectorResumenDTO convertToDTO(Director director) {
        DirectorResumenDTO dto = new DirectorResumenDTO();
        dto.setNombre(director.getNombre());
        dto.setApellido(director.getApellido());
        dto.setNacionalidad(director.getNacionalidad());
        dto.setImagenUrl(director.getImagenUrl());
        return dto;
    }
}