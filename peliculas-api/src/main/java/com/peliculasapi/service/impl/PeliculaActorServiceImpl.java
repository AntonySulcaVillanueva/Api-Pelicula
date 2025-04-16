package com.peliculasapi.service.impl;

import com.peliculasapi.entity.PeliculaActor;
import com.peliculasapi.repository.PeliculaActorRepository;
import com.peliculasapi.service.PeliculaActorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeliculaActorServiceImpl implements PeliculaActorService {

    private final PeliculaActorRepository peliculaActorRepository;

    public PeliculaActorServiceImpl(PeliculaActorRepository peliculaActorRepository) {
        this.peliculaActorRepository = peliculaActorRepository;
    }

    @Override
    public List<PeliculaActor> obtenerPeliculaActores() {
        return peliculaActorRepository.findAll();
    }

    @Override
    public Optional<PeliculaActor> obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID de la relación Película-Actor no es válido: " + id);
        }
        return peliculaActorRepository.findById(id)
                .or(() -> {
                    throw new RuntimeException("Relación Película-Actor no encontrada con ID: " + id);
                });
    }

    @Override
    public PeliculaActor guardarPeliculaActor(PeliculaActor peliculaActor) {
        if (peliculaActor == null) {
            throw new IllegalArgumentException("La información de la relación Película-Actor no puede ser nula.");
        }
        if (peliculaActor.getPersonaje() == null || peliculaActor.getPersonaje().isBlank()) {
            throw new IllegalArgumentException("El personaje no puede estar vacío.");
        }

        // Calcular valores automáticos
        if (peliculaActor.getContratoFirmado() == null) {
            peliculaActor.setContratoFirmado(false); // Contrato no firmado por defecto
        }
        if (peliculaActor.getCacheFinal() == null && peliculaActor.getBonosExtra() != null) {
            peliculaActor.setCacheFinal(peliculaActor.getBonosExtra()); // Cache final inicial basado en bonos
        }

        // Validar fecha de contrato si está firmado
        if (peliculaActor.getContratoFirmado() && peliculaActor.getFechaContrato() == null) {
            peliculaActor.setFechaContrato(java.time.LocalDate.now());
        }

        return peliculaActorRepository.save(peliculaActor);
    }

    @Override
    public PeliculaActor actualizarPeliculaActor(Long id, PeliculaActor peliculaActorActualizada) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID proporcionado no es válido: " + id);
        }
        if (peliculaActorActualizada == null) {
            throw new IllegalArgumentException("Los datos de la relación Película-Actor actualizada no pueden ser nulos.");
        }

        return peliculaActorRepository.findById(id).map(peliculaActor -> {
            if (peliculaActorActualizada.getPersonaje() != null && !peliculaActorActualizada.getPersonaje().isBlank()) {
                peliculaActor.setPersonaje(peliculaActorActualizada.getPersonaje());
            }
            if (peliculaActorActualizada.getContratoFirmado() != null) {
                peliculaActor.setContratoFirmado(peliculaActorActualizada.getContratoFirmado());
                // Si el contrato se firma y no hay fecha de contrato, establecerla automáticamente
                if (peliculaActor.getContratoFirmado() && peliculaActor.getFechaContrato() == null) {
                    peliculaActor.setFechaContrato(java.time.LocalDate.now());
                }
            }
            if (peliculaActorActualizada.getCacheFinal() != null) {
                peliculaActor.setCacheFinal(peliculaActorActualizada.getCacheFinal());
            } else if (peliculaActorActualizada.getBonosExtra() != null) {
                peliculaActor.setCacheFinal(peliculaActorActualizada.getBonosExtra());
            }
            if (peliculaActorActualizada.getBonosExtra() != null) {
                peliculaActor.setBonosExtra(peliculaActorActualizada.getBonosExtra());
            }
            if (peliculaActorActualizada.getComentarios() != null) {
                peliculaActor.setComentarios(peliculaActorActualizada.getComentarios());
            }

            return peliculaActorRepository.save(peliculaActor);
        }).orElseThrow(() -> new RuntimeException("Relación Película-Actor no encontrada con ID: " + id));
    }

    @Override
    public void eliminarPeliculaActor(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID proporcionado no es válido: " + id);
        }

        if (peliculaActorRepository.existsById(id)) {
            peliculaActorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Relación Película-Actor no encontrada con ID: " + id);
        }
    }
}