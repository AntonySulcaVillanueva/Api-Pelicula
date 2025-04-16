package com.peliculasapi.ServiceDTO.impl;

import com.peliculasapi.ServiceDTO.FichaTecnicaDetalladaServiceDTO;
import com.peliculasapi.dto.FichaTecnicaDetalladaDTO;
import com.peliculasapi.dto.ParticipacionActorDTO;
import com.peliculasapi.entity.Pelicula;
import com.peliculasapi.entity.PeliculaActor;
import com.peliculasapi.repository.PeliculaActorRepository;
import com.peliculasapi.repository.PeliculaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class FichaTecnicaDetalladaServiceDTOImpl implements FichaTecnicaDetalladaServiceDTO {

    // Instancia estática única (Singleton)
    private static FichaTecnicaDetalladaServiceDTOImpl instance;

    // Repositorios como dependencias
    private PeliculaRepository peliculaRepository;
    private PeliculaActorRepository peliculaActorRepository;

    // Constructor privado para evitar instanciación externa
    private FichaTecnicaDetalladaServiceDTOImpl(PeliculaRepository peliculaRepository, PeliculaActorRepository peliculaActorRepository) {
        this.peliculaRepository = peliculaRepository;
        this.peliculaActorRepository = peliculaActorRepository;
    }

    // Método estático para obtener la única instancia
    public static synchronized FichaTecnicaDetalladaServiceDTOImpl getInstance(PeliculaRepository peliculaRepository, PeliculaActorRepository peliculaActorRepository) {
        if (instance == null) {
            instance = new FichaTecnicaDetalladaServiceDTOImpl(peliculaRepository, peliculaActorRepository);
        }
        return instance;
    }

    @Override
    public FichaTecnicaDetalladaDTO getFichaTecnicaByPeliculaId(Long id) {
        Pelicula pelicula = peliculaRepository.findById(id).orElse(null);
        if (pelicula == null) {
            return null;
        }

        FichaTecnicaDetalladaDTO dto = new FichaTecnicaDetalladaDTO();
        dto.setTitulo(pelicula.getTitulo());
        dto.setClasificacion(pelicula.getClasificacion());
        dto.setIdiomaOriginal(pelicula.getIdiomaOriginal());
        dto.setFechaLanzamientoPublico(pelicula.getFechaLanzamientoPublico());

        if (pelicula.getGenero() != null) {
            dto.setGenero(pelicula.getGenero().getNombre());
        }

        if (pelicula.getDirector() != null) {
            dto.setDirectorNombreCompleto(pelicula.getDirector().getNombre() + " " + pelicula.getDirector().getApellido());
            dto.setDirectorNacionalidad(pelicula.getDirector().getNacionalidad());
            dto.setDirectorFoto(pelicula.getDirector().getImagenUrl());
        }

        List<PeliculaActor> peliculaActores = peliculaActorRepository.findByPeliculaId(id);
        dto.setRepartoTecnico(peliculaActores.stream().map(peliculaActor -> {
            ParticipacionActorDTO participacion = new ParticipacionActorDTO();
            participacion.setNombreCompleto(peliculaActor.getActor().getNombre() + " " + peliculaActor.getActor().getApellido());
            participacion.setPersonaje(peliculaActor.getPersonaje());
            participacion.setContratoFirmado(peliculaActor.getContratoFirmado());
            participacion.setFechaContrato(peliculaActor.getFechaContrato());
            participacion.setComentarios(peliculaActor.getComentarios());
            return participacion;
        }).collect(Collectors.toList()));

        return dto;
    }
}