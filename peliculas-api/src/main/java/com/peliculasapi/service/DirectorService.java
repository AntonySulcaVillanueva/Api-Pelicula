package com.peliculasapi.service;

import com.peliculasapi.entity.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorService {

    List<Director> obtenerDirectores();
    Optional<Director> obtenerPorId(Long id);
    Director guardarDirector(Director director);
    Director actualizarDirector(Long id,Director directorActualizado);
    void eliminarDirector(Long id);
}
