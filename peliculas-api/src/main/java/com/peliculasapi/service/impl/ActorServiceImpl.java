package com.peliculasapi.service.impl;

import com.peliculasapi.entity.Actor;
import com.peliculasapi.repository.ActorRepository;
import com.peliculasapi.service.ActorService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class  ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public List<Actor> obtenerActores() {
        return actorRepository.findAll();
    }

    @Override
    public Optional<Actor> obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del actor no es válido: " + id);
        }
        return actorRepository.findById(id)
                .or(() -> {
                    throw new RuntimeException("Actor no encontrado con ID: " + id);
                });
    }

    @Override
    public Actor guardarActor(Actor actor) {
        validarActor(actor);
        return actorRepository.save(actor);
    }

    @Override
    public Actor actualizarActor(Long id, Actor actorActualizado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID proporcionado no es válido: " + id);
        }
        if (actorActualizado == null) {
            throw new IllegalArgumentException("Los datos del actor actualizado no pueden ser nulos.");
        }

        return actorRepository.findById(id).map(actor -> {
            if (actorActualizado.getNombre() != null && !actorActualizado.getNombre().isBlank()) {
                actor.setNombre(actorActualizado.getNombre());
            }
            if (actorActualizado.getApellido() != null && !actorActualizado.getApellido().isBlank()) {
                actor.setApellido(actorActualizado.getApellido());
            }
            if (actorActualizado.getNacionalidad() != null && !actorActualizado.getNacionalidad().isBlank()) {
                actor.setNacionalidad(actorActualizado.getNacionalidad());
            }
            if (actorActualizado.getFotoUrl() != null && !actorActualizado.getFotoUrl().isBlank()) {
                if (!actorActualizado.getFotoUrl().matches("^(http|https)://.*$")) {
                    throw new IllegalArgumentException("La URL de la foto no es válida.");
                }
                actor.setFotoUrl(actorActualizado.getFotoUrl());
            }
            if (actorActualizado.getFechaNacimiento() != null) {
                if (actorActualizado.getFechaNacimiento().isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("La fecha de nacimiento debe ser en el pasado.");
                }
                actor.setFechaNacimiento(actorActualizado.getFechaNacimiento());
            }
            if (actorActualizado.getEmailAgente() != null && !actorActualizado.getEmailAgente().isBlank()) {
                if (!actorActualizado.getEmailAgente().matches("^[a-zA-Z0-9._%+-]+@(gmail\\.com|outlook\\.com|yahoo\\.com)$")) {
                    throw new IllegalArgumentException("El correo del agente debe ser un correo válido de Gmail, Outlook o Yahoo.");
                }
                actor.setEmailAgente(actorActualizado.getEmailAgente());
            }
            if (actorActualizado.getCacheEstimado() != null) {
                if (actorActualizado.getCacheEstimado().compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("El caché estimado debe ser un valor positivo o cero.");
                }
                actor.setCacheEstimado(actorActualizado.getCacheEstimado());
            }
            if (actorActualizado.getSalarioMensual() != null) {
                if (actorActualizado.getSalarioMensual().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("El salario mensual debe ser un valor positivo.");
                }
                actor.setSalarioMensual(actorActualizado.getSalarioMensual());
            }
            if (actorActualizado.getEstado() != null && !actorActualizado.getEstado().isBlank()) {
                if (!actorActualizado.getEstado().matches("^(Activo|Inactivo)$")) {
                    throw new IllegalArgumentException("El estado debe ser 'Activo' o 'Inactivo'.");
                }
                actor.setEstado(actorActualizado.getEstado());
            }
            if (actorActualizado.getNotas() != null && actorActualizado.getNotas().length() > 500) {
                throw new IllegalArgumentException("Las notas no pueden exceder los 500 caracteres.");
            }
            actor.setNotas(actorActualizado.getNotas());
            return actorRepository.save(actor);
        }).orElseThrow(() -> new RuntimeException("Actor no encontrado con ID: " + id));
    }

    @Override
    public void eliminarActor(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID proporcionado no es válido: " + id);
        }

        if (actorRepository.existsById(id)) {
            actorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Actor no encontrado con ID: " + id);
        }
    }

    private void validarActor(Actor actor) {
        if (actor == null) {
            throw new IllegalArgumentException("La información del actor no puede ser nula.");
        }
        if (actor.getNombre() == null || actor.getNombre().isBlank() || actor.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre del actor no puede estar vacío y debe tener un máximo de 100 caracteres.");
        }
        if (actor.getApellido() == null || actor.getApellido().isBlank() || actor.getApellido().length() > 100) {
            throw new IllegalArgumentException("El apellido del actor no puede estar vacío y debe tener un máximo de 100 caracteres.");
        }
        if (actor.getNacionalidad() == null || actor.getNacionalidad().isBlank() || actor.getNacionalidad().length() > 100) {
            throw new IllegalArgumentException("La nacionalidad no puede estar vacía y debe tener un máximo de 100 caracteres.");
        }
        if (actor.getFotoUrl() != null && !actor.getFotoUrl().matches("^(http|https)://.*$")) {
            throw new IllegalArgumentException("La URL de la foto no es válida.");
        }
        if (actor.getFechaNacimiento() != null && actor.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento debe ser en el pasado.");
        }
        if (actor.getEmailAgente() != null && !actor.getEmailAgente().matches("^[a-zA-Z0-9._%+-]+@(gmail\\.com|outlook\\.com|yahoo\\.com)$")) {
            throw new IllegalArgumentException("El correo del agente debe ser un correo válido de Gmail, Outlook o Yahoo.");
        }
        if (actor.getCacheEstimado() != null && actor.getCacheEstimado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El caché estimado debe ser un valor positivo o cero.");
        }
        if (actor.getSalarioMensual() != null && actor.getSalarioMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El salario mensual debe ser un valor positivo.");
        }
        if (actor.getEstado() == null || actor.getEstado().isBlank() || !actor.getEstado().matches("^(Activo|Inactivo)$")) {
            throw new IllegalArgumentException("El estado debe ser 'Activo' o 'Inactivo'.");
        }
        if (actor.getNotas() != null && actor.getNotas().length() > 500) {
            throw new IllegalArgumentException("Las notas no pueden exceder los 500 caracteres.");
        }
    }
}