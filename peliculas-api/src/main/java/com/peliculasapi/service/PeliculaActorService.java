package com.peliculasapi.service;

import com.peliculasapi.entity.PeliculaActor;

import java.util.List;
import java.util.Optional;

public interface PeliculaActorService {

    List<PeliculaActor> obtenerPeliculaActores();
    Optional<PeliculaActor> obtenerPorId(Long id);
    PeliculaActor guardarPeliculaActor(PeliculaActor peliculaActor);
    PeliculaActor actualizarPeliculaActor(Long id,PeliculaActor peliculaActorActualizado);
    void eliminarPeliculaActor(Long id);
}
