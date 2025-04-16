package com.peliculasapi.service.impl;

import com.peliculasapi.entity.Actor;
import com.peliculasapi.entity.Pelicula;
import com.peliculasapi.repository.PeliculaRepository;
import com.peliculasapi.service.PeliculaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeliculaServiceImpl implements PeliculaService {

    private final PeliculaRepository peliculaRepository;

    public PeliculaServiceImpl(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    // Obtener todas las películas
    @Override
    public List<Pelicula> obtenerPeliculas() {
        return peliculaRepository.findAll();
    }

    // Obtener una película por ID
    @Override
    public Optional<Pelicula> obtenerPorId(Long id) {
        validarId(id);
        return peliculaRepository.findById(id);
    }

    // Guardar una nueva película
    @Override
    public Pelicula guardarPelicula(Pelicula pelicula) {
        validarPelicula(pelicula);
        return peliculaRepository.save(pelicula);
    }

    // Actualizar una película existente
    @Override
    public Pelicula actualizarPelicula(Long id, Pelicula peliculaActualizada) {
        validarId(id);
        validarPelicula(peliculaActualizada);

        return peliculaRepository.findById(id).map(pelicula -> {
            // Actualizar solo los campos permitidos
            actualizarCampos(pelicula, peliculaActualizada);

            // Recalcular ingresos reales
            pelicula.calcularIngresosReales();

            return peliculaRepository.save(pelicula);
        }).orElseThrow(() -> new RuntimeException("Película no encontrada con ID: " + id));
    }

    // Eliminar una película por ID
    @Override
    public void eliminarPelicula(Long id) {
        validarId(id);

        if (peliculaRepository.existsById(id)) {
            peliculaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Película no encontrada con ID: " + id);
        }
    }

    // Métodos auxiliares

    // Validar que el ID sea válido
    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID proporcionado no es válido: " + id);
        }
    }

    // Validar que los datos de la película sean correctos
    private void validarPelicula(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("La información de la película no puede ser nula.");
        }
        if (pelicula.getTitulo() == null || pelicula.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título de la película no puede estar vacío.");
        }
        if (pelicula.getGenero() == null) {
            throw new IllegalArgumentException("El género de la película es obligatorio.");
        }
        if (pelicula.getDirector() == null) {
            throw new IllegalArgumentException("El director de la película es obligatorio.");
        }
    }

    // Actualizar los campos de una película
    private void actualizarCampos(Pelicula pelicula, Pelicula peliculaActualizada) {
        if (peliculaActualizada.getTitulo() != null && !peliculaActualizada.getTitulo().isBlank()) {
            pelicula.setTitulo(peliculaActualizada.getTitulo());
        }
        if (peliculaActualizada.getSinopsis() != null) {
            pelicula.setSinopsis(peliculaActualizada.getSinopsis());
        }
        if (peliculaActualizada.getGenero() != null) {
            pelicula.setGenero(peliculaActualizada.getGenero());
        }
        if (peliculaActualizada.getDirector() != null) {
            pelicula.setDirector(peliculaActualizada.getDirector());
        }
        if (peliculaActualizada.getDuracion() != null && peliculaActualizada.getDuracion() > 0) {
            pelicula.setDuracion(peliculaActualizada.getDuracion());
        }
        if (peliculaActualizada.getIdiomaOriginal() != null && !peliculaActualizada.getIdiomaOriginal().isBlank()) {
            pelicula.setIdiomaOriginal(peliculaActualizada.getIdiomaOriginal());
        }
        if (peliculaActualizada.getPresupuestoTotal() != null && peliculaActualizada.getPresupuestoTotal().doubleValue() > 0) {
            pelicula.setPresupuestoTotal(peliculaActualizada.getPresupuestoTotal());
        }
        if (peliculaActualizada.getGastosGenerales() != null && peliculaActualizada.getGastosGenerales().doubleValue() > 0) {
            pelicula.setGastosGenerales(peliculaActualizada.getGastosGenerales());
        }
        if (peliculaActualizada.getFechaLanzamientoPublico() != null) {
            pelicula.setFechaLanzamientoPublico(peliculaActualizada.getFechaLanzamientoPublico());
        }
    }
}