package com.peliculasapi.service.impl;

import com.peliculasapi.entity.Director;
import com.peliculasapi.repository.DirectorRepository;
import com.peliculasapi.service.DirectorService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;

    public DirectorServiceImpl(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    @Override
    public List<Director> obtenerDirectores() {
        return directorRepository.findAll();
    }

    @Override
    public Optional<Director> obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del director no es válido: " + id);
        }
        return directorRepository.findById(id)
                .or(() -> {
                    throw new RuntimeException("Director no encontrado con ID: " + id);
                });
    }

    @Override
    public Director guardarDirector(Director director) {
        validarDirector(director);
        return directorRepository.save(director);
    }

    @Override
    public Director actualizarDirector(Long id, Director directorActualizado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID proporcionado no es válido: " + id);
        }
        if (directorActualizado == null) {
            throw new IllegalArgumentException("Los datos del director actualizado no pueden ser nulos.");
        }

        return directorRepository.findById(id).map(director -> {
            if (directorActualizado.getNombre() != null && !directorActualizado.getNombre().isBlank()) {
                director.setNombre(directorActualizado.getNombre());
            }
            if (directorActualizado.getApellido() != null && !directorActualizado.getApellido().isBlank()) {
                director.setApellido(directorActualizado.getApellido());
            }
            if (directorActualizado.getNacionalidad() != null && !directorActualizado.getNacionalidad().isBlank()) {
                director.setNacionalidad(directorActualizado.getNacionalidad());
            }
            if (directorActualizado.getFechaNacimiento() != null) {
                if (directorActualizado.getFechaNacimiento().isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("La fecha de nacimiento debe ser en el pasado.");
                }
                director.setFechaNacimiento(directorActualizado.getFechaNacimiento());
            }
            if (directorActualizado.getEmailContacto() != null && !directorActualizado.getEmailContacto().isBlank()) {
                if (!directorActualizado.getEmailContacto().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                    throw new IllegalArgumentException("El email debe ser válido.");
                }
                director.setEmailContacto(directorActualizado.getEmailContacto());
            }
            if (directorActualizado.getTelefonoContacto() != null && directorActualizado.getTelefonoContacto().length() > 15) {
                throw new IllegalArgumentException("El teléfono de contacto no puede exceder los 15 caracteres.");
            }
            director.setTelefonoContacto(directorActualizado.getTelefonoContacto());
            if (directorActualizado.getDireccionOficina() != null && directorActualizado.getDireccionOficina().length() > 255) {
                throw new IllegalArgumentException("La dirección de la oficina no puede exceder los 255 caracteres.");
            }
            director.setDireccionOficina(directorActualizado.getDireccionOficina());
            if (directorActualizado.getImagenUrl() != null && !directorActualizado.getImagenUrl().isBlank()) {
                if (!directorActualizado.getImagenUrl().matches("^(http|https)://.*$")) {
                    throw new IllegalArgumentException("La URL de la imagen debe ser válida.");
                }
                director.setImagenUrl(directorActualizado.getImagenUrl());
            }
            if (directorActualizado.getSalarioMensual() != null) {
                if (directorActualizado.getSalarioMensual().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("El salario mensual debe ser un valor positivo.");
                }
                director.setSalarioMensual(directorActualizado.getSalarioMensual());
            }
            if (directorActualizado.getEstado() != null && !directorActualizado.getEstado().isBlank()) {
                if (!directorActualizado.getEstado().matches("^(Activo|Inactivo)$")) {
                    throw new IllegalArgumentException("El estado debe ser 'Activo' o 'Inactivo'.");
                }
                director.setEstado(directorActualizado.getEstado());
            }
            return directorRepository.save(director);
        }).orElseThrow(() -> new RuntimeException("Director no encontrado con ID: " + id));
    }

    @Override
    public void eliminarDirector(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID proporcionado no es válido: " + id);
        }

        if (directorRepository.existsById(id)) {
            directorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Director no encontrado con ID: " + id);
        }
    }

    private void validarDirector(Director director) {
        if (director == null) {
            throw new IllegalArgumentException("La información del director no puede ser nula.");
        }
        if (director.getNombre() == null || director.getNombre().isBlank() || director.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede estar vacío y debe tener un máximo de 100 caracteres.");
        }
        if (director.getApellido() == null || director.getApellido().isBlank() || director.getApellido().length() > 100) {
            throw new IllegalArgumentException("El apellido no puede estar vacío y debe tener un máximo de 100 caracteres.");
        }
        if (director.getNacionalidad() == null || director.getNacionalidad().isBlank() || director.getNacionalidad().length() > 100) {
            throw new IllegalArgumentException("La nacionalidad no puede estar vacía y debe tener un máximo de 100 caracteres.");
        }
        if (director.getFechaNacimiento() != null && director.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento debe ser en el pasado.");
        }
        if (director.getEmailContacto() != null && !director.getEmailContacto().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("El email debe ser válido.");
        }
        if (director.getTelefonoContacto() != null && director.getTelefonoContacto().length() > 15) {
            throw new IllegalArgumentException("El teléfono de contacto no puede exceder los 15 caracteres.");
        }
        if (director.getDireccionOficina() != null && director.getDireccionOficina().length() > 255) {
            throw new IllegalArgumentException("La dirección de la oficina no puede exceder los 255 caracteres.");
        }
        if (director.getImagenUrl() != null && !director.getImagenUrl().matches("^(http|https)://.*$")) {
            throw new IllegalArgumentException("La URL de la imagen debe ser válida.");
        }
        if (director.getSalarioMensual() != null && director.getSalarioMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El salario mensual debe ser un valor positivo.");
        }
        if (director.getEstado() == null || director.getEstado().isBlank() || !director.getEstado().matches("^(Activo|Inactivo)$")) {
            throw new IllegalArgumentException("El estado debe ser 'Activo' o 'Inactivo'.");
        }
    }
}