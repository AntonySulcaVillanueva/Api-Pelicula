package com.peliculasapi.ServiceDTO.impl;

import com.peliculasapi.ServiceDTO.PeliculaPublicaServiceDTO;
import com.peliculasapi.dto.ActorResumenDTO;
import com.peliculasapi.dto.DirectorResumenDTO;
import com.peliculasapi.dto.GeneroDTO;
import com.peliculasapi.dto.PeliculaPublicaDTO;
import com.peliculasapi.entity.Pelicula;
import com.peliculasapi.entity.PeliculaActor;
import com.peliculasapi.repository.PeliculaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PeliculaPublicaServiceDTOImpl implements PeliculaPublicaServiceDTO {

    // Instancia estática única (Singleton)
    private static PeliculaPublicaServiceDTOImpl instance;

    // Repositorio como dependencia
    private PeliculaRepository peliculaRepository;

    // Constructor privado para evitar instanciación externa
    private PeliculaPublicaServiceDTOImpl(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    // Método estático para obtener la única instancia
    public static synchronized PeliculaPublicaServiceDTOImpl getInstance(PeliculaRepository peliculaRepository) {
        if (instance == null) {
            instance = new PeliculaPublicaServiceDTOImpl(peliculaRepository);
        }
        return instance;
    }

    @Override
    public List<PeliculaPublicaDTO> getAllPeliculas() {
        return peliculaRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public PeliculaPublicaDTO getPeliculaById(Long id) {
        return peliculaRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    private PeliculaPublicaDTO convertToDTO(Pelicula pelicula) {
        PeliculaPublicaDTO dto = new PeliculaPublicaDTO();
        dto.setImagenUrl(pelicula.getImagenUrl());
        dto.setTitulo(pelicula.getTitulo());
        dto.setSubtitulo(pelicula.getSubtitulo());
        dto.setAño(pelicula.getAño());
        dto.setDuracion(pelicula.getDuracion());
        dto.setClasificacion(pelicula.getClasificacion());
        dto.setIdiomaOriginal(pelicula.getIdiomaOriginal());
        dto.setSinopsis(pelicula.getSinopsis());
        dto.setEstado(pelicula.getEstado());
        dto.setFechaLanzamientoPublico(pelicula.getFechaLanzamientoPublico());

        // Mapear el género
        if (pelicula.getGenero() != null) {
            GeneroDTO generoDTO = new GeneroDTO();
            generoDTO.setNombre(pelicula.getGenero().getNombre());
            generoDTO.setDescripcion(pelicula.getGenero().getDescripcion());
            dto.setGenero(generoDTO);
        }

        // Mapear el director
        if (pelicula.getDirector() != null) {
            DirectorResumenDTO directorDTO = new DirectorResumenDTO();
            directorDTO.setNombre(pelicula.getDirector().getNombre());
            directorDTO.setApellido(pelicula.getDirector().getApellido());
            directorDTO.setNacionalidad(pelicula.getDirector().getNacionalidad());
            directorDTO.setImagenUrl(pelicula.getDirector().getImagenUrl());
            dto.setDirector(directorDTO);
        }

        // Mapear los actores a través de la relación PeliculaActor
        if (pelicula.getPeliculasActor() != null && !pelicula.getPeliculasActor().isEmpty()) {
            List<ActorResumenDTO> actoresDTO = pelicula.getPeliculasActor().stream()
                    .map(PeliculaActor::getActor) // Obtener el actor desde la relación PeliculaActor
                    .map(actor -> {
                        ActorResumenDTO actorDTO = new ActorResumenDTO();
                        actorDTO.setNombre(actor.getNombre());
                        actorDTO.setApellido(actor.getApellido());
                        actorDTO.setFotoUrl(actor.getFotoUrl());
                        return actorDTO;
                    }).collect(Collectors.toList());
            dto.setActores(actoresDTO);
        }

        return dto;
    }
}