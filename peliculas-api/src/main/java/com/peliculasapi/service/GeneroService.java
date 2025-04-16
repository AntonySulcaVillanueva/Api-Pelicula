package com.peliculasapi.service;

import com.peliculasapi.entity.Genero;

import java.util.List;
import java.util.Optional;

public interface GeneroService {

    List<Genero> obtenerGeneros();
    Optional<Genero> obtenerPorId(Long id);
    Genero guardarGenero(Genero genero);
    Genero actualizarGenero(Long id,Genero generoActualizado);
    void eliminarGenero(Long id);
}
