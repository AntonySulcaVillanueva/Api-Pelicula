package com.peliculasapi.ServiceDTO.impl;

import com.peliculasapi.ServiceDTO.GeneroServiceDTO;
import com.peliculasapi.dto.GeneroDTO;
import com.peliculasapi.entity.Genero;
import com.peliculasapi.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneroServiceDTOImpl implements GeneroServiceDTO {

    @Autowired
    private GeneroRepository generoRepository;

    @Override
    public List<GeneroDTO> getAllGeneros() {
        return generoRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public GeneroDTO getGeneroById(Long id) {
        return generoRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    private GeneroDTO convertToDTO(Genero genero) {
        GeneroDTO dto = new GeneroDTO();
        dto.setNombre(genero.getNombre());
        dto.setDescripcion(genero.getDescripcion());
        return dto;
    }
}