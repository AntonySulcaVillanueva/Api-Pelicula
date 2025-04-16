package com.peliculasapi.service.impl;

import com.peliculasapi.entity.Genero;
import com.peliculasapi.repository.GeneroRepository;
import com.peliculasapi.service.GeneroService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeneroServiceImpl implements GeneroService {

    private final GeneroRepository generoRepository;

    public GeneroServiceImpl(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @Override
    public List<Genero> obtenerGeneros() {
        return generoRepository.findAll();
    }

    @Override
    public Optional<Genero> obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del género no es válido: " + id);
        }
        return generoRepository.findById(id)
                .or(() -> {
                    throw new RuntimeException("Género no encontrado con ID: " + id);
                });
    }

    @Override
    public Genero guardarGenero(Genero genero) {
        if (genero == null) {
            throw new IllegalArgumentException("La información del género no puede ser nula.");
        }
        if (genero.getNombre() == null || genero.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del género no puede estar vacío.");
        }
        return generoRepository.save(genero);
    }

    @Override
    public Genero actualizarGenero(Long id, Genero generoActualizado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID proporcionado no es válido: " + id);
        }
        if (generoActualizado == null) {
            throw new IllegalArgumentException("Los datos del género actualizado no pueden ser nulos.");
        }

        return generoRepository.findById(id).map(genero -> {
            if (generoActualizado.getNombre() != null && !generoActualizado.getNombre().isBlank()) {
                genero.setNombre(generoActualizado.getNombre());
            }
            if (generoActualizado.getDescripcion() != null && !generoActualizado.getDescripcion().isBlank()) {
                genero.setDescripcion(generoActualizado.getDescripcion());
            }
            return generoRepository.save(genero);
        }).orElseThrow(() -> new RuntimeException("Género no encontrado con ID: " + id));
    }

    @Override
    public void eliminarGenero(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID proporcionado no es válido: " + id);
        }

        if (generoRepository.existsById(id)) {
            generoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Género no encontrado con ID: " + id);
        }
    }
}