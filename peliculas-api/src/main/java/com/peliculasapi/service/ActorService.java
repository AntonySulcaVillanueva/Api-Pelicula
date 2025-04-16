package com.peliculasapi.service;

import com.peliculasapi.entity.Actor;

import java.util.List;
import java.util.Optional;

public interface ActorService {

    List<Actor> obtenerActores();
    Optional<Actor> obtenerPorId(Long id);
    Actor guardarActor(Actor actor);
    Actor actualizarActor(Long id,Actor actorActualizado);
    void eliminarActor(Long id);
}
