package com.peliculasapi.service;

import com.peliculasapi.entity.Pelicula;

import java.util.List;
import java.util.Optional;

public interface PeliculaService {

    List<Pelicula> obtenerPeliculas();
    Optional<Pelicula> obtenerPorId(Long id);
    Pelicula guardarPelicula(Pelicula pelicula);
    Pelicula actualizarPelicula(Long id,Pelicula peliculaActualizada);
    void eliminarPelicula(Long id);
}
